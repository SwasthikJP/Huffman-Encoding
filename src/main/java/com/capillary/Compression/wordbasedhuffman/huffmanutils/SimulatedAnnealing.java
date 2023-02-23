package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.FileZipperStats;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.IZipperStats;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;

import java.io.IOException;
import java.util.*;

public class SimulatedAnnealing {


    HashMap<Object,Object> ff;

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




    public IHashMap divideWords(IHashMap hashMap,double perc){

//        perc=58;//60% 59.44% 2.32Mb //100% 59.02% 2.34Mb

        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(((Map)hashMap.getMap()).entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> w1,
                               Map.Entry<String, Integer> w2)
            {
                return (w2.getValue()).compareTo(w1.getValue());
            }
        });


        IHashMap temp=new HashMapImpl();
        for(Object key:hashMap.keySet()){
            temp.put(key,hashMap.get(key));
        }



        for (int i=(int)(perc* list.size()/100);i<list.size();i++) {
            Map.Entry<String, Integer> aa=list.get(i);
            String word= aa.getKey();
//            if(word=="{^}" || word.length()>avgWordLen){
            if(word=="{^}"){
                continue;
            }
            int wordCount= aa.getValue();
            temp.remove(word);
            for(int j=0;j<word.length();j++){
                temp.put(word.charAt(j)+"",(int)temp.getOrDefault(word.charAt(j)+"",0)+wordCount);
            }
        }

        return temp;
    }


    public IHashMap calculateIdealSplit(IHashMap original) throws IOException {
        double[] best = new double[]{0.0,Integer.MAX_VALUE};
        double[] currentState = new double[]{0.0,Integer.MAX_VALUE};

        double temp = 1000;
        double coolingRate = 0.2;
        IHashMap bestFreqMap=null;

        IZipperStats zipperStats=new FileZipperStats();
        zipperStats.startTimer();

        while(temp>1)
        {
            double newPercent = Math.random();
           IHashMap newFreqMap =  divideWords(original,newPercent*100);


            double totalLen = this.calculateTotalLength(newFreqMap);
            //calculateTotalLen(): implement ur function to calculate total file len

            if(acceptanceProbability(currentState[1], totalLen, temp) > Math.random()) {
                currentState[0] = newPercent;
                currentState[1] = totalLen;
            }
            if(currentState[1] < best[1]) {
                best = currentState;
                bestFreqMap =  newFreqMap;
            }
            temp = temp / (1 + coolingRate * temp);
        }
        System.out.println("Simulated annealing optimal percentage is "+best[0]);
       zipperStats.stopTimer();
       zipperStats.displayTimeTaken("Simulated Annealing");
        return bestFreqMap;
    }

    private double calculateTotalLength(IHashMap frequencyMap) throws IOException{
        long minCompressionSize=Integer.MAX_VALUE;
        IHuffmanCompresser huffmanCompresser=new WordBasedHuffmanCompresser();
        Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
        IHashMap prefixCode=huffmanCompresser.generatePrefixCode(rootNode);

        long bodySize=calcBodySize(frequencyMap,prefixCode);
        long headerSize=calcHeaderSize(rootNode);

      return (bodySize+headerSize);
    }

    private double acceptanceProbability(double energy,double newEnergy,double temp){
        if(newEnergy<energy)
            return 1.0;
        return Math.exp((energy - newEnergy) / temp);
    }
}
