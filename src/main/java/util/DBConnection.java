package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/personal_finance_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully.");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found.");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
