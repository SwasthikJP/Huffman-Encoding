package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.utils.IHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StrKeyStrValHashMap implements IHashMap {
    Map<String,String> map;

    public StrKeyStrValHashMap(Map<String, String> map){
        this.map=map;
    }

    public StrKeyStrValHashMap(){
        map=new HashMap<>();
    }

    @Override
    public void put(Object key, Object value) {
     map.put((String) key,(String) value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key); //returns null is key exists
    }

    @Override
    public int getSize() {
        return map.size();
    }

    @Override
    public Set<?> keySet() {
        return map.keySet();
    }

    @Override
    public void remove(Object key) {
        map.remove(key);
    }

    @Override
    public Object getMap() {
        return null;
    }

    @Override
    public Boolean containsKey(Object key) {
        return map.containsKey(key);
    }


}
