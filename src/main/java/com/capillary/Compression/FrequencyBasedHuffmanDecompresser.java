package com.capillary.Compression;

import java.io.IOException;

public class FrequencyBasedHuffmanDecompresser implements IHuffmanDecompresser {

    Node rootNode;
    InputStream inputStream;

    public FrequencyBasedHuffmanDecompresser() {
        inputStream = null;
    }

    public FrequencyBasedHuffmanDecompresser(String compressFilepath) throws IOException {
        inputStream = new InputStream(compressFilepath);
        inputStream.loadBuffer();
    }

    @Override
    public void createHuffmanTree() {
        IHeaderInfoReaderWriter headerInfoReaderWriter = new PreorderHeaderInfoReaderWriter();
        rootNode = headerInfoReaderWriter.readHeaderInfo(inputStream);
    }

    @Override
    public String decodeFile(String filePath) throws IOException{
        if (inputStream == null) {
            return null;
        }
        String[] filePathSplit=filePath.split("\\.(?![^\\.]+$)");
        String decompressFilePath=filePathSplit[0];
        OutputStream outputStream = new OutputStream(decompressFilePath + ".unhuf"+".txt");

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