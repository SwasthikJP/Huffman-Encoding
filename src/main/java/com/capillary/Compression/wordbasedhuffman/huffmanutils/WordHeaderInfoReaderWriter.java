package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.utils.Node;
import com.capillary.Compression.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.Compression.utils.*;

import java.io.IOException;

public class WordHeaderInfoReaderWriter implements IHeaderInfoReaderWriter {

    @Override
    public Node readHeaderInfo(ByteInputStream byteInputStream) throws IOException {
        int bit= byteInputStream.getBit();
        if(bit==-1){
            throw new IOException("Incorrect Header");
        }

        if (bit == 0) {
            return new Node(readHeaderInfo(byteInputStream), readHeaderInfo(byteInputStream));
        }
        int wordLength= byteInputStream.getBits(8);
        String word="";
        for(int i=0;i<wordLength;i++){
            word=word+(char) byteInputStream.getBits(8);
        }
        return new Node(word, 0);
    }

    @Override
    public Boolean writeHeaderInfo(Node node, ByteOutputStream byteOutputStream) throws IOException {
        if(node==null){
            return false;
        }
        if (node.isLeafNode) {
            byteOutputStream.writeBit(1);
            String nodeValue=(String) node.value;
            byteOutputStream.writeBits(nodeValue.length(),8);
            for(Character c:nodeValue.toCharArray()){
                byteOutputStream.writeBits((int)c, 8);
            }
        } else {
            byteOutputStream.writeBit(0);
            return  writeHeaderInfo(node.left, byteOutputStream)&&writeHeaderInfo(node.right, byteOutputStream);
        }
        return true;
    }





}
