package com.capillary.zipper.wordbasedhuffman.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteDBOperation implements IDBOperation {
    private Connection connection;
    private Statement stmt;

    @Override
    public void createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    @Override
    public Object executeQuery(String query, String operation)throws Exception {
     stmt = connection.createStatement();
     Object object=null;
     switch (operation){
         case "select":
           stmt.close();
            object=stmt.executeQuery(query);
            break;

         case "insert":
           System.out.println( stmt.executeUpdate(query));

         case "create":
             object=stmt.executeUpdate(query);

         default:

     }

     return object;
    }

    @Override
    public void closeConnection() throws Exception{
      if(connection!=null)
          connection.close();
      if(stmt!=null)
          stmt.close();
    }
}
