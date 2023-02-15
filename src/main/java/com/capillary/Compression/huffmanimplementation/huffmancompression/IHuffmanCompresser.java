package com.capillary.Compression.huffmanimplementation.huffmancompression;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHuffmanCompresser {

    Boolean encodeFile(InputStream inputStream, OutputStream outputStream, IHashMap hashMap, Node rootNode) throws IOException;

    Node createHuffmanTree(IHashMap frequencyMap);

    IHashMap generatePrefixCode(Node rootNode);

    IHashMap calculateCharacterFrequency(InputStream inputStream) throws IOException;

}
