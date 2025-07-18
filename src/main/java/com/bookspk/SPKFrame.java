package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * SPK Analysis Frame with Modern UI
 * Clean color palette and gradient design
 */
public class SPKFrame extends JFrame {
    private BookDAO bookDAO;
    private JSlider borrowerCountSlider, bookConditionSlider, contentRelevanceSlider, loanDurationSlider;
    private JLabel spkResultLabel;
    private JPanel spkPanel;
    private JButton calculateButton;
    private JButton backButton;
    private JButton toggleCriteriaButton;
    private JTable spkTable;
    private DefaultTableModel spkTableModel;
    private JSplitPane splitPane;
    private JScrollPane criteriaScrollPane;
    private boolean criteriaVisible = true;
    
    // Result count selection
    private JComboBox<Integer> resultCountCombo;
    
    public SPKFrame(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Analisis SPK - Pemilihan Buku");
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
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
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
        
        JLabel titleLabel = new JLabel("Analisis Sistem Pendukung Keputusan (SPK)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        toggleCriteriaButton = new GradientButton("Hide SPK", ColorPalette.PRIMARY_ORANGE, ColorPalette.SECONDARY_ORANGE);
        toggleCriteriaButton.setPreferredSize(new Dimension(150, 35));
        toggleCriteriaButton.addActionListener(e -> toggleCriteriaPanel());
        
        backButton = new GradientButton("Kembali", ColorPalette.PRIMARY_GRAY, ColorPalette.SECONDARY_GRAY);
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e -> dispose());
        
        buttonPanel.add(toggleCriteriaButton);
        buttonPanel.add(backButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new GradientPanel(ColorPalette.getCardGradient());
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Create split pane for left-right layout
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        
        // Left Panel - Criteria with scroll pane
        JPanel criteriaPanel = createCriteriaPanel();
        criteriaScrollPane = new JScrollPane(criteriaPanel);
        criteriaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        criteriaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        criteriaScrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        criteriaScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        splitPane.setLeftComponent(criteriaScrollPane);
        
        // Right Panel - Results
        spkPanel = createResultsPanel();
        splitPane.setRightComponent(spkPanel);
        
        contentPanel.add(splitPane, BorderLayout.CENTER);
        
        // Set divider to exactly 50% after frame is visible and packed
        SwingUtilities.invokeLater(() -> {
            // Wait for frame to be fully visible
            Timer timer = new Timer(100, e -> {
                splitPane.setDividerLocation(0.5); // Exactly 50%
                ((Timer) e.getSource()).stop();
            });
            timer.setRepeats(false);
            timer.start();
        });
        
        return contentPanel;
    }
    
    private void toggleCriteriaPanel() {
        if (criteriaVisible) {
            // Hide criteria panel
            splitPane.setLeftComponent(null);
            toggleCriteriaButton.setText("Show SPK");
            criteriaVisible = false;
        } else {
            // Show criteria panel
            splitPane.setLeftComponent(criteriaScrollPane);
            toggleCriteriaButton.setText("Hide SPK");
            criteriaVisible = true;
            
            // Reset divider location
            SwingUtilities.invokeLater(() -> {
                splitPane.setDividerLocation(0.5);
            });
        }
        
        // Revalidate and repaint
        splitPane.revalidate();
        splitPane.repaint();
    }
    
    private JPanel createCriteriaPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Atur Bobot Kriteria SPK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
        
        // Jumlah Peminjam Slider
        JLabel borrowerCountLabel = new JLabel("Jumlah Peminjam (1-100):");
        borrowerCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowerCountLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(borrowerCountLabel, gbc);
        
        borrowerCountSlider = new JSlider(1, 100, 50);
        borrowerCountSlider.setMajorTickSpacing(20);
        borrowerCountSlider.setPaintTicks(true);
        borrowerCountSlider.setPaintLabels(true);
        borrowerCountSlider.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        borrowerCountSlider.setPreferredSize(new Dimension(350, 50));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(borrowerCountSlider, gbc);
        
        // Kondisi Fisik Buku Slider
        JLabel bookConditionLabel = new JLabel("Kondisi Fisik Buku (1-5):");
        bookConditionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookConditionLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(bookConditionLabel, gbc);
        
        bookConditionSlider = new JSlider(1, 5, 3);
        bookConditionSlider.setMajorTickSpacing(1);
        bookConditionSlider.setPaintTicks(true);
        bookConditionSlider.setPaintLabels(true);
        bookConditionSlider.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bookConditionSlider.setPreferredSize(new Dimension(350, 50));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(bookConditionSlider, gbc);
        
        // Relevansi Isi Buku Slider
        JLabel contentRelevanceLabel = new JLabel("Relevansi Isi Buku (1-5):");
        contentRelevanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentRelevanceLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(contentRelevanceLabel, gbc);
        
        contentRelevanceSlider = new JSlider(1, 5, 3);
        contentRelevanceSlider.setMajorTickSpacing(1);
        contentRelevanceSlider.setPaintTicks(true);
        contentRelevanceSlider.setPaintLabels(true);
        contentRelevanceSlider.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentRelevanceSlider.setPreferredSize(new Dimension(350, 50));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(contentRelevanceSlider, gbc);
        
        // Durasi Peminjaman Slider
        JLabel loanDurationLabel = new JLabel("Durasi Peminjaman (1-5):");
        loanDurationLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loanDurationLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(loanDurationLabel, gbc);
        
        loanDurationSlider = new JSlider(1, 5, 3);
        loanDurationSlider.setMajorTickSpacing(1);
        loanDurationSlider.setPaintTicks(true);
        loanDurationSlider.setPaintLabels(true);
        loanDurationSlider.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loanDurationSlider.setPreferredSize(new Dimension(350, 50));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loanDurationSlider, gbc);
        
