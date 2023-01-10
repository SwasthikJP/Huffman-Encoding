
public class PreorderHeaderInfoWriter implements IHeaderInfoWriter {

    @Override
    public void writeHeaderInfo(Node node, OutputStream outputStream) {
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
         writeHeaderInfo(node.left, outputStream);
         writeHeaderInfo( node.right,outputStream);
         }
         
    }

}
