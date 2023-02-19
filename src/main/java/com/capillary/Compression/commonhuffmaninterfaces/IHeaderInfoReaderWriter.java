package com.capillary.Compression.commonhuffmaninterfaces;

import com.capillary.Compression.utils.Node;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;

import java.io.IOException;

public interface IHeaderInfoReaderWriter {

    Node readHeaderInfo(ByteInputStream byteInputStream) throws IOException;
    Boolean writeHeaderInfo(Node node, ByteOutputStream byteOutputStream) throws IOException;
}
