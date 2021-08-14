package corpusGenerator;



import java.io.BufferedWriter;
import java.io.FileWriter;

public class InputOutputCorpusClassLevelGranularity extends InputOutput {
  public static final String EXTENSION_CORPUS_RAW = ".corpusRawClassLevelGranularity";
  
  public static final String EXTENSION_CORPUS_MAPPING = ".corpusMappingClassLevelGranularity";
  
  public static final String EXTENSION_CORPUS_DEBUG = ".corpusRawAndMappingDebugClassLevelGranularity";
  
  private String outputFileNameCorpusRaw;
  
  private String outputFileNameCorpusMapping;
  
  private String outputFileNameCorpusRawAndMappingDebug;
  
  private BufferedWriter outputFileCorpusRaw;
  
  private BufferedWriter outputFileCorpusMapping;
  
  private BufferedWriter outputFileCorpusRawAndMappingDebug;
  
  public String getOutputFileNameCorpusRaw() {
    return this.outputFileNameCorpusRaw;
  }
  
  public String getOutputFileNameCorpusMapping() {
    return this.outputFileNameCorpusMapping;
  }
  
  public String getOutputFileNameCorpusRawAndMappingDebug() {
    return this.outputFileNameCorpusRawAndMappingDebug;
  }
  
  public InputOutputCorpusClassLevelGranularity(String inputFileNameWithListOfInputFileNames, String outputFolderName, String outputFileNameWithoutExtension) {
    super(inputFileNameWithListOfInputFileNames, outputFolderName, outputFileNameWithoutExtension);
    this.outputFileNameCorpusRaw = outputFolderName + outputFileNameWithoutExtension + ".corpusRawClassLevelGranularity";
    this.outputFileNameCorpusMapping = outputFolderName + outputFileNameWithoutExtension + ".corpusMappingClassLevelGranularity";
    this.outputFileNameCorpusRawAndMappingDebug = outputFolderName + outputFileNameWithoutExtension + ".corpusRawAndMappingDebugClassLevelGranularity";
  }
  
  public void initializeOutputStream() throws Exception {
    this.outputFileCorpusRaw = new BufferedWriter(new FileWriter(this.outputFileNameCorpusRaw));
    this.outputFileCorpusMapping = new BufferedWriter(new FileWriter(this.outputFileNameCorpusMapping));
    this.outputFileCorpusRawAndMappingDebug = new BufferedWriter(new FileWriter(this.outputFileNameCorpusRawAndMappingDebug));
  }
  
  public void appendToCorpusMapping(String idMethod) {
    appendToFile(this.outputFileCorpusMapping, idMethod);
  }
  
  public void appendToCorpusRaw(String methodContent) {
    appendToFile(this.outputFileCorpusRaw, methodContent);
  }
  
  public void appendToCorpusDebug(String buf) {
    appendToFile(this.outputFileCorpusRawAndMappingDebug, buf);
  }
  
  public void closeOutputStreams() throws Exception {
    this.outputFileCorpusRaw.close();
    this.outputFileCorpusMapping.close();
    this.outputFileCorpusRawAndMappingDebug.close();
  }
  
  public void printMessageWhereOutputFilesWereSaved() {
    System.out.println("CorpusClassLevelGranularity: Corpus was saved to file: " + this.outputFileNameCorpusRaw);
    System.out.println("CorpusClassLevelGranularity: Mapping was saved to file: " + this.outputFileNameCorpusMapping);
    System.out.println("CorpusClassLevelGranularity: Corpus with debug information was saved to file: " + this.outputFileNameCorpusRawAndMappingDebug);
  }
}
