package com.capillary.Compression.huffmanimplementation;

import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.utils.IFrequencyMap;
import com.capillary.Compression.zipper.IZipperApp;
import com.capillary.Compression.utils.FileZipperStats;
import com.capillary.Compression.utils.IZipperStats;
import com.capillary.Compression.huffmanimplementation.huffmancompression.FrequencyBasedHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmancompression.IHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmandecompression.FrequencyBasedHuffmanDecompresser;
import com.capillary.Compression.huffmanimplementation.huffmandecompression.IHuffmanDecompresser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HuffmanZipperApp implements IZipperApp {


//    public double calculateAverageCode(IFrequencyMap characterFreq,String[] hashCode){
//        double totalCharacter=0;
//        double result=0;
//        for(int i=0;i<characterFreq.getSize();i++){
//            if(characterFreq.get(i)!=0) {
//                totalCharacter += characterFreq.get(i);
//                result+=characterFreq.get(i)*hashCode[i].length();
//            }
//        }
//        return result/totalCharacter;
//    }

    @Override
    public String compress(IFileHandler fileHandler) {
        String compressFilePath="";
//        IZipperStats compressionStats = new FileZipperStats();
//        compressionStats.startTimer();
        try
//                (InputStream fileInputStream=new FileInputStream(filePath);
//             InputStream fileInputStream2=new FileInputStream(filePath)
//        )
        {

            IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();

//            int[] frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileInputStream);
            IFrequencyMap frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileHandler.getInputStream());
            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
            String[] hashCode= huffmanCompresser.generatePrefixCode(rootNode);

//            System.out.println("average code length is "+calculateAverageCode(frequencyMap,hashCode));


//            String[] filePathSplit=filePath.split("\\.(?=[^\\.]+$)");
//            compressFilePath=filePathSplit[0]+".huf.txt";
//            FileOutputStream fileOutputStream=new FileOutputStream(compressFilePath);

            huffmanCompresser.encodeFile(fileHandler.getInputStream(),fileHandler.getOutputStream(),hashCode, rootNode);

//            compressionStats.stopTimer();
//            compressionStats.displayCompressionStats(filePath, compressFilePath);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return compressFilePath;
    }

    @Override
    public String decompress(IFileHandler fileHandler) {
        String decompressFilePath="";
//        IZipperStats compressionStats = new FileZipperStats();
//        compressionStats.startTimer();
        try {

            IHuffmanDecompresser huffmanDecompresser = new FrequencyBasedHuffmanDecompresser();

           Node rootNode=huffmanDecompresser.createHuffmanTree(fileHandler.getInputStream());



            huffmanDecompresser.decodeFile(fileHandler.getOutputStream(),rootNode);

//            compressionStats.stopTimer();
//            compressionStats.displayDecompressionStats(compressFilePath, decompressFilePath);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return decompressFilePath;

    }

}
