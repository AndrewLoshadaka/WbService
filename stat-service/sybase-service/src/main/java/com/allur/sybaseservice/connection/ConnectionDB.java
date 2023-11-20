package com.allur.sybaseservice.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    public static Connection getConnection() {
        try {
            Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
            String user = "dba";
            String password = "sql";
            String url = "jdbc:sybase:Tds:192.168.208.137:2638/allurbd";
            System.out.println("ok!");
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e){
            System.out.println("no!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
