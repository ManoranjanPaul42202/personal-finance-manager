package model;

import java.util.Date;

public class Transaction {
    private int id;
    private int userId;
    private double amount;
    private Date date;
    private String description;
    private int categoryId;
    private String type; // Added field for type

    // Constructors
    public Transaction() {}

    public Transaction(int id, int userId, double amount, Date date, String description, int categoryId, String type) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
        this.type = type;
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

    public double getAmount() {
       return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
       return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
       return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
       return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
       return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Optional: Method to format the date for display
    public String getFormattedDate() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(this.date);
    }
}
