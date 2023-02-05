package com.capillary.Compression;
import java.io.IOException;

public interface IHuffmanCompresser {

    String encodeFile(String filePath) throws IOException;

    void createHuffmanTree();

    void generatePrefixCode();

    void calculateCharacterFrequency(String filePath) throws IOException;

}
