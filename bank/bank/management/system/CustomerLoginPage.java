package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerLoginPage extends JFrame {
    // Components for the login page
    private JTextField accountNoField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;

    public CustomerLoginPage() {
        // Frame settings
        setTitle("Customer Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application when the frame is closed
        setLayout(new GridBagLayout()); // Use GridBagLayout for more flexible positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Add padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Set a background color
        getContentPane().setBackground(new Color(230, 230, 250)); // Light lavender background

        // Initialize components
        accountNoField = new JTextField(20); // Set preferred width
        passwordField = new JPasswordField(20); // Set preferred width
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");

        // Style buttons
        styleButton(loginButton);
        styleButton(cancelButton);

        // Set cursor to hand on button hover
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add components to the frame with GridBag constraints
        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        gbc.anchor = GridBagConstraints.EAST; // Align labels to the right
        add(new JLabel("Account Number:"), gbc);

        gbc.gridx = 1; // Second column
        gbc.anchor = GridBagConstraints.WEST; // Align text fields to the left
        add(accountNoField, gbc);

        gbc.gridx = 0; // First column
        gbc.gridy = 1; // Second row
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; // Second column
        add(passwordField, gbc);

        gbc.gridx = 0; // First column
        gbc.gridy = 2; // Third row
        gbc.anchor = GridBagConstraints.CENTER; // Center buttons
        add(loginButton, gbc);

        gbc.gridx = 1; // Second column
        add(cancelButton, gbc);

        // Action listeners for the buttons
        loginButton.addActionListener(this::handleLogin);
        cancelButton.addActionListener(e -> redirectToMainMenu()); // Redirect to main menu

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Font settings
        button.setBorderPainted(false); // No border
        button.setFocusPainted(false); // No focus border
        button.setPreferredSize(new Dimension(120, 40)); // Preferred button size
    }

    private void handleLogin(ActionEvent e) {
        String accountNo = accountNoField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validate input
        if (accountNo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both account number and password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database connection (replace with your actual connection logic)
        Conn conn = new Conn();
        try {
            // Prepare the SQL query to verify credentials
            String sql = "SELECT * FROM login WHERE account_no = ? AND password = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(sql);
            pstmt.setString(1, accountNo);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            // Check if any result exists
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Close the login window
                dispose();

                // Open the Customer Dashboard
                new CustomerDashboard(accountNo); // Pass account number to the dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Invalid account number or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            conn.close(); // Close connection in the finally block
        }
    }

    private void redirectToMainMenu() {
        // Close the current frame
        dispose();

        // Here you would typically create a new instance of your main menu or login selection screen
        new LoginPage(); // Assuming there's a MainMenu class for redirecting
    }

    public static void main(String[] args) {
        // Launch the Customer Login Page
        SwingUtilities.invokeLater(CustomerLoginPage::new);
    }
}
