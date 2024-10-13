package com.group6.assignment2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectionTest {
    public boolean test() {
        // Database credentials and URL
        String url = "jdbc:mysql://localhost:3306/cs241v2?createDatabaseIfNotExist=true";
        String user = "root";
        String password = "";

        try {
            // Register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Attempt to establish a connection
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                if (connection != null) {
                    System.out.println("Connected to the MySQL server successfully.");
                    return true;
                } else {
                    System.out.println("Failed to make connection!");
                    return false;
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("MySQL JDBC Driver not found.");
            return false;

        } catch (SQLException ex) {
            System.out.println("Connection failed. Check output console.");
            return false;
        }
    }
}
