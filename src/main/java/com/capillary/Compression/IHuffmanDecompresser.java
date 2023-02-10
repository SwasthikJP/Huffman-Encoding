package com.capillary.Compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHuffmanDecompresser {

    Node createHuffmanTree(InputStream inputStream) throws IOException;

    Boolean decodeFile(OutputStream outputStream, Node rootNode) throws IOException;

}
