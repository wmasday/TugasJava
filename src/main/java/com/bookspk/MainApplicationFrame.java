package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Main application frame (legacy - not used in current implementation)
 */
public class MainApplicationFrame extends JFrame {
    private User currentUser;
    
    public MainApplicationFrame(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Main Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private void openSPKFrame() {
        BookDAO bookDAO = new BookDAO();
        SPKFrame spkFrame = new SPKFrame(bookDAO);
        spkFrame.setVisible(true);
    }
    
    private void openNewSPKFrame() {
        NewSPKFrame newSPKFrame = new NewSPKFrame();
        newSPKFrame.setVisible(true);
    }
} 