package com.capillary.Compression.wordbasedhuffman;

import com.capillary.Compression.huffmanimplementation.huffmanutils.FileHandlerImplementation;
import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.zipper.IZipperApp;

public class App {
    public static void main(String[] args) {

        IZipperApp zipperApp=new WordHuffmanZipperApp();
        IFileHandler fileHandler=new FileHandlerImplementation("/home/swasthikjp/Downloads/pg100.txt","/home/swasthikjp/Downloads/pg100.huf.txt");
//        IFileHandler fileHandler=new FileHandlerImplementation("test.txt","test.huf.txt");

        zipperApp.compress(fileHandler);
//        System.out.println("hhh");
        IFileHandler fileHandler2=new FileHandlerImplementation("/home/swasthikjp/Downloads/pg100.huf.txt","/home/swasthikjp/Downloads/pg100.unhuf.txt");
//        IFileHandler fileHandler2=new FileHandlerImplementation("test.huf.txt","test.unhuf.txt");

        zipperApp.decompress(fileHandler2);
    }
}
