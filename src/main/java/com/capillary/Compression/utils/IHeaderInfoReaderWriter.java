package com.capillary.Compression.utils;

import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;

import java.io.IOException;

public interface IHeaderInfoReaderWriter {

    Node readHeaderInfo(InputStream inputStream) throws IOException;
    Boolean writeHeaderInfo(Node node, OutputStream outputStream) throws IOException;
}
