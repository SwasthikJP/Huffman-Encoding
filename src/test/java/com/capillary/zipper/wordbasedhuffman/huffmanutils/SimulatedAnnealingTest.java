package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.IHashMap;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimulatedAnnealingTest {

    SimulatedAnnealing simulatedAnnealing;

    @Test
    public void calculateIdealSplit_WhenFreqMapIsNull_ThenReturnNull() {
        simulatedAnnealing=new SimulatedAnnealing();

        assertNull(simulatedAnnealing.calculateIdealSplit(null));
    }

    @Test
    public void calculateIdealSplit_WhenFreqMapIsEmpty_ThenReturnEmptyFreqMap() {
        simulatedAnnealing=new SimulatedAnnealing();
        IHashMap hashMap=new HashMapImpl();
        assertEquals (hashMap,simulatedAnnealing.calculateIdealSplit(hashMap));
    }

    @Test
    public void calculateIdealSplit_WhenValidFreqMap_ThenReturnBestFreqMap() {
        simulatedAnnealing=new SimulatedAnnealing();

        IHashMap hashMap=new HashMapImpl();
        hashMap.put(" ",1);
        hashMap.put("aB",1);
        hashMap.put("1",1);
        hashMap.put("a",1);
        hashMap.put("{^}",1);
        hashMap.put("/",1);

        IHashMap result=simulatedAnnealing.calculateIdealSplit(hashMap);

        assertEquals(hashMap.getSize(),result.getSize());
        for(Object key: result.keySet()){
            assertEquals(hashMap.get(key),result.get(key));
        }

    }


}