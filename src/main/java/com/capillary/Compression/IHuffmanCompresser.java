package com.capillary.Compression;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHuffmanCompresser {

    Boolean encodeFile(InputStream inputStream, OutputStream outputStream) throws IOException;

    void createHuffmanTree();

    void generatePrefixCode();

    void calculateCharacterFrequency(InputStream inputStream) throws IOException;

}
