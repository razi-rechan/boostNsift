package corpusPreprocessor;

import corpusPreprocessor.Stemmer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Hashtable;

public class CorpusPreprocessor {
  public static final String SUFFIX_AFTER_SPLIT = "-AfterSplit";
  
  public static final String SUFFIX_AFTER_SPLIT_STOP = "-AfterSplitStop";
  
  public static final String SUFFIX_AFTER_SPLIT_STOP_STEM = "-AfterSplitStopStem";
  
  public static String FILE_NAME_LIST_OF_STOP_WORDS = "D:\\Implementations\\StopwordsPlusJava.txt";
  
  private String inputFileNameCorpus;
  
  private String inputFileNameCorpusWithoutPath;
  
  private String inputFileNameCorpusWithoutPathWithoutExtension;
  
  private String inputFileNameCorpusExtension;
  
  private String outputFolder;
  
  private Hashtable<String, Integer> listOfStopWords;
  
  public CorpusPreprocessor(String inputFileNameCorpus, String outputFolder) {
    this.inputFileNameCorpus = inputFileNameCorpus;
    this.inputFileNameCorpusWithoutPath = (new File(inputFileNameCorpus)).getName();
    this.inputFileNameCorpusExtension = this.inputFileNameCorpusWithoutPath.substring(this.inputFileNameCorpusWithoutPath.lastIndexOf("."));
    this.inputFileNameCorpusWithoutPathWithoutExtension = this.inputFileNameCorpusWithoutPath.substring(0, this.inputFileNameCorpusWithoutPath.indexOf(this.inputFileNameCorpusExtension));
    this.outputFolder = outputFolder;
    System.out.println("inputFileNameCorpus=" + inputFileNameCorpus);
    System.out.println("inputFileNameCorpusWithoutPath=" + this.inputFileNameCorpusWithoutPath);
    System.out.println("inputFileNameCorpusExtension=" + this.inputFileNameCorpusExtension);
    System.out.println("inputFileNameCorpusWithoutPathWithoutExtension=" + this.inputFileNameCorpusWithoutPathWithoutExtension);
    System.out.println("outputFolder=" + outputFolder);
  }
  
  public void preprocessCorpus() throws Exception {
    this.listOfStopWords = loadListOfStopWords();
    BufferedReader brOriginalCorpus = new BufferedReader(new FileReader(this.inputFileNameCorpus));
    BufferedWriter outCorpusAfterSplit = new BufferedWriter(new FileWriter(this.outputFolder + this.inputFileNameCorpusWithoutPathWithoutExtension + "-AfterSplit" + this.inputFileNameCorpusExtension));
    BufferedWriter outCorpusAfterSplitStop = new BufferedWriter(new FileWriter(this.outputFolder + this.inputFileNameCorpusWithoutPathWithoutExtension + "-AfterSplitStop" + this.inputFileNameCorpusExtension));
    BufferedWriter outCorpusAfterSplitStopStem = new BufferedWriter(new FileWriter(this.outputFolder + this.inputFileNameCorpusWithoutPathWithoutExtension + "-AfterSplitStopStem" + this.inputFileNameCorpusExtension));
    String t = "";
    String Path = null;
    if (this.inputFileNameCorpus.contains("queries")) {
      Path = this.outputFolder + "\\PreprocessedQueryFiles\\";
      t = "Query";
    } else {
      Path = this.outputFolder + "\\PreprocessedSourceFiles\\";
    } 
    File directory = new File(Path);
    if (!directory.exists())
      directory.mkdir(); 
    String Path1 = this.outputFolder + "\\LDASpace\\";
    File directory1 = new File(Path1);
    if (!directory1.exists())
      directory1.mkdir(); 
    int i = 1;
    String buf;
    while ((buf = brOriginalCorpus.readLine()) != null) {
      String bufAfterEliminatingNonLiterals = eliminateNonLiterals(buf);
      String bufAfterSplit = splitIdentifiers(bufAfterEliminatingNonLiterals, false);
      String bufAfterSplitStop = elimiateStopWords(bufAfterSplit, 1);
      String bufAfterSplitStopStem = stemBuffer(bufAfterSplitStop);
      outCorpusAfterSplit.write(bufAfterSplit + "\r\n");
      outCorpusAfterSplitStop.write(bufAfterSplitStop + "\r\n");
      outCorpusAfterSplitStopStem.write(bufAfterSplitStopStem + "\r\n");
      FileWriter writer = new FileWriter(Path + i + ".txt");
      FileWriter writer1 = new FileWriter(Path1 + t + i + ".txt");
      if (bufAfterSplitStopStem.length() == 0)
        bufAfterSplitStopStem = stemBuffer(bufAfterSplit); 
      if (bufAfterSplitStopStem.length() < 1)
        System.out.println(""); 
      writer.write(bufAfterSplitStopStem);
      writer.flush();
      writer1.write(bufAfterSplit);
      writer1.flush();
      i++;
    } 
    brOriginalCorpus.close();
    outCorpusAfterSplit.close();
    outCorpusAfterSplitStop.close();
    outCorpusAfterSplitStopStem.close();
  }
  
