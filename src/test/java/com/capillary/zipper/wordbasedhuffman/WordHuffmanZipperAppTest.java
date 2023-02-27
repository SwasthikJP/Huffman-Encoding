package com.capillary.zipper.wordbasedhuffman;

import com.capillary.zipper.characterbasedhuffman.huffmanutils.TestFileHandlerImplementation;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.zipper.utils.IHashMap;
import com.capillary.zipper.utils.Node;
import com.capillary.zipper.wordbasedhuffman.compression.WordBasedHuffmanCompresser;
import com.capillary.zipper.wordbasedhuffman.decompression.WordBasedHuffmanDecompresser;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.HashMapImpl;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.SimulatedAnnealing;
import com.capillary.zipper.zipper.IZipperApp;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WordHuffmanZipperAppTest {

 IZipperApp zipperApp;

    @Test
    public void compress_UsingTestFileHandler() throws Exception {



        IHuffmanCompresser huffmanCompresser= spy(WordBasedHuffmanCompresser.class);
        doReturn(new Node()).when(huffmanCompresser).createHuffmanTree(any(IHashMap.class));
        doReturn(new HashMapImpl()).when(huffmanCompresser).calculateCharacterFrequency(any(InputStream.class));
        doReturn(new HashMapImpl()).when(huffmanCompresser).generatePrefixCode(any(Node.class));
        doReturn(true).when(huffmanCompresser).encodeFile(any(InputStream.class),any(OutputStream.class),any(IHashMap.class),any(Node.class));


        SimulatedAnnealing simulatedAnnealing=spy(SimulatedAnnealing.class);
        doReturn(new HashMapImpl()).when(simulatedAnnealing).calculateIdealSplit(any(IHashMap.class));


        IHuffmanDecompresser huffmanDecompresser=spy(WordBasedHuffmanDecompresser.class);
        zipperApp=new WordHuffmanZipperApp(huffmanCompresser,huffmanDecompresser,simulatedAnnealing);
        byte[] content={};
        TestFileHandlerImplementation fileHandler= new TestFileHandlerImplementation(content);
        zipperApp.compress(fileHandler);

    }

    @Test
    public void decompress_usingTestFileHandler() throws Exception{


        IHuffmanCompresser huffmanCompresser= spy(WordBasedHuffmanCompresser.class);

        IHuffmanDecompresser huffmanDecompresser=spy(WordBasedHuffmanDecompresser.class);
        doReturn(new Node()).when(huffmanDecompresser).createHuffmanTree(any(InputStream.class));
        doReturn(true).when(huffmanDecompresser).decodeFile(any(OutputStream.class),any(Node.class));

        SimulatedAnnealing simulatedAnnealing=spy(SimulatedAnnealing.class);

        zipperApp=new WordHuffmanZipperApp(huffmanCompresser,huffmanDecompresser,simulatedAnnealing);
        byte[] content={};
        TestFileHandlerImplementation fileHandler= new TestFileHandlerImplementation(content);
         zipperApp.decompress(fileHandler);
    }
}