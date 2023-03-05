package com.capillary.zipper.characterbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.IFileHandler;

import java.io.*;

public class TestFileHandlerImplementation implements IFileHandler {

    byte[] content;
    public ByteArrayOutputStream outputStream;

    public TestFileHandlerImplementation(byte[] content){
        this.content=content;
        outputStream=new ByteArrayOutputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public InputStream getInputStreamOfOutputFile() throws IOException {
        return new ByteArrayInputStream(content);
    }

    public byte[] getOutputByteArray(){
        return  outputStream.toByteArray();
    }



}
