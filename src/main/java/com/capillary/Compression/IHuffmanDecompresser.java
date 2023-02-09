package com.capillary.Compression;

import java.io.IOException;
import java.io.OutputStream;

public interface IHuffmanDecompresser {

    void createHuffmanTree() throws IOException;

    Boolean decodeFile(OutputStream outputStream) throws IOException;

}
