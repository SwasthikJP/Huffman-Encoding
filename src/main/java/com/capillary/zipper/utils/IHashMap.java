package com.capillary.zipper.utils;

import java.util.Set;

public interface IHashMap {

    public void put(Object key,Object value);

    public Object get(Object key) ;

    public int getSize();

    Set<?> keySet();

    Object remove(Object key);

    Object getMap();

    Boolean containsKey(Object key);

    Object getOrDefault(Object key,Object defValue);


}
