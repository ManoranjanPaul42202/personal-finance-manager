package view;

import controller.AuthController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthController authController;

    public LoginView() {
        authController = new AuthController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Personal Finance Manager - Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main panel with light green background and padding
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(230, 245, 230));  // Light green background
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));  // Padding around the content

        // Title section
        JLabel lblTitle = new JLabel("Login to Finance Manager", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(34, 139, 34));  // Forest green color
        panel.add(lblTitle, BorderLayout.NORTH);

        // Center section with form fields
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(230, 245, 230)); // Match main panel background
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUsername.setForeground(new Color(0, 100, 0)); // Dark green
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);

        // Username Field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBackground(new Color(255, 255, 255)); // White background for input
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0), 1));
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Password Label
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPassword.setForeground(new Color(0, 100, 0)); // Dark green
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblPassword, gbc);

        // Password Field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBackground(new Color(255, 255, 255)); // White background for input
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0), 1));
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Button section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(230, 245, 230)); // Match main panel background

        // Create a single color for both buttons
        Color buttonColor = new Color(34, 139, 34);  // Forest green color

        // Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(buttonColor);  // Same forest green color
        btnLogin.setFocusPainted(false);  // Remove focus outline
        btnLogin.setPreferredSize(new Dimension(120, 35));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        // Rounded corners
        btnLogin.setBorder(BorderFactory.createLineBorder(buttonColor));
        btnLogin.setContentAreaFilled(false);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                if (c.isEnabled()) {
                    g.setColor(btnLogin.getBackground());
                } else {
                    g.setColor(new Color(169, 169, 169)); // Dark gray for disabled state
                }
                g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
                super.paint(g, c);
            }
        });
        buttonPanel.add(btnLogin);

        // Register Button (same color as Login button)
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(buttonColor);  // Same forest green color
        btnRegister.setFocusPainted(false);  // Remove focus outline
        btnRegister.setPreferredSize(new Dimension(120, 35));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        // Rounded corners
        btnRegister.setBorder(BorderFactory.createLineBorder(buttonColor));
        btnRegister.setContentAreaFilled(false);
        btnRegister.setOpaque(true);
        btnRegister.setBorderPainted(false);
        btnRegister.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                if (c.isEnabled()) {
                    g.setColor(btnRegister.getBackground());
                } else {
                    g.setColor(new Color(169, 169, 169)); // Dark gray for disabled state
                }
                g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
                super.paint(g, c);
            }
        });
        buttonPanel.add(btnRegister);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Action Listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAction();
            }
        });
    }

    private void loginAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = authController.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            // Open Dashboard
            DashboardView dashboard = new DashboardView(user);
            dashboard.setVisible(true);
            this.dispose(); // Close login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        boolean success = authController.register(username, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Username might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
