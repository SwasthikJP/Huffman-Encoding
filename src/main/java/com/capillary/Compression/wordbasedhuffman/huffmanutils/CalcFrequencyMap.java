package com.capillary.Compression.wordbasedhuffman.huffmanutils;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;

import java.io.*;
import java.util.concurrent.*;

public class CalcFrequencyMap {

   public IHashMap createFrequencyMap(InputStream inputStream) throws IOException {
        byte[] bytes=new byte[1024*1024];
        inputStream=new FileInputStream("/home/swasthikjp/Downloads/pg100.txt");
        IHashMap frequencyMap=new HashMapImpl();
        int totalBytes=0;
        int size=0;
        int total=0;
        int prev=0;
        byte[] tempBuffer;
       ExecutorService executorService= Executors.newFixedThreadPool(10);
       ConcurrentMap<Object,Object> concurrentHashMap=new ConcurrentHashMap<>();
        while((size=inputStream.read(bytes))!=-1){
            total++;
            totalBytes+=size;
            prev=size;
            tempBuffer=new byte[size];
            System.arraycopy(bytes,0,tempBuffer,0,size);
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(tempBuffer);
            ChunkFileFreqrencyMapCalc chunkFileFreqrencyMapCalc=new ChunkFileFreqrencyMapCalc(byteArrayInputStream,concurrentHashMap);
            executorService.execute(chunkFileFreqrencyMapCalc);
        }
       frequencyMap.put("{^}", 1);  ///////////

       executorService.shutdown();
         try {
             if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                 executorService.shutdownNow();
             }
         }catch (InterruptedException e){

         }

      concurrentHashMap.put("{^}", 1);
      System.out.println(concurrentHashMap.size());


        return null;
    }
}
