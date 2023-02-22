package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;

import java.io.*;

public class CalcFrequencyMap {

   public IHashMap createFrequencyMap(InputStream inputStream) throws IOException {
        byte[] bytes=new byte[1024];
        inputStream=new FileInputStream("/home/swasthikjp/Downloads/pg100.txt");
        IHashMap frequencyMap=new HashMapImpl();
        int totalBytes=0;
        int size=0;
        int total=0;
        while((size=inputStream.read(bytes))!=-1){
            total++;
            totalBytes+=size;
        }
       frequencyMap.put("{^}", 1);  ///////////

       ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
       ChunkFileFreqrencyMapCalc chunkFileFreqrencyMapCalc=new ChunkFileFreqrencyMapCalc(byteArrayInputStream);
         Thread t=new Thread(chunkFileFreqrencyMapCalc);
         t.start();
         try {
             t.join();
         }catch (InterruptedException e){

         }

       IHashMap hashMap=chunkFileFreqrencyMapCalc.getFrequencyMap();
      System.out.println( hashMap.getSize());


        return null;
    }
}
