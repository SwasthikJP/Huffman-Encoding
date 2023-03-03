package com.capillary.zipper.wordbasedhuffman.database;

public class App {
    public static void main(String[] args) throws Exception {
        SqliteDao sqliteDao=new SqliteDao();
//        sqliteDao.createTable();
//        sqliteDao.insert(new TreeNode("102",true));
        sqliteDao.getAll();
    }
}
