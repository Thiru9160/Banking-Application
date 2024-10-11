package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDashboard {
    private JFrame frame;
    private String accountNo;

    public CustomerDashboard(String accountNo) {
        this.accountNo = accountNo; // Store the account number

        // Set the Look and Feel to the system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Customer Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600); // Increased size to accommodate new buttons
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set a background color
        frame.getContentPane().setBackground(new Color(240, 248, 255)); // Alice Blue

        // Adding components to the dashboard
        JLabel welcomeLabel = new JLabel("Welcome to the Customer Dashboard!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font for welcome message
        JLabel accountLabel = new JLabel("Account Number: " + accountNo);
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Font for account number

        JButton viewBalanceButton = createButton("View Balance");
        JButton depositButton = createButton("Deposit");
        JButton withdrawButton = createButton("Withdraw");
        JButton showTransactionsButton = createButton("Show Transactions");
        JButton transferFundsButton = createButton("Transfer Funds");
        JButton changePasswordButton = createButton("Change Password"); // New Change Password Button
        JButton logoutButton = createButton("Logout");

        // Action for each button
        viewBalanceButton.addActionListener(e -> viewBalance());
        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        showTransactionsButton.addActionListener(e -> showTransactions());
        transferFundsButton.addActionListener(e -> transferFunds());
        changePasswordButton.addActionListener(e -> changePassword());
        logoutButton.addActionListener(e -> logout());

        // Layout the components
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(welcomeLabel, gbc);

        gbc.gridy++;
        frame.add(accountLabel, gbc);

        gbc.gridwidth = 1; // Reset gridwidth for buttons
        gbc.gridy++;
        frame.add(viewBalanceButton, gbc);

        gbc.gridy++;
        frame.add(depositButton, gbc);

        gbc.gridy++;
        frame.add(withdrawButton, gbc);

        gbc.gridy++;
        frame.add(showTransactionsButton, gbc);

        gbc.gridy++;
        frame.add(transferFundsButton, gbc);

        gbc.gridy++;
        frame.add(changePasswordButton, gbc);

        gbc.gridy++;
        frame.add(logoutButton, gbc);

        // Set frame to be visible
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
    }

    // Method to create a button with common properties
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40)); // Set button size
        button.setFont(new Font("Arial", Font.PLAIN, 16)); // Set button font
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        button.setBackground(new Color(70, 130, 180)); // Steel Blue background
        button.setForeground(Color.RED); // White text
        button.setFocusPainted(false); // Remove focus painting
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the text
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Change background on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Reset background
            }
        });
        return button;
    }

    // Method to handle changing the password
    private void changePassword() {
        String currentPassword = JOptionPane.showInputDialog(frame, "Enter your current password:");
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Current password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newPassword = JOptionPane.showInputDialog(frame, "Enter your new password:");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "New password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String confirmPassword = JOptionPane.showInputDialog(frame, "Confirm your new password:");
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verify current password and update to new password in the database
        if (verifyCurrentPassword(currentPassword)) {
            updatePasswordInDatabase(newPassword);
            JOptionPane.showMessageDialog(frame, "Password changed successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to verify the current password
    private boolean verifyCurrentPassword(String currentPassword) {
        String sql = "SELECT password FROM login WHERE account_no = ?";
        try (Conn conn = new Conn()) {
            try (PreparedStatement pstmt = conn.c.prepareStatement(sql)) {
                pstmt.setString(1, accountNo);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(currentPassword); // Compare with the current password
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update the password in the database
    private void updatePasswordInDatabase(String newPassword) {
        String sql = "UPDATE login SET password = ? WHERE account_no = ?";
        try (Conn conn = new Conn()) {
            try (PreparedStatement pstmt = conn.c.prepareStatement(sql)) {
                pstmt.setString(1, newPassword);
                pstmt.setString(2, accountNo);
                pstmt.executeUpdate(); // Update the password in the database
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to show transactions
    private void showTransactions() {
        DefaultListModel<String> transactionListModel = new DefaultListModel<>();
        String sql = "SELECT amount, transaction_type, target_account, transaction_date FROM transactions WHERE account_no = ?";

        try (Conn conn = new Conn()) {
            try (PreparedStatement pstmt = conn.c.prepareStatement(sql)) {
                pstmt.setString(1, accountNo);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    double amount = rs.getDouble("amount");
                    String type = rs.getString("transaction_type");
                    String targetAccount = rs.getString("target_account");
                    String date = rs.getString("transaction_date");

                    String transactionInfo = String.format("Amount: %.2f, Type: %s, Date: %s, Target Account: %s",
                            amount, type, date, targetAccount != null ? targetAccount : "N/A");

                    transactionListModel.addElement(transactionInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JList<String> transactionList = new JList<>(transactionListModel);
        JScrollPane scrollPane = new JScrollPane(transactionList);

        JOptionPane.showMessageDialog(frame, scrollPane, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to view balance
    private void viewBalance() {
        String balance = getBalanceFromDatabase();
        if (balance != null) {
            JOptionPane.showMessageDialog(frame, "Your account balance is: " + balance);
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to retrieve balance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to retrieve balance from the database
    private String getBalanceFromDatabase() {
        String balance = null;
        String sql = "SELECT s.initial_balance FROM signup2 s " +
                "JOIN login l ON s.customer_id = l.customer_id " +
                "WHERE l.account_no = ?";

        try (Conn conn = new Conn()) {
            try (PreparedStatement pstmt = conn.c.prepareStatement(sql)) {
                pstmt.setString(1, accountNo);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    balance = rs.getString("initial_balance"); // Retrieve the balance from signup2
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance; // Return the retrieved balance
    }

    // Method to handle deposit
    private void deposit() {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        if (amountStr == null || amountStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if the input is invalid
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount > 0) {
                updateBalanceInDatabase(amount, true); // Update balance with deposit amount
                recordTransaction("Deposit", amount, null); // Record the transaction
                JOptionPane.showMessageDialog(frame, "Deposited: " + amount);
            } else {
                JOptionPane.showMessageDialog(frame, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle withdrawal
    private void withdraw() {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        if (amountStr == null || amountStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if the input is invalid
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount > 0) {
                double currentBalance = Double.parseDouble(getBalanceFromDatabase());
                if (amount <= currentBalance) {
                    updateBalanceInDatabase(amount, false); // Update balance with withdrawal amount
                    recordTransaction("Withdrawal", amount, null); // Record the transaction
                    JOptionPane.showMessageDialog(frame, "Withdrawn: " + amount);
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to transfer funds
    private void transferFunds() {
        String targetAccount = JOptionPane.showInputDialog(frame, "Enter the target account number:");
        if (targetAccount == null || targetAccount.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Target account cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if the input is invalid
        }

        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to transfer:");
        if (amountStr == null || amountStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if the input is invalid
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount > 0) {
                double currentBalance = Double.parseDouble(getBalanceFromDatabase());
                if (amount <= currentBalance) {
                    updateBalanceInDatabase(amount, false); // Update balance with transfer amount (withdrawal)
                    recordTransaction("Transfer", amount, targetAccount); // Record the transfer transaction
                    JOptionPane.showMessageDialog(frame, "Transferred " + amount + " to account: " + targetAccount);
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to record transactions in the database
    private void recordTransaction(String type, double amount, String targetAccount) {
        String sql = "INSERT INTO transactions (account_no, amount, transaction_type, target_account) VALUES (?, ?, ?, ?)";
        try (Conn conn = new Conn()) {
            try (PreparedStatement pstmt = conn.c.prepareStatement(sql)) {
                pstmt.setString(1, accountNo);
                pstmt.setDouble(2, amount);
                pstmt.setString(3, type);
                pstmt.setString(4, targetAccount);
                pstmt.executeUpdate(); // Insert the transaction record
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update balance in the database (deposit or withdraw)
    private void updateBalanceInDatabase(double amount, boolean isDeposit) {
        String balance = getBalanceFromDatabase();
        if (balance != null) {
            double currentBalance = Double.parseDouble(balance);
            double newBalance = isDeposit ? currentBalance + amount : currentBalance - amount;

            String sql = "UPDATE signup2 SET initial_balance = ? WHERE customer_id = (SELECT customer_id FROM login WHERE account_no = ?)";
            try (Conn conn = new Conn()) {
                try (PreparedStatement pstmt = conn.c.prepareStatement(sql)) {
                    pstmt.setDouble(1, newBalance); // Set the new balance
                    pstmt.setString(2, accountNo);  // Bind the accountNo
                    pstmt.executeUpdate();          // Execute the update
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to logout
    private void logout() {
        frame.dispose(); // Close the dashboard window
        new CustomerLoginPage().setVisible(true); // Redirect to the login page (you would need to create this class)
    }

    public static void main(String[] args) {
        new CustomerDashboard("123456789"); // Replace with actual account number for testing
    }
}
