package com.bookspk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CriteriaCRUDFrame extends JFrame {
    private CriteriaDAO criteriaDAO = new CriteriaDAO();
    private JTable table;
    private DefaultTableModel tableModel;

    public CriteriaCRUDFrame() {
        setTitle("CRUD Criteria SPK");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
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
        
        // Content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        loadCriteria();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new GradientPanel(ColorPalette.getCardGradient());
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("Manajemen Kriteria SPK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnAdd = new GradientButton("Tambah", ColorPalette.PRIMARY_GREEN, ColorPalette.SECONDARY_GREEN);
        JButton btnEdit = new GradientButton("Edit", ColorPalette.PRIMARY_ORANGE, ColorPalette.SECONDARY_ORANGE);
        JButton btnDelete = new GradientButton("Hapus", ColorPalette.PRIMARY_RED, ColorPalette.SECONDARY_RED);
        JButton btnRefresh = new GradientButton("Refresh", ColorPalette.PRIMARY_BLUE, ColorPalette.SECONDARY_BLUE);
        
        btnAdd.setPreferredSize(new Dimension(100, 35));
        btnEdit.setPreferredSize(new Dimension(100, 35));
        btnDelete.setPreferredSize(new Dimension(100, 35));
        btnRefresh.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        btnAdd.addActionListener(e -> addCriteria());
        btnEdit.addActionListener(e -> editCriteria());
        btnDelete.addActionListener(e -> deleteCriteria());
        btnRefresh.addActionListener(e -> loadCriteria());
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new GradientPanel(ColorPalette.getCardGradient());
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Table
        String[] columns = {"ID", "Code", "Name", "Bobot"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionBackground(ColorPalette.PRIMARY_BLUE);
        table.setSelectionForeground(Color.WHITE);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);   // Code
        table.getColumnModel().getColumn(2).setPreferredWidth(200);  // Name
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Bobot
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_PRIMARY, 1));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }

    private void loadCriteria() {
        tableModel.setRowCount(0);
        List<Criteria> list = criteriaDAO.getAllCriteria();
        for (Criteria c : list) {
            tableModel.addRow(new Object[]{c.getId(), c.getCode(), c.getName(), c.getBobot()});
        }
    }

    private void addCriteria() {
        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField bobotField = new JTextField();
        
        // Style the dialog
        codeField.setPreferredSize(new Dimension(200, 25));
        nameField.setPreferredSize(new Dimension(200, 25));
        bobotField.setPreferredSize(new Dimension(200, 25));
        
        Object[] msg = {"Code:", codeField, "Name:", nameField, "Bobot:", bobotField};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Tambah Kriteria", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                String code = codeField.getText().trim();
                String name = nameField.getText().trim();
                float bobot = Float.parseFloat(bobotField.getText().trim());
                
                if (code.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Code dan Name tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Criteria c = new Criteria(0, code, name, bobot);
                if (criteriaDAO.addCriteria(c)) {
                    loadCriteria();
                    JOptionPane.showMessageDialog(this, "Kriteria berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan kriteria!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bobot harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCriteria() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris untuk edit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        String code = (String) tableModel.getValueAt(row, 1);
        String name = (String) tableModel.getValueAt(row, 2);
        float bobot = Float.parseFloat(tableModel.getValueAt(row, 3).toString());
        
        JTextField codeField = new JTextField(code);
        JTextField nameField = new JTextField(name);
        JTextField bobotField = new JTextField(String.valueOf(bobot));
        
        // Style the dialog
        codeField.setPreferredSize(new Dimension(200, 25));
        nameField.setPreferredSize(new Dimension(200, 25));
        bobotField.setPreferredSize(new Dimension(200, 25));
        
        Object[] msg = {"Code:", codeField, "Name:", nameField, "Bobot:", bobotField};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Edit Kriteria", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                String newCode = codeField.getText().trim();
                String newName = nameField.getText().trim();
                float newBobot = Float.parseFloat(bobotField.getText().trim());
                
                if (newCode.isEmpty() || newName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Code dan Name tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Criteria c = new Criteria(id, newCode, newName, newBobot);
                if (criteriaDAO.updateCriteria(c)) {
                    loadCriteria();
                    JOptionPane.showMessageDialog(this, "Kriteria berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengupdate kriteria!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bobot harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCriteria() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris untuk hapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 2);
        
        int opt = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus kriteria '" + name + "'?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
            
        if (opt == JOptionPane.YES_OPTION) {
            if (criteriaDAO.deleteCriteria(id)) {
                loadCriteria();
                JOptionPane.showMessageDialog(this, "Kriteria berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kriteria!", "Error", JOptionPane.ERROR_MESSAGE);
            }
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