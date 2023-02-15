package com.capillary.Compression.utils;

import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;

import java.io.IOException;

public interface IHeaderInfoReaderWriter2 {

    IHashMap readHeaderInfo(InputStream inputStream) throws Exception;
    Boolean writeHeaderInfo(Object hashMap, OutputStream outputStream) throws IOException;
}
