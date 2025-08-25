package model;

public class Budget {
    private int id;
    private int userId;
    private int categoryId;
    private double amount;

    // Constructors
    public Budget() {}

    public Budget(int id, int userId, int categoryId, double amount) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
       this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getAmount() {
       return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
