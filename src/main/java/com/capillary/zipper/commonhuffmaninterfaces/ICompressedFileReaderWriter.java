package com.capillary.zipper.commonhuffmaninterfaces;


import com.capillary.zipper.utils.Node;
import com.capillary.zipper.utils.IHashMap;
import com.capillary.zipper.utils.ByteInputStream;
import com.capillary.zipper.utils.ByteOutputStream;

import java.io.IOException;

public interface ICompressedFileReaderWriter {

    Boolean readCompressedFile(ByteInputStream byteInputStream, ByteOutputStream byteOutputStream, Node rootNode)throws IOException;

    Boolean writeCompressedFile(ByteInputStream byteInputStream, ByteOutputStream byteOutputStream, IHashMap hashMap)throws IOException;

    }
