package corpusGenerator;


import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ParserCorpusMethodLevelGranularity {
  private String fileContent;
  
  private String packageName;
  
  private InputOutputCorpusMethodLevelGranularity inputOutput;
  
  public ParserCorpusMethodLevelGranularity(InputOutputCorpusMethodLevelGranularity inputOutput, String fileContent) {
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
  
  public void exploreSourceCode(CompilationUnit compilationUnitSourceCode) {
    if (compilationUnitSourceCode.getPackage() != null)
      this.packageName = compilationUnitSourceCode.getPackage().getName().toString(); 
    List<ASTNode> declaredTypes = compilationUnitSourceCode.types();
    for (ASTNode currentDeclaredType : declaredTypes) {
      if (currentDeclaredType.getNodeType() == 55) {
        TypeDeclaration typeDeclaration = (TypeDeclaration)currentDeclaredType;
        exploreClassContents((TypeDeclaration)currentDeclaredType, "");
      } 
    } 
  }
  
  private void exploreClassContents(TypeDeclaration classNode, String prefixClass) {
    List<ASTNode> bodyDeclarations = classNode.bodyDeclarations();
    SimpleName className = classNode.getName();
    String fullClassName = prefixClass + className + ".";
    for (ASTNode bodyDeclaration : bodyDeclarations) {
      if (bodyDeclaration.getNodeType() == 31)
        exploreMethodContents((MethodDeclaration)bodyDeclaration, fullClassName); 
      if (bodyDeclaration.getNodeType() == 55)
        exploreClassContents((TypeDeclaration)bodyDeclaration, fullClassName); 
    } 
  }
  
  private void exploreMethodContents(MethodDeclaration methodDeclaration, String fullClassName) {
    String currentMethodName = methodDeclaration.getName().getFullyQualifiedName();
    String idMethod = fullClassName + currentMethodName;
    TypeDeclaration parentOfMethod = (TypeDeclaration)methodDeclaration.getParent();
    if (parentOfMethod.isInterface())
      return; 
    if (Modifier.isAbstract(methodDeclaration.getModifiers())) {
      System.out.println("ID=" + idMethod);
      System.err.println("Abstract method (ignored) ID=" + idMethod);
      return;
    } 
    List<SingleVariableDeclaration> listOfParameters = methodDeclaration.parameters();
    idMethod = idMethod + "\t" + listOfParameters.size() + "\t";
    for (SingleVariableDeclaration p : listOfParameters)
      idMethod = idMethod + p.getType() + "\t"; 
    int methodStartPosition = methodDeclaration.getStartPosition();
    int methodLength = methodDeclaration.getLength();
    String methodContents = this.fileContent.substring(methodStartPosition, methodStartPosition + methodLength);
    String idMethodWithPackageSeparator = this.packageName + "$" + idMethod;
    idMethod = this.packageName + "." + idMethod;
    this.inputOutput.appendToCorpusMapping(convertMethodIDToFinalFormat(idMethod));
    this.inputOutput.appendToCorpusMappingWithPackageSeparator(convertMethodIDToFinalFormat(idMethodWithPackageSeparator));
    this.inputOutput.appendToCorpusRaw(convertMultipleLinesToSingleLines(methodContents));
    this.inputOutput.appendToCorpusDebug(idMethod);
    this.inputOutput.appendToCorpusDebug(convertMultipleLinesToSingleLines(methodContents) + "\r\n");
  }
  
  private String convertMultipleLinesToSingleLines(String methodContentsMultipleLines) {
    StringBuilder methodContentsSingleLine = new StringBuilder();
    String[] splittedLines = methodContentsMultipleLines.split("\r\n");
    for (String buf : splittedLines)
      methodContentsSingleLine.append(buf + "\t"); 
    return methodContentsSingleLine.toString();
  }
  
  private static String convertMethodIDToFinalFormat(String idMethod) {
    String[] splittedBuf = idMethod.split("\t");
    String methodNameFullPath = splittedBuf[0];
    String methodNameFullPathFinal = methodNameFullPath + "(";
    int numberOfParameters = Integer.parseInt(splittedBuf[1]);
    if (numberOfParameters != 0) {
      for (int indexParameter = 0; indexParameter < numberOfParameters - 1; indexParameter++)
        methodNameFullPathFinal = methodNameFullPathFinal + splittedBuf[indexParameter + 2] + ","; 
      methodNameFullPathFinal = methodNameFullPathFinal + splittedBuf[numberOfParameters + 1];
    } 
    methodNameFullPathFinal = methodNameFullPathFinal + ")";
    return methodNameFullPathFinal;
  }
}

