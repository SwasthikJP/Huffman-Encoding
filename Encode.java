import java.io.IOException;

public class Encode {
    Node rootNode;
    String fileName;


    public Encode(){
        rootNode=null;
        fileName="";
    }

    public Encode(String fileName){
        rootNode=null;
        this.fileName=fileName;
    }

    public void PreOrder(OutputStream outputStream,Node node) throws IOException{
     if(node==null){
        return;
     }
     if(node.isLeafNode){
        outputStream.writeBit(1);
        System.out.println(1+"   "+(char)node.value+"  | ");
        outputStream.writeBits(node.value, 8);
     }else{
        outputStream.writeBit(0);
        System.out.println(0+" | ");
     PreOrder(outputStream, node.left);
     PreOrder(outputStream, node.right);
     }
     
    }

    public boolean writeEncodedFile() throws IOException{
        InputStream inputStream=new InputStream(fileName);
        ArrayStoredCharacterCount arrayStoredCharacterCount=new ArrayStoredCharacterCount(inputStream);
        Integer[] ar=arrayStoredCharacterCount.calculateCharacterCount();
    
        HeapBasedOptimalTree heapBasedOptimalTree=new HeapBasedOptimalTree(ar);
        heapBasedOptimalTree.buildOptimalTree();
        rootNode=heapBasedOptimalTree.getOptimalTree();
        PreorderBasedAssignCode preorderBasedAssignCode=new PreorderBasedAssignCode(rootNode);
       String[] ls= preorderBasedAssignCode.getCodeList();
  
       OutputStream outputStream=new OutputStream("result.txt");
        PreOrder(outputStream,rootNode);

       return true;
    }
}
