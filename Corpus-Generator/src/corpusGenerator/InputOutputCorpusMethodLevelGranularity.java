package corpusGenerator;


import java.io.BufferedWriter;
import java.io.FileWriter;

public class InputOutputCorpusMethodLevelGranularity extends InputOutput {
  public static final String EXTENSION_CORPUS_RAW = ".corpusRawMethodLevelGranularity";
  
  public static final String EXTENSION_CORPUS_MAPPING = ".corpusMappingMethodLevelGranularity";
  
  public static final String EXTENSION_CORPUS_MAPPING_WITH_PACKAGE_SEPARATOR = ".corpusMappingWithPackageSeparatorMethodLevelGranularity";
  
  public static final String EXTENSION_CORPUS_DEBUG = ".corpusRawAndMappingDebugMethodLevelGranularity";
  
  private String outputFileNameCorpusRaw;
  
  private String outputFileNameCorpusMapping;
  
  private String outputFileNameCorpusMappingWithPackageSeparator;
  
  private String outputFileNameCorpusRawAndMappingDebug;
  
  private BufferedWriter outputFileCorpusRaw;
  
  private BufferedWriter outputFileCorpusMapping;
  
  private BufferedWriter outputFileCorpusMappingWithPackageSeparator;
  
  private BufferedWriter outputFileCorpusRawAndMappingDebug;
  
  public String getOutputFileNameCorpusRaw() {
    return this.outputFileNameCorpusRaw;
  }
  
  public String getOutputFileNameCorpusMapping() {
    return this.outputFileNameCorpusMapping;
  }
  
  public String getOutputFileNameCorpusMappingWithPackageSeparator() {
    return this.outputFileNameCorpusMappingWithPackageSeparator;
  }
  
  public String getOutputFileNameCorpusRawAndMappingDebug() {
    return this.outputFileNameCorpusRawAndMappingDebug;
  }
  
  public InputOutputCorpusMethodLevelGranularity(String inputFileNameWithListOfInputFileNames, String outputFolderName, String outputFileNameWithoutExtension) {
    super(inputFileNameWithListOfInputFileNames, outputFolderName, outputFileNameWithoutExtension);
    this.outputFileNameCorpusRaw = outputFolderName + outputFileNameWithoutExtension + ".corpusRawMethodLevelGranularity";
    this.outputFileNameCorpusMapping = outputFolderName + outputFileNameWithoutExtension + ".corpusMappingMethodLevelGranularity";
    this.outputFileNameCorpusMappingWithPackageSeparator = outputFolderName + outputFileNameWithoutExtension + ".corpusMappingWithPackageSeparatorMethodLevelGranularity";
    this.outputFileNameCorpusRawAndMappingDebug = outputFolderName + outputFileNameWithoutExtension + ".corpusRawAndMappingDebugMethodLevelGranularity";
  }
  
  public void initializeOutputStream() throws Exception {
    this.outputFileCorpusRaw = new BufferedWriter(new FileWriter(this.outputFileNameCorpusRaw));
    this.outputFileCorpusMapping = new BufferedWriter(new FileWriter(this.outputFileNameCorpusMapping));
    this.outputFileCorpusMappingWithPackageSeparator = new BufferedWriter(new FileWriter(this.outputFileNameCorpusMappingWithPackageSeparator));
    this.outputFileCorpusRawAndMappingDebug = new BufferedWriter(new FileWriter(this.outputFileNameCorpusRawAndMappingDebug));
  }
  
  public void appendToCorpusMapping(String idMethod) {
    appendToFile(this.outputFileCorpusMapping, idMethod);
  }
  
  public void appendToCorpusMappingWithPackageSeparator(String idMethod) {
    appendToFile(this.outputFileCorpusMappingWithPackageSeparator, idMethod);
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
    this.outputFileCorpusMappingWithPackageSeparator.close();
    this.outputFileCorpusRawAndMappingDebug.close();
  }
  
  public void printMessageWhereOutputFilesWereSaved() {
    System.out.println("CorpusMethodLevelGranularity: Corpus was saved to file: " + this.outputFileNameCorpusRaw);
    System.out.println("CorpusMethodLevelGranularity: Mapping was saved to file: " + this.outputFileNameCorpusMapping);
    System.out.println("CorpusMethodLevelGranularity: Mapping with package separator was saved to file: " + this.outputFileNameCorpusMappingWithPackageSeparator);
    System.out.println("CorpusMethodLevelGranularity: Corpus with debug information was saved to file: " + this.outputFileNameCorpusRawAndMappingDebug);
  }
}

