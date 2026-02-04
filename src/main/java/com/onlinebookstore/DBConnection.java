package com.onlinebookstore;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static Connection con = null;

    public static Connection getConnection() {
        try {
            // 1. Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Database details
            String url = "jdbc:mysql://localhost:3306/library";
            String username = "root";
            String password = "root";

            // 3. Create connection
            con = DriverManager.getConnection(url, username, password);

            if (con != null) {
                System.out.println("✅ Database connected successfully");
            }

        } catch (Exception e) {
            System.out.println("❌ Database connection failed");
            e.printStackTrace();
        }
        return con;
    }

    // 🔹 Temporary test (REMOVE after checking)
    public static void main(String[] args) {
        getConnection();
    }
}
