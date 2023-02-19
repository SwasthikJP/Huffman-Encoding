package com.capillary.Compression.commonhuffmaninterfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHuffmanDecompresser {

    Object createHuffmanTree(InputStream inputStream) throws IOException;

    Boolean decodeFile(OutputStream outputStream, Object header) throws IOException;

}
