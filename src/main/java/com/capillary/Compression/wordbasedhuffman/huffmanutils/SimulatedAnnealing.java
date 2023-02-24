package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.FileZipperStats;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.IZipperStats;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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




    public IHashMap divideWords(IHashMap hashMap,List<Map.Entry<String, Integer>> list,double perc){

//        perc=58;//60% 59.44% 2.32Mb //100% 59.02% 2.34Mb
//perc=90;
        IZipperStats zipperStats=new FileZipperStats();
        zipperStats.startTimer();

        IHashMap temp=new HashMapImpl();
        for(int i=0;i<list.size();i++){
            Map.Entry<String, Integer> aa=list.get(i);
            temp.put(aa.getKey(),aa.getValue());
        }

//        HashMap<String,Integer> temp=new HashMap<>();
//        temp=(HashMap<String, Integer>) hashMap.getMap();


        for (int i=(int)(perc* list.size()/100);i<list.size();i++) {
            Map.Entry<String, Integer> aa=list.get(i);
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
        zipperStats.stopTimer();
        zipperStats.displayTimeTaken("for loop of divide words "+perc);
//        IHashMap mp=new HashMapImpl();
        return temp;
    }


//    public IHashMap calculateIdealSplit(IHashMap original) throws IOException {
//        double[] best = new double[]{0.0,Integer.MAX_VALUE};
//        double[] currentState = new double[]{0.0,Integer.MAX_VALUE};
//
//        double temp = 1000;
//        double coolingRate = 0.2;
//        IHashMap bestFreqMap=null;
//
//        IZipperStats zipperStats=new FileZipperStats();
//        zipperStats.startTimer();
//        int totalCount=0;
//        while(temp>1)
//        {
//            totalCount++;
//            double newPercent = Math.random();
//           IHashMap newFreqMap =  divideWords(original,getsortedFrequencyList(original),newPercent*100);
//
//            double totalLen = this.calculateTotalLength(newFreqMap);
//            //calculateTotalLen(): implement ur function to calculate total file len
//
//            if(acceptanceProbability(currentState[1], totalLen, temp) > Math.random()) {
//                currentState[0] = newPercent;
//                currentState[1] = totalLen;
//            }
//            if(currentState[1] < best[1]) {
//                best = currentState;
//                bestFreqMap =  newFreqMap;
//            }
//            temp = temp / (1 + coolingRate * temp);
//            System.out.println("totalCount is "+totalCount+" "+temp);
//        }
//        System.out.println("Simulated annealing optimal percentage is "+best[0]);
//       zipperStats.stopTimer();
//       zipperStats.displayTimeTaken("Simulated Annealing");
//        return bestFreqMap;
//    }

    public IHashMap calculateIdealSplit(IHashMap original) throws IOException {
        double[] best = new double[]{0.0,Integer.MAX_VALUE};
        double[] currentState = new double[]{0.0,Integer.MAX_VALUE};

        double temp = 1000;
        double coolingRate = 0.2;
        IHashMap bestFreqMap=null;

        IZipperStats zipperStats=new FileZipperStats();
        zipperStats.startTimer();

        List<Map.Entry<String, Integer>> frequencyList=getsortedFrequencyList(original);
        List<Double> temperatureList=new ArrayList<>();

        while(temp>1){
            temperatureList.add(temp);
            temp = temp / (1 + coolingRate * temp);
        }
////
        TopPercWords[] topPercWords=new TopPercWords[temperatureList.size()];

        ExecutorService executorService= Executors.newFixedThreadPool(temperatureList.size());

        for(int i=0;i<temperatureList.size();i++){
            double newPercent = Math.random();
            topPercWords[i]=new TopPercWords(frequencyList,newPercent*100);
            executorService.execute(topPercWords[i]);
        }


        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        for(int i=0;i<temperatureList.size();i++){

             TopPercWords curTopPercWord=topPercWords[i];

            if(acceptanceProbability(currentState[1], curTopPercWord.getMinCompressionSize(), temperatureList.get(i)) > Math.random()) {
                currentState[0] = curTopPercWord.getTopPerc();
                currentState[1] = curTopPercWord.getMinCompressionSize();
            }
            if(currentState[1] < best[1]) {
                best = currentState;
                bestFreqMap = curTopPercWord.getOptimalFreqMap();
            }
        }

        ////

        System.out.println("Simulated annealing optimal percentage is "+best[0]);
        zipperStats.stopTimer();
        zipperStats.displayTimeTaken("Simulated Annealing");
        return bestFreqMap;
    }

    private List<Map.Entry<String, Integer>> getsortedFrequencyList(IHashMap hashMap){

        List<Map.Entry<String, Integer> > list =
                new ArrayList<>(((Map)hashMap.getMap()).entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> w1,
                               Map.Entry<String, Integer> w2)
            {
                return (w2.getValue()).compareTo(w1.getValue());
            }
        });
        return list;
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
