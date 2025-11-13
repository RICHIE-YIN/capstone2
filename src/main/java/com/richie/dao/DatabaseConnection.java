package com.richie.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static void main(String[] args) {
        try{
            Connection conn = getConnection();
            System.out.println("Connected to: " + conn.getCatalog());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/foodhall";
    private static final String LOCAL_USER = "richieyin";
    private static final String LOCAL_PASS = "";

    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("DATABASE_URL");

        if(dbUrl != null) {
            //for production
            if(dbUrl.startsWith("postgres://")) {
                dbUrl = dbUrl.replace("postgres://", "jbdc:postgresql://");
            }
            System.out.println("Using production database");
            return DriverManager.getConnection(dbUrl);
        } else {
            //for local
            System.out.println("Using local database");
            return DriverManager.getConnection(LOCAL_URL, LOCAL_USER, LOCAL_PASS);
        }
    }
}
