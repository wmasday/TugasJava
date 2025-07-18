package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Modern Register GUI Frame with Clean Color Palette
 */
public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;
    private UserDAO userDAO;

    public RegisterFrame() {
        userDAO = new UserDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Daftar - Sistem SPK Pemilihan Buku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // Title panel with gradient
        JPanel titlePanel = new GradientPanel(ColorPalette.getCardGradient());
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("Buat Akun Baru");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Bergabunglah dengan komunitas pemilihan buku kami");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(ColorPalette.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        // Form panel with gradient
        JPanel formPanel = new GradientPanel(ColorPalette.getCardGradient());
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
                new EmptyBorder(25, 25, 25, 25)));

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
                new EmptyBorder(8, 12, 8, 12)));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameField, gbc);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(250, 35));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
                new EmptyBorder(8, 12, 8, 12)));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(emailField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
                new EmptyBorder(8, 12, 8, 12)));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        formPanel.add(passwordField, gbc);

        // Confirm Password field
        JLabel confirmPasswordLabel = new JLabel("Konfirmasi Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        formPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(250, 35));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
                new EmptyBorder(8, 12, 8, 12)));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ColorPalette.ERROR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        formPanel.add(statusLabel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        registerButton = new GradientButton("Daftar", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        backButton = new GradientButton("Kembali ke Login", ColorPalette.PRIMARY_GRAY, ColorPalette.SECONDARY_GRAY);

        // Customize button sizes
        registerButton.setPreferredSize(new Dimension(150, 50));
        backButton.setPreferredSize(new Dimension(150, 50));

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
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
                    performRegistration();
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener);
        emailField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        confirmPasswordField.addKeyListener(enterKeyListener);
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

            GradientPaint gradient = new GradientPaint(0, 0, gradientColors[0], getWidth(), getHeight(),
                    gradientColors[1]);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.dispose();
        }
    }

    private void setupEventListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginFrame();
            }
        });
    }

    private void performRegistration() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Silakan isi semua field");
            statusLabel.setForeground(ColorPalette.ERROR);
            return;
        }

        if (username.length() < 3) {
            statusLabel.setText("Username harus memiliki minimal 3 karakter");
            statusLabel.setForeground(ColorPalette.ERROR);
            return;
        }

        if (!email.contains("@")) {
            statusLabel.setText("Silakan masukkan alamat email yang valid");
            statusLabel.setForeground(ColorPalette.ERROR);
            return;
        }

        if (password.length() < 6) {
            statusLabel.setText("Password harus memiliki minimal 6 karakter");
            statusLabel.setForeground(ColorPalette.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Password tidak cocok");
            statusLabel.setForeground(ColorPalette.ERROR);
            return;
        }

        // Check if username already exists
        if (userDAO.usernameExists(username)) {
            statusLabel.setText("Username sudah digunakan");
            statusLabel.setForeground(ColorPalette.ERROR);
            return;
        }

        // Show loading state
        registerButton.setText("Mendaftar...");
        registerButton.setEnabled(false);
        statusLabel.setText("");

        // Perform registration in background
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                User newUser = new User(username, password, email);
                return userDAO.registerUser(newUser);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        statusLabel.setText("Pendaftaran berhasil! Anda dapat login sekarang.");
                        statusLabel.setForeground(ColorPalette.SUCCESS);

                        // Clear fields
                        usernameField.setText("");
                        emailField.setText("");
                        passwordField.setText("");
                        confirmPasswordField.setText("");

                        // Auto-return to login after 2 seconds
                        Timer timer = new Timer(2000, e -> openLoginFrame());
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        statusLabel.setText("Pendaftaran gagal. Silakan coba lagi.");
                        statusLabel.setForeground(ColorPalette.ERROR);
                    }
                } catch (Exception e) {
                    statusLabel.setText("Error: " + e.getMessage());
                    statusLabel.setForeground(ColorPalette.ERROR);
                } finally {
                    registerButton.setText("Daftar");
                    registerButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void openLoginFrame() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
}