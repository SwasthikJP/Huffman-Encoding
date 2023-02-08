package com.capillary.Compression;

import java.io.IOException;

public interface IHuffmanDecompresser {

    void createHuffmanTree() throws IOException;

    String decodeFile(String filePath) throws IOException;

}
