package com.store_management.store_management.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/store_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";


//    private static final String URL = "jdbc:mysql://192.168.1.200:3306/productorder";
//    private static final String USERNAME = "cakeorders";
//    private static final String PASSWORD = "qyx5N%DU^W1FSym2";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
