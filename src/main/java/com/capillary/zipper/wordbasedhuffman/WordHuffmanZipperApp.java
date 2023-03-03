package com.capillary.zipper.wordbasedhuffman;

import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.zipper.utils.*;
import com.capillary.zipper.wordbasedhuffman.compression.WordBasedHuffmanCompresser;
import com.capillary.zipper.wordbasedhuffman.decompression.WordBasedHuffmanDecompresser;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.Checksum;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.SimulatedAnnealing;
import com.capillary.zipper.zipper.IZipperApp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class WordHuffmanZipperApp implements IZipperApp {

    private  IHuffmanCompresser huffmanCompresser;
    private  IHuffmanDecompresser huffmanDecompresser;
    private  SimulatedAnnealing simulatedAnnealing;
    private Checksum checksum;


    public WordHuffmanZipperApp(){
        huffmanCompresser=new WordBasedHuffmanCompresser();
        huffmanDecompresser=new WordBasedHuffmanDecompresser();
        simulatedAnnealing=new SimulatedAnnealing();
        checksum=new Checksum();
    }

    public WordHuffmanZipperApp(IHuffmanCompresser huffmanCompresser,IHuffmanDecompresser huffmanDecompresser,SimulatedAnnealing simulatedAnnealing,Checksum checksum){
        this.huffmanCompresser=huffmanCompresser;
        this.huffmanDecompresser=huffmanDecompresser;
        this.simulatedAnnealing=simulatedAnnealing;
        this.checksum=checksum;
    }

    @Override
    public void compress(IFileHandler fileHandler) {
        try {
            IZipperStats zipperStats=new FileZipperStats();
            IZipperStats zipperStats2=new FileZipperStats();
           zipperStats2.startTimer();
      List checkSum=checksum.calcCheckSum(new ByteInputStream(fileHandler.getInputStream()));

            zipperStats.startTimer();
            IHashMap frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileHandler.getInputStream());
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("calculateCharacterFrequency()");


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


            zipperStats.calculateAverageCodeLength(frequencyMap,hashMap);

            OutputStream outputStream=fileHandler.getOutputStream();

            checksum.writeCheckSum(checkSum,outputStream);

            zipperStats.startTimer();
            huffmanCompresser.encodeFile(fileHandler.getInputStream(),outputStream,hashMap, rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("encodeFile()");
            zipperStats2.stopTimer();
            zipperStats2.displayTimeTaken("Compression");
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

            InputStream inputStream=fileHandler.getInputStream();
            List inputFileCheckSum=checksum.readCheckSum(inputStream);

            Node rootNode=(Node)huffmanDecompresser.createHuffmanTree(inputStream);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("createHuffmanTree()");

            zipperStats.startTimer();
            huffmanDecompresser.decodeFile(fileHandler.getOutputStream(),rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("decodeFile()");

            List outputFileCheckSum=checksum.calcCheckSum(new ByteInputStream(fileHandler.getInputStreamOfOutputFile()));
           checksum.validateCheckSum(inputFileCheckSum,outputFileCheckSum);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
