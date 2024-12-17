package edu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databaseconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/edu_aimuseum";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connected successfully.");
            return connection;
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
