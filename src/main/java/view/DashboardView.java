package view;

import controller.CategoryController;
import controller.TransactionController;
import model.Category;
import model.Transaction;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.io.File;


@SuppressWarnings("serial")
public class DashboardView extends JFrame {
    private User user;
    private TransactionController transactionController;
    private CategoryController categoryController;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JLabel lblTotalAmount;


    public DashboardView(User user) {
        this.user = user;
        transactionController = new TransactionController();
        categoryController = new CategoryController();
        initializeUI();
        loadTransactions();
    }

    private void initializeUI() {
        setTitle("Personal Finance Manager - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel lblWelcome = new JLabel("Welcome, " + user.getUsername() + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblWelcome);

        JPanel summaryPanel = new JPanel(new BorderLayout());
        lblTotalAmount = new JLabel("Total Balance: ₹0.00");
        lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalAmount.setForeground(new Color(0, 128, 0));
        lblTotalAmount.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        summaryPanel.add(lblTotalAmount, BorderLayout.CENTER);
        summaryPanel.add(topPanel, BorderLayout.EAST);

        panel.add(summaryPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Amount", "Date", "Description", "Category", "Type"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JButton btnAdd = new JButton("Add Transaction");
        btnAdd.setFocusable(false);
        JButton btnEdit = new JButton("Edit Transaction");
        btnEdit.setFocusable(false);
        JButton btnDelete = new JButton("Delete Transaction");
        btnDelete.setFocusable(false);
        JButton btnDownloadReport = new JButton("Download Report");
        btnDelete.setFocusable(false);
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnDownloadReport);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        btnAdd.addActionListener(e -> showAddTransactionDialog());
        btnEdit.addActionListener(e -> showEditTransactionDialog());
        btnDelete.addActionListener(e -> showDeleteTransactionDialog());
        btnDownloadReport.addActionListener(e -> downloadReport());
    }

    private void loadTransactions() {
    List<Transaction> transactions = transactionController.getTransactionsByUser(user.getId());
    tableModel.setRowCount(0);

    double totalIncome = 0.0;
    double totalExpense = 0.0;

    for (Transaction t : transactions) {
        String categoryName = getCategoryNameById(t.getCategoryId());
        tableModel.addRow(new Object[]{
                t.getId(),
                t.getAmount(),
                t.getFormattedDate(),
                t.getDescription(),
                categoryName != null ? categoryName : "Uncategorized",
                t.getType()
        });

        if ("Income".equalsIgnoreCase(t.getType())) {
            totalIncome += t.getAmount();
        } else if ("Expense".equalsIgnoreCase(t.getType())) {
            totalExpense += t.getAmount();
        }
    }

    double totalBalance = totalIncome - totalExpense;
    lblTotalAmount.setText(String.format("Total Balance: ₹%.2f", totalBalance));
    lblTotalAmount.setForeground(totalBalance >= 0 ? new Color(0, 128, 0) : Color.RED);
}


    private String getCategoryNameById(int categoryId) {
        Category category = categoryController.getCategoryById(categoryId);
        return category != null ? category.getName() : null;
    }

    private void showAddTransactionDialog() {

        String[] types = {"Income", "Expense"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);

        JComboBox<Category> categoryComboBox = new JComboBox<>();
        populateCategoryComboBox("Income", categoryComboBox);

        typeComboBox.addActionListener(e -> {
            String selectedType = (String) typeComboBox.getSelectedItem();
            populateCategoryComboBox(selectedType, categoryComboBox);
        });

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date());

        JTextField descriptionField = new JTextField(20);
        JTextField amountField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);
        panel.add(new JLabel("Category:"));
        panel.add(categoryComboBox);
        panel.add(new JLabel("Date:"));
        panel.add(dateSpinner);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add Transaction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String type = (String) typeComboBox.getSelectedItem();
                Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
                if (selectedCategory == null) {
                    JOptionPane.showMessageDialog(this, "Please select a valid category.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int categoryId = selectedCategory.getId();

                Date date = (Date) dateSpinner.getValue();
                String description = descriptionField.getText().trim();
                String amountText = amountField.getText().trim();

                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Description cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Amount cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountText);

                if (amount < 0) {
                    JOptionPane.showMessageDialog(this, "Amount cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Transaction transaction = new Transaction();
                transaction.setUserId(user.getId());
                transaction.setAmount(amount);
                transaction.setDate(date);
                transaction.setDescription(description);
                transaction.setCategoryId(categoryId);
                transaction.setType(type);

                boolean success = transactionController.addTransaction(transaction);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Transaction added successfully!");
                    loadTransactions();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add transaction.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void showEditTransactionDialog() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
        double currentAmount = (double) tableModel.getValueAt(selectedRow, 1);
        String currentDateStr = (String) tableModel.getValueAt(selectedRow, 2);
        String currentDescription = (String) tableModel.getValueAt(selectedRow, 3);
        String currentCategoryName = (String) tableModel.getValueAt(selectedRow, 4);
        String currentType = (String) tableModel.getValueAt(selectedRow, 5);

        List<Transaction> transactions = transactionController.getTransactionsByUser(user.getId());
        Transaction currentTransaction = null;
        for (Transaction t : transactions) {
            if (t.getId() == transactionId) {
                currentTransaction = t;
                break;
            }
        }

        if (currentTransaction == null) {
            JOptionPane.showMessageDialog(this, "Selected transaction not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] types = {"Income", "Expense"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);
        typeComboBox.setSelectedItem(currentType);

        JComboBox<Category> categoryComboBox = new JComboBox<>();
        populateCategoryComboBox(currentType, categoryComboBox);
        for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
            Category cat = categoryComboBox.getItemAt(i);
            if (cat.getId() == currentTransaction.getCategoryId()) {
                categoryComboBox.setSelectedIndex(i);
                break;
            }
        }

        typeComboBox.addActionListener(e -> {
            String selectedType = (String) typeComboBox.getSelectedItem();
            populateCategoryComboBox(selectedType, categoryComboBox);
        });

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(currentTransaction.getDate());

        JTextField descriptionField = new JTextField(currentDescription, 20);
        JTextField amountField = new JTextField(String.valueOf(currentAmount), 10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);
        panel.add(new JLabel("Category:"));
        panel.add(categoryComboBox);
        panel.add(new JLabel("Date:"));
        panel.add(dateSpinner);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Edit Transaction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String type = (String) typeComboBox.getSelectedItem();
                Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
                if (selectedCategory == null) {
                    JOptionPane.showMessageDialog(this, "Please select a valid category.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int categoryId = selectedCategory.getId();

                Date date = (Date) dateSpinner.getValue();
                String description = descriptionField.getText().trim();
                String amountText = amountField.getText().trim();

                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Description cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Amount cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountText);

                if (amount < 0) {
                    JOptionPane.showMessageDialog(this, "Amount cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                currentTransaction.setAmount(amount);
                currentTransaction.setDate(date);
                currentTransaction.setDescription(description);
                currentTransaction.setCategoryId(categoryId);
                currentTransaction.setType(type);

                boolean success = transactionController.updateTransaction(currentTransaction);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Transaction updated successfully!");
                    loadTransactions();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update transaction.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void showDeleteTransactionDialog() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
        double currentAmount = (double) tableModel.getValueAt(selectedRow, 1);
        String currentDateStr = (String) tableModel.getValueAt(selectedRow, 2);
        String currentDescription = (String) tableModel.getValueAt(selectedRow, 3);
        String currentCategoryName = (String) tableModel.getValueAt(selectedRow, 4);
        String currentType = (String) tableModel.getValueAt(selectedRow, 5);

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the following transaction?\n" +
                        "Amount: " + currentAmount + "\n" +
                        "Date: " + currentDateStr + "\n" +
                        "Description: " + currentDescription + "\n" +
                        "Category: " + currentCategoryName + "\n" +
                        "Type: " + currentType,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            TransactionController transactionController = new TransactionController();
            List<Transaction> transactions = transactionController.getTransactionsByUser(user.getId());
            Transaction currentTransaction = null;

            for (Transaction t : transactions) {
                if (t.getId() == transactionId) {
                    currentTransaction = t;
                    break;
                }
            }

            if (currentTransaction == null) {
                JOptionPane.showMessageDialog(this, "Selected transaction not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = transactionController.deleteTransaction(transactionId, user.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Transaction deleted successfully!");
                loadTransactions(); // Refresh the transaction table after deletion
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete transaction.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void downloadReport() {
        List<Transaction> transactions = transactionController.getTransactionsByUser(user.getId());
        StringBuilder csvData = new StringBuilder();
        csvData.append("ID,Amount,Date,Description,Category,Type\n"); // Header

        for (Transaction t : transactions) {
            String categoryName = getCategoryNameById(t.getCategoryId());
            csvData.append(String.format("%d,%.2f,%s,%s,%s,%s\n",
                    t.getId(),
                    t.getAmount(),
                    t.getFormattedDate(),
                    t.getDescription(),
                    categoryName != null ? categoryName : "Uncategorized",
                    t.getType()));
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        fileChooser.setSelectedFile(new File("transactions_report.csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter fileWriter = new FileWriter(fileToSave)) {
                fileWriter.write(csvData.toString());
                JOptionPane.showMessageDialog(this, "Report downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void populateCategoryComboBox(String type, JComboBox<Category> comboBox) {
        comboBox.removeAllItems();
        List<Category> categories = categoryController.getCategoriesByType(type);
        for (Category category : categories) {
            comboBox.addItem(category);
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.setUsername("TestUser");
        DashboardView dashboardView = new DashboardView(user);
        dashboardView.setVisible(true);
    }
}
