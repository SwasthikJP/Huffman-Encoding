package com.capillary.zipper.commonhuffmaninterfaces;

import com.capillary.zipper.utils.Node;
import com.capillary.zipper.utils.ByteInputStream;
import com.capillary.zipper.utils.ByteOutputStream;

import java.io.IOException;

public interface IHeaderInfoReaderWriter {

    Node readHeaderInfo(ByteInputStream byteInputStream) throws IOException;
    Boolean writeHeaderInfo(Node node, ByteOutputStream byteOutputStream) throws IOException;
}
