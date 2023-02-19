package com.capillary.Compression.characterbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.utils.ByteOutputStream;

import java.io.IOException;

public class CompressedFileReaderWriterImpl implements ICompressedFileReaderWriter {


    @Override
    public Boolean readCompressedFile(ByteInputStream byteInputStream, ByteOutputStream byteOutputStream, Node rootNode) throws IOException {

        int bit;
        Node node=(Node)rootNode;
        while ((bit = byteInputStream.getBit()) != -1) {

            if(node==null){
                break;
            }

            if (node.isLeafNode) {
                if ((int)node.value == 256) {
                    byteOutputStream.closeStream();
                    return true;
                }
                byteOutputStream.writeByte((int)node.value);
                node =(Node) rootNode;
            }
            if (bit == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (node!=null && node.value!=null && (int)node.value == 256) {
            byteOutputStream.closeStream();
            return true;
        }
        return false;
    }


    @Override
    public Boolean writeCompressedFile(ByteInputStream byteInputStream, ByteOutputStream byteOutputStream, IHashMap hashMap) throws IOException {
        int character;
        String hashCode="";
        while ((character = byteInputStream.getByte()) != -1) {
            if(!hashMap.containsKey(character)){
                return false;
            }
            hashCode=(String) hashMap.get(character);
            byteOutputStream.writeBits(hashCode, hashCode.length());
        }
        if(!hashMap.containsKey(256)){
            return false;
        }
        hashCode=(String)hashMap.get(256);
        byteOutputStream.writeBits(hashCode,hashCode.length());
        return true;
    }


}
