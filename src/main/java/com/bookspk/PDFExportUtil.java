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
        // Use landscape orientation
        PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
        PDPage page = new PDPage(landscape);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        float margin = 40;
        float yStart = page.getMediaBox().getHeight() - margin;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;
        float rowHeight = 20;
        float cellMargin = 5;
        int fontSize = 10;

        // Draw title
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(title);
        contentStream.endText();
        yPosition -= 2 * rowHeight;

        TableModel model = table.getModel();
        int cols = model.getColumnCount();
        int rows = model.getRowCount();
        float[] colWidths = new float[cols];
        float colWidth = tableWidth / cols;
        for (int i = 0; i < cols; i++) colWidths[i] = colWidth;

        // Draw header
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
        float xPosition = margin;
        for (int i = 0; i < cols; i++) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize); // Ensure setFont before showText
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition + cellMargin, yPosition);
            contentStream.showText(model.getColumnName(i));
            contentStream.endText();
            xPosition += colWidths[i];
        }
        yPosition -= rowHeight;

        // Draw rows
        for (int row = 0; row < rows; row++) {
            xPosition = margin;
            for (int col = 0; col < cols; col++) {
                Object value = model.getValueAt(row, col);
                String text = value == null ? "" : value.toString();
                contentStream.setFont(PDType1Font.HELVETICA, fontSize); // Ensure setFont before showText
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + cellMargin, yPosition);
                contentStream.showText(text);
                contentStream.endText();
                xPosition += colWidths[col];
            }
            yPosition -= rowHeight;
            // Add new page if needed
            if (yPosition < margin + rowHeight) {
                contentStream.close();
                page = new PDPage(landscape);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = yStart;
            }
        }
        contentStream.close();
        document.save(file);
        document.close();
    }
} 