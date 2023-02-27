package com.capillary.zipper.characterbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.IHashMap;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayBasedHashMapTest {

    IHashMap hashMap;

    @Test
    public void put() {
        Object[] map=new Object[257];
        hashMap=new ArrayBasedHashMap(map);
        hashMap.put(0,1);
        assertEquals(1,map[0]);
    }

    @Test
    public void get() {
        Object[] map=new Object[257];
        map[0]=1;
        hashMap=new ArrayBasedHashMap(map);
        assertEquals(1,hashMap.get(0));
    }

    @Test
    public void getSize() {
        Object[] map=new Object[257];
        hashMap=new ArrayBasedHashMap(map);
        assertEquals(257,hashMap.getSize());
    }

    @Test
    public void keySet() {
        Object[] map=new Object[257];
        hashMap=new ArrayBasedHashMap(map);
        int i=0;
        for(Object key:hashMap.keySet()){
            assertEquals(i++,key);
        }
    }

    @Test
    public void remove_WhenKeyPresent_ThenValueMatch() {
        Object[] map=new Object[257];
        map[0]=1;
        hashMap=new ArrayBasedHashMap(map);
        assertEquals(1,hashMap.remove(0));
    }

    @Test
    public void remove_WhenKeyNotPresent_ThenNullMatch() {
        Object[] map=new Object[257];
        map[0]=1;
        hashMap=new ArrayBasedHashMap(map);
        assertNull(hashMap.remove(3));
    }

    @Test
    public void getMap() {
        Object[] map=new Object[257];
        map[0]=1;
        hashMap=new ArrayBasedHashMap(map);
        Object resultMap=hashMap.getMap();
        assertEquals(map,resultMap);
    }

    @Test
    public void containsKey_WhenContainsKey_ReturnTrue() {
        Object[] map=new Object[257];
        map[0]=1;
        hashMap=new ArrayBasedHashMap(map);
        assertTrue(hashMap.containsKey(0));
    }

    @Test
    public void containsKey_WhenNotContainsKey_ReturnFalse() {
        Object[] map=new Object[257];
        map[0]=1;
        hashMap=new ArrayBasedHashMap(map);
        assertFalse(hashMap.containsKey(1));
    }


    @Test
    public void getOrDefault_WhenKeyPresent_ThenValueMatch() {
        Object[] map=new Object[257];
        map[0]=1;
        hashMap=new ArrayBasedHashMap(map);
        assertEquals(1,hashMap.getOrDefault(0,0));
    }

    @Test
    public void getOrDefault_WhenKeyNotPresent_ThenDefaultValueMatch() {
        Object[] map=new Object[257];
        map[0]=1;
        hashMap=new ArrayBasedHashMap(map);
        assertEquals(0,hashMap.getOrDefault(5,0));
    }
}