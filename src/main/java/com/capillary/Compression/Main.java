package com.capillary.Compression;
import com.capillary.Compression.characterbasedhuffman.huffmanutils.FileHandlerImplementation;
import com.capillary.Compression.characterbasedhuffman.CharacterHuffmanZipperApp;
import com.capillary.Compression.utils.FileZipperStats;
import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.utils.IZipperStats;
import com.capillary.Compression.zipper.IZipperApp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int choice;
        Scanner scanner = new Scanner(System.in);
        IZipperApp iApp = new CharacterHuffmanZipperApp();
        String filePath;

        while (true) {
            System.out.println("1.Compress a file\n2.Decompress a file\n3.Exit");
            System.out.println("Enter your choice: ");

            choice = scanner.nextInt();
            IZipperStats compressionStats = new FileZipperStats();
            switch (choice) {
                case 1:

                    System.out.println("Enter the file path for compression");
                    filePath = scanner.next();
                    String[] filePathSplit=filePath.split("\\.(?=[^\\.]+$)");
                    String compressFilePath=filePathSplit[0]+".huf.txt";

                    compressionStats.startTimer();
                    IFileHandler fileHandler=new FileHandlerImplementation(filePath,compressFilePath);
                    iApp.compress(fileHandler);
                    compressionStats.stopTimer();
                    compressionStats.displayCompressionStats(filePath, compressFilePath);
                    break;

                case 2:
                    System.out.println("Enter the file path for decompression");
                    filePath = scanner.next();
                    filePathSplit=filePath.split("\\.(?![^\\.]+$)");
                    String decompressFilePath=filePathSplit[0]+ ".unhuf"+".txt";


                    compressionStats.startTimer();
                    IFileHandler iFileHandler = new FileHandlerImplementation(filePath,decompressFilePath);
                    iApp.decompress(iFileHandler);

                    compressionStats.stopTimer();
                    compressionStats.displayDecompressionStats(filePath, decompressFilePath);
                    break;

                case 3:
                    break;

                default:
                    System.out.println("Invalid Input");
                    break;
            }
            if (choice == 3) {
                break;
            }

        }

//        IZipperApp zipperApp=new HuffmanZipperApp();
//        zipperApp.compress("tt.txt");

    }
}
