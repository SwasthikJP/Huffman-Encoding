package com.capillary.Compression.huffmanimplementation;

import com.capillary.Compression.utils.IFileHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

    public byte[] getOutputByteArray(){
        return  outputStream.toByteArray();
    }



}
