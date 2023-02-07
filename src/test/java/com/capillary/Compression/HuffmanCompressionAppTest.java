package com.capillary.Compression;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;

public class HuffmanCompressionAppTest {

    HuffmanCompressionApp huffmanCompressionApp;

    @Test
    public void compress_WhenNormalFile_ThenMatchFileContent() throws IOException {
       huffmanCompressionApp=new HuffmanCompressionApp();
       String filePath="createHuffmanTreeNormal.txt";
       String compressedFilepath= huffmanCompressionApp.compress(filePath);
        byte[] expectedByteArray={76,41,11,0,88}; //aB
        InputStream inputStream=new FileInputStream(compressedFilepath);
        byte[] compressedByteArray=inputStream.readAllBytes();
        assertArrayEquals(expectedByteArray,compressedByteArray);
    }

//    @Test(expected = FileNotFoundException.class)
//    public void compress_WhenIncorrectFilePath_ThenCatchFileNotFoundException() throws IOException {
//        huffmanCompressionApp=new HuffmanCompressionApp();
//        String filePath="IncorrectFilePath.txt";
//        String compressedFilepath= huffmanCompressionApp.compress(filePath);
//
//    }

    @Test
    public void decompress_WhenNormalFile_ThenMatchFileContent() throws IOException {

        huffmanCompressionApp=new HuffmanCompressionApp();
        String decompressedFilePath= huffmanCompressionApp.decompress("createHuffmanTreeNormal.huf.txt");
        String decompressedFile="aB";

        byte[] expectedFileContent=decompressedFile.getBytes(Charset.forName("UTF-8"));
        byte[] resultantFileContent = (new FileInputStream(decompressedFilePath).readAllBytes());

        assertEquals(expectedFileContent.length,resultantFileContent.length);
        assertTrue(Arrays.equals(expectedFileContent,resultantFileContent));
    }

}