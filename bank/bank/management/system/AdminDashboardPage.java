package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.UUID;

public class AdminDashboardPage extends JFrame {
    private JButton registerButton;
    private JButton manageAccountsButton;
    private JButton logoutButton;

    public AdminDashboardPage() {
        // Set frame properties
        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Set layout to BorderLayout for better component arrangement
        setLayout(new BorderLayout());

        // Create a main panel with a light background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout()); // Use GridBagLayout to center buttons
        mainPanel.setBackground(new Color(230, 230, 250)); // Light lavender background color

        // Initialize and configure the register button
        registerButton = new JButton("Register Customer");
        registerButton.addActionListener(this::registerCustomer);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set hand cursor

        // Initialize and configure the manage accounts button
        manageAccountsButton = new JButton("Manage Accounts");
        manageAccountsButton.addActionListener(this::manageAccounts);
        manageAccountsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set hand cursor

        // Initialize and configure the logout button
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this::logout);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set hand cursor

        // Add components to the panel in the center
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Add spacing between buttons
        gbc.anchor = GridBagConstraints.CENTER; // Center the buttons

        // Optional welcome message at the top
        JLabel welcomeLabel = new JLabel("Welcome to the Admin Dashboard");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size for welcome message
        gbc.gridy = 0;
        mainPanel.add(welcomeLabel, gbc);

        // Add buttons below the welcome message
        gbc.gridy++;
        mainPanel.add(registerButton, gbc);

        gbc.gridy++;
        mainPanel.add(manageAccountsButton, gbc);

        gbc.gridy++;
        mainPanel.add(logoutButton, gbc);

        // Add the main panel to the frame
        add(mainPanel, BorderLayout.CENTER);

        // Set frame visibility
        setVisible(true);
    }

    private void registerCustomer(ActionEvent e) {
        // Generate unique customer ID
        String customerId = UUID.randomUUID().toString();
        // Open the Customer Registration Page with the generated customer ID
        new CustomerRegistrationPage(customerId);
    }

    private void manageAccounts(ActionEvent e) {
        // Open the Manage Accounts Page
        new ManageAccountsPage();
    }

    private void logout(ActionEvent e) {
        // Close the current dashboard
        dispose();
        // Open the Admin Login Page again
        new AdminLoginPage();
    }

    public static void main(String[] args) {
        // Launch the Admin Dashboard Page
        SwingUtilities.invokeLater(AdminDashboardPage::new);
    }
}
