package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Modern Edit Profile Frame with Clean Color Palette
 * User profile management with gradient design
 */
public class EditProfileFrame extends JFrame {
    private User currentUser;
    private UserDAO userDAO;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton saveButton;
    private JButton changePasswordButton;
    private JButton backButton;
    private JLabel statusLabel;
    
    public EditProfileFrame(User user) {
        this.currentUser = user;
        this.userDAO = new UserDAO();
        initializeUI();
        loadUserData();
    }
    
    private void initializeUI() {
        setTitle("Edit Profile - Book Selection SPK");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        // Main panel with gradient background
        JPanel mainPanel = new GradientPanel(ColorPalette.getBackgroundGradient());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new GradientPanel(ColorPalette.getCardGradient());
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("Edit Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        backButton = new GradientButton("Back", ColorPalette.PRIMARY_GRAY, ColorPalette.SECONDARY_GRAY);
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e -> dispose());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new GradientPanel(ColorPalette.getCardGradient());
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        // Create scroll pane with proper settings
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Main form panel
        JPanel formPanel = createFormPanel();
        scrollPane.setViewportView(formPanel);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ColorPalette.ERROR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        contentPanel.add(statusLabel, BorderLayout.SOUTH);
        
        return contentPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(400, 800)); // Increased height for vertical layout
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Profile Information Section
        JLabel profileTitleLabel = new JLabel("Profile Information");
        profileTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        profileTitleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 10, 20, 10);
        panel.add(profileTitleLabel, gbc);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 5, 10);
        panel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(350, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 15, 10);
        panel.add(usernameField, gbc);
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 5, 10);
        panel.add(emailLabel, gbc);
        
        emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(350, 40));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 15, 10);
        panel.add(emailField, gbc);
        
        // Save button
        saveButton = new GradientButton("Save Changes", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        saveButton.setPreferredSize(new Dimension(150, 45));
        saveButton.addActionListener(e -> saveProfile());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 30, 10);
        panel.add(saveButton, gbc);
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(ColorPalette.BORDER_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 10, 20, 10);
        panel.add(separator, gbc);
        
        // Change Password Section
        JLabel passwordTitleLabel = new JLabel("Change Password");
        passwordTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passwordTitleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 10, 20, 10);
        panel.add(passwordTitleLabel, gbc);
        
        // Current Password field
        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentPasswordLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 5, 10);
        panel.add(currentPasswordLabel, gbc);
        
        currentPasswordField = new JPasswordField(20);
        currentPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentPasswordField.setPreferredSize(new Dimension(350, 40));
        currentPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 15, 10);
        panel.add(currentPasswordField, gbc);
        
        // New Password field
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        newPasswordLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 5, 10);
        panel.add(newPasswordLabel, gbc);
        
        newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        newPasswordField.setPreferredSize(new Dimension(350, 40));
        newPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 15, 10);
        panel.add(newPasswordField, gbc);
        
        // Confirm Password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 5, 10);
        panel.add(confirmPasswordLabel, gbc);
        
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(350, 40));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 15, 10);
        panel.add(confirmPasswordField, gbc);
        
        // Change password button
        changePasswordButton = new GradientButton("Change Password", ColorPalette.PRIMARY_ORANGE, ColorPalette.SECONDARY_ORANGE);
        changePasswordButton.setPreferredSize(new Dimension(150, 45));
        changePasswordButton.addActionListener(e -> changePassword());
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(changePasswordButton, gbc);
        
        return panel;
    }
    
    private void loadUserData() {
        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail());
    }
    
    private void saveProfile() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        
        if (username.isEmpty() || email.isEmpty()) {
            showStatus("Please fill in all fields", ColorPalette.ERROR);
            return;
        }
        
        if (!email.contains("@")) {
            showStatus("Please enter a valid email address", ColorPalette.ERROR);
            return;
        }
        
        // Update user object
        currentUser.setUsername(username);
        currentUser.setEmail(email);
        
        // Save to database
        if (userDAO.updateUser(currentUser)) {
            showStatus("Profile updated successfully!", ColorPalette.SUCCESS);
        } else {
            showStatus("Failed to update profile", ColorPalette.ERROR);
        }
    }
    
    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showStatus("Please fill in all password fields", ColorPalette.ERROR);
            return;
        }
        
        if (!currentUser.getPassword().equals(currentPassword)) {
            showStatus("Current password is incorrect", ColorPalette.ERROR);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            showStatus("New passwords do not match", ColorPalette.ERROR);
            return;
        }
        
        if (newPassword.length() < 6) {
            showStatus("Password must be at least 6 characters long", ColorPalette.ERROR);
            return;
        }
        
        // Update password
        currentUser.setPassword(newPassword);
        
        if (userDAO.updateUser(currentUser)) {
            showStatus("Password changed successfully!", ColorPalette.SUCCESS);
            clearPasswordFields();
        } else {
            showStatus("Failed to change password", ColorPalette.ERROR);
        }
    }
    
    private void clearPasswordFields() {
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    
    // Gradient Panel class
    private static class GradientPanel extends JPanel {
        private Color[] gradientColors;
        
        public GradientPanel(Color[] gradientColors) {
            this.gradientColors = gradientColors;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            GradientPaint gradient = new GradientPaint(0, 0, gradientColors[0], getWidth(), getHeight(), gradientColors[1]);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.dispose();
        }
    }
} 