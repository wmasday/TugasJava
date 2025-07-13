package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Modern Book CRUD Frame with Clean Color Palette
 * Complete Create, Read, Update, Delete functionality
 */
public class BookCRUDFrame extends JFrame {
    private BookDAO bookDAO;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, authorField, publisherField, isbnField;
    private JTextField yearField, pagesField, ratingField, priceField;
    private JComboBox<String> categoryComboBox;
    private JTextArea descriptionArea;
    private JButton addButton, updateButton, deleteButton, clearButton, refreshButton;
    private JLabel statusLabel;
    private int selectedBookId = -1;
    
    public BookCRUDFrame() {
        this.bookDAO = new BookDAO();
        initializeUI();
        loadBooks();
    }
    
    private void initializeUI() {
        setTitle("Book Management - CRUD Operations");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        
        // Content panel with split pane
        JSplitPane splitPane = createSplitPane();
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new GradientPanel(ColorPalette.getCardGradient());
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("Book Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        refreshButton = new GradientButton("Refresh", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        refreshButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.addActionListener(e -> loadBooks());
        
        JButton backButton = new GradientButton("Back", ColorPalette.PRIMARY_GRAY, ColorPalette.SECONDARY_GRAY);
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JSplitPane createSplitPane() {
        // Left panel - Book table
        JPanel leftPanel = createTablePanel();
        
        // Right panel - Form
        JPanel rightPanel = createFormPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600);
        splitPane.setDividerSize(5);
        splitPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        
        return splitPane;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new GradientPanel(ColorPalette.getCardGradient());
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Table title
        JLabel tableTitle = new JLabel("Book List");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(tableTitle, BorderLayout.NORTH);
        
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
        
        // Add mouse listener for row selection
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookTable.getSelectedRow();
                if (row >= 0) {
                    loadBookToForm(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new GradientPanel(ColorPalette.getCardGradient());
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Form title
        JLabel formTitle = new JLabel("Book Details");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(formTitle, BorderLayout.NORTH);
        
        // Form content
        JPanel formContent = createFormContent();
        panel.add(formContent, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createFormContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        addFormField(panel, "Title:", titleField = new JTextField(20), gbc, 0);
        
        // Author
        addFormField(panel, "Author:", authorField = new JTextField(20), gbc, 1);
        
        // Category
        addComboBoxField(panel, "Category:", createCategoryComboBox(), gbc, 2);
        
        // Publisher
        addFormField(panel, "Publisher:", publisherField = new JTextField(20), gbc, 3);
        
        // Year
        addFormField(panel, "Year:", yearField = new JTextField(20), gbc, 4);
        
        // Pages
        addFormField(panel, "Pages:", pagesField = new JTextField(20), gbc, 5);
        
        // Rating
        addFormField(panel, "Rating:", ratingField = new JTextField(20), gbc, 6);
        
        // Price
        addFormField(panel, "Price:", priceField = new JTextField(20), gbc, 7);
        
        // ISBN
        addFormField(panel, "ISBN:", isbnField = new JTextField(20), gbc, 8);
        
        // Description
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        panel.add(descLabel, gbc);
        
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setPreferredSize(new Dimension(250, 100));
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        panel.add(descScrollPane, gbc);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ColorPalette.ERROR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);
        
        return panel;
    }
    
    private JComboBox<String> createCategoryComboBox() {
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setPreferredSize(new Dimension(250, 35));
        categoryComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        // Load categories from database
        loadCategories();
        
        return categoryComboBox;
    }
    
    private void loadCategories() {
        categoryComboBox.removeAllItems();
        List<String> categories = bookDAO.getAllCategories();
        
        // Add default option
        categoryComboBox.addItem("Select Category");
        
        // Add categories from database
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
    }
    
    private void addComboBoxField(JPanel panel, String labelText, JComboBox<String> comboBox, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(comboBox, gbc);
    }
    
    private void addFormField(JPanel panel, String labelText, JTextField field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(field, gbc);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);
        
        addButton = new GradientButton("Add Book", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        updateButton = new GradientButton("Update Book", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        deleteButton = new GradientButton("Delete Book", ColorPalette.PRIMARY_RED, ColorPalette.SECONDARY_RED);
        clearButton = new GradientButton("Clear Form", ColorPalette.PRIMARY_ORANGE, ColorPalette.SECONDARY_ORANGE);
        
        // Customize button sizes
        addButton.setPreferredSize(new Dimension(120, 40));
        updateButton.setPreferredSize(new Dimension(120, 40));
        deleteButton.setPreferredSize(new Dimension(120, 40));
        clearButton.setPreferredSize(new Dimension(120, 40));
        
        // Add event listeners
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        clearButton.addActionListener(e -> clearForm());
        
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = bookDAO.getAllBooks();
        
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
        
        clearForm();
        showStatus("Books loaded successfully", ColorPalette.SUCCESS);
    }
    
    private void loadBookToForm(int row) {
        // Get book ID from database using title and author as unique identifiers
        String title = (String) tableModel.getValueAt(row, 0);
        String author = (String) tableModel.getValueAt(row, 1);
        
        // Find book by title and author
        Book book = bookDAO.getBookByTitleAndAuthor(title, author);
        if (book != null) {
            selectedBookId = book.getId();
            
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            categoryComboBox.setSelectedItem(book.getCategory());
            publisherField.setText(book.getPublisher());
            yearField.setText(String.valueOf(book.getYear()));
            pagesField.setText(String.valueOf(book.getPages()));
            ratingField.setText(String.valueOf(book.getRating())); // Use simple string conversion
            priceField.setText(String.valueOf(book.getPrice()));
            isbnField.setText(book.getIsbn());
            descriptionArea.setText(book.getDescription());
            
            showStatus("Book selected for editing", ColorPalette.INFO);
        } else {
            showStatus("Error: Could not find book details", ColorPalette.ERROR);
        }
    }
    
    private void addBook() {
        if (!validateForm()) {
            return;
        }
        
        Book book = createBookFromForm();
        if (bookDAO.addBook(book)) {
            showStatus("Book added successfully", ColorPalette.SUCCESS);
            loadBooks();
            loadCategories(); // Refresh categories
            clearForm();
        } else {
            showStatus("Failed to add book", ColorPalette.ERROR);
        }
    }
    
    private void updateBook() {
        if (selectedBookId == -1) {
            showStatus("Please select a book to update", ColorPalette.WARNING);
            return;
        }
        
        if (!validateForm()) {
            return;
        }
        
        Book book = createBookFromForm();
        book.setId(selectedBookId);
        
        if (bookDAO.updateBook(book)) {
            showStatus("Book updated successfully", ColorPalette.SUCCESS);
            loadBooks();
            loadCategories(); // Refresh categories
            clearForm();
        } else {
            showStatus("Failed to update book", ColorPalette.ERROR);
        }
    }
    
    private void deleteBook() {
        if (selectedBookId == -1) {
            showStatus("Please select a book to delete", ColorPalette.WARNING);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this book?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            if (bookDAO.deleteBook(selectedBookId)) {
                showStatus("Book deleted successfully", ColorPalette.SUCCESS);
                loadBooks();
                clearForm();
            } else {
                showStatus("Failed to delete book", ColorPalette.ERROR);
            }
        }
    }
    
    private void clearForm() {
        selectedBookId = -1;
        titleField.setText("");
        authorField.setText("");
        categoryComboBox.setSelectedIndex(0); // Reset to "Select Category"
        publisherField.setText("");
        yearField.setText("");
        pagesField.setText("");
        ratingField.setText("");
        priceField.setText("");
        isbnField.setText("");
        descriptionArea.setText("");
        statusLabel.setText("");
    }
    
    private Book createBookFromForm() {
        Book book = new Book();
        book.setTitle(titleField.getText().trim());
        book.setAuthor(authorField.getText().trim());
        book.setCategory((String) categoryComboBox.getSelectedItem());
        book.setPublisher(publisherField.getText().trim());
        book.setYear(Integer.parseInt(yearField.getText().trim()));
        book.setPages(Integer.parseInt(pagesField.getText().trim()));
        book.setRating(Double.parseDouble(ratingField.getText().trim()));
        book.setPrice(Double.parseDouble(priceField.getText().trim()));
        book.setIsbn(isbnField.getText().trim());
        book.setDescription(descriptionArea.getText().trim());
        return book;
    }
    
    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            showStatus("Title is required", ColorPalette.ERROR);
            return false;
        }
        
        if (authorField.getText().trim().isEmpty()) {
            showStatus("Author is required", ColorPalette.ERROR);
            return false;
        }
        
        if (categoryComboBox.getSelectedItem() == null || 
            categoryComboBox.getSelectedItem().equals("Select Category")) {
            showStatus("Please select a category", ColorPalette.ERROR);
            return false;
        }
        
        // Validate Year
        try {
            int year = Integer.parseInt(yearField.getText().trim());
            if (year < 1900 || year > 2024) {
                showStatus("Year must be between 1900 and 2024", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Please enter a valid year (e.g., 2023)", ColorPalette.ERROR);
            return false;
        }
        
        // Validate Pages
        try {
            int pages = Integer.parseInt(pagesField.getText().trim());
            if (pages <= 0) {
                showStatus("Pages must be greater than 0", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Please enter a valid number of pages", ColorPalette.ERROR);
            return false;
        }
        
        // Validate Rating
        try {
            double rating = Double.parseDouble(ratingField.getText().trim());
            if (rating < 0.0 || rating > 5.0) {
                showStatus("Rating must be between 0.0 and 5.0", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Please enter a valid rating (e.g., 4.5)", ColorPalette.ERROR);
            return false;
        }
        
        // Validate Price
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price < 0.0) {
                showStatus("Price must be greater than or equal to 0", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Please enter a valid price (e.g., 150000)", ColorPalette.ERROR);
            return false;
        }
        
        return true;
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