  public Hashtable<String, Integer> loadListOfStopWords() throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(FILE_NAME_LIST_OF_STOP_WORDS));
    Hashtable<String, Integer> listOfStopWords = new Hashtable<>();
    String buf;
    while ((buf = br.readLine()) != null)
      listOfStopWords.put(buf, new Integer(1)); 
    br.close();
    return listOfStopWords;
  }
  
  private String eliminateNonLiterals(String originalBuffer) {
    String newBuffer = originalBuffer.replaceAll("[^a-zA-Z_]", " ");
    return newBuffer;
  }
  
  public String splitIdentifiers(String originalBuffer, boolean keepCompoundIdentifier) {
    String[] words = originalBuffer.split(" ");
    StringBuilder newBuffer = new StringBuilder();
    for (String word : words) {
      String originalWord = word;
      if (word.length() != 0) {
        boolean isCompoundIdentifier = false;
        if (word.indexOf('_') >= 0) {
          isCompoundIdentifier = true;
          word = word.replaceAll("_", " ");
        } 
        StringBuilder newWord = new StringBuilder(word);
        for (int i = newWord.length() - 1; i >= 0; i--) {
          if (Character.isUpperCase(newWord.charAt(i))) {
            if (i > 0 && 
              Character.isLowerCase(newWord.charAt(i - 1))) {
              newWord.insert(i, ' ');
              isCompoundIdentifier = true;
            } 
          } else if (Character.isLowerCase(newWord.charAt(i))) {
            if (i > 0 && 
              Character.isUpperCase(newWord.charAt(i - 1))) {
              newWord.insert(i - 1, ' ');
              isCompoundIdentifier = true;
            } 
          } 
        } 
        newBuffer.append(newWord.toString().toLowerCase());
        newBuffer.append(' ');
        if (keepCompoundIdentifier)
          if (isCompoundIdentifier) {
            newBuffer.append(originalWord.toLowerCase());
            newBuffer.append(' ');
          }  
      } 
    } 
    System.out.println("=====");
    System.out.println(newBuffer.toString());
    return newBuffer.toString();
  }
  
  private static boolean isAllDigits(String word) {
    char[] charactersWord = word.toCharArray();
    for (char c : charactersWord) {
      if (!Character.isDigit(c))
        return false; 
    } 
    return true;
  }
  
  private String elimiateStopWords(String originalBuffer, int numberOfCharactersForWordToRemove) {
    String[] words = originalBuffer.split(" ");
    StringBuilder newBufferAfterEliminatingStopWords = new StringBuilder();
    for (String word : words) {
      if (word.length() != 0)
        if (this.listOfStopWords.get(word) == null)
          if (!isAllDigits(word))
            if (word.length() > numberOfCharactersForWordToRemove) {
              newBufferAfterEliminatingStopWords.append(word);
              newBufferAfterEliminatingStopWords.append(' ');
            }    
    } 
    return newBufferAfterEliminatingStopWords.toString();
  }
  
  private String stemBuffer(String originalBuffer) {
    String[] words = originalBuffer.split(" ");
    StringBuilder newBufferStemmed = new StringBuilder();
    for (String word : words) {
      if (word.length() != 0) {
        Stemmer stemmer = new Stemmer();
        for (int i = 0; i < word.length(); i++)
          stemmer.add(word.charAt(i)); 
        stemmer.stem();
        newBufferStemmed.append(stemmer.toString());
        newBufferStemmed.append(' ');
      } 
    } 
    return newBufferStemmed.toString();
  }
}

