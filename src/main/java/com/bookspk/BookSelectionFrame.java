package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Main Book Selection Frame with SPK Analysis
 * Modern UI with clean color palette
 */
public class BookSelectionFrame extends JFrame {
    private User currentUser;
    private BookDAO bookDAO;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryComboBox;
    private JButton spkButton;
    private JButton crudButton;
    private JButton editProfileButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;
    
    public BookSelectionFrame(User user) {
        this.currentUser = user;
        this.bookDAO = new BookDAO();
        initializeUI();
        loadBooks();
    }
    
    private void initializeUI() {
        setTitle("Book Selection SPK System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        // Main panel with gradient background
        JPanel mainPanel = new GradientPanel(ColorPalette.getBackgroundGradient());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with tabs
        JTabbedPane tabbedPane = createTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new GradientPanel(ColorPalette.getCardGradient());
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        spkButton = new GradientButton("SPK Analysis", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        crudButton = new GradientButton("Manage Books", ColorPalette.PRIMARY_ORANGE, ColorPalette.SECONDARY_ORANGE);
        editProfileButton = new GradientButton("Edit Profile", ColorPalette.PRIMARY_PURPLE, ColorPalette.SECONDARY_PURPLE);
        logoutButton = new GradientButton("Logout", ColorPalette.PRIMARY_RED, ColorPalette.SECONDARY_RED);
        
        // Customize button sizes
        spkButton.setPreferredSize(new Dimension(120, 35));
        crudButton.setPreferredSize(new Dimension(120, 35));
        editProfileButton.setPreferredSize(new Dimension(120, 35));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(spkButton);
        buttonPanel.add(crudButton);
        buttonPanel.add(editProfileButton);
        buttonPanel.add(logoutButton);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Add event listeners
        spkButton.addActionListener(e -> openSPKFrame());
        crudButton.addActionListener(e -> openCRUDFrame());
        editProfileButton.addActionListener(e -> openEditProfileFrame());
        logoutButton.addActionListener(e -> logout());
        
        return headerPanel;
    }
    
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // All Books Tab
        JPanel allBooksPanel = createAllBooksPanel();
        tabbedPane.addTab("All Books", allBooksPanel);
        
        return tabbedPane;
    }
    
    private JPanel createAllBooksPanel() {
        JPanel panel = new GradientPanel(ColorPalette.getCardGradient());
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);
        
        JLabel categoryLabel = new JLabel("Filter by Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All Categories");
        List<String> categories = bookDAO.getAllCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setPreferredSize(new Dimension(150, 30));
        
        JButton refreshButton = new GradientButton("Refresh", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        refreshButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.addActionListener(e -> loadBooks());
        
        filterPanel.add(categoryLabel);
        filterPanel.add(categoryComboBox);
        filterPanel.add(refreshButton);
        
        // Table
        String[] columnNames = {"Title", "Author", "Category", "Publisher", "Year", "Pages", "Rating", "Price", "ISBN"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        bookTable.setSelectionBackground(ColorPalette.PRIMARY_BLUE);
        bookTable.setSelectionForeground(Color.WHITE);
        
        // Add category filter listener
        categoryComboBox.addActionListener(e -> loadBooks());
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    

    
    private void loadBooks() {
        tableModel.setRowCount(0);
        
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        List<Book> books;
        
        if ("All Categories".equals(selectedCategory)) {
            books = bookDAO.getAllBooks();
        } else {
            books = bookDAO.getBooksByCategory(selectedCategory);
        }
        
        for (Book book : books) {
            Object[] row = {
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPublisher(),
                book.getYear(),
                book.getPages(),
                String.format("%.1f", book.getRating()),
                String.format("Rp %.0f", book.getPrice()),
                book.getIsbn()
            };
            tableModel.addRow(row);
        }
    }
    
    private void openSPKFrame() {
        SPKFrame spkFrame = new SPKFrame(bookDAO);
        spkFrame.setVisible(true);
    }
    
    private void openCRUDFrame() {
        BookCRUDFrame crudFrame = new BookCRUDFrame();
        crudFrame.setVisible(true);
    }
    
    private void openEditProfileFrame() {
        EditProfileFrame editFrame = new EditProfileFrame(currentUser);
        editFrame.setVisible(true);
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
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