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

    @Override
    public void createHuffmanTree() {
        if (inputStream == null) {
            return ;
        }
        IHeaderInfoReaderWriter headerInfoReaderWriter = new PreorderHeaderInfoReaderWriter();
        rootNode = headerInfoReaderWriter.readHeaderInfo(inputStream);
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

            if (node.isLeafNode) {
                if (node.value == 256) {
                    outputStream.closeStream();
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
