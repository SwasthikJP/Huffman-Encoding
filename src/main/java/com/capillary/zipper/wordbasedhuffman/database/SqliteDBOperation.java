package com.capillary.zipper.wordbasedhuffman.database;

import java.sql.*;

public class SqliteDBOperation implements IDBOperation {
    private Connection connection;
    private PreparedStatement stmt;

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
    public Object executeQuery(String query, String operation,Object blob)throws Exception {

     Object object=null;
     switch (operation){
         case "select":
        stmt = connection.prepareStatement(query);
            object=stmt.executeQuery();
            break;

         case "insert":
             stmt=connection.prepareStatement(query);
             stmt.setBytes(1,(byte[]) blob);
           System.out.println( stmt.executeUpdate());
           break;

         case "create":
             stmt = connection.prepareStatement(query);
             object=stmt.executeUpdate();
             break;
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
