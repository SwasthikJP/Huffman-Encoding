package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.FileZipperStats;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.IZipperStats;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TopPercWords  implements Runnable{

    IHashMap optimalFreqMap;
    double topPerc;
    List<Map.Entry<String, Integer>> frequencyList;
    double minCompressionSize;

    public TopPercWords(List<Map.Entry<String, Integer>> frequencyList,double perc){
        this.frequencyList=frequencyList;
        this.topPerc=perc;
    }

    @Override
    public void run() {

        IZipperStats zipperStats=new FileZipperStats();
        zipperStats.startTimer();

        IHashMap temp=new HashMapImpl();
        for(int i=0;i<frequencyList.size();i++){
            Map.Entry<String, Integer> aa=frequencyList.get(i);
            temp.put(aa.getKey(),aa.getValue());
        }


        for (int i=(int)(topPerc* frequencyList.size()/100);i<frequencyList.size();i++) {
            Map.Entry<String, Integer> aa=frequencyList.get(i);
            String word= aa.getKey();
//            if(word=="{^}" || word.length()>avgWordLen){
            if(word.equals("{^}")){
                continue;
            }
            int wordCount= aa.getValue();
            temp.remove(word);
            for(Character c:word.toCharArray()){
                temp.put(c+"",(int)temp.getOrDefault(c+"",0)+wordCount);
            }
        }

//        IHashMap mp=new HashMapImpl();
        optimalFreqMap=temp;
        minCompressionSize=calculateTotalLength(optimalFreqMap);
        zipperStats.stopTimer();
        zipperStats.displayTimeTaken("for loop of divide words "+topPerc);
    }


    private double calculateTotalLength(IHashMap frequencyMap) {
        try {

        IHuffmanCompresser huffmanCompresser=new WordBasedHuffmanCompresser();
        Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
        IHashMap prefixCode=huffmanCompresser.generatePrefixCode(rootNode);

        long bodySize=calcBodySize(frequencyMap,prefixCode);
        long headerSize=calcHeaderSize(rootNode);

        return (bodySize+headerSize);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return Integer.MAX_VALUE;
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

    IHashMap getOptimalFreqMap(){
        return optimalFreqMap;
    }

    double getTopPerc(){
        return topPerc;
    }

    double getMinCompressionSize(){
        return minCompressionSize;
    }

}
