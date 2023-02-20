package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.utils.FileZipperStats;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.IZipperStats;

import java.util.*;

public class WordDivision {

    public IHashMap divideWords(IHashMap hashMap){

        int perc=60;//60% 59.44% 2.32Mb //100% 59.02% 2.34Mb

        IZipperStats zipperStats=new FileZipperStats();
        long avgWordLen=zipperStats.calcAverageWordLength(hashMap);

        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(((Map)hashMap.getMap()).entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> w1,
                               Map.Entry<String, Integer> w2)
            {
                return (w2.getValue()).compareTo(w1.getValue());
            }
        });




        for (int i=perc* list.size()/100;i<list.size();i++) {
            Map.Entry<String, Integer> aa=list.get(i);
            String word= aa.getKey();
//            if(word=="{^}" || word.length()>avgWordLen){
            if(word=="{^}"){
                continue;
            }
            int wordCount= aa.getValue();
            hashMap.remove(word);
            for(int j=0;j<word.length();j++){
                hashMap.put(word.charAt(j)+"",(int)hashMap.getOrDefault(word.charAt(j)+"",0)+wordCount);
            }
        }

        System.out.println(hashMap.getSize());
        return hashMap;
    }

}
