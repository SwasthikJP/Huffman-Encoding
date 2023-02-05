package com.capillary.Compression;
public interface IHeaderInfoReaderWriter {

    Node readHeaderInfo(InputStream inputStream);
    void writeHeaderInfo(Node rootNode, OutputStream outputStream);

}
