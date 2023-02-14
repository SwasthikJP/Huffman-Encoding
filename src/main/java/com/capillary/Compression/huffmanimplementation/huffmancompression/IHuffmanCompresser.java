package com.capillary.Compression.huffmanimplementation.huffmancompression;
import com.capillary.Compression.utils.IFrequencyMap;
import com.capillary.Compression.huffmanimplementation.Node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHuffmanCompresser {

    Boolean encodeFile(InputStream inputStream, OutputStream outputStream, String[] hashCode, Node rootNode) throws IOException;

    Node createHuffmanTree(IFrequencyMap frequencyMap);

    String[] generatePrefixCode(Node rootNode);

    IFrequencyMap calculateCharacterFrequency(InputStream inputStream) throws IOException;

}
