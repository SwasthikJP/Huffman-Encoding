package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;
import com.capillary.Compression.utils.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.InputStream;
import com.capillary.Compression.utils.OutputStream;

import java.io.IOException;

public class CompressedWordFileReaderWriterImpl implements ICompressedFileReaderWriter {
    @Override
    public void writeCompressedFile(InputStream inputStream, OutputStream outputStream, IHashMap hashMap) throws IOException {
        int character;
        String hashCode="";
        String  temp="";
        while ((character = inputStream.getByte()) != -1) {
//            if(character==32 || character==13 || character==10){
            if ((""+(char)character).matches("^[^a-zA-Z0-9]+$")) {
                if(temp!="") {
                    if(hashMap.containsKey(temp)) {
                        hashCode = (String) hashMap.get(temp);
                        outputStream.writeBits(hashCode, hashCode.length());
                    }else{
                        for(int i=0;i<temp.length();i++){
                            hashCode=(String) hashMap.get(temp.charAt(i)+"");
                            outputStream.writeBits(hashCode,hashCode.length());
                        }
                    }
                    temp="";
                }
                hashCode = (String) hashMap.get((char)character+"");
                outputStream.writeBits(hashCode, hashCode.length());
            }else {
                temp=temp+(char)character;
            }
        }
        if(temp!=""){
            if(hashMap.containsKey(temp)) {
                hashCode = (String) hashMap.get(temp);
                outputStream.writeBits(hashCode, hashCode.length());
            }else{
                for(int i=0;i<temp.length();i++){
                    hashCode=(String) hashMap.get(temp.charAt(i)+"");
                    outputStream.writeBits(hashCode,hashCode.length());
                }
            }
        }
        hashCode = (String) hashMap.get("{^}");
        outputStream.writeBits(hashCode, hashCode.length());
    }

    @Override
    public Boolean readCompressedFile(InputStream inputStream, OutputStream outputStream, Node rootNode) throws IOException {
        int bit;
        Node node=rootNode;
        while ((bit = inputStream.getBit()) != -1) {


            if (node.isLeafNode) {
                if (node.value.equals("{^}")) {
                    outputStream.closeStream();
                    return true;
                }
                String word=(String) node.value;
                for(int i=0;i<word.length();i++){
                   outputStream.writeByte(word.charAt(i));}
                node =(Node) rootNode;
            }
            if (bit == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;

    }

}
