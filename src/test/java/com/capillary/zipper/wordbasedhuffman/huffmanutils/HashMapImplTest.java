package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.IHashMap;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class HashMapImplTest {

    IHashMap hashMap;

    @Test
    public void put_WhenPutValue_ThenValueMatch() {
        Map<Object,Object> map=new HashMap<>();
        hashMap=new HashMapImpl(map);
        hashMap.put("a",1);
        assertEquals(1,map.get("a"));
    }

    @Test
    public void get_WhenGetValue_ThenValueMatch() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        hashMap=new HashMapImpl(map);
        assertEquals(1,hashMap.get("a"));
    }

    @Test
    public void getSize_SizeMatch() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        map.put("b",1);
        hashMap=new HashMapImpl(map);
        assertEquals(2,hashMap.getSize());
    }

    @Test
    public void keySet() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        map.put("b",1);
        hashMap=new HashMapImpl(map);
        Set<?> set=hashMap.keySet();
        Set<?> expectedSet=map.keySet();
         for(Object key:set) {
            assertTrue(map.containsKey(key) );
        }
    }

    @Test
    public void remove_ReturnValueMatch() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        map.put("b",2);
        hashMap=new HashMapImpl(map);
        assertEquals(2,hashMap.remove("b"));
    }

    @Test
    public void getMap_ReturnMap() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        map.put("b",2);
        hashMap=new HashMapImpl(map);
        assertEquals(map,hashMap.getMap());
    }
    @Test
    public void containsKey_WhenKeyPresent_ReturnTrue() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        hashMap=new HashMapImpl(map);
        assertTrue(hashMap.containsKey("a"));
    }

    @Test
    public void containsKey_WhenKeyNotPresent_ReturnFalse() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        hashMap=new HashMapImpl(map);
        assertFalse(hashMap.containsKey("b"));
    }

    @Test
    public void getOrDefault_WhenKeyPresent_ReturnValueMatch() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        hashMap=new HashMapImpl(map);
        assertEquals(1,hashMap.getOrDefault("a",0));
    }

    @Test
    public void getOrDefault_WhenKeyNotPresent_ReturnDefaultValueMatch() {
        Map<Object,Object> map=new HashMap<>();
        map.put("a",1);
        hashMap=new HashMapImpl(map);
        assertEquals(0,hashMap.getOrDefault("NotPresent",0));
    }

}