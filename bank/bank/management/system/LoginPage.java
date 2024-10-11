package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPage extends JFrame {

    // Constructor
    public LoginPage() {
        // Frame settings for full-screen
        setTitle("Bank Management System - Main Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a custom panel for the background
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for centering components

        // Initialize description label
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to online banking!<br>"
                + "Everything is on your fingertips.<br>"
                + "You can manage your bank account anywhere and anytime.<br>"
                + "Let's look on this and enjoy the user-friendly system."
                + "</div></html>");
        welcomeLabel.setForeground(new Color(255, 215, 0));  // Light golden color for contrast
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font style and size
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text

        // Initialize buttons
        JButton adminLoginButton = new JButton("Admin Login");
        JButton customerLoginButton = new JButton("Customer Login");

        // Set button size (optional, as they will be auto-sized)
        Dimension buttonSize = new Dimension(200, 40); // Adjust size for better appearance
        adminLoginButton.setPreferredSize(buttonSize);
        customerLoginButton.setPreferredSize(buttonSize);

        // Set hand cursor on hover
        adminLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Transparent panel
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Grid with 2 rows and some gap
        buttonPanel.add(adminLoginButton);
        buttonPanel.add(customerLoginButton);

        // Add action listeners for the buttons
        adminLoginButton.addActionListener(this::openAdminLoginPage);
        customerLoginButton.addActionListener(this::openCustomerLoginPage);

        // GridBagLayout constraints for placing components
        GridBagConstraints gbc = new GridBagConstraints();

        // Place the welcome label at the top
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 50, 20, 50);  // Some padding around the label
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(welcomeLabel, gbc);

        // Place the button panel below the label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 50, 50, 50); // Padding around the button panel
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(buttonPanel, gbc);

        // Add background panel to the frame
        add(backgroundPanel, BorderLayout.CENTER);

        // Set frame to be visible
        setVisible(true);
    }

    // Method to open Admin Login Page
    private void openAdminLoginPage(ActionEvent e) {
        new AdminLoginPage();  // Opens the admin login page
        dispose();  // Close the main login page after navigating
    }

    // Method to open Customer Login Page
    private void openCustomerLoginPage(ActionEvent e) {
        new CustomerLoginPage();  // Opens the customer login page
        dispose();  // Close the main login page after navigating
    }

    public static void main(String[] args) {
        // Run the Main Login Page
        SwingUtilities.invokeLater(LoginPage::new);
    }

    // Custom JPanel class for the background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        // Constructor to load the background image
        public BackgroundPanel() {
            // Load image from resources (Make sure the image is in the correct directory)
            backgroundImage = new ImageIcon("bank/management/system/resource/1.jpg").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image, scaled to the size of the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
