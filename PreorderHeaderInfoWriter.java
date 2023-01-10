
public class PreorderHeaderInfoWriter implements IHeaderInfoWriter {

   @Override
   public void writeHeaderInfo(Node node, OutputStream outputStream) {
      if (node == null) {
         return;
      }
      if (node.isLeafNode) {
         outputStream.writeBit(1);
         outputStream.writeBits(node.value, 9);
      } else {
         outputStream.writeBit(0);
         writeHeaderInfo(node.left, outputStream);
         writeHeaderInfo(node.right, outputStream);
      }

   }

}
