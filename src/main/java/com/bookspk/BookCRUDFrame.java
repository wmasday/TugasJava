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
    private JTextField borrowerCountField, loanDurationField;
    private JComboBox<String> categoryComboBox, bookConditionComboBox, contentRelevanceComboBox;
    private JTextArea descriptionArea;
    private JButton addButton, updateButton, deleteButton, clearButton, refreshButton;
    private JLabel statusLabel;
    private int selectedBookId = -1;
    
    // Toggle functionality
    private JButton toggleDetailsButton;
    private JSplitPane splitPane;
    private JPanel formPanel;
    private boolean detailsVisible = true;
    
    public BookCRUDFrame() {
        this.bookDAO = new BookDAO();
        initializeUI();
        loadBooks();
    }
    
    private void initializeUI() {
        setTitle("Manajemen Buku - CRUD");
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
        splitPane = createSplitPane();
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
        
        JLabel titleLabel = new JLabel("Sistem Manajemen Buku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        // Toggle details button
        toggleDetailsButton = new GradientButton("Hide Detail", ColorPalette.PRIMARY_ORANGE, ColorPalette.SECONDARY_ORANGE);
        toggleDetailsButton.setPreferredSize(new Dimension(120, 35));
        toggleDetailsButton.addActionListener(e -> toggleDetailsPanel());
        
        refreshButton = new GradientButton("Muat Ulang", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        refreshButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.addActionListener(e -> loadBooks());
        
        JButton backButton = new GradientButton("Kembali", ColorPalette.PRIMARY_GRAY, ColorPalette.SECONDARY_GRAY);
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e -> dispose());
        
        buttonPanel.add(toggleDetailsButton);
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
        formPanel = createFormPanel();
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, formPanel);
        splitPane.setDividerLocation(700);
        splitPane.setDividerSize(5);
        splitPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        
        return splitPane;
    }
    
    private void toggleDetailsPanel() {
        if (detailsVisible) {
            // Hide details panel
            splitPane.setRightComponent(null);
            toggleDetailsButton.setText("Show Detail");
            detailsVisible = false;
        } else {
            // Show details panel
            splitPane.setRightComponent(formPanel);
            splitPane.setDividerLocation(700);
            toggleDetailsButton.setText("Hide Detail");
            detailsVisible = true;
        }
        
        // Revalidate and repaint
        splitPane.revalidate();
        splitPane.repaint();
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new GradientPanel(ColorPalette.getCardGradient());
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Table title
        JLabel tableTitle = new JLabel("Daftar Buku");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(tableTitle, BorderLayout.NORTH);
        
        // Table with new SPK criteria columns
        String[] columnNames = {"Judul", "Penulis", "Kategori", "Penerbit", "Tahun", "Halaman", "Rating", "Harga", "ISBN", "Jumlah Peminjam", "Kondisi Fisik", "Relevansi Isi", "Durasi Peminjaman"};
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
        JLabel formTitle = new JLabel("Detail Buku");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(formTitle, BorderLayout.NORTH);
        
        // Form content wrapped in scroll pane
        JPanel formContent = createFormContent();
        JScrollPane scrollPane = new JScrollPane(formContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
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
        addFormField(panel, "Judul:", titleField = new JTextField(20), gbc, 0);
        
        // Author
        addFormField(panel, "Penulis:", authorField = new JTextField(20), gbc, 1);
        
        // Category
        addComboBoxField(panel, "Kategori:", createCategoryComboBox(), gbc, 2);
        
        // Publisher
        addFormField(panel, "Penerbit:", publisherField = new JTextField(20), gbc, 3);
        
        // Year
        addFormField(panel, "Tahun:", yearField = new JTextField(20), gbc, 4);
        
        // Pages
        addFormField(panel, "Halaman:", pagesField = new JTextField(20), gbc, 5);
        
        // Rating
        addFormField(panel, "Rating:", ratingField = new JTextField(20), gbc, 6);
        
        // Price
        addFormField(panel, "Harga:", priceField = new JTextField(20), gbc, 7);
        
        // ISBN
        addFormField(panel, "ISBN:", isbnField = new JTextField(20), gbc, 8);
        
        // SPK Criteria Section
        JLabel spkLabel = new JLabel("Kriteria SPK:");
        spkLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        spkLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(spkLabel, gbc);
        
        // Borrower Count
        addFormField(panel, "Jumlah Peminjam:", borrowerCountField = new JTextField(20), gbc, 10);
        
        // Book Condition
        addComboBoxField(panel, "Kondisi Fisik Buku:", createBookConditionComboBox(), gbc, 11);
        
        // Content Relevance
        addComboBoxField(panel, "Relevansi Isi Buku:", createContentRelevanceComboBox(), gbc, 12);
        
        // Loan Duration
        addFormField(panel, "Durasi Peminjaman:", loanDurationField = new JTextField(20), gbc, 13);
        
        // Description
        JLabel descLabel = new JLabel("Deskripsi:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 14;
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
        gbc.gridy = 14;
        gbc.gridwidth = 1;
        panel.add(descScrollPane, gbc);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ColorPalette.ERROR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 15;
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
    
    private JComboBox<String> createBookConditionComboBox() {
        bookConditionComboBox = new JComboBox<>();
        bookConditionComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookConditionComboBox.setPreferredSize(new Dimension(250, 35));
        bookConditionComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        // Add book condition options
        bookConditionComboBox.addItem("Pilih Kondisi");
        bookConditionComboBox.addItem("Rusak Berat");
        bookConditionComboBox.addItem("Rusak Ringan");
        bookConditionComboBox.addItem("Sedikit Baik");
        bookConditionComboBox.addItem("Baik");
        bookConditionComboBox.addItem("Sangat Baik");
        
        return bookConditionComboBox;
    }
    
    private JComboBox<String> createContentRelevanceComboBox() {
        contentRelevanceComboBox = new JComboBox<>();
        contentRelevanceComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentRelevanceComboBox.setPreferredSize(new Dimension(250, 35));
        contentRelevanceComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        // Add content relevance options
        contentRelevanceComboBox.addItem("Pilih Relevansi");
        contentRelevanceComboBox.addItem("Tidak Relevan");
        contentRelevanceComboBox.addItem("Kurang Relevan");
        contentRelevanceComboBox.addItem("Cukup Relevan");
        contentRelevanceComboBox.addItem("Relevan");
        contentRelevanceComboBox.addItem("Sangat Relevan");
        
        return contentRelevanceComboBox;
    }
    
    private void loadCategories() {
        categoryComboBox.removeAllItems();
        List<String> categories = bookDAO.getAllCategories();
        
        // Add default option
        categoryComboBox.addItem("Pilih Kategori");
        
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
        
        addButton = new GradientButton("Tambah Buku", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        updateButton = new GradientButton("Perbarui Buku", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        deleteButton = new GradientButton("Hapus Buku", ColorPalette.PRIMARY_RED, ColorPalette.SECONDARY_RED);
        clearButton = new GradientButton("Bersihkan Form", ColorPalette.PRIMARY_ORANGE, ColorPalette.SECONDARY_ORANGE);
        
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
                book.getIsbn(),
                book.getBorrowerCount(),
                book.getBookCondition(),
                book.getContentRelevance(),
                book.getLoanDuration() + " hari"
            };
            tableModel.addRow(row);
        }
        
        clearForm();
        showStatus("Buku berhasil dimuat", ColorPalette.SUCCESS);
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
            ratingField.setText(String.valueOf(book.getRating()));
            priceField.setText(String.valueOf(book.getPrice()));
            isbnField.setText(book.getIsbn());
            borrowerCountField.setText(String.valueOf(book.getBorrowerCount()));
            bookConditionComboBox.setSelectedItem(book.getBookCondition());
            contentRelevanceComboBox.setSelectedItem(book.getContentRelevance());
            loanDurationField.setText(String.valueOf(book.getLoanDuration()));
            descriptionArea.setText(book.getDescription());
            
            showStatus("Buku dipilih untuk diedit", ColorPalette.INFO);
        } else {
            showStatus("Error: Tidak dapat menemukan detail buku", ColorPalette.ERROR);
        }
    }
    
    private void addBook() {
        if (!validateForm()) {
            return;
        }
        
        Book book = createBookFromForm();
        if (bookDAO.addBook(book)) {
            showStatus("Buku berhasil ditambahkan", ColorPalette.SUCCESS);
            loadBooks();
        } else {
            showStatus("Error: Gagal menambahkan buku", ColorPalette.ERROR);
        }
    }
    
    private void updateBook() {
        if (selectedBookId == -1) {
            showStatus("Silakan pilih buku untuk diperbarui", ColorPalette.ERROR);
            return;
        }
        
        if (!validateForm()) {
            return;
        }
        
        Book book = createBookFromForm();
        book.setId(selectedBookId);
        
        if (bookDAO.updateBook(book)) {
            showStatus("Buku berhasil diperbarui", ColorPalette.SUCCESS);
            loadBooks();
            selectedBookId = -1;
        } else {
            showStatus("Error: Gagal memperbarui buku", ColorPalette.ERROR);
        }
    }
    
    private void deleteBook() {
        if (selectedBookId == -1) {
            showStatus("Silakan pilih buku untuk dihapus", ColorPalette.ERROR);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus buku ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            if (bookDAO.deleteBook(selectedBookId)) {
                showStatus("Buku berhasil dihapus", ColorPalette.SUCCESS);
                loadBooks();
                selectedBookId = -1;
            } else {
                showStatus("Error: Gagal menghapus buku", ColorPalette.ERROR);
            }
        }
    }
    
    private void clearForm() {
        titleField.setText("");
        authorField.setText("");
        categoryComboBox.setSelectedIndex(0);
        publisherField.setText("");
        yearField.setText("");
        pagesField.setText("");
        ratingField.setText("");
        priceField.setText("");
        isbnField.setText("");
        borrowerCountField.setText("");
        bookConditionComboBox.setSelectedIndex(0);
        contentRelevanceComboBox.setSelectedIndex(0);
        loanDurationField.setText("");
        descriptionArea.setText("");
        selectedBookId = -1;
        showStatus("Form dibersihkan", ColorPalette.INFO);
    }
    
    private Book createBookFromForm() {
        Book book = new Book(
            titleField.getText(),
            authorField.getText(),
            categoryComboBox.getSelectedItem().toString(),
            publisherField.getText(),
            Integer.parseInt(yearField.getText()),
            Integer.parseInt(pagesField.getText()),
            Double.parseDouble(ratingField.getText()),
            Double.parseDouble(priceField.getText()),
            isbnField.getText(),
            descriptionArea.getText()
        );
        
        // Set SPK criteria fields
        book.setBorrowerCount(Integer.parseInt(borrowerCountField.getText()));
        book.setBookCondition(bookConditionComboBox.getSelectedItem().toString());
        book.setContentRelevance(contentRelevanceComboBox.getSelectedItem().toString());
        book.setLoanDuration(Integer.parseInt(loanDurationField.getText()));
        
        return book;
    }
    
    private boolean validateForm() {
        // Check if required fields are filled
        if (titleField.getText().trim().isEmpty()) {
            showStatus("Error: Judul wajib diisi", ColorPalette.ERROR);
            return false;
        }
        
        if (authorField.getText().trim().isEmpty()) {
            showStatus("Error: Penulis wajib diisi", ColorPalette.ERROR);
            return false;
        }
        
        if (categoryComboBox.getSelectedIndex() == 0) {
            showStatus("Error: Silakan pilih kategori", ColorPalette.ERROR);
            return false;
        }
        
        if (publisherField.getText().trim().isEmpty()) {
            showStatus("Error: Penerbit wajib diisi", ColorPalette.ERROR);
            return false;
        }
        
        // Validate numeric fields
        try {
            int year = Integer.parseInt(yearField.getText());
            if (year < 1900 || year > 2024) {
                showStatus("Error: Tahun harus antara 1900 dan 2024", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Error: Tahun harus berupa angka valid", ColorPalette.ERROR);
            return false;
        }
        
        try {
            int pages = Integer.parseInt(pagesField.getText());
            if (pages <= 0 || pages > 5000) {
                showStatus("Error: Halaman harus antara 1 dan 5000", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Error: Halaman harus berupa angka valid", ColorPalette.ERROR);
            return false;
        }
        
        try {
            double rating = Double.parseDouble(ratingField.getText());
            if (rating < 0.0 || rating > 5.0) {
                showStatus("Error: Rating harus antara 0.0 dan 5.0", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Error: Rating harus berupa angka valid", ColorPalette.ERROR);
            return false;
        }
        
        try {
            double price = Double.parseDouble(priceField.getText());
            if (price < 0.0) {
                showStatus("Error: Harga harus berupa angka positif", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Error: Harga harus berupa angka valid", ColorPalette.ERROR);
            return false;
        }
        
        if (isbnField.getText().trim().isEmpty()) {
            showStatus("Error: ISBN wajib diisi", ColorPalette.ERROR);
            return false;
        }
        
        if (isbnField.getText().length() < 10) {
            showStatus("Error: ISBN harus minimal 10 karakter", ColorPalette.ERROR);
            return false;
        }
        
        // Validate SPK criteria fields
        try {
            int borrowerCount = Integer.parseInt(borrowerCountField.getText());
            if (borrowerCount < 0 || borrowerCount > 1000) {
                showStatus("Error: Jumlah peminjam harus antara 0 dan 1000", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Error: Jumlah peminjam harus berupa angka valid", ColorPalette.ERROR);
            return false;
        }
        
        if (bookConditionComboBox.getSelectedIndex() == 0) {
            showStatus("Error: Silakan pilih kondisi fisik buku", ColorPalette.ERROR);
            return false;
        }
        
        if (contentRelevanceComboBox.getSelectedIndex() == 0) {
            showStatus("Error: Silakan pilih relevansi isi buku", ColorPalette.ERROR);
            return false;
        }
        
        try {
            int loanDuration = Integer.parseInt(loanDurationField.getText());
            if (loanDuration < 1 || loanDuration > 30) {
                showStatus("Error: Durasi peminjaman harus antara 1 dan 30 hari", ColorPalette.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Error: Durasi peminjaman harus berupa angka valid", ColorPalette.ERROR);
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