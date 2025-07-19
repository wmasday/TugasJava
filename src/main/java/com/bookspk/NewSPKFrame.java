package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.Component;
import java.util.List;
import java.util.Map;

public class NewSPKFrame extends JFrame {
    private BookDAO bookDAO;
    private NewSPKCalculator calculator;
    private JTabbedPane tabbedPane;
    private List<NewSPKCalculator.SPKResult> currentResults;
    
    public NewSPKFrame() {
        bookDAO = new BookDAO();
        calculator = new NewSPKCalculator();
        
        setTitle("Sistem Pendukung Keputusan - Analisis Buku");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        initComponents();
        calculateSPK();
    }
    
    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new GradientPanel(ColorPalette.getBackgroundGradient());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Create tabs
        tabbedPane.addTab("Matriks Keputusan", createDecisionMatrixTab());
        tabbedPane.addTab("Normalisasi Matriks", createNormalizedMatrixTab());
        tabbedPane.addTab("Hasil Akumulatif", createFinalResultsTab());
        
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
        
        JLabel titleLabel = new JLabel("Sistem Pendukung Keputusan - Analisis Buku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton refreshButton = new GradientButton("Refresh", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        JButton exportButton = new GradientButton("Export PDF", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        
        refreshButton.setPreferredSize(new Dimension(100, 35));
        exportButton.setPreferredSize(new Dimension(120, 35));
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        refreshButton.addActionListener(e -> calculateSPK());
        exportButton.addActionListener(e -> exportToPDF());
        
        return headerPanel;
    }
    
    private JPanel createDecisionMatrixTab() {
        JPanel panel = new GradientPanel(ColorPalette.getCardGradient());
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Matriks Keputusan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Table will be populated later
        JTable table = new JTable();
        styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createNormalizedMatrixTab() {
        JPanel panel = new GradientPanel(ColorPalette.getCardGradient());
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Normalisasi Matriks Keputusan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Table will be populated later
        JTable table = new JTable();
        styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFinalResultsTab() {
        JPanel panel = new GradientPanel(ColorPalette.getCardGradient());
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Hasil Akumulatif & Ranking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Table will be populated later
        JTable table = new JTable();
        styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionBackground(ColorPalette.PRIMARY_BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(ColorPalette.BORDER_PRIMARY);
    }
    
    private void calculateSPK() {
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada data buku untuk dianalisis!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        currentResults = calculator.calculateSPK(books);
        Map<String, Object> matrix = calculator.getSPKMatrix(currentResults);
        
        // Update tables
        updateDecisionMatrixTable((List<List<Object>>) matrix.get("decisionMatrix"));
        updateNormalizedMatrixTable((List<List<Object>>) matrix.get("normalizedMatrix"));
        updateFinalResultsTable((List<List<Object>>) matrix.get("finalResults"));
        
        JOptionPane.showMessageDialog(this, "Analisis SPK berhasil dihitung!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateDecisionMatrixTable(List<List<Object>> data) {
        if (data.isEmpty()) return;
        
        JTable table = getTableFromTab(0);
        if (table == null) return;
        
        DefaultTableModel model = new DefaultTableModel();
        
        // Add columns
        for (Object header : data.get(0)) {
            model.addColumn(header);
        }
        
        // Add data rows
        for (int i = 1; i < data.size(); i++) {
            model.addRow(data.get(i).toArray());
        }
        
        table.setModel(model);
        
        // Set column alignment
        table.getColumnModel().getColumn(0).setCellRenderer(new CenterAlignedRenderer()); // Title - left aligned
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CenterAlignedRenderer()); // Numeric - center aligned
        }
    }
    
    private void updateNormalizedMatrixTable(List<List<Object>> data) {
        if (data.isEmpty()) return;
        
        JTable table = getTableFromTab(1);
        if (table == null) return;
        
        DefaultTableModel model = new DefaultTableModel();
        
        // Add columns
        for (Object header : data.get(0)) {
            model.addColumn(header);
        }
        
        // Add data rows
        for (int i = 1; i < data.size(); i++) {
            model.addRow(data.get(i).toArray());
        }
        
        table.setModel(model);
        
        // Set column alignment
        table.getColumnModel().getColumn(0).setCellRenderer(new LeftAlignedRenderer()); // Title - left aligned
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CenterAlignedRenderer()); // Numeric - center aligned
        }
    }
    
    private void updateFinalResultsTable(List<List<Object>> data) {
        if (data.isEmpty()) return;
        
        JTable table = getTableFromTab(2);
        if (table == null) return;
        
        DefaultTableModel model = new DefaultTableModel();
        
        // Add columns
        for (Object header : data.get(0)) {
            model.addColumn(header);
        }
        
        // Add data rows
        for (int i = 1; i < data.size(); i++) {
            model.addRow(data.get(i).toArray());
        }
        
        table.setModel(model);
        
        // Set column alignment
        table.getColumnModel().getColumn(0).setCellRenderer(new CenterAlignedRenderer()); // Ranking - center aligned
        table.getColumnModel().getColumn(1).setCellRenderer(new LeftAlignedRenderer()); // Title - left aligned
        for (int i = 2; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CenterAlignedRenderer()); // Numeric - center aligned
        }
    }
    
    private JTable getTableFromTab(int tabIndex) {
        if (tabIndex >= tabbedPane.getTabCount()) return null;
        
        JPanel panel = (JPanel) tabbedPane.getComponentAt(tabIndex);
        if (panel == null) return null;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                return (JTable) scrollPane.getViewport().getView();
            }
        }
        
        return null;
    }
    
    private void exportToPDF() {
        if (currentResults == null || currentResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada data SPK untuk diekspor! Silakan refresh terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export SPK Analysis to PDF");
        fileChooser.setSelectedFile(new java.io.File("SPK_Analysis.pdf"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                SPKPDFExporter.exportSPKAnalysis(currentResults, fileToSave);
                JOptionPane.showMessageDialog(this, "PDF berhasil diekspor ke: " + fileToSave.getAbsolutePath(), "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mengekspor PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Custom renderer for center alignment
    private static class CenterAlignedRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }
    
    // Custom renderer for left alignment
    private static class LeftAlignedRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.LEFT);
            return c;
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