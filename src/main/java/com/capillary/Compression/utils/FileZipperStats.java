package com.capillary.Compression.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.Duration;

public class FileZipperStats implements IZipperStats {

    long startTime;
    long elapsedTime;
    long totalBytesWritten;

    public FileZipperStats(){
        startTime=0;
        elapsedTime=0;
        totalBytesWritten=0;
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

//        System.out.println("Compressed file path is " + compressedFilePath);

        System.out.println("\r \nTime taken for compression: " + duration.toHoursPart() + " : "
                + duration.toMinutesPart() + " : " + duration.toSecondsPart() + " : " + duration.toMillisPart());

           File originalfile = new File(inputFilePath);
           File compressedFile = new File(compressedFilePath);

           if (originalfile.exists() && compressedFile.exists()) {
               System.out.println("Original File Size: " + originalfile.length() + " bytes");
               System.out.println("Compressed File Size: " + compressedFile.length() + " bytes\n");
               System.out.println("Compression ratio is "+(double) originalfile.length()/(double)compressedFile.length());
               System.out.println("Compression Percentage is "+((double)(originalfile.length()-compressedFile.length())/(double) originalfile.length())*100+" %");
           }

    }

    @Override
    public void calculateAverageCodeLength(IHashMap frequencyMap, IHashMap hashCodeMap) {
        double totalCharacter=0;
        double result=0;
        for(Object key:frequencyMap.keySet()){
            if(frequencyMap.containsKey(key)) {
                totalCharacter += (int)frequencyMap.get(key);
                result+=(int)frequencyMap.get(key)*((String)hashCodeMap.get(key)).length();
            }
        }

        System.out.println("Average Code length is "+result/totalCharacter);
    }

    @Override
    public void calcHeaderSize(ByteOutputStream byteOutputStream) {
        totalBytesWritten=byteOutputStream.totalByteWritten();
        System.out.println("Header Size is "+totalBytesWritten +" bytes");
    }

    @Override
    public void calcCompressedBodySize(ByteOutputStream byteOutputStream) {
        System.out.println("Compressed Body Size is "+(byteOutputStream.totalByteWritten()-totalBytesWritten) +" bytes");
        totalBytesWritten=byteOutputStream.totalByteWritten();
    }


    @Override
    public long calcAverageWordLength(IHashMap frequencyMap) {
        long totalWordLength=0;
        for(Object key:frequencyMap.keySet()){
            totalWordLength+=((String)key).length();
        }
        System.out.println("Average word length is "+totalWordLength/ frequencyMap.getSize());
        return totalWordLength/frequencyMap.getSize();
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