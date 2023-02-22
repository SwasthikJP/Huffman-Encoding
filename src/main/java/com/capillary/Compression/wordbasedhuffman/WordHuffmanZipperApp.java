package com.capillary.Compression.wordbasedhuffman;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.Compression.utils.*;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;
import com.capillary.Compression.wordbasedhuffman.decompression.WordBasedHuffmanDecompresser;
import com.capillary.Compression.zipper.IZipperApp;

public class WordHuffmanZipperApp implements IZipperApp {

    private  IHuffmanCompresser huffmanCompresser;
    private  IHuffmanDecompresser huffmanDecompresser;

    public WordHuffmanZipperApp(){
        huffmanCompresser=new WordBasedHuffmanCompresser();
        huffmanDecompresser=new WordBasedHuffmanDecompresser();
    }

    public WordHuffmanZipperApp(IHuffmanCompresser huffmanCompresser,IHuffmanDecompresser huffmanDecompresser){
        this.huffmanCompresser=huffmanCompresser;
        this.huffmanDecompresser=huffmanDecompresser;
    }






    @Override
    public void compress(IFileHandler fileHandler) {
        try {
        IZipperStats zipperStats=new FileZipperStats();
            IHashMap frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileHandler.getInputStream());

            zipperStats.startTimer();
            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("createHuffmanTree");

            zipperStats.startTimer();
            IHashMap hashMap= huffmanCompresser.generatePrefixCode(rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("generatePrefixCode");


            zipperStats.calculateAverageCodeLength(frequencyMap,hashMap);
           zipperStats.startTimer();
            huffmanCompresser.encodeFile(fileHandler.getInputStream(),fileHandler.getOutputStream(),hashMap, rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("encodeFile");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void decompress(IFileHandler fileHandler) {
        try {

            IZipperStats zipperStats=new FileZipperStats();
            zipperStats.startTimer();
            Node rootNode=(Node)huffmanDecompresser.createHuffmanTree(fileHandler.getInputStream());
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("createHuffmanTree");
            zipperStats.startTimer();
            huffmanDecompresser.decodeFile(fileHandler.getOutputStream(),rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("decodeFile");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
