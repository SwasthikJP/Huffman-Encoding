package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.utils.IHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StringKeyValueHashMap implements IHashMap {
    Map<String,String> map;

    public StringKeyValueHashMap(Map<String, String> map){
        this.map=map;
    }

    public StringKeyValueHashMap(){
        map=new HashMap<>();
    }

    @Override
    public void put(Object key, Object value) {
     map.put((String) key,(String) value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public int getSize() {
        return map.size();
    }

    @Override
    public Set<?> keySet() {
        return map.keySet();
    }


}
