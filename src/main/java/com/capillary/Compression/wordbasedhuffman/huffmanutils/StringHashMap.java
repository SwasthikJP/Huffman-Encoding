package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.utils.IHashMap;

import java.awt.desktop.PreferencesEvent;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

public class StringHashMap implements IHashMap {

    HashMap<String,Integer> map;

    public StringHashMap(){
        map=new HashMap<>();
    }

    @Override
    public void put(Object key, Object value) {
     map.put((String) key,(Integer) value);
    }

    @Override
    public Object get(Object key) {
        if(map.containsKey(key))
          return map.get((String) key);
        return 0;
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
