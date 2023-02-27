package com.capillary.zipper.wordbasedhuffman;

import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.zipper.utils.*;
import com.capillary.zipper.wordbasedhuffman.compression.WordBasedHuffmanCompresser;
import com.capillary.zipper.wordbasedhuffman.decompression.WordBasedHuffmanDecompresser;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.SimulatedAnnealing;
import com.capillary.zipper.zipper.IZipperApp;

public class WordHuffmanZipperApp implements IZipperApp {

    private  IHuffmanCompresser huffmanCompresser;
    private  IHuffmanDecompresser huffmanDecompresser;
    private  SimulatedAnnealing simulatedAnnealing;

    public WordHuffmanZipperApp(){
        huffmanCompresser=new WordBasedHuffmanCompresser();
        huffmanDecompresser=new WordBasedHuffmanDecompresser();
        simulatedAnnealing=new SimulatedAnnealing();
    }

    public WordHuffmanZipperApp(IHuffmanCompresser huffmanCompresser,IHuffmanDecompresser huffmanDecompresser,SimulatedAnnealing simulatedAnnealing){
        this.huffmanCompresser=huffmanCompresser;
        this.huffmanDecompresser=huffmanDecompresser;
        this.simulatedAnnealing=simulatedAnnealing;
    }

    @Override
    public void compress(IFileHandler fileHandler) {
        try {
        IZipperStats zipperStats=new FileZipperStats();

            zipperStats.startTimer();
            IHashMap frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileHandler.getInputStream());
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("calculateCharacterFrequency()");
            zipperStats.displayNewLine();

            zipperStats.startTimer();
            frequencyMap= simulatedAnnealing.calculateIdealSplit(frequencyMap);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("simulatedAnnealing()");

            zipperStats.startTimer();
            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("createHuffmanTree()");

            zipperStats.startTimer();
            IHashMap hashMap= huffmanCompresser.generatePrefixCode(rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("generatePrefixCode()");
            zipperStats.displayNewLine();

            zipperStats.calculateAverageCodeLength(frequencyMap,hashMap);

            zipperStats.startTimer();
            huffmanCompresser.encodeFile(fileHandler.getInputStream(),fileHandler.getOutputStream(),hashMap, rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("encodeFile()");

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
            zipperStats.displayTimeTaken("createHuffmanTree()");

            zipperStats.startTimer();
            huffmanDecompresser.decodeFile(fileHandler.getOutputStream(),rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("decodeFile()");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
