package com.capillary.zipper.commonhuffmaninterfaces;
import com.capillary.zipper.utils.IHashMap;
import com.capillary.zipper.utils.Node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHuffmanCompresser {

    IHashMap calculateCharacterFrequency(InputStream inputStream) throws IOException;

    Node createHuffmanTree(IHashMap frequencyMap);

    IHashMap generatePrefixCode(Node rootNode);

    Boolean encodeFile(InputStream inputStream, OutputStream outputStream, IHashMap hashMap, Node rootNode) throws IOException;


}
