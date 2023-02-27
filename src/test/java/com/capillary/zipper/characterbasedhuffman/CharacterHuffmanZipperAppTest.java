package com.capillary.zipper.characterbasedhuffman;

import com.capillary.zipper.characterbasedhuffman.compression.CharacterBasedHuffmanCompresser;
import com.capillary.zipper.characterbasedhuffman.decompression.CharacterBasedHuffmanDecompresser;
import com.capillary.zipper.characterbasedhuffman.huffmanutils.TestFileHandlerImplementation;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.zipper.utils.IHashMap;
import com.capillary.zipper.utils.Node;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.HashMapImpl;
import com.capillary.zipper.zipper.IZipperApp;
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