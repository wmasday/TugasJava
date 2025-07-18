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
        setTitle("Sistem SPK Pemilihan Buku");
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
        welcomeLabel = new JLabel("Selamat Datang, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        spkButton = new GradientButton("Analisis SPK", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        crudButton = new GradientButton("Manage Book", ColorPalette.PRIMARY_ORANGE, ColorPalette.SECONDARY_ORANGE);
        editProfileButton = new GradientButton("Edit Profil", ColorPalette.PRIMARY_PURPLE, ColorPalette.SECONDARY_PURPLE);
        logoutButton = new GradientButton("Keluar", ColorPalette.PRIMARY_RED, ColorPalette.SECONDARY_RED);
        JButton manageCriteriaButton = new GradientButton("Manage Criteria", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        
        // Customize button sizes
        spkButton.setPreferredSize(new Dimension(120, 35));
        crudButton.setPreferredSize(new Dimension(120, 35));
        editProfileButton.setPreferredSize(new Dimension(120, 35));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        manageCriteriaButton.setPreferredSize(new Dimension(140, 35));
        
        buttonPanel.add(spkButton);
        buttonPanel.add(crudButton);
        buttonPanel.add(editProfileButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(manageCriteriaButton);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Add event listeners
        spkButton.addActionListener(e -> openSPKFrame());
        crudButton.addActionListener(e -> openCRUDFrame());
        editProfileButton.addActionListener(e -> openEditProfileFrame());
        logoutButton.addActionListener(e -> logout());
        manageCriteriaButton.addActionListener(e -> openCriteriaFrame());
        
        return headerPanel;
    }
    
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // All Books Tab
        JPanel allBooksPanel = createAllBooksPanel();
        tabbedPane.addTab("Semua Buku", allBooksPanel);
        
        return tabbedPane;
    }
    
    private JPanel createAllBooksPanel() {
        JPanel panel = new GradientPanel(ColorPalette.getCardGradient());
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);
        
        JLabel categoryLabel = new JLabel("Filter Kategori:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("Semua Kategori");
        List<String> categories = bookDAO.getAllCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setPreferredSize(new Dimension(150, 30));
        
        JButton refreshButton = new GradientButton("Muat Ulang", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        refreshButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.addActionListener(e -> loadBooks());
        
        // Print to PDF button
        JButton printPDFButton = new GradientButton("Ekspor ke PDF", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        printPDFButton.setPreferredSize(new Dimension(130, 30));
        printPDFButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Semua Buku ke PDF");
            fileChooser.setSelectedFile(new java.io.File("Semua_Buku.pdf"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                try {
                    PDFExportUtil.exportTableToPDF(bookTable, "All Books", fileToSave);
                    JOptionPane.showMessageDialog(this, "PDF berhasil diekspor!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mengekspor PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        filterPanel.add(categoryLabel);
        filterPanel.add(categoryComboBox);
        filterPanel.add(refreshButton);
        filterPanel.add(printPDFButton);
        
        // Table with new SPK criteria columns
        String[] columnNames = {"Judul", "Penulis", "Kategori", "Penerbit", "Tahun", "Halaman", "Rating", "Harga", "ISBN", "Jumlah Peminjam", "Kondisi Fisik Buku", "Relevansi Isi Buku", "Durasi Peminjaman"};
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
        
        // Add mouse listener for row selection to show book details
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Single click
                    int row = bookTable.getSelectedRow();
                    if (row >= 0) {
                        showBookDetailsModal(row);
                    }
                }
            }
        });
        
        // Add category filter listener
        categoryComboBox.addActionListener(e -> loadBooks());
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showBookDetailsModal(int row) {
        // Get book data from table
        String title = (String) tableModel.getValueAt(row, 0);
        String author = (String) tableModel.getValueAt(row, 1);
        
        // Find book by title and author
        Book book = bookDAO.getBookByTitleAndAuthor(title, author);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Tidak dapat menemukan detail buku", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create modal dialog
        JDialog modal = new JDialog(this, "Detail Buku", true);
        modal.setLayout(new BorderLayout());
        modal.setSize(700, 600);
        modal.setLocationRelativeTo(this);
        modal.setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new GradientPanel(ColorPalette.getBackgroundGradient());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header panel
        JPanel headerPanel = new GradientPanel(ColorPalette.getCardGradient());
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("Detail Buku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        JButton closeButton = new GradientButton("Tutup", ColorPalette.PRIMARY_GRAY, ColorPalette.SECONDARY_GRAY);
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> modal.dispose());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(closeButton, BorderLayout.EAST);
        
        // Content panel
        JPanel contentPanel = new GradientPanel(ColorPalette.getCardGradient());
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Create scrollable content
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        
        JPanel detailsPanel = createBookDetailsPanel(book);
        scrollPane.setViewportView(detailsPanel);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        modal.add(mainPanel);
        modal.setVisible(true);
    }
    
    private JPanel createBookDetailsPanel(Book book) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Basic Information Section
        JLabel basicTitleLabel = new JLabel("Informasi Dasar");
        basicTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        basicTitleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 20, 8);
        panel.add(basicTitleLabel, gbc);
        
        // Title
        addDetailField(panel, "Judul:", book.getTitle(), gbc, 1);
        
        // Author
        addDetailField(panel, "Penulis:", book.getAuthor(), gbc, 2);
        
        // Category
        addDetailField(panel, "Kategori:", book.getCategory(), gbc, 3);
        
        // Publisher
        addDetailField(panel, "Penerbit:", book.getPublisher(), gbc, 4);
        
        // Year
        addDetailField(panel, "Tahun:", String.valueOf(book.getYear()), gbc, 5);
        
        // Pages
        addDetailField(panel, "Halaman:", String.valueOf(book.getPages()), gbc, 6);
        
        // Rating
        addDetailField(panel, "Rating:", String.format("%.1f", book.getRating()), gbc, 7);
        
        // Price
        addDetailField(panel, "Harga:", String.format("Rp %.0f", book.getPrice()), gbc, 8);
        
        // ISBN
        addDetailField(panel, "ISBN:", book.getIsbn(), gbc, 9);
        
        // SPK Criteria Section
        JLabel spkTitleLabel = new JLabel("Kriteria SPK");
        spkTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        spkTitleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 8, 20, 8);
        panel.add(spkTitleLabel, gbc);
        
        // Borrower Count
        addDetailField(panel, "Jumlah Peminjam:", String.valueOf(book.getBorrowerCount()), gbc, 11);
        
        // Book Condition
        addDetailField(panel, "Kondisi Fisik Buku:", book.getBookCondition(), gbc, 12);
        
        // Content Relevance
        addDetailField(panel, "Relevansi Isi Buku:", book.getContentRelevance(), gbc, 13);
        
        // Loan Duration
        addDetailField(panel, "Durasi Peminjaman:", book.getLoanDuration() + " hari", gbc, 14);
        
        // Description Section
        JLabel descTitleLabel = new JLabel("Deskripsi");
        descTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        descTitleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 8, 20, 8);
        panel.add(descTitleLabel, gbc);
        
        // Description text area with proper sizing
        JTextArea descArea = new JTextArea(book.getDescription());
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(ColorPalette.CARD_BACKGROUND);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Set preferred size to use full width
        descArea.setPreferredSize(new Dimension(550, 120));
        descArea.setMinimumSize(new Dimension(550, 120));
        
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 8, 15, 8);
        panel.add(descArea, gbc);
        
        return panel;
    }
    
    private void addDetailField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComponent.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 8, 5, 8);
        panel.add(labelComponent, gbc);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComponent.setForeground(ColorPalette.TEXT_SECONDARY);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.7;
        panel.add(valueComponent, gbc);
    }
    
    private void loadBooks() {
        tableModel.setRowCount(0);
        
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        List<Book> books;
        
        if ("Semua Kategori".equals(selectedCategory)) {
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
                book.getIsbn(),
                book.getBorrowerCount(),
                book.getBookCondition(),
                book.getContentRelevance(),
                book.getLoanDuration() + " hari"
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
        EditProfileFrame editProfileFrame = new EditProfileFrame(currentUser);
        editProfileFrame.setVisible(true);
    }
    
    private void openCriteriaFrame() {
        CriteriaCRUDFrame frame = new CriteriaCRUDFrame();
        frame.setVisible(true);
    }
    
    private void logout() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin keluar?",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
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