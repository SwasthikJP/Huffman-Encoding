package com.capillary.Compression.huffmanimplementation;

import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.zipper.IZipperApp;
import com.capillary.Compression.huffmanimplementation.huffmancompression.FrequencyBasedHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmancompression.IHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmandecompression.FrequencyBasedHuffmanDecompresser;
import com.capillary.Compression.huffmanimplementation.huffmandecompression.IHuffmanDecompresser;

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
    public void compress(IFileHandler fileHandler) {

//        IZipperStats compressionStats = new FileZipperStats();
//        compressionStats.startTimer();
        try {

            IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();

            IHashMap frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileHandler.getInputStream());
            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
            IHashMap hashMap= huffmanCompresser.generatePrefixCode(rootNode);

//            System.out.println("average code length is "+calculateAverageCode(frequencyMap,hashCode));


            huffmanCompresser.encodeFile(fileHandler.getInputStream(),fileHandler.getOutputStream(),hashMap, rootNode);

//            compressionStats.stopTimer();
//            compressionStats.displayCompressionStats(filePath, compressFilePath);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void decompress(IFileHandler fileHandler) {

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

    }

}
