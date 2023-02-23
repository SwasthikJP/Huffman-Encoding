package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.*;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordDivision {

    HashMap<Object,Object> ff;
    IHashMap optimalFrequencyMap;

    private long dfs(Node node){
        if(node==null){
            return 0;
        }
        if(node.isLeafNode){
            return 1+8+(((String)(node.value)).length()*8);
        }
        return 1+dfs(node.left)+dfs(node.right);
    }

    private long calcHeaderSize(Node node){
        return dfs(node)/8;
    }



    private long calcBodySize(IHashMap hashMap,IHashMap prefixCode)throws IOException {
        long bodySize=0;
        long buffer=0;
        long prefixSize=0;

        for(Object key:prefixCode.keySet()){
            prefixSize=((int)(hashMap.get(key))*((String)(prefixCode.get(key))).length());
            bodySize+=(prefixSize/8);
            buffer=buffer+(prefixSize%8);
            if(buffer>8){
                bodySize++;
                buffer=buffer-8;
            }
        }
        if(buffer>0)
           bodySize++;
        return bodySize;
    }


    public IHashMap optimalWordDivision(IHashMap hashMap)throws IOException{
IZipperStats zipperStats=new FileZipperStats();
zipperStats.startTimer();
        List<Map.Entry<String, Integer> > frequencyList =
                new LinkedList<Map.Entry<String, Integer> >(((Map)hashMap.getMap()).entrySet());


        Collections.sort(frequencyList, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> w1,
                               Map.Entry<String, Integer> w2)
            {
                return (w2.getValue()).compareTo(w1.getValue());
            }
        });

        int[] percList={100,70,40,-1};

        CalcBestPerc[] calcBestPercs=new CalcBestPerc[percList.length-1];
//        Thread[] thread=new Thread[percList.length-1];

        ExecutorService executorService= Executors.newFixedThreadPool(percList.length-1);

        for(int i=0;i<percList.length-1;i++){
            calcBestPercs[i]=new CalcBestPerc(frequencyList,percList[i+1]+1,percList[i]);
            executorService.execute(calcBestPercs[i]);
        }

        long minCompressionSize=Integer.MAX_VALUE;
        int optimalPerc=100;


    executorService.shutdown();

        try {
            if (!executorService.awaitTermination(6000, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

   for(int i=0;i<calcBestPercs.length;i++){
       if(calcBestPercs[i].getMinCompressionSize()< minCompressionSize){
           minCompressionSize=calcBestPercs[i].getMinCompressionSize();
           optimalPerc=calcBestPercs[i].getOptimalPerc();
           optimalFrequencyMap=calcBestPercs[i].getOptimalFrequencyMap();
       }
   }


   System.out.println("Optimal fileSize is "+minCompressionSize+" bytes");
   System.out.println("Optimal Word perc is "+optimalPerc+"%");
   zipperStats.stopTimer();
   zipperStats.displayTimeTaken("optimalWordDivision");
   return optimalFrequencyMap;

    }


    public IHashMap calcOptimalPercOfWords(IHashMap hashMap)throws IOException{

        List<Map.Entry<String, Integer> > frequencyList =
                new LinkedList<Map.Entry<String, Integer> >(((Map)hashMap.getMap()).entrySet());


        Collections.sort(frequencyList, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> w1,
                               Map.Entry<String, Integer> w2)
            {
                return (w2.getValue()).compareTo(w1.getValue());
            }
        });


        IHashMap frequencyMap=new HashMapImpl();
        long minCompressionSize=Integer.MAX_VALUE;
        int optimalPerc=100;

        for(int i=0;i<frequencyList.size();i++){
            Map.Entry<String, Integer> aa = frequencyList.get(i);
            String word = aa.getKey();
            frequencyMap.put(word,aa.getValue());
        }


        int prev=frequencyList.size();

        for(int perc=100;perc>=0;perc--) {


            for (int i = perc * frequencyList.size() / 100; i < prev; i++) {

                Map.Entry<String, Integer> aa = frequencyList.get(i);
                String word = aa.getKey();
                if (word == "{^}") {
                    continue;
                }
                int wordCount = aa.getValue();

                   frequencyMap.remove(word);//use map<String,String>
                   for (int j = 0; j < word.length(); j++) { // not taking count of w
                       frequencyMap.put(word.charAt(j) + "", ((int) frequencyMap.getOrDefault(word.charAt(j) + "", 0)) + wordCount);
                   }

            }
          prev= perc * frequencyList.size() / 100;

            IHuffmanCompresser huffmanCompresser=new WordBasedHuffmanCompresser();
            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
            IHashMap prefixCode=huffmanCompresser.generatePrefixCode(rootNode);

            long bodySize=calcBodySize(frequencyMap,prefixCode);
            long headerSize=calcHeaderSize(rootNode);

            if((bodySize+headerSize)<minCompressionSize){
                minCompressionSize=bodySize+headerSize;
                optimalPerc=perc;
            }
        }

        System.out.println("Optimal percentage is "+optimalPerc+"%");
        System.out.println("Optimal fileSize is "+minCompressionSize+" bytes");

       return optimalFrequencyMap;
    }


}
