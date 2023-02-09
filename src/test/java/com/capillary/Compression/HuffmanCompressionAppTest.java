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
        File file=new File(compressedFilepath);
        file.delete();
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
        File file=new File("createHuffmanTreeNormal.huf.txt");
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        byte[] fileInputByteArray={76,41,11,0,88};
        fileOutputStream.write(fileInputByteArray);
//        fileOutputStream.close();

        huffmanCompressionApp=new HuffmanCompressionApp();
        String decompressedFilePath= huffmanCompressionApp.decompress("createHuffmanTreeNormal.huf.txt");
        String decompressedFileContent="aB";

        File decompresedTestFile=new File(decompressedFilePath);

        byte[] expectedFileContent=decompressedFileContent.getBytes(Charset.forName("UTF-8"));
        byte[] resultantFileContent = (new FileInputStream(decompresedTestFile).readAllBytes());

        assertEquals(expectedFileContent.length,resultantFileContent.length);
        assertTrue(Arrays.equals(expectedFileContent,resultantFileContent));

        file.delete();
        decompresedTestFile.delete();
    }

}