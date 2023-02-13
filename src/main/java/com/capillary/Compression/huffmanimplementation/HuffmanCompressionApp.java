package com.capillary.Compression.huffmanimplementation;

import com.capillary.Compression.compressionApp.ICompressionApp;
import com.capillary.Compression.utils.FileCompressionStats;
import com.capillary.Compression.utils.ICompressionStats;
import com.capillary.Compression.huffmanimplementation.huffmancompression.FrequencyBasedHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmancompression.IHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmandecompression.FrequencyBasedHuffmanDecompresser;
import com.capillary.Compression.huffmanimplementation.huffmandecompression.IHuffmanDecompresser;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HuffmanCompressionApp implements ICompressionApp {


    public double calculateAverageCode(int[] characterFreq,String[] hashCode){
        double totalCharacter=0;
        double result=0;
        for(int i=0;i<characterFreq.length;i++){
            if(characterFreq[i]!=0) {
                totalCharacter += characterFreq[i];
                result+=characterFreq[i]*hashCode[i].length();
            }
        }
        return result/totalCharacter;
    }

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

            System.out.println("average code length is "+calculateAverageCode(frequencyMap,hashCode));


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
