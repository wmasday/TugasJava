package com.bookspk;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;

public class PDFExportUtil {
    /**
     * Exports the given JTable to a PDF file.
     * @param table JTable to export
     * @param title Title for the PDF document
     * @param file Destination PDF file
     * @throws IOException if PDF writing fails
     */
    public static void exportTableToPDF(JTable table, String title, File file) throws IOException {
        PDDocument document = new PDDocument();
        PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
        PDPage page = new PDPage(landscape);
        document.addPage(page);

        int pageNumber = 1;
        java.util.List<PDPage> pages = new java.util.ArrayList<>();
        pages.add(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        float margin = 40;
        float yStart = page.getMediaBox().getHeight() - margin;
        int fontSize = 9; // Reduced font size
        int cardsPerRow = 2; // 2 cards per row
        float cardSpacing = 18; // Slightly reduced spacing
        float lineSpacing = 13; // Reduced line spacing
        float cellMargin = 7; // Reduced padding inside card
        float cardWidth = (page.getMediaBox().getWidth() - 2 * margin - (cardsPerRow - 1) * cardSpacing) / cardsPerRow;
        float cardHeightEstimate = 0;
        java.awt.Color cardBgColor = new java.awt.Color(245, 245, 255); // Subtle blueish background
        float cardCornerRadius = 8f; // Slightly smaller corner radius
        float tableColGap = 10; // Gap between field name and value in card
        float cardTitleTopPadding = 8; // Extra top padding for first field
        float labelColPercent = 0.3f; // 30% for field name
        float valueColPercent = 0.7f; // 70% for value

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy HH:mm", new java.util.Locale("id"));
        String dateStr = sdf.format(new java.util.Date());
        float headerHeight = 60;
        float footerHeight = 30;
        float headerY = page.getMediaBox().getHeight() - margin;
        float footerY = margin / 2;
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();

        // Mapping English column names to Indonesian labels
        java.util.Map<String, String> labelMap = new java.util.HashMap<>();
        labelMap.put("Title", "Judul");
        labelMap.put("Author", "Penulis");
        labelMap.put("Category", "Kategori");
        labelMap.put("Publisher", "Penerbit");
        labelMap.put("Year", "Tahun");
        labelMap.put("Pages", "Halaman");
        labelMap.put("Rating", "Rating");
        labelMap.put("Price", "Harga");
        labelMap.put("ISBN", "ISBN");
        labelMap.put("Borrower Count", "Jumlah Peminjam");
        labelMap.put("Book Condition", "Kondisi Fisik Buku");
        labelMap.put("Content Relevance", "Relevansi Isi Buku");
        labelMap.put("Loan Duration", "Durasi Peminjaman");
        labelMap.put("Rank", "Peringkat");
        labelMap.put("SPK Score", "Nilai SPK");

        TableModel model = table.getModel();
        int cols = model.getColumnCount();
        int rows = model.getRowCount();

        int cardIndex = 0;
        float xCard = margin;
        float yCard = yStart - headerHeight;
        float maxCardHeightInRow = 0;

        drawHeader(contentStream, title, dateStr, margin, headerY, pageWidth);
        drawFooter(contentStream, pageNumber, pageWidth, footerY);

        for (int row = 0; row < rows; row++) {
            float labelColWidth = cardWidth * labelColPercent;
            float valueColWidth = cardWidth * valueColPercent - 2 * cellMargin;
            float cardHeight = (cols * lineSpacing) + (2 * cellMargin) + cardTitleTopPadding;
            for (int col = 0; col < cols; col++) {
                String colName = model.getColumnName(col);
                String key = labelMap.getOrDefault(colName, colName) + ": ";
                Object value = model.getValueAt(row, col);
                String text = value == null ? "" : value.toString();
                float keyWidth = labelColWidth;
                float maxValueWidth = valueColWidth;
                String[] lines = wrapText(text, PDType1Font.HELVETICA, fontSize, maxValueWidth);
                if (lines.length > 1) {
                    cardHeight += (lines.length - 1) * lineSpacing;
                }
            }
            cardHeightEstimate = cardHeight;

            if (yCard - cardHeightEstimate < margin + footerHeight) {
                contentStream.close();
                page = new PDPage(landscape);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                pages.add(page);
                pageNumber++;
                yCard = yStart - headerHeight;
                xCard = margin;
                cardIndex = 0;
                maxCardHeightInRow = 0;
                drawHeader(contentStream, title, dateStr, margin, headerY, pageWidth);
                drawFooter(contentStream, pageNumber, pageWidth, footerY);
            }

            drawRoundedRect(contentStream, xCard, yCard - cardHeightEstimate + cellMargin, cardWidth, cardHeightEstimate - cellMargin, cardCornerRadius, cardBgColor);
            contentStream.setStrokingColor(120, 120, 180);
            contentStream.addRect(xCard, yCard - cardHeightEstimate + cellMargin, cardWidth, cardHeightEstimate - cellMargin);
            contentStream.stroke();

            float xText = xCard + cellMargin;
            float yText = yCard - cellMargin - cardTitleTopPadding;

            for (int col = 0; col < cols; col++) {
                String colName = model.getColumnName(col);
                String key = labelMap.getOrDefault(colName, colName) + ": ";
                Object value = model.getValueAt(row, col);
                String text = value == null ? "" : value.toString();

                // Draw key (bold, left column, fixed width)
                contentStream.setNonStrokingColor(0, 32, 96);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
                contentStream.beginText();
                contentStream.newLineAtOffset(xText, yText);
                contentStream.showText(key);
                contentStream.endText();

                // Draw value (right column, wrap if needed, fixed width)
                contentStream.setNonStrokingColor(30, 30, 30);
                contentStream.setFont(PDType1Font.HELVETICA, fontSize);
                float xValue = xText + labelColWidth;
                float yValue = yText;
                String[] lines = wrapText(text, PDType1Font.HELVETICA, fontSize, valueColWidth);
                for (String line : lines) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xValue, yValue);
                    contentStream.showText(line);
                    contentStream.endText();
                    yValue -= lineSpacing;
                }
                yText -= Math.max(lineSpacing, (lines.length) * lineSpacing);
            }

            if (cardHeightEstimate > maxCardHeightInRow) {
                maxCardHeightInRow = cardHeightEstimate;
            }

            cardIndex++;
            if (cardIndex % cardsPerRow == 0) {
                yCard -= maxCardHeightInRow + cardSpacing;
                xCard = margin;
                maxCardHeightInRow = 0;
            } else {
                xCard += cardWidth + cardSpacing;
            }
        }
        contentStream.close();
        document.save(file);
        document.close();
    }

    /**
     * Export SPK analysis with criteria, weights, and scoring table to PDF.
     * @param table JTable containing SPK results
     * @param file Destination PDF file
     * @throws IOException if PDF writing fails
     */
    public static void exportSPKAnalysisToPDF(JTable table, File file) throws IOException {
        PDDocument document = new PDDocument();
        PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
        PDPage page = new PDPage(landscape);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        float margin = 40;
        float yStart = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        float x = margin;
        float y = yStart;
        int fontSize = 11;
        float lineSpacing = 16;

        // Header
        contentStream.setNonStrokingColor(0, 32, 96);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Laporan Analisis SPK Pemilihan Buku");
        contentStream.endText();
        y -= lineSpacing + 8;

        // Tanggal
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy HH:mm", new java.util.Locale("id"));
        String dateStr = sdf.format(new java.util.Date());
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.setNonStrokingColor(80, 80, 80);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Tanggal ekspor: " + dateStr);
        contentStream.endText();
        y -= lineSpacing + 2;

        // Kriteria dan Bobot
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 13);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Kriteria dan Bobot Penilaian:");
        contentStream.endText();
        y -= lineSpacing;
        contentStream.setFont(PDType1Font.HELVETICA, fontSize);
        String[] kriteria = {
            "C1. Jumlah Peminjam (30%)",
            "C2. Kategori Jenis Buku & Rating (20%)",
            "C3. Kondisi Fisik Buku (15%)",
            "C4. Relevansi Isi Buku (25%)",
            "C5. Durasi Peminjaman (10%)"
        };
        for (String k : kriteria) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + 10, y);
            contentStream.showText(k);
            contentStream.endText();
            y -= lineSpacing - 2;
        }
        y -= 4;
        // Tabel penilaian kriteria
        String[][] tabelKriteria = {
            {"C1. Jumlah Peminjam", "1-20: Bobot 1", "21-40: Bobot 2", "41-60: Bobot 3", "61-80: Bobot 4", "81-100: Bobot 5"},
            {"C2. Kategori & Rating", "Ensiklopedia, 1.0-1.9: 1", "Komik & Manga, 2.0-3.1: 2", "Non-Fiksi, 3.1-4.0: 3", "Fiksi, 4.1-4.5: 4", "Pendidikan, 4.6-5.0: 5"},
            {"C3. Kondisi Fisik", "Rusak Berat: 1", "Rusak Ringan: 2", "Sedikit Baik: 3", "Baik: 4", "Sangat Baik: 5"},
            {"C4. Relevansi Isi", "Tidak Relevan: 1", "Kurang Relevan: 2", "Cukup Relevan: 3", "Relevan: 4", "Sangat Relevan: 5"},
            {"C5. Durasi Peminjaman", "<3 Hari: 1", "3-6 Hari: 2", "7-10 Hari: 3", "11-14 Hari: 4", ">14 Hari: 5"}
        };
        float tableX = x + 10;
        float tableY = y;
        float cellW = 110;
        float cellH = 15;
        contentStream.setFont(PDType1Font.HELVETICA, 9);
        for (int i = 0; i < tabelKriteria.length; i++) {
            float cx = tableX;
            for (int j = 0; j < tabelKriteria[i].length; j++) {
                contentStream.addRect(cx, tableY - cellH, cellW, cellH);
                contentStream.setNonStrokingColor(255, 255, 255);
                contentStream.fill();
                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setLineWidth(0.5f);
                contentStream.addRect(cx, tableY - cellH, cellW, cellH);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.newLineAtOffset(cx + 2, tableY - cellH + 4);
                contentStream.showText(tabelKriteria[i][j]);
                contentStream.endText();
                cx += cellW;
            }
            tableY -= cellH;
        }
        y = tableY - 2 * lineSpacing;

        // Judul hasil
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 13);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Tabel Hasil Analisis SPK");
        contentStream.endText();
        y -= lineSpacing;

        // Tabel hasil SPK (tambah kolom Keterangan Nilai, tanpa Penulis)
        TableModel model = table.getModel();
        int[] colIndexes = {0, 1, 3, 4, 5, 6, 7, 8}; // Peringkat, Judul, Kategori, Jumlah Peminjam, Kondisi Fisik, Relevansi, Durasi, Nilai SPK
        String[] headers = {"Peringkat", "Judul", "Kategori", "Jumlah Peminjam", "Kondisi Fisik", "Relevansi Isi", "Durasi", "Nilai SPK", "Keterangan Nilai"};
        float[] colWidths = {70, 140, 80, 70, 70, 70, 55, 60, 100};
        float tableStartX = x;
        float tableStartY = y;
        float rowH = 14;
        // Header row halaman pertama
        float cxHeaderFirst = tableStartX;
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
        for (int i = 0; i < headers.length; i++) {
            contentStream.addRect(cxHeaderFirst, tableStartY - rowH, colWidths[i], rowH);
            contentStream.setNonStrokingColor(220, 220, 255);
            contentStream.fill();
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setLineWidth(0.5f);
            contentStream.addRect(cxHeaderFirst, tableStartY - rowH, colWidths[i], rowH);
            contentStream.stroke();
            contentStream.beginText();
            contentStream.newLineAtOffset(cxHeaderFirst + 2, tableStartY - rowH + 3);
            contentStream.showText(headers[i]);
            contentStream.endText();
            cxHeaderFirst += colWidths[i];
        }
        y = tableStartY - rowH;
        // Data rows
        contentStream.setFont(PDType1Font.HELVETICA, 9);
        for (int row = 0; row < model.getRowCount(); row++) {
            // Cek jika tidak cukup ruang, buat halaman baru dan tulis header tabel lagi
            if (y < margin + 60) {
                contentStream.close();
                page = new PDPage(landscape);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                y = yStart;
                // Tulis ulang judul tabel
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 13);
                contentStream.beginText();
                contentStream.newLineAtOffset(x, y);
                contentStream.showText("Tabel Hasil Analisis SPK (lanjutan)");
                contentStream.endText();
                y -= lineSpacing;
                // Header row halaman baru
                float cxHeaderNext = tableStartX;
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
                for (int i = 0; i < headers.length; i++) {
                    contentStream.addRect(cxHeaderNext, y - rowH, colWidths[i], rowH);
                    contentStream.setNonStrokingColor(220, 220, 255);
                    contentStream.fill();
                    contentStream.setNonStrokingColor(0, 0, 0);
                    contentStream.setLineWidth(0.5f);
                    contentStream.addRect(cxHeaderNext, y - rowH, colWidths[i], rowH);
                    contentStream.stroke();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(cxHeaderNext + 2, y - rowH + 3);
                    contentStream.showText(headers[i]);
                    contentStream.endText();
                    cxHeaderNext += colWidths[i];
                }
                y -= rowH;
                contentStream.setFont(PDType1Font.HELVETICA, 9);
            }
            float cx = tableStartX;
            for (int c = 0; c < colIndexes.length; c++) {
                Object val = model.getValueAt(row, colIndexes[c]);
                String text = val == null ? "" : val.toString();
                contentStream.addRect(cx, y - rowH, colWidths[c], rowH);
                contentStream.setNonStrokingColor(255, 255, 255);
                contentStream.fill();
                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setLineWidth(0.5f);
                contentStream.addRect(cx, y - rowH, colWidths[c], rowH);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.newLineAtOffset(cx + 2, y - rowH + 3);
                contentStream.showText(text);
                contentStream.endText();
                cx += colWidths[c];
            }
            // Tambah kolom keterangan nilai
            String nilaiStr = model.getValueAt(row, 8) != null ? model.getValueAt(row, 8).toString() : "";
            double nilai = 0.0;
            try { nilai = Double.parseDouble(nilaiStr.replace(",", ".")); } catch (Exception ex) {}
            String ket;
            if (nilai >= 0.85) ket = "Sangat Baik";
            else if (nilai >= 0.70) ket = "Baik";
            else if (nilai >= 0.55) ket = "Cukup";
            else if (nilai >= 0.40) ket = "Kurang";
            else ket = "Sangat Kurang";
            contentStream.addRect(cx, y - rowH, colWidths[8], rowH);
            contentStream.setNonStrokingColor(255, 255, 255);
            contentStream.fill();
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setLineWidth(0.5f);
            contentStream.addRect(cx, y - rowH, colWidths[8], rowH);
            contentStream.stroke();
            contentStream.beginText();
            contentStream.newLineAtOffset(cx + 2, y - rowH + 3);
            contentStream.showText(ket);
            contentStream.endText();
            y -= rowH;
        }
        contentStream.close();
        document.save(file);
        document.close();
    }

    // Helper to draw header
    private static void drawHeader(PDPageContentStream contentStream, String title, String dateStr, float margin, float headerY, float pageWidth) throws IOException {
        contentStream.setNonStrokingColor(0, 32, 96); // Dark blue
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, headerY);
        contentStream.showText(title);
        contentStream.endText();
        // Date
        contentStream.setFont(PDType1Font.HELVETICA, 11);
        contentStream.setNonStrokingColor(80, 80, 80);
        contentStream.beginText();
        contentStream.newLineAtOffset(pageWidth - margin - 180, headerY);
        contentStream.showText("Tanggal ekspor: " + dateStr);
        contentStream.endText();
        // Line
        contentStream.setStrokingColor(180, 180, 180);
        contentStream.moveTo(margin, headerY - 10);
        contentStream.lineTo(pageWidth - margin, headerY - 10);
        contentStream.stroke();
    }

    // Helper to draw footer
    private static void drawFooter(PDPageContentStream contentStream, int pageNumber, float pageWidth, float footerY) throws IOException {
        contentStream.setNonStrokingColor(120, 120, 120);
        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(pageWidth / 2 - 30, footerY);
        contentStream.showText("Halaman " + pageNumber);
        contentStream.endText();
    }

    // Helper to draw rounded rectangle with fill color
    private static void drawRoundedRect(PDPageContentStream cs, float x, float y, float w, float h, float r, java.awt.Color fillColor) throws IOException {
        cs.saveGraphicsState();
        cs.setNonStrokingColor(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
        cs.moveTo(x + r, y);
        cs.lineTo(x + w - r, y);
        cs.curveTo(x + w, y, x + w, y, x + w, y + r);
        cs.lineTo(x + w, y + h - r);
        cs.curveTo(x + w, y + h, x + w, y + h, x + w - r, y + h);
        cs.lineTo(x + r, y + h);
        cs.curveTo(x, y + h, x, y + h, x, y + h - r);
        cs.lineTo(x, y + r);
        cs.curveTo(x, y, x, y, x + r, y);
        cs.closePath();
        cs.fill();
        cs.restoreGraphicsState();
    }

    // Helper to wrap text for PDFBox
    private static String[] wrapText(String text, PDType1Font font, int fontSize, float maxWidth) throws IOException {
        if (text == null || text.isEmpty()) return new String[] {""};
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        java.util.List<String> lines = new java.util.ArrayList<>();
        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            float width = font.getStringWidth(testLine) / 1000 * fontSize;
            if (width > maxWidth) {
                if (line.length() > 0) lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
        }
        if (line.length() > 0) lines.add(line.toString());
        return lines.toArray(new String[0]);
    }
} 