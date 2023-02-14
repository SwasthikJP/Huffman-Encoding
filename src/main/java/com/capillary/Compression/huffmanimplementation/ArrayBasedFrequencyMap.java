package com.capillary.Compression.huffmanimplementation;

import com.capillary.Compression.utils.IFrequencyMap;

public class ArrayBasedFrequencyMap implements IFrequencyMap {

    int[] map;
    static int size=257;

    public ArrayBasedFrequencyMap(){
        map=new int[size];
    }


    @Override
    public void put(Object key, int value)throws ArrayIndexOutOfBoundsException {
        if((int)key>=0 && (int)key<size){
            map[(int)key]=value;
        }else{
            throw new ArrayIndexOutOfBoundsException("Hash key out of bound");
        }
    }

    @Override
    public int get(Object key) {
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
