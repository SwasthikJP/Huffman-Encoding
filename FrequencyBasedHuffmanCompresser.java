import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class FrequencyBasedHuffmanCompresser implements IHuffmanCompresser {

    int[] characterFrequency;
    String[] huffmanCode;
    Node rootNode;

    public FrequencyBasedHuffmanCompresser(){
        characterFrequency=new int[257];
        huffmanCode=new String[257];
        rootNode=null;
    }

    @Override
    public void calculateCharacterFrequency(String filePath) {
        int character;
        InputStream inputStream=new InputStream(filePath);
        while((character=inputStream.getBits(8))!=-1){
            characterFrequency[character]++;
        }
        characterFrequency[256]=1;
    }



    public void combineSubTrees(PriorityQueue<Node> pq){
        while(pq.size()!=1){
            Node a=pq.poll();
            Node b=pq.poll();
            pq.add(new Node(a,b));
        }
        rootNode=pq.poll();
    }

    @Override
    public void createHuffmanTree() {

    PriorityQueue<Node> subTrees=new PriorityQueue<>( 1, new Comparator<Node>() {
      @Override public int compare(Node a, Node b) {
        return Integer.compare(a.frequency, b.frequency);
      }
    });

    for(int i=0;i<characterFrequency.length;i++){
        if(characterFrequency[i]!=0){
            subTrees.add(new Node(i,characterFrequency[i]));
        }
    }
    combineSubTrees(subTrees);
        
    }


    public void preOrder(Node node,String code){
        if(node==null){
            return;
        }
        if(node.isLeafNode){
            huffmanCode[node.value]=code;
        }else{
            preOrder(node.left, code+"0");
            preOrder(node.right, code+"1");
        }
    }

    @Override
    public void generatePrefixCode() {
        preOrder(rootNode, "");
    }

    public void writeEncodedCharacters(InputStream inputStream, OutputStream outputStream){
     
        int character;
        while((character=inputStream.getBits(8))!=-1){
            outputStream.writeBits(huffmanCode[character], huffmanCode[character].length());
        }
       outputStream.writeBits(huffmanCode[256], huffmanCode[256].length());
    } 

    @Override
    public String encodeFile(String filePath) {
       OutputStream outputStream=new OutputStream(filePath+".huf");
       InputStream inputStream=new InputStream(filePath);

       IHeaderInfoWriter headerInfoWriter=new PreorderHeaderInfoWriter();
       headerInfoWriter.writeHeaderInfo(rootNode,outputStream);

       writeEncodedCharacters(inputStream,outputStream);
       outputStream.closeStream();
       return filePath+".huf";
    }


}
