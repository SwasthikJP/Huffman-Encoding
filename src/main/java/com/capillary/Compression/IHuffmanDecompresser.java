package com.capillary.Compression;

import java.io.IOException;

public interface IHuffmanDecompresser {

    void createHuffmanTree();

    String decodeFile(String filePath) throws IOException;

}
