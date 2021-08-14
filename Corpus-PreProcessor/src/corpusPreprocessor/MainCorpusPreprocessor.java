package corpusPreprocessor;


import corpusPreprocessor.CorpusPreprocessor;

public class MainCorpusPreprocessor {
  static void testjEdit() throws Exception {
    String inputFileNameCorpus = "TestCases/Input/Corpus-jEdit4.3.corpusRawMethodLevelGranularity";
    String outputFolder = "TestCases/Output/";
    CorpusPreprocessor corpusPreprocessor = new CorpusPreprocessor(inputFileNameCorpus, outputFolder);
    corpusPreprocessor.preprocessCorpus();
  }
  
  static void testSystem1() throws Exception {
    String inputFileNameCorpus = "D:\\Implementations\\Experiments\\jEdit4.3\\Source\\Corpus-jEdit4.3.corpusRawMethodLevelGranularity";
    String outputFolder = "D:\\Implementations\\Experiments\\jEdit4.3\\";
    CorpusPreprocessor corpusPreprocessor = new CorpusPreprocessor(inputFileNameCorpus, outputFolder);
    corpusPreprocessor.preprocessCorpus();
  }
  
  static void testSystem2() throws Exception {
    String inputFileNameCorpus = "TestCases/Input/Corpus-System2.corpusRawMethodLevelGranularity";
    String outputFolder = "TestCases/Output/";
    CorpusPreprocessor corpusPreprocessor = new CorpusPreprocessor(inputFileNameCorpus, outputFolder);
    corpusPreprocessor.preprocessCorpus();
  }
  
  static void testSystem3() throws Exception {
    String inputFileNameCorpus = "TestCases/Input/Corpus-System3.corpusRawMethodLevelGranularity";
    String outputFolder = "TestCases/Output/";
    CorpusPreprocessor corpusPreprocessor = new CorpusPreprocessor(inputFileNameCorpus, outputFolder);
    corpusPreprocessor.preprocessCorpus();
  }
  
  public static void main(String[] args) throws Exception {
    testSystem1();
  }
}
