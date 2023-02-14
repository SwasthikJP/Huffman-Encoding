package com.capillary.Compression.huffmanimplementation;

import com.capillary.Compression.utils.IFileHandler;

import java.io.*;

public class FileHandlerImplementation implements IFileHandler {
    String inputFilePath;
    String outputFilePath;

    public FileHandlerImplementation(String inputFilePath,String outputFilePath){
        this.inputFilePath=inputFilePath;
        this.outputFilePath=outputFilePath;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(inputFilePath);
    }

    @Override
    public OutputStream getOutputStream()throws IOException {
        return new FileOutputStream(outputFilePath);
    }


}
