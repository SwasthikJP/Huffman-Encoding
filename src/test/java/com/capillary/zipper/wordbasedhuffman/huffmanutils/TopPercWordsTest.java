package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.IHashMap;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TopPercWordsTest {

    TopPercWords topPercWords;

    @Test
    public void run_WhenValidFreqList_ThenFreqMapMatch() {


        IHashMap hashMap=new HashMapImpl();
        hashMap.put(" ",1);
        hashMap.put("aB",1);
        hashMap.put("1",1);
        hashMap.put("a",1);
        hashMap.put("{^}",1);
        hashMap.put("/",1);  //not recognizing 1 or recognizing 1 as 0

        List<Map.Entry<String, Integer> > list =
                new ArrayList<>(((Map)hashMap.getMap()).entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> w1,
                               Map.Entry<String, Integer> w2)
            {
                return (w2.getValue()).compareTo(w1.getValue());
            }
        });

        topPercWords=new TopPercWords(list,10);
        topPercWords.run();
        IHashMap result=topPercWords.getOptimalFreqMap();

        IHashMap expected=hashMap;
        hashMap.put("a",2);
        hashMap.put("B",1);
        hashMap.remove("aB");

        assertEquals(hashMap.getSize(),result.getSize());

        for(Object key:hashMap.keySet()){
            System.out.println(key+" "+result.get(key));
            assertTrue(result.containsKey(key));
        }



    }

    @Test
    public void getOptimalFreqMap_FreqMapMatch() {
        topPercWords=new TopPercWords(null,0);
        assertNull(topPercWords.getOptimalFreqMap());
    }

    @Test
    public void getTopPerc_WhenValidPerc_ThenPercMatch() {
        double perc=100;
        topPercWords=new TopPercWords(null,perc);
        assertEquals(perc,topPercWords.getTopPerc(),0);
    }

    @Test
    public void getMinCompressionSize_ThenValueMatch() {
        topPercWords=new TopPercWords(null,0);
        assertEquals(0,topPercWords.getMinCompressionSize(),0);
    }
}