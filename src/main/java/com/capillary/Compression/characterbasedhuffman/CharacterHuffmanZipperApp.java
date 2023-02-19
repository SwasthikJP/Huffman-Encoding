package com.capillary.Compression.characterbasedhuffman;

import com.capillary.Compression.utils.Node;
import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.zipper.IZipperApp;
import com.capillary.Compression.characterbasedhuffman.compression.CharacterBasedHuffmanCompresser;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.characterbasedhuffman.decompression.CharacterBasedHuffmanDecompresser;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanDecompresser;

public class CharacterHuffmanZipperApp implements IZipperApp {


    private IHuffmanCompresser huffmanCompresser;
    private IHuffmanDecompresser huffmanDecompresser;

    public CharacterHuffmanZipperApp(){
        huffmanCompresser=new CharacterBasedHuffmanCompresser();
        huffmanDecompresser=new CharacterBasedHuffmanDecompresser();
    }

    public CharacterHuffmanZipperApp(IHuffmanCompresser huffmanCompresser, IHuffmanDecompresser huffmanDecompresser){
        this.huffmanCompresser=huffmanCompresser;
        this.huffmanDecompresser=huffmanDecompresser;
    }




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



           Node rootNode=(Node)huffmanDecompresser.createHuffmanTree(fileHandler.getInputStream());

            huffmanDecompresser.decodeFile(fileHandler.getOutputStream(),rootNode);

//            compressionStats.stopTimer();
//            compressionStats.displayDecompressionStats(compressFilePath, decompressFilePath);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
