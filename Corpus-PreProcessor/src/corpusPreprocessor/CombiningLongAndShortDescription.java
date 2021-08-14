package corpusPreprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class CombiningLongAndShortDescription {
  public static void main(String[] args) throws IOException {
    writeCombinition("D:\\Implementations\\Experiments\\Eclipse3.0\\Queries", "D:\\Implementations\\Experiments\\Eclipse3.0\\");
  }
  
  public static void writeCombinition(String shortP, String outputFolder) throws FileNotFoundException, IOException {
    ArrayList<String> Short = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    String line = "", corpus = "";
    File dir = new File(shortP);
    String[] extensions = { "txt" };
    List<File> files = (List<File>)FileUtils.listFiles(dir, extensions, true);
    for (File file : files) {
      id.add(ExtractFeatureID(file.getName()));
      try (BufferedReader br = new BufferedReader(new FileReader(file.getCanonicalPath()))) {
        while ((line = br.readLine()) != null) {
          if (line.length() < 2)
            continue; 
          corpus = corpus + " " + line;
        } 
        Short.add(corpus);
        corpus = "";
      } 
    } 
    FileWriter writer = new FileWriter(outputFolder + "\\queries.txt");
    for (int i = 0; i < Short.size(); i++) {
      writer.write((String)Short.get(i) + "\n");
      writer.flush();
    } 
  }
  
  public static String ExtractFeatureID(String file) {
    return file.replaceAll("[^\\d.]", "").replaceAll("\\.", "");
  }
}

