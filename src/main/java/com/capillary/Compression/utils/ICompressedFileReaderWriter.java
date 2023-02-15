package com.capillary.Compression.utils;

import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;

import java.io.IOException;

public interface ICompressedFileReaderWriter {

    void writeCompressedFile(InputStream inputStream, OutputStream outputStream, IHashMap hashMap)throws IOException;

    Boolean readCompressedFile(InputStream inputStream, OutputStream outputStream, Node node)throws IOException;

    }
