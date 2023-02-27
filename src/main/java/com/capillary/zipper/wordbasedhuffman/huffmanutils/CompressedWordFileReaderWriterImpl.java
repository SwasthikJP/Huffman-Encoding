package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.Node;
import com.capillary.zipper.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.zipper.utils.IHashMap;
import com.capillary.zipper.utils.ByteInputStream;
import com.capillary.zipper.utils.ByteOutputStream;

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
                for(Character c:word.toCharArray()){
                    byteOutputStream.writeByte(c);}
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

              if (!Character.isLetterOrDigit((char)character)) { //ignore alphaNumeric characters
//                if(temp!="") {
                    if(hashMap.containsKey(temp)) {
                        hashCode = (String) hashMap.get(temp);
                        byteOutputStream.writeBits(hashCode, hashCode.length());
                    }else{
                        for(Character c:temp.toCharArray()){
                            hashCode=(String) hashMap.get(c+"");
                            byteOutputStream.writeBits(hashCode,hashCode.length());
                        }
                    }
                    temp="";
//                }
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
                for(Character c:temp.toCharArray()){
                    hashCode=(String) hashMap.get(c+"");
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
