package com.capillary.Compression;

public class ArrayBasedFrequencyMap implements IFrequencyMap {

    int[] map;
    int size;

    public ArrayBasedFrequencyMap(int size){
        map=new int[size];
        this.size=size;
    }


    @Override
    public void put(int key, int value)throws ArrayIndexOutOfBoundsException {
        if(key>=0 && key<size){
            map[key]=value;
        }else{
            throw new ArrayIndexOutOfBoundsException("Hash key out of bound");
        }
    }

    @Override
    public int get(int key) {
        if(key>=0 && key<size){
            return map[key];
        }else{
            throw new ArrayIndexOutOfBoundsException("Hash key out of bound");
        }
    }
}
