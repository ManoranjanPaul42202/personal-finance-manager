package controller;

import model.User;
import util.DBConnection;
import util.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {

    // Register a new user
    public boolean register(String username, String password) {
        String hashedPassword = PasswordUtils.hashPassword(password);
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error registering user.");
            e.printStackTrace();
            return false;
        }
    }

    // Authenticate a user
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                String storedHash = rs.getString("password_hash");
                if(PasswordUtils.verifyPassword(password, storedHash)){
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(storedHash);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during login.");
            e.printStackTrace();
        }
        return null;
    }
}
