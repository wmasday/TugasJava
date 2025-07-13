package com.bookspk;

import javax.swing.*;

/**
 * Main application launcher
 */
public class LoginApplication {
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