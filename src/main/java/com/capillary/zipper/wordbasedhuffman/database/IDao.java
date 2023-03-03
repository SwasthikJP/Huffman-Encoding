package com.capillary.zipper.wordbasedhuffman.database;

import java.util.List;

public interface IDao<T> {


    T get(long id);
    List<T> getAll() throws Exception;
    void insert(T item) throws Exception;
    void update(T item,Object[] params);
    void delete(T t);
    void createTable()throws Exception;
}
