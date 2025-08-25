package controller;

import model.Transaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionController {

    // Add a new transaction
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }
        if (transaction.getDate() == null) {
            throw new IllegalArgumentException("Transaction date cannot be null.");
        }
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative.");
        }
        if (transaction.getType() == null || transaction.getType().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be empty.");
        }

        String sql = "INSERT INTO transactions (user_id, amount, date, description, category_id, type) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getUserId());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setDate(3, new java.sql.Date(transaction.getDate().getTime()));
            stmt.setString(4, transaction.getDescription());
            stmt.setInt(5, transaction.getCategoryId());
            stmt.setString(6, transaction.getType());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve all transactions for a user
    public List<Transaction> getTransactionsByUser(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setUserId(rs.getInt("user_id"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setDate(rs.getDate("date"));
                transaction.setDescription(rs.getString("description"));
                transaction.setCategoryId(rs.getInt("category_id"));
                transaction.setType(rs.getString("type"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    // Update a transaction
    public boolean updateTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }
        if (transaction.getDate() == null) {
            throw new IllegalArgumentException("Transaction date cannot be null.");
        }
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative.");
        }
        if (transaction.getType() == null || transaction.getType().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be empty.");
        }

        String sql = "UPDATE transactions SET amount = ?, date = ?, description = ?, category_id = ?, type = ? WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, transaction.getAmount());
            stmt.setDate(2, new java.sql.Date(transaction.getDate().getTime()));
            stmt.setString(3, transaction.getDescription());
            stmt.setInt(4, transaction.getCategoryId());
            stmt.setString(5, transaction.getType());
            stmt.setInt(6, transaction.getId());
            stmt.setInt(7, transaction.getUserId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete a transaction
    // Delete a transaction
public boolean deleteTransaction(int transactionId, int userId) {
    if (transactionId <= 0) {
        throw new IllegalArgumentException("Transaction ID must be positive.");
    }
    if (userId <= 0) {
        throw new IllegalArgumentException("User ID must be positive.");
    }

    String sql = "DELETE FROM transactions WHERE id = ? AND user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, transactionId);
        stmt.setInt(2, userId);
        int rowsDeleted = stmt.executeUpdate();

        if (rowsDeleted == 0) {
            System.err.println("No transaction deleted. Transaction ID or User ID may not exist.");
        }

        return rowsDeleted > 0;

    } catch (SQLException e) {
        System.err.println("SQL Error during transaction deletion: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

}
