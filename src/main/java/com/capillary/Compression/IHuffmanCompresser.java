package com.capillary.Compression;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHuffmanCompresser {

    Boolean encodeFile(InputStream inputStream, OutputStream outputStream, String[] hashCode,Node rootNode) throws IOException;

    Node createHuffmanTree(int[] frequencyMap);

    String[] generatePrefixCode(Node rootNode);

    int[] calculateCharacterFrequency(InputStream inputStream) throws IOException;

}
