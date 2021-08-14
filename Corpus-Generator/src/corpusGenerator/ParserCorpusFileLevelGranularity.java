package corpusGenerator;


import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ParserCorpusFileLevelGranularity {
  private String fileContent;
  
  private String packageName;
  
  private InputOutputCorpusFileLevelGranularity inputOutput;
  
  private String inputFileName;
  
  public ParserCorpusFileLevelGranularity(InputOutputCorpusFileLevelGranularity inputOutput, String fileContent, String inputFileName) {
    this.inputOutput = inputOutput;
    this.fileContent = fileContent;
    this.inputFileName = inputFileName;
    this.packageName = "";
  }
  
  public CompilationUnit parseSourceCode() {
    char[] fileContentAsChar = this.fileContent.toCharArray();
    ASTParser parser = ASTParser.newParser(4);
    parser.setKind(8);
    parser.setSource(fileContentAsChar);
    return (CompilationUnit)parser.createAST(null);
  }
  
  public void exploreSourceCodeFileLevelGranularity(CompilationUnit compilationUnitSourceCode) {
    this.packageName = compilationUnitSourceCode.getPackage().getName().toString();
    List<ASTNode> declaredTypes = compilationUnitSourceCode.types();
    StringBuilder bufMultipleClassesInSameFile = new StringBuilder();
    for (ASTNode currentDeclaredType : declaredTypes) {
      if (currentDeclaredType.getNodeType() == 55) {
        TypeDeclaration typeDeclaration = (TypeDeclaration)currentDeclaredType;
        bufMultipleClassesInSameFile.append(exploreClassContentsFileLevelGranularity(typeDeclaration, "") + "\t");
      } 
    } 
    this.inputOutput.appendToCorpusMapping(this.inputFileName.replace("\\", "/"));
    this.inputOutput.appendToCorpusRaw(bufMultipleClassesInSameFile.toString());
  }
  
  private String exploreClassContentsFileLevelGranularity(TypeDeclaration classNode, String prefixClass) {
    String idClass = this.packageName + "." + classNode.getName();
    System.out.println("ID=" + idClass);
    String classContent = this.fileContent.substring(classNode.getStartPosition(), classNode.getStartPosition() + classNode.getLength());
    String classContentSingleLine = convertMultipleLinesToSingleLinesWithReplace(classContent);
    this.inputOutput.appendToCorpusDebug(idClass);
    this.inputOutput.appendToCorpusDebug(classContentSingleLine + "\r\n");
    return classContentSingleLine;
  }
  
  private String convertMultipleLinesToSingleLinesWithReplace(String methodContentsMultipleLines) {
    String methodContentsMultipleLinesOutput = methodContentsMultipleLines;
    methodContentsMultipleLinesOutput = methodContentsMultipleLinesOutput.replace("\r", "\t");
    methodContentsMultipleLinesOutput = methodContentsMultipleLinesOutput.replace("\n", "\t");
    return methodContentsMultipleLinesOutput;
  }
}
