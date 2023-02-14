package com.capillary.Compression.utils;

import java.io.File;
import java.time.Duration;

public class FileZipperStats implements IZipperStats {

    long startTime;
    long elapsedTime;

    public FileZipperStats(){
        startTime=0;
        elapsedTime=0;
    }

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        elapsedTime = System.nanoTime() - startTime;
    }

    @Override
    public void displayCompressionStats(String inputFilePath, String compressedFilePath) throws NullPointerException,SecurityException{

        Duration duration = Duration.ofNanos(elapsedTime);

        System.out.println("Compressed file path is " + compressedFilePath);

        System.out.println("\r \nTime taken for compression: " + duration.toHoursPart() + " : "
                + duration.toMinutesPart() + " : " + duration.toSecondsPart() + " : " + duration.toMillisPart());

           File originalfile = new File(inputFilePath);
           File compressedFile = new File(compressedFilePath);

           if (originalfile.exists() && compressedFile.exists()) {
               System.out.println("Original File Size: " + originalfile.length() + " bytes");
               System.out.println("Compressed File Size: " + compressedFile.length() + " bytes\n");
           }

    }

    @Override
    public void displayDecompressionStats(String compressedFilePath, String decompressedFilePath)throws NullPointerException,SecurityException {

        Duration duration = Duration.ofNanos(elapsedTime);

        System.out.println("Decompressed file path is " + decompressedFilePath);

        System.out.println("\r \n Time taken for decompression: " + duration.toHoursPart() + " : "
                + duration.toMinutesPart() + " : " + duration.toSecondsPart() + " : " + duration.toMillisPart());


            File compressedFile = new File(compressedFilePath);
            File decompressedFile = new File(decompressedFilePath);

            if (compressedFile.exists() && decompressedFile.exists()) {
                System.out.println("Compressed File Size: " + compressedFile.length() + " bytes");
                System.out.println("Decompressed File Size: " + decompressedFile.length() + " bytes\n ");
            }


    }

}