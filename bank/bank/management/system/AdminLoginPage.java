package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLoginPage extends JFrame {
    private JTextField adminUserField;
    private JPasswordField adminPasswordField;
    private JButton loginButton;
    private JButton cancelButton;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankmanagementsystem?user=root";
    private static final String DB_USERNAME = "root";  // Replace with your DB username
    private static final String DB_PASSWORD = "Thirumalesh9160@";  // Replace with your DB password

    public AdminLoginPage() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Admin Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window on screen

        // Create a custom JPanel for colorful gradient background
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);  // Set padding around components

        // Username label and text field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));  // Set font size and style
        userLabel.setForeground(Color.DARK_GRAY);  // Set label color based on background
        adminUserField = new JTextField(15);  // Set preferred width
        adminUserField.setPreferredSize(new Dimension(200, 30));  // Set text field size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        backgroundPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        backgroundPanel.add(adminUserField, gbc);

        // Password label and text field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));  // Set font size and style
        passwordLabel.setForeground(Color.DARK_GRAY);  // Set label color based on background
        adminPasswordField = new JPasswordField(15);  // Set preferred width
        adminPasswordField.setPreferredSize(new Dimension(200, 30));  // Set text field size
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        backgroundPanel.add(adminPasswordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 40));  // Set button size
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));  // Set font size and style
        loginButton.setBackground(new Color(70, 130, 180));  // Steel Blue for better contrast
        loginButton.setForeground(Color.GREEN);  // Set button text color to white
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        backgroundPanel.add(loginButton, gbc);

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 40));  // Set button size
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));  // Set font size and style
        cancelButton.setBackground(new Color(255, 105, 97));  // Light Coral for contrast
        cancelButton.setForeground(Color.RED);  // Set button text color to white
        gbc.gridx = 1;
        gbc.gridy = 2;
        backgroundPanel.add(cancelButton, gbc);

        // Change cursor to hand when hovering over buttons
        setButtonCursorToHand(loginButton);
        setButtonCursorToHand(cancelButton);

        loginButton.addActionListener(this::handleAdminLogin);
        cancelButton.addActionListener(e -> redirectToMainMenu());

        // Add the background panel to the frame
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    // Custom JPanel class to paint the colorful gradient background
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Create a vertical gradient from light blue to pale lavender
            Color color1 = new Color(173, 216, 230);  // Light Blue
            Color color2 = new Color(230, 230, 250);  // Lavender

            int width = getWidth();
            int height = getHeight();

            GradientPaint gradient = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
        }
    }

    // Method to handle admin login by verifying credentials from the admindetails table
    private void handleAdminLogin(ActionEvent e) {
        String username = adminUserField.getText().trim();
        String password = new String(adminPasswordField.getPassword());

        if (validateAdminCredentials(username, password)) {
            JOptionPane.showMessageDialog(this, "Admin Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the login page
            new AdminDashboardPage(); // Open Admin Dashboard
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to validate admin credentials from the database
    private boolean validateAdminCredentials(String username, String password) {
        boolean isValid = false;

        // Database connection and query
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM admindetails WHERE adminname = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            // If the result set contains any rows, the credentials are valid
            isValid = resultSet.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return isValid;
    }

    // Method to change cursor to hand when hovering over buttons
    private void setButtonCursorToHand(JButton button) {
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void redirectToMainMenu() {
        dispose();
        new LoginPage(); // Assuming there's a LoginPage class
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLoginPage::new);
    }
}
