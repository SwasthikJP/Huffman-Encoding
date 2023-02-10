package com.capillary.Compression;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HuffmanCompressionApp implements ICompressionApp {

    @Override
    public String compress(String filePath) {
        String compressFilePath="";
        ICompressionStats compressionStats = new FileCompressionStats();
        compressionStats.startTimer();
        try (FileInputStream fileInputStream=new FileInputStream(filePath);
             FileInputStream fileInputStream2=new FileInputStream(filePath)
        ){

            IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();

            int[] frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileInputStream);
            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
            String[] hashCode= huffmanCompresser.generatePrefixCode(rootNode);


            String[] filePathSplit=filePath.split("\\.(?=[^\\.]+$)");
            compressFilePath=filePathSplit[0]+".huf.txt";
            FileOutputStream fileOutputStream=new FileOutputStream(compressFilePath);

            huffmanCompresser.encodeFile(fileInputStream2,fileOutputStream,hashCode, rootNode);

            compressionStats.stopTimer();
            compressionStats.displayCompressionStats(filePath, compressFilePath);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return compressFilePath;
    }

    @Override
    public String decompress(String compressFilePath) {
        String decompressFilePath="";
        ICompressionStats compressionStats = new FileCompressionStats();
        compressionStats.startTimer();
        try (
                FileInputStream fileInputStream=new FileInputStream(compressFilePath)
                ){

            IHuffmanDecompresser huffmanDecompresser = new FrequencyBasedHuffmanDecompresser();

           Node rootNode=huffmanDecompresser.createHuffmanTree(fileInputStream);

            String[] filePathSplit=compressFilePath.split("\\.(?![^\\.]+$)");
            decompressFilePath=filePathSplit[0]+ ".unhuf"+".txt";
            FileOutputStream fileOutputStream=new FileOutputStream(decompressFilePath);

            huffmanDecompresser.decodeFile(fileOutputStream,rootNode);

            compressionStats.stopTimer();
            compressionStats.displayDecompressionStats(compressFilePath, decompressFilePath);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return decompressFilePath;

    }

}
