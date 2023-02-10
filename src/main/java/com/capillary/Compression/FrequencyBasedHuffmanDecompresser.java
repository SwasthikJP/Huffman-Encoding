package com.capillary.Compression;
import java.io.IOException;

public class FrequencyBasedHuffmanDecompresser implements IHuffmanDecompresser {


   private InputStream inputStream;


    private Node readHeaderInfo(InputStream inputStream) throws IOException {
        int bit=inputStream.getBit();
        if(bit==-1){
            throw new IOException("Incorrect Header");
        }

        if (bit == 0) {
            return new Node(readHeaderInfo(inputStream), readHeaderInfo(inputStream));
        }
        return new Node(inputStream.getBits(9), 0);
    }

    @Override
    public Node createHuffmanTree(java.io.InputStream fileInputStream) throws IOException{
        inputStream = new InputStream(fileInputStream);
        inputStream.loadBuffer();
        return readHeaderInfo(inputStream);
    }

    @Override
    public Boolean decodeFile( java.io.OutputStream fileOutputStream, Node rootNode) throws IOException{

        if (inputStream == null || rootNode==null) {
            return false;
        }
       OutputStream outputStream = new OutputStream(fileOutputStream);

        int bit;
        Node node = rootNode;
        while ((bit = inputStream.getBit()) != -1) {


            if (node.isLeafNode) {
                if (node.value == 256) {
                    outputStream.closeStream();
                    return true;
                }
                outputStream.writeByte(node.value);
                node = rootNode;
            }
            if (bit == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;
    }

}
