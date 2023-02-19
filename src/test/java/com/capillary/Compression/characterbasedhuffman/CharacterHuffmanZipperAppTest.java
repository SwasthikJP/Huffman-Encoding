package com.capillary.Compression.characterbasedhuffman;

import com.capillary.Compression.characterbasedhuffman.compression.CharacterBasedHuffmanCompresser;
import com.capillary.Compression.characterbasedhuffman.decompression.CharacterBasedHuffmanDecompresser;
import com.capillary.Compression.characterbasedhuffman.huffmanutils.TestFileHandlerImplementation;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.HashMapImpl;
import com.capillary.Compression.zipper.IZipperApp;
import org.junit.Test;

import java.io.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class CharacterHuffmanZipperAppTest {

    IZipperApp zipperApp;

    @Test
    public void compress_UsingTestFileHandler() throws Exception {

        IHuffmanCompresser huffmanCompresser= spy(CharacterBasedHuffmanCompresser.class);
        doReturn(new Node()).when(huffmanCompresser).createHuffmanTree(any(IHashMap.class));
        doReturn(new HashMapImpl()).when(huffmanCompresser).calculateCharacterFrequency(any(InputStream.class));
        doReturn(new HashMapImpl()).when(huffmanCompresser).generatePrefixCode(any(Node.class));
        doReturn(true).when(huffmanCompresser).encodeFile(any(InputStream.class),any(OutputStream.class),any(IHashMap.class),any(Node.class));

        IHuffmanDecompresser huffmanDecompresser=spy(CharacterBasedHuffmanDecompresser.class);
        zipperApp=new CharacterHuffmanZipperApp(huffmanCompresser,huffmanDecompresser);
        byte[] content={};
        TestFileHandlerImplementation fileHandler= new TestFileHandlerImplementation(content);
        zipperApp.compress(fileHandler);

    }

    @Test
    public void decompress_usingTestFileHandler() throws Exception{


        IHuffmanCompresser huffmanCompresser= spy(CharacterBasedHuffmanCompresser.class);

        IHuffmanDecompresser huffmanDecompresser=spy(CharacterBasedHuffmanDecompresser.class);
        doReturn(new Node()).when(huffmanDecompresser).createHuffmanTree(any(InputStream.class));
        doReturn(true).when(huffmanDecompresser).decodeFile(any(OutputStream.class),any(Node.class));

        zipperApp=new CharacterHuffmanZipperApp(huffmanCompresser,huffmanDecompresser);
        byte[] content={};
        TestFileHandlerImplementation fileHandler= new TestFileHandlerImplementation(content);
        zipperApp.decompress(fileHandler);
    }

}