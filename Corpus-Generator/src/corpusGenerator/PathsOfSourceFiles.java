package corpusGenerator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class PathsOfSourceFiles {

	  public static void main(String[] args) throws IOException {
	    FileWriter writer = new FileWriter("D:\\Implementations\\Experiments\\-CommonsLang\\Source\\SourcePaths.txt");
	    File dir = new File("D:\\Implementations\\Experiments\\-CommonsLang\\commons-lang3-3.5-src\\commons-lang3-3.5-src");
	    String[] extensions = { "java" };
	    List<File> files = (List<File>)FileUtils.listFiles(dir, extensions, true);
	    for (File file : files)
	      writer.append(file.getCanonicalPath() + "\r\n"); 
	    writer.flush();
	  }
	

}
