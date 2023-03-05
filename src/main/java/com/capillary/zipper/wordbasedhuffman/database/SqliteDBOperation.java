package com.capillary.zipper.wordbasedhuffman.database;

import com.capillary.zipper.utils.FileZipperStats;
import com.capillary.zipper.utils.IZipperStats;

import java.sql.*;
import java.util.logging.Logger;

public class SqliteDBOperation implements IDBOperation {
    private Connection connection;
    private PreparedStatement stmt;

    public SqliteDBOperation(){}




    @Override
    public void createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            Logger.getAnonymousLogger().info("Connection established with database");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

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
        object=stmt.executeUpdate();
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
