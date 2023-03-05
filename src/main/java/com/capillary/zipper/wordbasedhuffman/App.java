package com.capillary.zipper.wordbasedhuffman;

import com.capillary.zipper.utils.FileHandlerImplementation;
import com.capillary.zipper.utils.FileZipperStats;
import com.capillary.zipper.utils.IFileHandler;
import com.capillary.zipper.utils.IZipperStats;
import com.capillary.zipper.zipper.IZipperApp;

public class App {
    public static void main(String[] args)throws Exception {

        IZipperApp zipperApp=new WordHuffmanZipperApp();
//        IFileHandler fileHandler=new FileHandlerImplementation("/home/swasthikjp/Downloads/pg100.txt","/home/swasthikjp/Downloads/pg100.huf.txt");
        IFileHandler fileHandler=new FileHandlerImplementation("/home/swasthikjp/Downloads/bigfile.txt","/home/swasthikjp/Downloads/bigfile.huf.txt");

//        IFileHandler fileHandler=new FileHandlerImplementation("test.txt","test.huf.txt");
        IZipperStats compressionStats = new FileZipperStats();
        compressionStats.startTimer();
        zipperApp.compress(fileHandler);
                    compressionStats.stopTimer();
        compressionStats.displayTimeTaken("compression");
            compressionStats.displayCompressionStats("/home/swasthikjp/Downloads/bigfile.txt", "/home/swasthikjp/Downloads/bigfile.huf.txt");

//        IFileHandler fileHandler2=new FileHandlerImplementation("/home/swasthikjp/Downloads/pg100.huf.txt","/home/swasthikjp/Downloads/pg100.unhuf.txt");
        IFileHandler fileHandler2=new FileHandlerImplementation("/home/swasthikjp/Downloads/bigfile.huf.txt","/home/swasthikjp/Downloads/bigfile.unhuf.txt");

//        IFileHandler fileHandler2=new FileHandlerImplementation("test.huf.txt","test.unhuf.txt");
     compressionStats.startTimer();
        zipperApp.decompress(fileHandler2);
        compressionStats.stopTimer();
        compressionStats.displayTimeTaken("decompression");
    }
}
