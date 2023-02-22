package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CalcBestPerc implements Runnable{

    List<Map.Entry<String, Integer> > frequencyList;
    int startPerc;
    int endPerc;
    long minCompressionSize;
    int optimalPerc;
    IHashMap optimalFrequencyMap;


    public CalcBestPerc(List<Map.Entry<String, Integer> > list,int startPerc,int endPerc){
        frequencyList=list;
        this.startPerc=startPerc;
        this.endPerc=endPerc;
        minCompressionSize=Integer.MAX_VALUE;
        optimalPerc=100;
    }


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



    private long calcBodySize(IHashMap hashMap,IHashMap prefixCode) {
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

    @Override
    public void run() {

        IHashMap frequencyMap=new HashMapImpl();


        for(int i=0;i<frequencyList.size();i++){
            Map.Entry<String, Integer> aa = frequencyList.get(i);
            String word = aa.getKey();
            frequencyMap.put(word,aa.getValue());
        }


        int prev=frequencyList.size();

        for(int perc=endPerc;perc>=startPerc;perc--) {


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
                optimalFrequencyMap=frequencyMap;
            }
        }
//        System.out.println("Thread Optimal percentage is "+optimalPerc+"%");
//        System.out.println("Thread Optimal fileSize is "+minCompressionSize+" bytes");


    }


    long getMinCompressionSize(){
        return minCompressionSize;
    }

    int getOptimalPerc(){
        return optimalPerc;
    }

    IHashMap getOptimalFrequencyMap(){
        return optimalFrequencyMap;
    }
}
