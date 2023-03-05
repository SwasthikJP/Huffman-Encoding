package com.capillary.zipper.wordbasedhuffman.database;

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class SqliteDBOperationTest {

    IDBOperation dbOperation;

    @Test
    public void createConnection()throws Exception {
        dbOperation=new SqliteDBOperation();
        dbOperation.createConnection();
    }

    @Test
    public void executeQuery() throws Exception{
        dbOperation=new SqliteDBOperation();
        dbOperation.executeQuery("","",new Object());
    }

    @Test
    public void closeConnection() throws Exception{
        dbOperation=new SqliteDBOperation();
        dbOperation.createConnection();
        dbOperation.closeConnection();
    }
}