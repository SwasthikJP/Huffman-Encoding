package com.capillary.zipper.wordbasedhuffman.database;

public interface IDBOperation {
    void createConnection() throws Exception;

    Object executeQuery(String query, String operation,Object blob)throws Exception;

    void closeConnection() throws Exception;
}
