package com.capillary.zipper.wordbasedhuffman.database;


import com.capillary.zipper.utils.FileZipperStats;
import com.capillary.zipper.utils.IZipperStats;
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

    public SqliteDao(IDBOperation dbOperation) throws Exception{
        this.dbOperation=dbOperation;
        dbOperation.createConnection();
    }

    @Override
    public Map<Object,Object> get(String checkSum) throws Exception{
        if(checkSum==null){
            return new HashMap<>();
        }
        IZipperStats zipperStats=new FileZipperStats();
        zipperStats.startTimer();
        ResultSet rs=(ResultSet)   dbOperation.executeQuery( "SELECT * FROM FREQMAP WHERE CHECKSUM = '"+checkSum+"';" ,"select",null);

//        List<Node> list=new ArrayList<>();
        Map<Object,Object> map=null;
        while(rs.next()){
            InputStream inputStream=rs.getBinaryStream("MAP");
            ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
            map=(Map<Object, Object>) objectInputStream.readObject();
        }
        rs.close();
        zipperStats.stopTimer();
        zipperStats.displayTimeTaken("Frequency Map DB Access");
        if(map==null)
            return new HashMap<>();
        return map;
    }

    @Override
    public int insert(Map<Object,Object> hashMap,String checkSum) throws Exception {

        ByteArrayOutputStream bObj = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(bObj);
            if(hashMap != null){
                out.writeObject(hashMap);
                out.close();
                bObj.close();
                byte[] byteOut = bObj.toByteArray();

                String query = "INSERT INTO FREQMAP (CHECKSUM,MAP) "
                        + "VALUES ( '"+checkSum+"' ,?);";

              int i=(int)  dbOperation.executeQuery(query,"insert",byteOut);
              return i;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

return 0;
    }

    @Override
    public void update(Map<Object,Object> hashMap,String checkSum) {

    }

    @Override
    public void delete(String checkSum) {

    }

    @Override
    public int createTable() throws Exception {
   int i=(int) dbOperation.executeQuery( "CREATE TABLE IF NOT EXISTS FREQMAP " +
            "(CHECKSUM CHAR(20) PRIMARY KEY NOT NULL," +
            "MAP BLOB NOT NULL)","create" ,null);
   return i;
    }


}
