package com.capillary.zipper.wordbasedhuffman.database;


import com.capillary.zipper.utils.Node;

import javax.print.DocFlavor;
import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqliteDao implements IDao<Map<Object,Object>> {
    private  IDBOperation dbOperation;

    public SqliteDao() throws Exception {
        dbOperation=new SqliteDBOperation();
        dbOperation.createConnection();
    }

    @Override
    public Map<Object,Object> get(String checkSum) throws Exception{
        ResultSet rs=(ResultSet)   dbOperation.executeQuery( "SELECT * FROM FREQMAP WHERE CHECKSUM = '"+checkSum+"';" ,"select",null);

//        List<Node> list=new ArrayList<>();
        Map<Object,Object> map=null;
        while(rs.next()){
           System.out.println(rs.getString("CHECKSUM"));
            InputStream inputStream=rs.getBinaryStream("MAP");
            ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
            map=(Map<Object, Object>) objectInputStream.readObject();
        }
        rs.close();
        if(map==null)
            return new HashMap<>();
        return map;
    }

    @Override
    public void insert(Map<Object,Object> hashMap,String checkSum) throws Exception {

        ByteArrayOutputStream bObj = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(bObj);
            if(hashMap != null){
                out.writeObject(hashMap);
                out.close();
                bObj.close();
                byte[] byteOut = bObj.toByteArray();
                System.out.println(byteOut.length);

                String query = "INSERT INTO FREQMAP (CHECKSUM,MAP) "
                        + "VALUES ( '"+checkSum+"' ,?);";

                dbOperation.executeQuery(query,"insert",byteOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void update(Map<Object,Object> hashMap,String checkSum) {

    }

    @Override
    public void delete(String checkSum) {

    }

    @Override
    public void createTable() throws Exception {
    dbOperation.executeQuery( "CREATE TABLE IF NOT EXISTS FREQMAP " +
            "(CHECKSUM CHAR(20) PRIMARY KEY NOT NULL," +
            "MAP BLOB NOT NULL)","create" ,null);
    }


}
