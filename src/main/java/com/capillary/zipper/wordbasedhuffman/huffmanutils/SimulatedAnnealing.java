package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.IHashMap;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SimulatedAnnealing {


    public IHashMap calculateIdealSplit(IHashMap original)  {

        if(original==null || original.getSize()==0){
            return original;
        }

        double[] best = new double[]{0.0,Integer.MAX_VALUE};
        double[] currentState = new double[]{0.0,Integer.MAX_VALUE};

        double temp = 1000;//temperature
        double coolingRate = 0.2;
        IHashMap bestFreqMap=null;


        List<Map.Entry<String, Integer>> frequencyList=getsortedFrequencyList(original);
        List<Double> temperatureList=new ArrayList<>();

        while(temp>1){
            temperatureList.add(temp);
            temp = temp / (1 + coolingRate * temp);
        }

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

        Logger.getAnonymousLogger().info("Simulated Annealing Optimal Percentage is "+(int)best[0]+"%\n");

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


    private double acceptanceProbability(double energy,double newEnergy,double temp){
        if(newEnergy<energy)
            return 1.0;
        return Math.exp((energy - newEnergy) / temp);
    }
}
