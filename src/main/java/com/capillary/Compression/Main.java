package com.capillary.Compression;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int choice;
        Scanner scanner = new Scanner(System.in);
        ICompressionApp iApp = new HuffmanCompressionApp();
        String filePath;

        while (true) {
            System.out.println("1.Compress a file\n2.Decompress a file\n3.Exit");
            System.out.println("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:

                    System.out.println("Enter the file path for compression");
                    filePath = scanner.next();
                    iApp.compress(filePath);

                    break;

                case 2:
                    System.out.println("Enter the file path for decompression");
                    filePath = scanner.next();
                    iApp.decompress(filePath);
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

    }
}
