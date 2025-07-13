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
    private JSlider ratingSlider, priceSlider, yearSlider, pagesSlider;
    private JLabel spkResultLabel;
    private JPanel spkPanel;
    private JButton calculateButton;
    private JButton backButton;
    private JTable spkTable;
    private DefaultTableModel spkTableModel;
    
    public SPKFrame(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("SPK Analysis - Book Selection");
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
        
        JLabel titleLabel = new JLabel("Decision Support System (SPK) Analysis");
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
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Create split pane for left-right layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        
        // Left Panel - Criteria
        JPanel criteriaPanel = createCriteriaPanel();
        splitPane.setLeftComponent(criteriaPanel);
        
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
        JLabel titleLabel = new JLabel("Set Criteria Weights");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
        
        // Rating Slider
        JLabel ratingLabel = new JLabel("Rating (0-5):");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ratingLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(ratingLabel, gbc);
        
        ratingSlider = new JSlider(0, 5, 3);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ratingSlider.setPreferredSize(new Dimension(350, 50));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(ratingSlider, gbc);
        
        // Price Slider
        JLabel priceLabel = new JLabel("Price (0-1M):");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(priceLabel, gbc);
        
        priceSlider = new JSlider(0, 1000000, 500000);
        priceSlider.setMajorTickSpacing(200000);
        priceSlider.setPaintTicks(true);
        priceSlider.setPaintLabels(true);
        priceSlider.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        priceSlider.setPreferredSize(new Dimension(350, 50));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(priceSlider, gbc);
        
        // Year Slider
        JLabel yearLabel = new JLabel("Year (1900-2024):");
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        yearLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(yearLabel, gbc);
        
        yearSlider = new JSlider(1900, 2024, 2020);
        yearSlider.setMajorTickSpacing(20);
        yearSlider.setPaintTicks(true);
        yearSlider.setPaintLabels(true);
        yearSlider.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearSlider.setPreferredSize(new Dimension(350, 50));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(yearSlider, gbc);
        
        // Pages Slider
        JLabel pagesLabel = new JLabel("Pages (0-1000):");
        pagesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pagesLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(pagesLabel, gbc);
        
        pagesSlider = new JSlider(0, 1000, 300);
        pagesSlider.setMajorTickSpacing(200);
        pagesSlider.setPaintTicks(true);
        pagesSlider.setPaintLabels(true);
        pagesSlider.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pagesSlider.setPreferredSize(new Dimension(350, 50));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(pagesSlider, gbc);
        
        // Calculate Button
        calculateButton = new GradientButton("Calculate SPK", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        calculateButton.setPreferredSize(new Dimension(220, 50));
        calculateButton.addActionListener(e -> calculateSPK());
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
        
        JLabel titleLabel = new JLabel("SPK Analysis Results");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);
        
        JLabel filterLabel = new JLabel("Show Top:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        JComboBox<Integer> resultCountCombo = new JComboBox<>(new Integer[]{3, 5, 10, 15, 20});
        resultCountCombo.setSelectedItem(5);
        resultCountCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultCountCombo.setPreferredSize(new Dimension(80, 30));
        
        JButton refreshButton = new GradientButton("Refresh", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        refreshButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.addActionListener(e -> calculateSPK());
        
        filterPanel.add(filterLabel);
        filterPanel.add(resultCountCombo);
        filterPanel.add(refreshButton);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);
        
        // Results table
        String[] columnNames = {"Rank", "Title", "Author", "Category", "Rating", "Price", "Year", "Pages", "SPK Score"};
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
        spkTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // Rank
        spkTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
        spkTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Author
        spkTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Category
        spkTable.getColumnModel().getColumn(4).setPreferredWidth(60);  // Rating
        spkTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Price
        spkTable.getColumnModel().getColumn(6).setPreferredWidth(60);  // Year
        spkTable.getColumnModel().getColumn(7).setPreferredWidth(60);  // Pages
        spkTable.getColumnModel().getColumn(8).setPreferredWidth(100); // SPK Score
        
        JScrollPane scrollPane = new JScrollPane(spkTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        scrollPane.setPreferredSize(new Dimension(700, 500));
        
        // Status label
        spkResultLabel = new JLabel("Click 'Calculate SPK' to see results");
        spkResultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spkResultLabel.setForeground(ColorPalette.TEXT_SECONDARY);
        spkResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(spkResultLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void calculateSPK() {
        // Get weights from sliders
        double ratingWeight = ratingSlider.getValue() / 5.0;
        double priceWeight = (1000000 - priceSlider.getValue()) / 1000000.0;
        double yearWeight = (yearSlider.getValue() - 1900) / (2024 - 1900);
        double pagesWeight = pagesSlider.getValue() / 1000.0;
        
        double[] weights = {ratingWeight, priceWeight, yearWeight, pagesWeight};
        
        // Get top books using SPK (default 5, can be changed by filter)
        int resultCount = 5; // Default value
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
                String.format("%.1f", book.getRating()),
                String.format("Rp %.0f", book.getPrice()),
                book.getYear(),
                book.getPages(),
                String.format("%.3f", spkScore)
            };
            spkTableModel.addRow(row);
        }
        
        // Update status
        spkResultLabel.setText(String.format("SPK Analysis completed. Showing top %d books based on your criteria preferences.", topBooks.size()));
        spkResultLabel.setForeground(ColorPalette.SUCCESS);
        
        spkPanel.revalidate();
        spkPanel.repaint();
        
        // Ensure divider stays at 50%
        SwingUtilities.invokeLater(() -> {
            Container parent = spkPanel.getParent();
            if (parent instanceof JSplitPane) {
                JSplitPane splitPane = (JSplitPane) parent;
                splitPane.setDividerLocation(0.5);
            }
        });
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