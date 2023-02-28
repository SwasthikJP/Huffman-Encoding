package com.capillary.zipper;
import com.capillary.zipper.utils.FileHandlerImplementation;
import com.capillary.zipper.characterbasedhuffman.CharacterHuffmanZipperApp;
import com.capillary.zipper.utils.FileZipperStats;
import com.capillary.zipper.utils.IFileHandler;
import com.capillary.zipper.utils.IZipperStats;
import com.capillary.zipper.wordbasedhuffman.WordHuffmanZipperApp;
import com.capillary.zipper.zipper.IZipperApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger LOGGER = Logger.getLogger(Main.class.getName());
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT.%1$tL] [%4$-7s] %5$s %n");
        LOGGER = Logger.getLogger(Main.class.getName());

    }

        public static void main(String[] args) throws FileNotFoundException, FileAlreadyExistsException {
            long start = System.currentTimeMillis();
            LOGGER.setLevel(Level.CONFIG);
            Runtime runtime = Runtime.getRuntime();


            //check if minimum number of arguments are available for the program
            if (args.length < 4) {
                System.err.println("Usage: java main  -option input_file output_file");
                System.exit(1);
                return;
            }

            File ipFile = new File(args[args.length - 2]);
            File opFile = new File(args[args.length - 1]);
            if (!ipFile.exists()) throw new FileNotFoundException("ERROR: File Not Found");
            if (opFile.exists()) throw new FileAlreadyExistsException("ERROR: File Already Exists");
            LOGGER.log(Level.INFO, "File Check Completed. Execution Started");
            if (args[1].equals("-s") || args[1].equals("--static")) {
                IZipperApp iApp = new CharacterHuffmanZipperApp();
                IZipperStats compressionStats = new FileZipperStats();
                if ((args[0].equals("-c") || args[0].equals("--compress")) &&
                        (args[1].equals("-s") || args[1].equals("--static"))) {
                    compressionStats.startTimer();
                    IFileHandler fileHandler = new FileHandlerImplementation(ipFile.getPath(), opFile.getPath());
                    iApp.compress(fileHandler);
                    compressionStats.stopTimer();
                    compressionStats.displayCompressionStats(ipFile.getPath(), opFile.getPath());
                } else if (args[0].equals("-d") || args[0].equals("--decompress")) {
                    compressionStats.startTimer();
                    IFileHandler iFileHandler = new FileHandlerImplementation(ipFile.getPath(), opFile.getPath());
                    iApp.decompress(iFileHandler);

                    compressionStats.stopTimer();
                    compressionStats.displayTimeTaken("Decompression");
                }
            }


            if ((args[1].equals("-w") || args[1].equals("--word"))) {
                IZipperStats compressionStats = new FileZipperStats();
                IZipperApp zipperApp = new WordHuffmanZipperApp();
                if ((args[0].equals("-c") || args[0].equals("--compress"))) {
                    IFileHandler fileHandler = new FileHandlerImplementation(ipFile.getPath(), opFile.getPath());

//        IFileHandler fileHandler=new FileHandlerImplementation("test.txt","test.huf.txt");
                    compressionStats.startTimer();
                    zipperApp.compress(fileHandler);
                    compressionStats.stopTimer();
                    compressionStats.displayCompressionStats(ipFile.getPath(),opFile.getPath());
                } else if (args[0].equals("-d") || args[0].equals("--decompress")) {
                    IFileHandler fileHandler2 = new FileHandlerImplementation(ipFile.getPath(), opFile.getPath());

//        IFileHandler fileHandler2=new FileHandlerImplementation("test.huf.txt","test.unhuf.txt");
                    compressionStats.startTimer();
                    zipperApp.decompress(fileHandler2);
                    compressionStats.stopTimer();
                    compressionStats.displayTimeTaken("decompression");
                }

            }

            LOGGER.log(Level.INFO, "Process Completed. Execution Stopped");

        }
}
