package com.capillary.zipper.wordbasedhuffman.database;

import com.capillary.zipper.utils.IHashMap;
import com.capillary.zipper.utils.Node;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.sqlite.jdbc4.JDBC4ResultSet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SqliteDaoTest {

    IDao sqlDao;

    @Test
    public void get_WhenValidCheckSum_ThenReturnMap()throws Exception {
        IDBOperation dbOperation= Mockito.spy(SqliteDBOperation.class);

        Map<Object,Object> map=new HashMap();
        doReturn(map).when(dbOperation).executeQuery(any(String.class),any(String.class),any(IHashMap.class));
        sqlDao=new SqliteDao(dbOperation);
       assertEquals(map,sqlDao.get("123"));
    }

    @Test
    public void get_WhenInValidCheckSum_ThenReturnMap()throws Exception {
        IDBOperation dbOperation= Mockito.spy(SqliteDBOperation.class);

        doReturn(new HashMap<>()).when(dbOperation).executeQuery(any(String.class),any(String.class),any(IHashMap.class));
        sqlDao=new SqliteDao(dbOperation);
        Map<Object,Object> map=(Map<Object, Object>) sqlDao.get(null);
        assertEquals(0,map.size());
    }

    @Test
    public void insert_WhenValidInput() throws Exception{
        IDBOperation dbOperation= Mockito.spy(SqliteDBOperation.class);

        Map<Object,Object> map=new HashMap();
        doReturn(1).when(dbOperation).executeQuery(any(String.class),any(String.class),any(Object.class));
        sqlDao=new SqliteDao(dbOperation);
        assertEquals(1, sqlDao.insert(map,"123"));
    }

    @Test
    public void update() throws Exception{
        IDBOperation dbOperation= Mockito.spy(SqliteDBOperation.class);
        sqlDao=new SqliteDao(dbOperation);
        sqlDao.update(new HashMap<>(),"");
    }

    @Test
    public void delete()throws Exception{
        IDBOperation dbOperation= Mockito.spy(SqliteDBOperation.class);
        sqlDao=new SqliteDao(dbOperation);
        sqlDao.delete("");
    }

    @Test
    public void createTable() throws Exception{
        IDBOperation dbOperation= Mockito.spy(SqliteDBOperation.class);

        Map<Object,Object> map=new HashMap();
        doReturn(1).when(dbOperation).executeQuery(any(String.class),any(String.class),isNull());
        sqlDao=new SqliteDao(dbOperation);
        assertEquals(1, sqlDao.createTable());
    }
}