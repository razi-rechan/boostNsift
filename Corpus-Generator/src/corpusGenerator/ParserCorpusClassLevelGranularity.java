package corpusGenerator;


import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ParserCorpusClassLevelGranularity {
  private String fileContent;
  
  private String packageName;
  
  private InputOutputCorpusClassLevelGranularity inputOutput;
  
  public ParserCorpusClassLevelGranularity(InputOutputCorpusClassLevelGranularity inputOutput, String fileContent) {
    this.inputOutput = inputOutput;
    this.fileContent = fileContent;
    this.packageName = "";
  }
  
  public CompilationUnit parseSourceCode() {
    char[] fileContentAsChar = this.fileContent.toCharArray();
    ASTParser parser = ASTParser.newParser(4);
    parser.setKind(8);
    parser.setSource(fileContentAsChar);
    return (CompilationUnit)parser.createAST(null);
  }
  
  public void exploreSourceCodeClassLevelGranularity(CompilationUnit compilationUnitSourceCode) {
    this.packageName = compilationUnitSourceCode.getPackage().getName().toString();
    List<ASTNode> declaredTypes = compilationUnitSourceCode.types();
    for (ASTNode currentDeclaredType : declaredTypes) {
      if (currentDeclaredType.getNodeType() == 55) {
        TypeDeclaration typeDeclaration = (TypeDeclaration)currentDeclaredType;
        exploreClassContentsClassLevelGranularity((TypeDeclaration)currentDeclaredType, "");
      } 
    } 
  }
  
  private void exploreClassContentsClassLevelGranularity(TypeDeclaration classNode, String prefixClass) {
    String idClass = this.packageName + "." + classNode.getName();
    System.out.println("ID=" + idClass);
    System.out.println(classNode.getStartPosition());
    System.out.println(classNode.getLength());
    String classContent = this.fileContent.substring(classNode.getStartPosition(), classNode.getStartPosition() + classNode.getLength());
    System.out.println(classContent);
    this.inputOutput.appendToCorpusMapping(idClass);
    this.inputOutput.appendToCorpusRaw(convertMultipleLinesToSingleLinesWithReplace(classContent));
    this.inputOutput.appendToCorpusDebug(idClass);
    this.inputOutput.appendToCorpusDebug(convertMultipleLinesToSingleLinesWithReplace(classContent) + "\r\n");
  }
  
  private String convertMultipleLinesToSingleLinesWithReplace(String methodContentsMultipleLines) {
    String methodContentsMultipleLinesOutput = methodContentsMultipleLines;
    methodContentsMultipleLinesOutput = methodContentsMultipleLinesOutput.replace("\r", "\t");
    methodContentsMultipleLinesOutput = methodContentsMultipleLinesOutput.replace("\n", "\t");
    return methodContentsMultipleLinesOutput;
  }
}
