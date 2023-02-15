package com.capillary.Compression.wordbasedhuffman;

import com.capillary.Compression.huffmanimplementation.huffmanutils.FileHandlerImplementation;
import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.zipper.IZipperApp;

public class App {
    public static void main(String[] args) {

        IZipperApp zipperApp=new WordHuffmanZipperApp();
        IFileHandler fileHandler=new FileHandlerImplementation("/home/swasthikjp/Downloads/pg100.txt","/home/swasthikjp/Downloads/pg100.huf.txt");
        zipperApp.compress(fileHandler);
        System.out.println("hhh");
        IFileHandler fileHandler2=new FileHandlerImplementation("/home/swasthikjp/Downloads/pg100.huf.txt","/home/swasthikjp/Downloads/pg100.unhuf.txt");
        zipperApp.decompress(fileHandler2);
    }
}
