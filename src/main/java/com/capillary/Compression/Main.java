package com.capillary.Compression;
import com.capillary.Compression.huffmanimplementation.FileHandlerImplementation;
import com.capillary.Compression.huffmanimplementation.HuffmanZipperApp;
import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.zipper.IZipperApp;

import java.util.Scanner;
import java.util.logging.FileHandler;

public class Main {

    public static void main(String[] args) {

        int choice;
        Scanner scanner = new Scanner(System.in);
        IZipperApp iApp = new HuffmanZipperApp();
        String filePath;

        while (true) {
            System.out.println("1.Compress a file\n2.Decompress a file\n3.Exit");
            System.out.println("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:

                    System.out.println("Enter the file path for compression");
                    filePath = scanner.next();
            String[] filePathSplit=filePath.split("\\.(?=[^\\.]+$)");
            String compressFilePath=filePathSplit[0]+".huf.txt";
                    IFileHandler fileHandler=new FileHandlerImplementation(filePath,compressFilePath);
                    iApp.compress(fileHandler);

                    break;

                case 2:
                    System.out.println("Enter the file path for decompression");
                    filePath = scanner.next();
                    filePathSplit=filePath.split("\\.(?![^\\.]+$)");
                    String decompressFilePath=filePathSplit[0]+ ".unhuf"+".txt";
                    IFileHandler iFileHandler = new FileHandlerImplementation(filePath,decompressFilePath);
                    iApp.decompress(iFileHandler);
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
