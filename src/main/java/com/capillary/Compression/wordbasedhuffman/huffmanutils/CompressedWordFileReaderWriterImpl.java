package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.utils.Node;
import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;

import java.io.IOException;

public class CompressedWordFileReaderWriterImpl implements ICompressedFileReaderWriter {

    @Override
    public Boolean readCompressedFile(ByteInputStream byteInputStream, ByteOutputStream byteOutputStream, Node rootNode) throws IOException {
        int bit;
        Node node=rootNode;
        if(node==null){
            return false;
        }
        while ((bit = byteInputStream.getBit()) != -1) {

            if(node==null){
                break;
            }

            if (node.isLeafNode) {
                if (node.value.equals("{^}")) {
                    byteOutputStream.closeStream();
                    return true;
                }
                String word=(String) node.value;
                for(int i=0;i<word.length();i++){
                    byteOutputStream.writeByte(word.charAt(i));}
                node =(Node) rootNode;
            }
            if (bit == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (node!=null && node.value!=null && node.value.equals("{^}")) {
            byteOutputStream.closeStream();
            return true;
        }
        return false;

    }

    @Override
    public Boolean writeCompressedFile(ByteInputStream byteInputStream, ByteOutputStream byteOutputStream, IHashMap hashMap) throws IOException {
        int character;
        String hashCode="";
        String  temp="";
        while ((character = byteInputStream.getByte()) != -1) {
//            if(character==32 || character==13 || character==10){
            if ((""+(char)character).matches("^[^a-zA-Z0-9]+$")) { //ignore alphaNumeric characters
                if(temp!="") {
                    if(hashMap.containsKey(temp)) {
                        hashCode = (String) hashMap.get(temp);
                        byteOutputStream.writeBits(hashCode, hashCode.length());
                    }else{
                        for(int i=0;i<temp.length();i++){
                            hashCode=(String) hashMap.get(temp.charAt(i)+"");
                            byteOutputStream.writeBits(hashCode,hashCode.length());
                        }
                    }
                    temp="";
                }
                hashCode = (String) hashMap.get((char)character+"");
                byteOutputStream.writeBits(hashCode, hashCode.length());
            }else {
                temp=temp+(char)character;
            }
        }
        if(temp!=""){
            if(hashMap.containsKey(temp)) {
                hashCode = (String) hashMap.get(temp);
                byteOutputStream.writeBits(hashCode, hashCode.length());
            }else{
                for(int i=0;i<temp.length();i++){
                    hashCode=(String) hashMap.get(temp.charAt(i)+"");
                    byteOutputStream.writeBits(hashCode,hashCode.length());
                }
            }
        }
        if(!hashMap.containsKey("{^}")){
            return false;
        }
        hashCode = (String) hashMap.get("{^}");
        byteOutputStream.writeBits(hashCode, hashCode.length());
        return true;
    }


}
