package com.capillary.Compression.huffmanimplementation;

import com.capillary.Compression.utils.IFileHandler;
import org.junit.Test;

import java.io.*;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.FileHandler;

import static org.junit.Assert.*;

public class HuffmanZipperAppTest {

    HuffmanZipperApp huffmanZipperApp;

    @Test
    public void compress_WhenNormalFile_ThenMatchFileContent() throws IOException {
       huffmanZipperApp =new HuffmanZipperApp();
       byte[] content={97,66};
        TestFileHandlerImplementation fileHandler= new TestFileHandlerImplementation(content);
       huffmanZipperApp.compress(fileHandler);
        byte[] expectedByteArray={76,41,11,0,88}; //aB

        assertArrayEquals(expectedByteArray,fileHandler.getOutputByteArray());

    }

    @Test
    public void compress_WhenIncorrectFilePath_ThenCatchFileNotFoundException() throws FileNotFoundException {
        huffmanZipperApp =new HuffmanZipperApp();
        String filePath="IncorrectFilePath.txt";
        IFileHandler fileHandler=new FileHandlerImplementation(filePath,"filePath");
        huffmanZipperApp.compress(fileHandler);
    }

    @Test
    public void decompress_WhenNormalFile_ThenMatchFileContent() throws IOException {

        byte[] fileInputByteArray={76,41,11,0,88};


        huffmanZipperApp =new HuffmanZipperApp();
        TestFileHandlerImplementation fileHandler=new TestFileHandlerImplementation(fileInputByteArray);
        huffmanZipperApp.decompress(fileHandler);
        String decompressedFileContent="aB";

        byte[] expectedFileContent=decompressedFileContent.getBytes(Charset.forName("UTF-8"));
        byte[] resultantFileContent = fileHandler.getOutputByteArray();

        assertEquals(expectedFileContent.length,resultantFileContent.length);
        assertTrue(Arrays.equals(expectedFileContent,resultantFileContent));


    }


    @Test
    public void decompress_WhenIncorrectFilePath_ThenCatchFileNotFoundException() throws FileNotFoundException {
        huffmanZipperApp =new HuffmanZipperApp();
        String filePath="IncorrectFilePath.txt";
        IFileHandler fileHandler=new FileHandlerImplementation(filePath,filePath);
        huffmanZipperApp.decompress(fileHandler);
    }

}