
public class FrequencyBasedHuffmanDecompresser implements IHuffmanDecompresser {

    Node rootNode;
    InputStream inputStream;

    public FrequencyBasedHuffmanDecompresser(){
        inputStream=null;
    }

    public FrequencyBasedHuffmanDecompresser(String compressFilepath) {
     inputStream=new InputStream(compressFilepath);
    }

    @Override
    public void createHuffmanTree() {
     IHeaderInfoReader headerInfoReader=new PreorderHeaderInfoReader();
     rootNode=headerInfoReader.readHeaderInfo(inputStream);
    }

    @Override
    public String decodeFile(String filePath) {
        if(inputStream==null){
            return null;
          }
      OutputStream outputStream=new OutputStream(filePath+".unhuf");
   
     int bit;
     Node node=rootNode;
     while((bit=inputStream.getBit())!=-1){
        if(node.isLeafNode){
            if(node.value==256){
                outputStream.closeStream();
                return filePath+".unhuf";
            }
            outputStream.writeBits(node.value, 8);
            node=rootNode;
        }
        if(bit==0){
            node=node.left;
        }else{
            node=node.right;
        }
     }
        return null;
    }

}
