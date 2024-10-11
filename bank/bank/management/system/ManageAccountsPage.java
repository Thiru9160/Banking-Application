package bank.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ManageAccountsPage {
    private final JFrame frame;
    private final JTable accountsTable;
    private final String[] columnNames = {"Customer ID", "Full Name", "Account Type", "Balance"};
    private Connection connection;

    public ManageAccountsPage() {
        // Set up the frame
        frame = new JFrame("Manage Accounts");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 248, 255)); // Light background color

        JLabel titleLabel = new JLabel("Manage Accounts", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204)); // Title color

        // Fetch data from the Signup2 table and populate the table
        Object[][] data = fetchAccountData();

        // Create the accounts table with fetched data
        accountsTable = new JTable(data, columnNames);
        accountsTable.setRowHeight(30); // Set row height
        accountsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        accountsTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(accountsTable);

        // Custom table header
        accountsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        accountsTable.getTableHeader().setBackground(new Color(200, 200, 255)); // Header background
        accountsTable.getTableHeader().setForeground(Color.BLACK);

        // Center align the table text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < accountsTable.getColumnCount(); i++) {
            accountsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Create buttons for account actions
        final JPanel buttonPanel = createButtonPanel();

        // Add components to the frame
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255)); // Match panel color with frame background

        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        // Set button font size for better visibility
        editButton.setFont(new Font("Arial", Font.PLAIN, 16));
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Change cursor to hand pointer when hovering over buttons
        editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Set tooltips
        editButton.setToolTipText("Edit selected account");
        deleteButton.setToolTipText("Delete selected account");
        backButton.setToolTipText("Return to previous menu");

        // Add action listeners for buttons
        editButton.addActionListener(e -> editAccount());
        deleteButton.addActionListener(e -> deleteAccount());
        backButton.addActionListener(e -> frame.dispose());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        return buttonPanel;
    }

    // Fetch account data from the Signup2 table
    private Object[][] fetchAccountData() {
        Vector<Object[]> data = new Vector<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT customer_id, full_name, account_type, initial_balance AS balance FROM signup2";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Iterate through the result set and add each row to the data vector
            while (resultSet.next()) {
                String customerId = resultSet.getString("customer_id");
                String fullName = resultSet.getString("full_name");
                String accountType = resultSet.getString("account_type");
                String balance = resultSet.getString("balance");

                // Add row data to the vector
                data.add(new Object[]{customerId, fullName, accountType, balance});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching account data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Convert the vector to a 2D array and return
        return data.toArray(new Object[0][]);
    }

    // Method for editing an account
    private void editAccount() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String customerId = (String) accountsTable.getValueAt(selectedRow, 0);
            String currentFullName = (String) accountsTable.getValueAt(selectedRow, 1);
            String currentAccountType = (String) accountsTable.getValueAt(selectedRow, 2);
            String currentBalance = (String) accountsTable.getValueAt(selectedRow, 3);

            // Show a form dialog to edit account details
            JTextField fullNameField = new JTextField(currentFullName);

            // Create a JComboBox for account type with limited options
            String[] accountTypes = {"Saving Account", "Current Account"};
            JComboBox<String> accountTypeComboBox = new JComboBox<>(accountTypes);
            accountTypeComboBox.setSelectedItem(currentAccountType); // Set current type

            JTextField balanceField = new JTextField(currentBalance);

            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Full Name:"));
            panel.add(fullNameField);
            panel.add(new JLabel("Account Type:"));
            panel.add(accountTypeComboBox); // Add JComboBox for account type
            panel.add(new JLabel("Balance:"));
            panel.add(balanceField);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newFullName = fullNameField.getText();
                String newAccountType = (String) accountTypeComboBox.getSelectedItem(); // Get selected account type
                String newBalance = balanceField.getText();

                // Update the database with the new values
                updateAccountInDatabase(customerId, newFullName, newAccountType, newBalance);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an account to edit.");
        }
    }

    // Method to update account information in the database
    private void updateAccountInDatabase(String customerId, String fullName, String accountType, String balance) {
        String updateQuery = "UPDATE signup2 SET full_name = ?, account_type = ?, initial_balance = ? WHERE customer_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, accountType);
            preparedStatement.setString(3, balance);
            preparedStatement.setString(4, customerId);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Account updated successfully.");
            refreshTableData(); // Refresh the table after updating the account
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error updating account: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method for deleting an account
    private void deleteAccount() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String customerId = (String) accountsTable.getValueAt(selectedRow, 0);

            // Confirmation dialog for deletion
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete account " + customerId + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    String query = "DELETE FROM signup2 WHERE customer_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, customerId);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(frame, "Account " + customerId + " deleted.");
                    refreshTableData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an account to delete.");
        }
    }

    // Method to refresh table data after any update or deletion
    private void refreshTableData() {
        Object[][] data = fetchAccountData();
        accountsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
