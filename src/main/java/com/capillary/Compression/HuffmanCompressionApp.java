package com.capillary.Compression;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HuffmanCompressionApp implements ICompressionApp {

    @Override
    public String compress(String filePath) {
        String compressFilePath="";
        ICompressionStats compressionStats = new FileCompressionStats();
        compressionStats.startTimer();
        try {
            FileInputStream fileInputStream=new FileInputStream(filePath);
            IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();
            huffmanCompresser.calculateCharacterFrequency(fileInputStream);
            huffmanCompresser.createHuffmanTree();
            huffmanCompresser.generatePrefixCode();
            FileInputStream fileInputStream2=new FileInputStream(filePath);
            compressFilePath = huffmanCompresser.encodeFile(fileInputStream2,filePath);
            compressionStats.stopTimer();
            compressionStats.displayCompressionStats(filePath, compressFilePath);
            System.out.println("Compressed file path is " + compressFilePath);
        }
        catch (FileNotFoundException e){
            compressionStats.stopTimer();
            e.printStackTrace();

        }
        catch (IOException e){
            compressionStats.stopTimer();
            e.printStackTrace();
        }
        return compressFilePath;
    }

    @Override
    public String decompress(String compressFilepath) {
        String decompressFilepath="";
        ICompressionStats compressionStats = new FileCompressionStats();
        compressionStats.startTimer();
        try {
            FileInputStream fileInputStream=new FileInputStream(compressFilepath);
            IHuffmanDecompresser huffmanDecompresser = new FrequencyBasedHuffmanDecompresser(fileInputStream);
            huffmanDecompresser.createHuffmanTree();
            decompressFilepath = huffmanDecompresser.decodeFile(compressFilepath);

            compressionStats.stopTimer();
            compressionStats.displayDecompressionStats(compressFilepath, decompressFilepath);

            System.out.println("Decompressed file path is " + decompressFilepath);
        }
        catch (FileNotFoundException e){
            compressionStats.stopTimer();
            e.printStackTrace();
        }
        catch (IOException e){
            compressionStats.stopTimer();
            e.printStackTrace();
        }
        return decompressFilepath;

    }

}
