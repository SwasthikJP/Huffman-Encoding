package com.capillary.zipper.wordbasedhuffman.database;

import com.capillary.zipper.utils.Node;

import javax.print.DocFlavor;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqliteDao implements IDao<Node> {
    private  IDBOperation dbOperation;

    public SqliteDao() throws Exception {
        dbOperation=new SqliteDBOperation();
        dbOperation.createConnection();
    }

    @Override
    public Node get(long id) {
        return null;
    }



    @Override
    public List getAll() throws Exception {
        ResultSet rs=(ResultSet)   dbOperation.executeQuery( "SELECT * FROM TREE;" ,"select");

        List<Node> list=new ArrayList<>();
        while(rs.next()){
            list.add(new Node(rs.getString("HASH"),rs.getBoolean("ISLEAF")));
        }
        rs.close();
        return list;
    }

    @Override
    public void insert(Node item) throws Exception {
        dbOperation.executeQuery("INSERT INTO TREE (HASH,ISLEAF) " +
               "VALUES ('1001',TRUE);","insert");
    }

    @Override
    public void update(Node item, Object[] params) {

    }

    @Override
    public void delete(Node treeNode) {

    }

    @Override
    public void createTable() throws Exception {
    dbOperation.executeQuery( "CREATE TABLE TREE " +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "HASH CHAR(50) NOT NULL," +
            " ISLEAF  BOOLEAN   NOT NULL )","create" );
    }


}
