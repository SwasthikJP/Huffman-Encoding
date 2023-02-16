package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.utils.IHashMap;

import java.util.HashMap;
import java.util.Set;

public class StrKeyIntValHashMap implements IHashMap {

    HashMap<String,Integer> map;

    public StrKeyIntValHashMap(){
        map=new HashMap<>();
    }

    @Override
    public void put(Object key, Object value) {
     map.put((String) key,(Integer) value);
    }

    @Override
    public Object get(Object key) {
        if(map.containsKey(key))
          return map.get( key);
        return 0;
    }

    @Override
    public int getSize() {
        for(String str:map.keySet()){
            if(str.length()>3)
                System.out.println(str.length()+" -- "+str+" --- "+map.get(str));
        }

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
        return map;
    }

    @Override
    public Boolean containsKey(Object key) {
        return null;
    }


}
