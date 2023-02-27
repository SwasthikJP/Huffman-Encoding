package com.capillary.zipper.characterbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.IHashMap;

import java.util.HashSet;
import java.util.Set;

public class ArrayBasedHashMap implements IHashMap {

    Object[] map;
    static int size=257;

    public ArrayBasedHashMap(){
        map=new Object[size];
    }

    public ArrayBasedHashMap(Object[] map){
        this.map=map;
    }


    @Override
    public void put(Object key, Object value) {
            map[(int)key]=value;
    }

    @Override
    public Object get(Object key) {
            return map[(int)key];
    }

    @Override
    public int getSize() {
        return map.length;
    }

    @Override
    public Set<?> keySet() {
        Set<Integer> set=new HashSet<>();
        for(Integer i=0;(int)i<size;i++){
            set.add(i);
        }
        return set;
    }

    @Override
    public Object remove(Object key) {
        Object val=map[(int) key];
        map[(int)key]=null;
       return val;
    }

    @Override
    public Object getMap() {
        return map;
    }

    @Override
    public Boolean containsKey(Object key) {
        if(map[(int)key]!=null){
            return true;
        }
        return false;
    }

    @Override
    public Object getOrDefault(Object key, Object defValue) {
        if(map[(int)key]!=null){
            return map[(int)key];
        }
        return defValue;
    }


}
