package corpusGenerator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;

public abstract class InputOutput {
  public static final String LINE_ENDING = "\r\n";
  
  private String inputFileNameWithListOfInputFileNames;
  
  private String outputFolderName;
  
  private String outputFileNameWithoutExtension;
  
  public InputOutput(String inputFileNameWithListOfInputFileNames, String outputFolderName, String outputFileNameWithoutExtension) {
    this.inputFileNameWithListOfInputFileNames = inputFileNameWithListOfInputFileNames;
    this.outputFolderName = outputFolderName;
    this.outputFileNameWithoutExtension = outputFileNameWithoutExtension;
  }
  
  public String getInputFileNameWithListOfInputFileNames() {
    return this.inputFileNameWithListOfInputFileNames;
  }
  
  public static String readFile(String fileName) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      StringBuilder fileContent = new StringBuilder();
      String buf;
      while ((buf = br.readLine()) != null)
        fileContent.append(buf + "\r\n"); 
      br.close();
      return fileContent.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static void appendToFile(BufferedWriter outputFile, String buf) {
    try {
      outputFile.write(buf + "\r\n");
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public abstract void initializeOutputStream() throws Exception;
  
  public abstract void closeOutputStreams() throws Exception;
  
  public abstract void printMessageWhereOutputFilesWereSaved();
}

