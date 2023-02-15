package com.capillary.Compression.huffmanimplementation.huffmanutils;

import com.capillary.Compression.utils.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.InputStream;
import com.capillary.Compression.utils.OutputStream;

import java.io.IOException;

public class CompressedFileReaderWriterImpl implements ICompressedFileReaderWriter {
    @Override
    public void writeCompressedFile(InputStream inputStream, OutputStream outputStream, IHashMap hashMap) throws IOException {
        int character;
        String hashCode="";
        while ((character = inputStream.getByte()) != -1) {
            hashCode=(String) hashMap.get(character);
            outputStream.writeBits(hashCode, hashCode.length());
        }
        hashCode=(String)hashMap.get(256);
        outputStream.writeBits(hashCode,hashCode.length());
    }

    @Override
    public Boolean readCompressedFile(InputStream inputStream, OutputStream outputStream, Node rootNode) throws IOException {
        int bit;
        Node node=rootNode;
        while ((bit = inputStream.getBit()) != -1) {


            if (node.isLeafNode) {
                if (node.value == 256) {
                    outputStream.closeStream();
                    return true;
                }
                outputStream.writeByte(node.value);
                node = rootNode;
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