        // Calculate Button
        calculateButton = new GradientButton("Hitung SPK", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        calculateButton.setPreferredSize(new Dimension(220, 50));
        calculateButton.addActionListener(e -> {
            // Get the selected count from combo box and pass it to calculateSPK
            Integer selectedCount = (Integer) resultCountCombo.getSelectedItem();
            calculateSPK(selectedCount != null ? selectedCount : 5);
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 12, 12, 12); // Extra top margin for button
        panel.add(calculateButton, gbc);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Title and filter panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Hasil SPK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);
        
        // JLabel filterLabel = new JLabel("Tampilkan Teratas:");
        // filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // filterLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Create combo box with more options up to 100
        Integer[] resultCountOptions = {3, 5, 10, 15, 20, 25, 30, 40, 50, 75, 100};
        resultCountCombo = new JComboBox<>(resultCountOptions);
        resultCountCombo.setSelectedItem(5);
        resultCountCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultCountCombo.setPreferredSize(new Dimension(80, 30));
        
        JButton refreshButton = new GradientButton("Muat Ulang", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        refreshButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.addActionListener(e -> {
            // Get the selected count from combo box and pass it to calculateSPK
            Integer selectedCount = (Integer) resultCountCombo.getSelectedItem();
            calculateSPK(selectedCount != null ? selectedCount : 5);
        });
        
        // Dropdown untuk ekspor PDF
        String[] exportOptions = {"Ekspor ke PDF", "Ekspor Analisis SPK"};
        JComboBox<String> exportCombo = new JComboBox<>(exportOptions);
        exportCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exportCombo.setPreferredSize(new Dimension(170, 30));
        JButton exportButton = new GradientButton("Export", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        exportButton.setPreferredSize(new Dimension(110, 30));
        exportButton.addActionListener(e -> {
            int selected = exportCombo.getSelectedIndex();
            JFileChooser fileChooser = new JFileChooser();
            if (selected == 0) {
                fileChooser.setDialogTitle("Simpan Hasil SPK ke PDF");
                fileChooser.setSelectedFile(new java.io.File("Hasil_SPK.pdf"));
            } else {
                fileChooser.setDialogTitle("Simpan Analisis SPK Lengkap ke PDF");
                fileChooser.setSelectedFile(new java.io.File("Analisis_SPK_Lengkap.pdf"));
            }
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                try {
                    if (selected == 0) {
                        PDFExportUtil.exportTableToPDF(spkTable, "Hasil Analisis SPK", fileToSave);
                        JOptionPane.showMessageDialog(this, "PDF berhasil diekspor!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        PDFExportUtil.exportSPKAnalysisToPDF(spkTable, fileToSave);
                        JOptionPane.showMessageDialog(this, "PDF analisis SPK berhasil diekspor!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mengekspor PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // filterPanel.add(filterLabel);
        filterPanel.add(resultCountCombo);
        filterPanel.add(refreshButton);
        filterPanel.add(exportCombo);
        filterPanel.add(exportButton);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);
        
        // Results table with new SPK criteria columns
        String[] columnNames = {"Peringkat", "Judul", "Penulis", "Kategori", "Jumlah Peminjam", "Kondisi Fisik Buku", "Relevansi Isi Buku", "Durasi Peminjaman", "Nilai SPK"};
        spkTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        spkTable = new JTable(spkTableModel);
        spkTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        spkTable.setRowHeight(30);
        spkTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        spkTable.setSelectionBackground(ColorPalette.PRIMARY_BLUE);
        spkTable.setSelectionForeground(Color.WHITE);
        
        // Set column widths
        spkTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // Rank
        spkTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Title
        spkTable.getColumnModel().getColumn(2).setPreferredWidth(120);  // Author
        spkTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Category
        spkTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Borrower Count
        spkTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Book Condition
        spkTable.getColumnModel().getColumn(6).setPreferredWidth(120);  // Content Relevance
        spkTable.getColumnModel().getColumn(7).setPreferredWidth(100);  // Loan Duration
        spkTable.getColumnModel().getColumn(8).setPreferredWidth(100);  // SPK Score
        
        JScrollPane scrollPane = new JScrollPane(spkTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        scrollPane.setPreferredSize(new Dimension(900, 500));
        
        // Status label
        spkResultLabel = new JLabel("Klik 'Hitung SPK' untuk melihat hasil");
        spkResultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spkResultLabel.setForeground(ColorPalette.TEXT_SECONDARY);
        spkResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(spkResultLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void calculateSPK() {
        // Default to 5 if no parameter provided (for backward compatibility)
        calculateSPK(5);
    }
    
    private void calculateSPK(int resultCount) {
        // Get weights from sliders for new SPK criteria
        double borrowerCountWeight = borrowerCountSlider.getValue() / 100.0;
        double bookConditionWeight = bookConditionSlider.getValue() / 5.0;
        double contentRelevanceWeight = contentRelevanceSlider.getValue() / 5.0;
        double loanDurationWeight = loanDurationSlider.getValue() / 5.0;
        
        double[] weights = {borrowerCountWeight, bookConditionWeight, contentRelevanceWeight, loanDurationWeight};
        
        // Get top books using SPK with the specified result count
        List<BookDAO.BookSPKResult> topBooks = bookDAO.getTopBooksSPK(resultCount, weights);
        
        // Clear existing table data
        spkTableModel.setRowCount(0);
        
        // Add results to table
        for (int i = 0; i < topBooks.size(); i++) {
            BookDAO.BookSPKResult result_book = topBooks.get(i);
            Book book = result_book.getBook();
            double spkScore = result_book.getSpkScore();
            
            Object[] row = {
                i + 1, // Rank
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getBorrowerCount(),
                book.getBookCondition(),
                book.getContentRelevance(),
                book.getLoanDuration() + " hari",
                String.format("%.3f", spkScore)
            };
            spkTableModel.addRow(row);
        }
        
        // Update status
        spkResultLabel.setText(String.format("Analisis SPK selesai. Menampilkan buku teratas berdasarkan preferensi kriteria Anda.", topBooks.size()));
        spkResultLabel.setForeground(ColorPalette.SUCCESS);
        
        spkPanel.revalidate();
        spkPanel.repaint();
        
        // Ensure divider stays at 50% only if criteria is visible
        if (criteriaVisible) {
            SwingUtilities.invokeLater(() -> {
                Container parent = spkPanel.getParent();
                if (parent instanceof JSplitPane) {
                    JSplitPane splitPane = (JSplitPane) parent;
                    splitPane.setDividerLocation(0.5);
                }
            });
        }
    }
    
    // Helper methods to find sliders and labels safely
    private JSlider findSlider(JPanel panel, int index) {
        Component[] components = panel.getComponents();
        int sliderCount = 0;
        for (Component comp : components) {
            if (comp instanceof JSlider) {
                if (sliderCount == index) {
                    return (JSlider) comp;
                }
                sliderCount++;
            }
        }
        return null;
    }
    
    private JLabel findLabel(JPanel panel, int index) {
        Component[] components = panel.getComponents();
        int labelCount = 0;
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                if (labelCount == index) {
                    return (JLabel) comp;
                }
                labelCount++;
            }
        }
        return null;
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