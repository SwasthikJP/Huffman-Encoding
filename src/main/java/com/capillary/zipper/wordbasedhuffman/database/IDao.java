package com.capillary.zipper.wordbasedhuffman.database;

import java.util.List;

public interface IDao<T> {


    T get(String checkSum) throws Exception;
    int insert(T item,String  checkSum) throws Exception;
    void update(T item,String checkSum);
    void delete(String checkSum);
    int createTable()throws Exception;
}
