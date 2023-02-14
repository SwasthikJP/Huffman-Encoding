package com.capillary.Compression.huffmanimplementation;

import com.capillary.Compression.utils.IHashMap;

public class StringArrayHashMap implements IHashMap {

    String[] map;
    static int size=257;

    public StringArrayHashMap(){
        map=new String[257];
    }

    @Override
    public void put(Object key, Object value)throws ArrayIndexOutOfBoundsException {
        if((int)key>=0 && (int)key<size){
            map[(int)key]=(String) value;
        }else{
            throw new ArrayIndexOutOfBoundsException("Hash key out of bound");
        }
    }

    @Override
    public String get(Object key) {
        if((int)key>=0 && (int)key<size){
            return map[(int)key];
        }else{
            throw new ArrayIndexOutOfBoundsException("Hash key out of bound");
        }
    }

    @Override
    public int getSize() {
        return map.length;
    }
}
