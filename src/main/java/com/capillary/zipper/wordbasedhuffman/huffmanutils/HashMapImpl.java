package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.IHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashMapImpl implements IHashMap {

    Map<Object,Object> map;

    public HashMapImpl(){
        map=new HashMap<>();
    }

    public HashMapImpl(Map<Object,Object> map){
        this.map=map;
    }

    @Override
    public void put(Object key, Object value) {
     map.put(key,value);
    }

    @Override
    public Object get(Object key) {
          return map.get( key);
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
    public Object remove(Object key) {
       return map.remove(key);
    }

    @Override
    public Object getMap() {
        return map;
    }

    @Override
    public Boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public Object getOrDefault(Object key, Object defValue) {
        return map.getOrDefault(key, defValue);
    }



}
