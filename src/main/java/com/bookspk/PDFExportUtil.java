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