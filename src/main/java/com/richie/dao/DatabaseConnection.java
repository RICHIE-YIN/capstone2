package com.richie.dao;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String LOCAL_URL  = "jdbc:postgresql://localhost:5432/foodhall";
    private static final String LOCAL_USER = "richieyin";
    private static final String LOCAL_PASS = "";

    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println("Connected to: " + conn.getCatalog());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("DATABASE_URL");

        // ===== PRODUCTION (Railway) =====
        if (dbUrl != null && !dbUrl.isEmpty()) {
            System.out.println("Using production database (Railway)");

            try {
                // Example dbUrl: postgresql://user:pass@host:port/dbname?sslmode=disable
                URI uri = new URI(dbUrl);

                String[] userInfo = uri.getUserInfo().split(":");
                String username = userInfo[0];
                String password = userInfo[1];

                String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();

                // keep any query params like sslmode
                if (uri.getQuery() != null && !uri.getQuery().isEmpty()) {
                    jdbcUrl += "?" + uri.getQuery();
                }

                return DriverManager.getConnection(jdbcUrl, username, password);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid DATABASE_URL: " + dbUrl, e);
            }
        }

        // ===== LOCAL DEV =====
        System.out.println("Using local database");
        return DriverManager.getConnection(LOCAL_URL, LOCAL_USER, LOCAL_PASS);
    }
}


//package com.richie.dao;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DatabaseConnection {
//
//    public static void main(String[] args) {
//        try{
//            Connection conn = getConnection();
//            System.out.println("Connected to: " + conn.getCatalog());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/foodhall";
//    private static final String LOCAL_USER = "richieyin";
//    private static final String LOCAL_PASS = "";
//
//    public static Connection getConnection() throws SQLException {
//        String dbUrl = System.getenv("DATABASE_URL");
//
//        if(dbUrl != null) {
//            //for production
//            if(dbUrl.startsWith("postgres://")) {
//                dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://");
//            }
//            System.out.println("Using production database");
//            return DriverManager.getConnection(dbUrl);
//        } else {
//            //for local
//            System.out.println("Using local database");
//            return DriverManager.getConnection(LOCAL_URL, LOCAL_USER, LOCAL_PASS);
//        }
//    }
//}
