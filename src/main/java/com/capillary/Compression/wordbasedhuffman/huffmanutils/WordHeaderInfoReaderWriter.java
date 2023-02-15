package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;
import com.capillary.Compression.utils.*;

import java.io.IOException;

public class WordHeaderInfoReaderWriter implements IHeaderInfoReaderWriter {

    @Override
    public Node readHeaderInfo(InputStream inputStream) throws IOException {
        int bit=inputStream.getBit();
        if(bit==-1){
            throw new IOException("Incorrect Header");
        }

        if (bit == 0) {
            return new Node(readHeaderInfo(inputStream), readHeaderInfo(inputStream));
        }
        int wordLength=inputStream.getBits(8);
        String word="";
        for(int i=0;i<wordLength;i++){
            word=word+(char)inputStream.getBits(8);
        }
        return new Node(word, 0);
    }

    @Override
    public Boolean writeHeaderInfo(Node node, OutputStream outputStream) throws IOException {
        if(node==null){
            return false;
        }
        if (node.isLeafNode) {
            outputStream.writeBit(1);
            String nodeValue=(String) node.value;
            outputStream.writeBits(nodeValue.length(),8);
            for(int i=0;i<nodeValue.length();i++){
                outputStream.writeBits((int)nodeValue.charAt(i), 8);
            }
        } else {
            outputStream.writeBit(0);
            return  writeHeaderInfo(node.left, outputStream)&&writeHeaderInfo(node.right, outputStream);
        }
        return true;
    }





}
