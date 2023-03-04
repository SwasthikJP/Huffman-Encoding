package com.capillary.zipper.wordbasedhuffman.database;

import com.capillary.zipper.utils.IHashMap;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.HashMapImpl;

import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        SqliteDao sqliteDao=new SqliteDao();
//        sqliteDao.createTable();
        IHashMap hashMap=new HashMapImpl();
        hashMap.put("101","101");

//        sqliteDao.insert((Map<Object, Object>)  hashMap.getMap(),"103");
        sqliteDao.get("");
    }
}
