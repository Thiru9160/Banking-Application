package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class CustomerRegistrationPage extends JFrame {
    // Components
    private JTextField fullNameField;
    private JTextField addressField;
    private JTextField mobileNoField;
    private JTextField emailField;
    private JComboBox<String> accountTypeField;
    private JTextField initialBalanceField;
    private JTextField dobField;
    private JTextField idProofField;
    private JTextField customerIdField; // Field to display customer ID
    private JButton registerButton;
    private JButton cancelButton;
    private String customerId; // To store the customer ID passed from AdminDashboardPage

    public CustomerRegistrationPage(String customerId) {
        this.customerId = customerId; // Set the customer ID from the parameter

        // Frame settings
        setTitle("Customer Registration");
        setSize(450, 650); // Increased height for the heading
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout()); // Change to GridBagLayout for better control

        // Set a background color
        getContentPane().setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Add heading
        JLabel headingLabel = new JLabel("NEW CUSTOMER REGISTRATION");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headingLabel.setForeground(new Color(70, 130, 180)); // Steel Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        add(headingLabel, gbc);

        // Initialize components with increased size
        fullNameField = createTextField();
        addressField = createTextField();
        mobileNoField = createTextField();
        emailField = createTextField();
        accountTypeField = new JComboBox<>(new String[]{"Saving Account", "Current Account"});
        initialBalanceField = createTextField();
        dobField = createTextField();
        idProofField = createTextField();

        customerIdField = createTextField();
        customerIdField.setText(customerId);
        customerIdField.setEditable(false);

        registerButton = createButton("Register");
        cancelButton = createButton("Cancel");

        // Add components to the frame using GridBagLayout constraints
        gbc.gridwidth = 1; // Reset to single column width

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Mobile No:"), gbc);
        gbc.gridx = 1;
        add(mobileNoField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Email ID:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1;
        add(accountTypeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Initial Balance (min 1000):"), gbc);
        gbc.gridx = 1;
        add(initialBalanceField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("ID Proof:"), gbc);
        gbc.gridx = 1;
        add(idProofField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        add(customerIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(registerButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        // Action listeners
        registerButton.addActionListener(this::handleRegistration);
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(15);
        textField.setBackground(Color.WHITE);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        textField.setPreferredSize(new Dimension(250, 30)); // Increase height of text fields
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 40)); // Increase button size
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Light Steel Blue
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Steel Blue
            }
        });
        return button;
    }

    private void handleRegistration(ActionEvent e) {
        try {
            // Retrieve values from text fields
            String fullName = fullNameField.getText().trim();
            String address = addressField.getText().trim();
            String mobileNo = mobileNoField.getText().trim();
            String email = emailField.getText().trim();
            String accountType = (String) accountTypeField.getSelectedItem();
            String initialBalanceStr = initialBalanceField.getText().trim();
            String dob = dobField.getText().trim();
            String idProof = idProofField.getText().trim();

            // Validate initial balance
            double initialBalance = validateInitialBalance(initialBalanceStr);
            if (initialBalance < 0) return; // Early exit if validation fails

            // Validate email
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert data into the database
            insertCustomerData(fullName, address, mobileNo, email, accountType, initialBalance, dob, idProof);

            // Mask the password when displaying it
            String maskedPassword = "****"; // Do not display the real password
            String message = "Customer registered successfully!\nCustomer ID: " + customerId + "\nAccount Number: " + generateAccountNo() + "\nPassword: " + maskedPassword;

            // Notify user with masked password
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields(); // Clear fields after successful registration
        } catch (SQLException sqlEx) {
            JOptionPane.showMessageDialog(this, "Error: " + sqlEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        // Simple regex for validating email
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private double validateInitialBalance(String initialBalanceStr) {
        double initialBalance;
        try {
            initialBalance = Double.parseDouble(initialBalanceStr);
            if (initialBalance < 1000) {
                JOptionPane.showMessageDialog(this, "Initial balance must be at least 1000!", "Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Initial balance must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        return initialBalance;
    }

    private void insertCustomerData(String fullName, String address, String mobileNo, String email, String accountType,
                                    double initialBalance, String dob, String idProof) throws SQLException {
        // Database connection (replace Conn with actual implementation)
        Conn conn = new Conn();
        if (conn != null && conn.c != null) { // Ensure that the connection is not null
            try {
                // Insert customer details into signup2 table
                String sql = "INSERT INTO signup2 (full_name, address, mobile_no, email, account_type, initial_balance, dob, id_proof) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.c.prepareStatement(sql)) {
                    pstmt.setString(1, fullName);
                    pstmt.setString(2, address);
                    pstmt.setString(3, mobileNo);
                    pstmt.setString(4, email);
                    pstmt.setString(5, accountType);
                    pstmt.setBigDecimal(6, BigDecimal.valueOf(initialBalance));
                    pstmt.setDate(7, Date.valueOf(dob));
                    pstmt.setString(8, idProof);
                    pstmt.executeUpdate();
                }

                // Generate account number and password
                String accountNo = generateAccountNo();
                String rawPassword = generatePassword();  // Generate raw password
                String hashedPassword = hashPassword(rawPassword);  // Hash the password

                // Get the last inserted ID to use for customer_id
                String customer_id = getLastInsertedCustomerId(conn); // Method to fetch the last inserted ID

                // Insert account number and hashed password into login table
                String loginSql = "INSERT INTO login (customer_id, account_no, password) VALUES (?, ?, ?)";
                try (PreparedStatement loginStmt = conn.c.prepareStatement(loginSql)) {
                    loginStmt.setString(1, customer_id);
                    loginStmt.setString(2, accountNo);
                    loginStmt.setString(3, hashedPassword);  // Store the hashed password
                    loginStmt.executeUpdate();
                }


            } finally {
                conn.close(); // Close connection in the finally block
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getLastInsertedCustomerId(Conn conn) throws SQLException {
        // SQL to fetch the last inserted customer_id (assuming it's auto-incremented)
        String sql = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement pstmt = conn.c.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString(1); // Assuming customer_id is the first column
            }
        }
        return null; // Return null if not found
    }

    // Clear the input fields after successful registration
    private void clearFields() {
        fullNameField.setText("");
        addressField.setText("");
        mobileNoField.setText("");
        emailField.setText("");
        initialBalanceField.setText("");
        dobField.setText("");
        idProofField.setText("");
        customerIdField.setText(customerId); // Reset to current customer ID
    }

    // Generate a random account number (6 digits)
    private String generateAccountNo() {
        Random random = new Random();
        return "ACC" + (100000 + random.nextInt(900000));
    }

    // Generate a random password (4 digits)
    private String generatePassword() {
        Random random = new Random();
        return "pass" + (1000 + random.nextInt(9000));
    }

    // Hash the password using SHA-256 for security
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerRegistrationPage("CUST123")); // Test the page with a sample customer ID
    }
}
