package com.capillary.zipper.wordbasedhuffman;

import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.zipper.utils.*;
import com.capillary.zipper.wordbasedhuffman.compression.WordBasedHuffmanCompresser;
import com.capillary.zipper.wordbasedhuffman.database.SqliteDao;
import com.capillary.zipper.wordbasedhuffman.decompression.WordBasedHuffmanDecompresser;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.Checksum;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.HashMapImpl;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.SimulatedAnnealing;
import com.capillary.zipper.zipper.IZipperApp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class WordHuffmanZipperApp implements IZipperApp {

    private  IHuffmanCompresser huffmanCompresser;
    private  IHuffmanDecompresser huffmanDecompresser;
    private  SimulatedAnnealing simulatedAnnealing;
    private Checksum checksum;
    private SqliteDao sqliteDao;


    public WordHuffmanZipperApp() throws Exception{
        huffmanCompresser=new WordBasedHuffmanCompresser();
        huffmanDecompresser=new WordBasedHuffmanDecompresser();
        simulatedAnnealing=new SimulatedAnnealing();
        checksum=new Checksum();
        sqliteDao=new SqliteDao();
    }

    public WordHuffmanZipperApp(IHuffmanCompresser huffmanCompresser,IHuffmanDecompresser huffmanDecompresser,SimulatedAnnealing simulatedAnnealing,Checksum checksum,SqliteDao sqliteDao){
        this.huffmanCompresser=huffmanCompresser;
        this.huffmanDecompresser=huffmanDecompresser;
        this.simulatedAnnealing=simulatedAnnealing;
        this.checksum=checksum;
        this.sqliteDao=sqliteDao;
    }

    @Override
    public void compress(IFileHandler fileHandler) {
        try {
            IZipperStats zipperStats=new FileZipperStats();

      List checkSum=checksum.calcCheckSum(new ByteInputStream(fileHandler.getInputStream()));


            sqliteDao.createTable();
            Map<Object,Object> map=null;
            IHashMap frequencyMap=null;
            Boolean freqMapPresent=false;
            if( (map=sqliteDao.get(checksum.getcheckSum(checkSum))).size()!=0){
                freqMapPresent=true;
                 frequencyMap=new HashMapImpl(map);
            }else {
                zipperStats.startTimer();
               frequencyMap = huffmanCompresser.calculateCharacterFrequency(fileHandler.getInputStream());
                zipperStats.stopTimer();
                zipperStats.displayTimeTaken("calculateCharacterFrequency()");


                zipperStats.startTimer();
                frequencyMap = simulatedAnnealing.calculateIdealSplit(frequencyMap);
                zipperStats.stopTimer();
                zipperStats.displayTimeTaken("simulatedAnnealing()");

            }

            zipperStats.startTimer();
            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("createHuffmanTree()");

            zipperStats.startTimer();
            IHashMap hashMap= huffmanCompresser.generatePrefixCode(rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("generatePrefixCode()");


//            zipperStats.calculateAverageCodeLength(frequencyMap,hashMap);

            OutputStream outputStream=fileHandler.getOutputStream();

            checksum.writeCheckSum(checkSum,outputStream);

            zipperStats.startTimer();
            huffmanCompresser.encodeFile(fileHandler.getInputStream(),outputStream,hashMap, rootNode);
            zipperStats.stopTimer();
            zipperStats.displayTimeTaken("encodeFile()");


            if(!freqMapPresent) {
                zipperStats.startTimer();
                sqliteDao.createTable();
                sqliteDao.insert((Map<Object, Object>) frequencyMap.getMap(), checksum.getcheckSum(checkSum));
                zipperStats.stopTimer();
                zipperStats.displayTimeTaken("FreqMap Write to DB ");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void decompress(IFileHandler fileHandler) {
        try {
            IZipperStats zipperStats=new FileZipperStats();

//            zipperStats.startTimer();
//            huffmanDecompresser.createHuffmanTree(inputStream);
//            zipperStats.stopTimer();
//            zipperStats.displayTimeTaken("createHuffmanTree()");

            Node rootNode=null;
            InputStream inputStream=fileHandler.getInputStream();
            List inputFileCheckSum=checksum.readCheckSum(inputStream);
            huffmanDecompresser=new WordBasedHuffmanDecompresser(inputStream);


            Map<Object,Object> map=null;

            sqliteDao.createTable();
            if( (map=sqliteDao.get(checksum.getcheckSum(inputFileCheckSum))).size()!=0){
                IHashMap hashMap=new HashMapImpl(map);
                zipperStats.startTimer();
                rootNode=huffmanCompresser.createHuffmanTree(hashMap);
                zipperStats.stopTimer();
                zipperStats.displayTimeTaken("build huffman tree");
            }else{
               throw new Exception("header not found");
            }


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
