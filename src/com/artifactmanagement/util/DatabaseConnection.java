// package com.artifactmanagement.util;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// public class DatabaseConnection {
//     static {
//         try {
//             // Load the MySQL JDBC driver
//             Class.forName("com.mysql.cj.jdbc.Driver");
//         } catch (ClassNotFoundException e) {
//             System.err.println("MySQL JDBC Driver not found!");
//             e.printStackTrace();
//         }
//     }
//     public static Connection getConnection() throws SQLException {
//         return DriverManager.getConnection("jdbc:mysql://localhost:3306/artifact_management", "root", "Nischal@2025");
//     }
//     public static boolean testConnection() {
//         try (Connection conn = getConnection()) {
//             return conn != null && !conn.isClosed();
//         } catch (SQLException e) {
//             System.err.println("Database connection test failed: " + e.getMessage());
//             return false;
//         }
//     }
//     public static void closeConnection(Connection connection) {
//         if (connection != null) {
//             try {
//                 connection.close();
//             } catch (SQLException e) {
//                 System.err.println("Error closing connection: " + e.getMessage());
//             }
//         }
//     }
// }

package com.artifactmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {

        String url = "jdbc:mysql://localhost:3307/artifact_management";
String user = "root";
String password = "Nischal@2025";

        return DriverManager.getConnection(url, user, password);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}