package com.capillary.Compression;

import java.io.FileOutputStream;
import java.io.IOException;

public class FrequencyBasedHuffmanDecompresser implements IHuffmanDecompresser {

    Node rootNode;
    InputStream inputStream;

    public FrequencyBasedHuffmanDecompresser() {
        inputStream = null;
    }

    public FrequencyBasedHuffmanDecompresser(java.io.InputStream fileInputStream) throws IOException {
        inputStream = new InputStream(fileInputStream);
        inputStream.loadBuffer();
    }


    public Node readHeaderInfo(InputStream inputStream) throws IOException {
        int bit=inputStream.getBit();
        if(bit==-1){
//            System.out.println("file ended");
            IOException e=new IOException("Incorrect Header");
            throw e;
        }

        if (bit == 0) {
            return new Node(readHeaderInfo(inputStream), readHeaderInfo(inputStream));
        }
        return new Node(inputStream.getBits(9), 0);
    }

    @Override
    public void createHuffmanTree() throws IOException{
        if (inputStream == null) {
            return ;
        }
        rootNode = readHeaderInfo(inputStream);
    }

    @Override
    public String decodeFile(String filePath) throws IOException{
        if (inputStream == null || rootNode==null) {
            return null;
        }
        String[] filePathSplit=filePath.split("\\.(?![^\\.]+$)");
        String decompressFilePath=filePathSplit[0];
        FileOutputStream fileOutputStream=new FileOutputStream(decompressFilePath + ".unhuf"+".txt");
        OutputStream outputStream = new OutputStream(fileOutputStream);

        int bit;
        Node node = rootNode;
        while ((bit = inputStream.getBit()) != -1) {

            if(node==null){
                IOException e=new IOException("Incorrect Header");
                throw e;
            }
            if (node.isLeafNode) {
                if (node.value == 256) {
                    outputStream.closeStream();
                    System.out.println("hello");
                    return decompressFilePath + ".unhuf"+".txt";
                }
                // outputStream.writeBits(node.value, 8);
                outputStream.writeByte(node.value);
                node = rootNode;
            }
            if (bit == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

}
