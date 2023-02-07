package com.capillary.Compression;
import java.io.IOException;
import java.io.InputStream;

public interface IHuffmanCompresser {

    String encodeFile(InputStream inputStream,String filePath) throws IOException;

    void createHuffmanTree();

    void generatePrefixCode();

    void calculateCharacterFrequency(InputStream inputStream) throws IOException;

}
