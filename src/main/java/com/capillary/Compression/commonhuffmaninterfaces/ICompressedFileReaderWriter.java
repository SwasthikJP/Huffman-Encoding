package com.capillary.Compression.commonhuffmaninterfaces;


import com.capillary.Compression.utils.Node;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;

import java.io.IOException;

public interface ICompressedFileReaderWriter {

    Boolean readCompressedFile(ByteInputStream byteInputStream, ByteOutputStream byteOutputStream, Node rootNode)throws IOException;

    Boolean writeCompressedFile(ByteInputStream byteInputStream, ByteOutputStream byteOutputStream, IHashMap hashMap)throws IOException;

    }
