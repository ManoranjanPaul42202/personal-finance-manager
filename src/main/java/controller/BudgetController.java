package controller;

import model.Budget;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetController {

    // Add a new budget
    public boolean addBudget(Budget budget) {
        String sql = "INSERT INTO budgets (user_id, category_id, amount) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, budget.getUserId());
            stmt.setInt(2, budget.getCategoryId());
            stmt.setDouble(3, budget.getAmount());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding budget.");
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve all budgets for a user
    public List<Budget> getBudgetsByUser(int userId) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM budgets WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Budget budget = new Budget();
                budget.setId(rs.getInt("id"));
                budget.setUserId(rs.getInt("user_id"));
                budget.setCategoryId(rs.getInt("category_id"));
                budget.setAmount(rs.getDouble("amount"));
                budgets.add(budget);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving budgets.");
            e.printStackTrace();
        }
        return budgets;
    }

    // Update a budget
    public boolean updateBudget(Budget budget) {
        String sql = "UPDATE budgets SET category_id = ?, amount = ? WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, budget.getCategoryId());
            stmt.setDouble(2, budget.getAmount());
            stmt.setInt(3, budget.getId());
            stmt.setInt(4, budget.getUserId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating budget.");
            e.printStackTrace();
            return false;
        }
    }

    // Delete a budget
    public boolean deleteBudget(int budgetId, int userId) {
        String sql = "DELETE FROM budgets WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, budgetId);
            stmt.setInt(2, userId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting budget.");
            e.printStackTrace();
            return false;
        }
    }
}
