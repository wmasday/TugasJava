package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Login GUI Frame
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;
    private UserDAO userDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Login - Sistem SPK Pemilihan Buku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
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
        
        // Title panel with gradient
        JPanel titlePanel = new GradientPanel(ColorPalette.getCardGradient());
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel titleLabel = new JLabel("Selamat Datang Kembali");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        titlePanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Masuk ke akun Anda");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(ColorPalette.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);
        
        // Form panel with gradient
        JPanel formPanel = new GradientPanel(ColorPalette.getCardGradient());
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(250, 35));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameField, gbc);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(passwordField, gbc);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ColorPalette.ERROR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(statusLabel, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        loginButton = new GradientButton("Masuk", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        registerButton = new GradientButton("Daftar", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        
        // Customize button sizes
        loginButton.setPreferredSize(new Dimension(130, 45));
        registerButton.setPreferredSize(new Dimension(130, 45));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        formPanel.add(buttonPanel, gbc);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add event listeners
        setupEventListeners();
        
        // Add key listener for Enter key
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }
    
    private void setupEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterFrame();
            }
        });
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Silakan masukkan username dan password");
            statusLabel.setForeground(ColorPalette.ERROR);
            return;
        }
        
        // Show loading state
        loginButton.setText("Masuk...");
        loginButton.setEnabled(false);
        statusLabel.setText("");
        
        // Perform authentication in background
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                return userDAO.authenticate(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        statusLabel.setText("Login berhasil!");
                        statusLabel.setForeground(ColorPalette.SUCCESS);
                        
                        // Open main application window
                        SwingUtilities.invokeLater(() -> {
                            openMainApplication(user);
                        });
                    } else {
                        statusLabel.setText("Username atau password tidak valid");
                        statusLabel.setForeground(ColorPalette.ERROR);
                    }
                } catch (Exception e) {
                    statusLabel.setText("Error: " + e.getMessage());
                    statusLabel.setForeground(ColorPalette.ERROR);
                } finally {
                    loginButton.setText("Masuk");
                    loginButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }
    
    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        this.setVisible(false);
    }
    
    private void openMainApplication(User user) {
        BookSelectionFrame bookFrame = new BookSelectionFrame(user);
        bookFrame.setVisible(true);
        this.dispose();
    }
    
    // Remove the old createGradientButton and GradientPanel methods since we're using the new GradientButton class
    
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
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting system look and feel: " + e.getMessage());
        }
        
        // Start the application on EDT
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
} 