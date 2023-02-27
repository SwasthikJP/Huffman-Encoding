package com.capillary.zipper.characterbasedhuffman.huffmanutils;

import com.capillary.zipper.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.zipper.utils.ByteInputStream;
import com.capillary.zipper.utils.Node;
import com.capillary.zipper.utils.ByteOutputStream;

import java.io.IOException;

public class PreorderHeaderInfoReaderWriter implements IHeaderInfoReaderWriter {
    @Override
    public Node readHeaderInfo(ByteInputStream byteInputStream) throws IOException {
        int bit= byteInputStream.getBit();
        if(bit==-1){
            throw new IOException("Incorrect Header");
        }

        if (bit == 0) {
            return new Node(readHeaderInfo(byteInputStream), readHeaderInfo(byteInputStream));
        }
        return new Node(byteInputStream.getBits(9), 0);
    }

    @Override
    public Boolean writeHeaderInfo(Node node, ByteOutputStream byteOutputStream) throws IOException {
        if(node==null){
            return false;
        }
        if (node.isLeafNode) {
            byteOutputStream.writeBit(1);
            byteOutputStream.writeBits((int)node.value, 9);
        } else {
            byteOutputStream.writeBit(0);
            return  writeHeaderInfo(node.left, byteOutputStream)&&writeHeaderInfo(node.right, byteOutputStream);
        }
        return true;
    }
}
