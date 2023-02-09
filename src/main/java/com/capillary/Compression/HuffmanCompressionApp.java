package com.capillary.Compression;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
            String[] filePathSplit=filePath.split("\\.(?=[^\\.]+$)");
            compressFilePath=filePathSplit[0]+".huf.txt";
            FileOutputStream fileOutputStream=new FileOutputStream(compressFilePath);
            if(huffmanCompresser.encodeFile(fileInputStream2,fileOutputStream)){
                System.out.println("Compressed file path is " + compressFilePath);
            }
            compressionStats.stopTimer();
            compressionStats.displayCompressionStats(filePath, compressFilePath);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return compressFilePath;
    }

    @Override
    public String decompress(String compressFilePath) {
        String decompressFilePath="";
        ICompressionStats compressionStats = new FileCompressionStats();
        compressionStats.startTimer();
        try {
            FileInputStream fileInputStream=new FileInputStream(compressFilePath);
            IHuffmanDecompresser huffmanDecompresser = new FrequencyBasedHuffmanDecompresser(fileInputStream);
            huffmanDecompresser.createHuffmanTree();


            String[] filePathSplit=compressFilePath.split("\\.(?![^\\.]+$)");
            decompressFilePath=filePathSplit[0]+ ".unhuf"+".txt";
            FileOutputStream fileOutputStream=new FileOutputStream(decompressFilePath);

            if(huffmanDecompresser.decodeFile(fileOutputStream)){
                System.out.println("Decompressed file path is " + decompressFilePath);
            }
            compressionStats.stopTimer();
            compressionStats.displayDecompressionStats(compressFilePath, decompressFilePath);

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return decompressFilePath;

    }

}
