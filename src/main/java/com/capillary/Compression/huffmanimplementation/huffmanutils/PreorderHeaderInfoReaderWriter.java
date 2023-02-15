package com.capillary.Compression.huffmanimplementation.huffmanutils;

import com.capillary.Compression.utils.IHeaderInfoReaderWriter;
import com.capillary.Compression.utils.InputStream;
import com.capillary.Compression.utils.OutputStream;

import java.io.IOException;

public class PreorderHeaderInfoReaderWriter implements IHeaderInfoReaderWriter {
    @Override
    public Node readHeaderInfo(InputStream inputStream) throws IOException {
        int bit=inputStream.getBit();
        if(bit==-1){
            throw new IOException("Incorrect Header");
        }

        if (bit == 0) {
            return new Node(readHeaderInfo(inputStream), readHeaderInfo(inputStream));
        }
        return new Node(inputStream.getBits(9), 0);
    }

    @Override
    public Boolean writeHeaderInfo(Node node, OutputStream outputStream) throws IOException {
        if(node==null){
            return false;
        }
        if (node.isLeafNode) {
            outputStream.writeBit(1);
            outputStream.writeBits(node.value, 9);
        } else {
            outputStream.writeBit(0);
            return  writeHeaderInfo(node.left, outputStream)&&writeHeaderInfo(node.right, outputStream);
        }
        return true;
    }
}
