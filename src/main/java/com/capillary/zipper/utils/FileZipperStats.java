package com.capillary.zipper.utils;

import java.io.File;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileZipperStats implements IZipperStats {

    private static Logger LOGGER;
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT.%1$tL] [%4$-7s] %5$s %n");
        LOGGER = Logger.getLogger(FileZipperStats.class.getName());
        LOGGER.setLevel(Level.ALL);
    }

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

           File originalfile = new File(inputFilePath);
           File compressedFile = new File(compressedFilePath);

           if (originalfile.exists() && compressedFile.exists()) {
               LOGGER.info("Text File Size: " + originalfile.length() + " bytes");
               LOGGER.info("Compressed File Size: " + compressedFile.length() + " bytes\n");
               LOGGER.info("Compression ratio is "+(float)((double) originalfile.length()/(double)compressedFile.length()));
               LOGGER.info("Compression Percentage is "+(float)(((double)(originalfile.length()-compressedFile.length())/(double) originalfile.length())*100)+" %");
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


        LOGGER.info("Average Code length is "+(int)(result/totalCharacter));
    }

    @Override
    public void calcHeaderSize(ByteOutputStream byteOutputStream) {
        totalBytesWritten=byteOutputStream.totalByteWritten();
        LOGGER.info("Header Size is "+totalBytesWritten +" bytes");
    }

    @Override
    public void calcCompressedBodySize(ByteOutputStream byteOutputStream) {
        LOGGER.info("Compressed Body Size is "+(byteOutputStream.totalByteWritten()-totalBytesWritten) +" bytes");
        totalBytesWritten=byteOutputStream.totalByteWritten();
    }


    @Override
    public long calcAverageWordLength(IHashMap frequencyMap) {
        long totalWordLength=0;
        for(Object key:frequencyMap.keySet()){
            totalWordLength+=((String)key).length();
        }
        LOGGER.info("Average word length is "+totalWordLength/ frequencyMap.getSize());

        return totalWordLength/frequencyMap.getSize();
    }

    @Override
    public void displayTimeTaken(String methodName) {
        Duration duration = Duration.ofNanos(elapsedTime);
        LOGGER.log(Level.INFO, "Time taken for "+methodName+" is "+duration.toSecondsPart() + "s : " + duration.toMillisPart()+" ms");

    }

    @Override
    public void displayNewLine() {
        LOGGER.log(Level.INFO, "\n");
    }

}