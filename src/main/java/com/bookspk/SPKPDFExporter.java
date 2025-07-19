package com.bookspk;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SPKPDFExporter {
    
    private static final Font TITLE_FONT = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
    private static final Font HEADER_FONT = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
    private static final Font NORMAL_FONT = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);
    private static final Font SMALL_FONT = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.NORMAL);
    
    public static void exportSPKAnalysis(List<NewSPKCalculator.SPKResult> results, File outputFile) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 80, 50);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        
        // Add header and footer
        writer.setPageEvent(new HeaderFooterPageEvent());
        
        document.open();
        
        // Add title page
        addTitlePage(document);
        document.newPage();
        
        // Add criteria information
        addCriteriaInfo(document);
        document.newPage();
        
        // Add decision matrix
        addDecisionMatrix(document, results);
        document.newPage();
        
        // Add normalized matrix
        addNormalizedMatrix(document, results);
        document.newPage();
        
        // Add final results
        addFinalResults(document, results);
        
        document.close();
    }
    
    private static void addTitlePage(Document document) throws DocumentException {
        // Title
        Paragraph title = new Paragraph("SISTEM PENDUKUNG KEPUTUSAN", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Subtitle
        Paragraph subtitle = new Paragraph("ANALISIS PEMILIHAN BUKU", TITLE_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(30);
        document.add(subtitle);
        
        // Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        Paragraph date = new Paragraph("Tanggal: " + sdf.format(new Date()), NORMAL_FONT);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(50);
        document.add(date);
        
        // Description
        Paragraph description = new Paragraph(
            "Dokumen ini berisi hasil analisis Sistem Pendukung Keputusan (SPK) " +
            "untuk pemilihan buku berdasarkan kriteria yang telah ditentukan. " +
            "Analisis ini menggunakan metode pembobotan dengan 5 kriteria utama."
        );
        description.setAlignment(Element.ALIGN_JUSTIFIED);
        description.setSpacingAfter(30);
        document.add(description);
        
        // Criteria summary
        Paragraph criteriaSummary = new Paragraph("Kriteria yang digunakan:", HEADER_FONT);
        criteriaSummary.setSpacingAfter(10);
        document.add(criteriaSummary);
        
        String[] criteria = {
            "C1. Jumlah Peminjam (30%)",
            "C2. Kategori Jenis Buku (20%)",
            "C3. Kondisi Fisik Buku (15%)",
            "C4. Relevansi Isi Buku (25%)",
            "C5. Durasi Peminjaman (10%)"
        };
        
        for (String criterion : criteria) {
            Paragraph p = new Paragraph("• " + criterion, NORMAL_FONT);
            p.setIndentationLeft(20);
            p.setSpacingAfter(5);
            document.add(p);
        }
    }
    
    private static void addCriteriaInfo(Document document) throws DocumentException {
        Paragraph title = new Paragraph("INFORMASI KRITERIA", HEADER_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // C1
        addCriteriaDetail(document, "C1. JUMLAH PEMINJAM", 
            "Bobot: 30%", 
            "• 1-20 peminjam: Bobot 1\n" +
            "• 21-40 peminjam: Bobot 2\n" +
            "• 41-60 peminjam: Bobot 3\n" +
            "• 61-80 peminjam: Bobot 4\n" +
            "• 81-100 peminjam: Bobot 5"
        );
        
        // C2
        addCriteriaDetail(document, "C2. KATEGORI JENIS BUKU", 
            "Bobot: 20%", 
            "• Ensiklopedia (Rating 1.0-1.9): Bobot 1\n" +
            "• Komik & Manga (Rating 2.0-3.1): Bobot 2\n" +
            "• Non-Fiksi (Rating 3.1-4.0): Bobot 3\n" +
            "• Fiksi (Rating 4.1-4.5): Bobot 4\n" +
            "• Pendidikan (Rating 4.6-5.0): Bobot 5"
        );
        
        // C3
        addCriteriaDetail(document, "C3. KONDISI FISIK BUKU", 
            "Bobot: 15%", 
            "• Rusak Berat: Bobot 1\n" +
            "• Rusak Ringan: Bobot 2\n" +
            "• Sedikit Baik: Bobot 3\n" +
            "• Baik: Bobot 4\n" +
            "• Sangat Baik: Bobot 5"
        );
        
        // C4
        addCriteriaDetail(document, "C4. RELEVANSI ISI BUKU", 
            "Bobot: 25%", 
            "• Tidak Relevan: Bobot 1\n" +
            "• Kurang Relevan: Bobot 2\n" +
            "• Cukup Relevan: Bobot 3\n" +
            "• Relevan: Bobot 4\n" +
            "• Sangat Relevan: Bobot 5"
        );
        
        // C5
        addCriteriaDetail(document, "C5. DURASI PEMINJAMAN", 
            "Bobot: 10%", 
            "• < 3 Hari (Sangat Singkat): Bobot 1\n" +
            "• 3-6 Hari (Singkat): Bobot 2\n" +
            "• 7-10 Hari (Sedang): Bobot 3\n" +
            "• 11-14 Hari (Lama): Bobot 4\n" +
            "• > 14 Hari (Sangat Lama): Bobot 5"
        );
    }
    
    private static void addCriteriaDetail(Document document, String title, String weight, String details) throws DocumentException {
        Paragraph titleP = new Paragraph(title, HEADER_FONT);
        titleP.setSpacingAfter(5);
        document.add(titleP);
        
        Paragraph weightP = new Paragraph(weight, NORMAL_FONT);
        weightP.setSpacingAfter(10);
        document.add(weightP);
        
        Paragraph detailsP = new Paragraph(details, NORMAL_FONT);
        detailsP.setIndentationLeft(20);
        detailsP.setSpacingAfter(15);
        document.add(detailsP);
    }
    
    private static void addDecisionMatrix(Document document, List<NewSPKCalculator.SPKResult> results) throws DocumentException {
        Paragraph title = new Paragraph("MATRIKS KEPUTUSAN", HEADER_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        
        // Headers
        String[] headers = {"Judul Buku", "C1", "C2", "C3", "C4", "C5"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(5);
            table.addCell(cell);
        }
        
        // Data
        for (NewSPKCalculator.SPKResult result : results) {
            // Title - left aligned
            PdfPCell titleCell = new PdfPCell(new Phrase(result.getBook().getTitle(), NORMAL_FONT));
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(titleCell);
            
            // Numeric values - center aligned
            PdfPCell c1Cell = new PdfPCell(new Phrase(String.valueOf(result.getCriteriaScores().get("C1")), NORMAL_FONT));
            c1Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1Cell);
            
            PdfPCell c2Cell = new PdfPCell(new Phrase(String.valueOf(result.getCriteriaScores().get("C2")), NORMAL_FONT));
            c2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c2Cell);
            
            PdfPCell c3Cell = new PdfPCell(new Phrase(String.valueOf(result.getCriteriaScores().get("C3")), NORMAL_FONT));
            c3Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c3Cell);
            
            PdfPCell c4Cell = new PdfPCell(new Phrase(String.valueOf(result.getCriteriaScores().get("C4")), NORMAL_FONT));
            c4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c4Cell);
            
            PdfPCell c5Cell = new PdfPCell(new Phrase(String.valueOf(result.getCriteriaScores().get("C5")), NORMAL_FONT));
            c5Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c5Cell);
        }
        
        document.add(table);
    }
    
    private static void addNormalizedMatrix(Document document, List<NewSPKCalculator.SPKResult> results) throws DocumentException {
        Paragraph title = new Paragraph("NORMALISASI MATRIKS KEPUTUSAN", HEADER_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        
        // Headers
        String[] headers = {"Judul Buku", "C1", "C2", "C3", "C4", "C5", "Nilai Akhir"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(5);
            table.addCell(cell);
        }
        
        // Data
        for (NewSPKCalculator.SPKResult result : results) {
            // Title - left aligned
            PdfPCell titleCell = new PdfPCell(new Phrase(result.getBook().getTitle(), NORMAL_FONT));
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(titleCell);
            
            // Numeric values - center aligned
            PdfPCell c1Cell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C1")), NORMAL_FONT));
            c1Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1Cell);
            
            PdfPCell c2Cell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C2")), NORMAL_FONT));
            c2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c2Cell);
            
            PdfPCell c3Cell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C3")), NORMAL_FONT));
            c3Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c3Cell);
            
            PdfPCell c4Cell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C4")), NORMAL_FONT));
            c4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c4Cell);
            
            PdfPCell c5Cell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C5")), NORMAL_FONT));
            c5Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c5Cell);
            
            PdfPCell finalScoreCell = new PdfPCell(new Phrase(String.format("%.4f", result.getFinalScore()), NORMAL_FONT));
            finalScoreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(finalScoreCell);
        }
        
        document.add(table);
    }
    
    private static void addFinalResults(Document document, List<NewSPKCalculator.SPKResult> results) throws DocumentException {
        Paragraph title = new Paragraph("HASIL AKUMULATIF & RANKING", HEADER_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        
        // Headers
        String[] headers = {"Ranking", "Judul Buku", "C1 (30%)", "C2 (20%)", "C3 (15%)", "C4 (25%)", "C5 (10%)", "Nilai Akhir"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(5);
            table.addCell(cell);
        }
        
        // Data
        for (NewSPKCalculator.SPKResult result : results) {
            // Ranking - center aligned
            PdfPCell rankCell = new PdfPCell(new Phrase(String.valueOf(result.getRank()), NORMAL_FONT));
            rankCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(rankCell);
            
            // Title - left aligned
            PdfPCell titleCell = new PdfPCell(new Phrase(result.getBook().getTitle(), NORMAL_FONT));
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(titleCell);
            
            // Weighted scores - center aligned
            PdfPCell c1WeightedCell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C1") * 0.30), NORMAL_FONT));
            c1WeightedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1WeightedCell);
            
            PdfPCell c2WeightedCell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C2") * 0.20), NORMAL_FONT));
            c2WeightedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c2WeightedCell);
            
            PdfPCell c3WeightedCell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C3") * 0.15), NORMAL_FONT));
            c3WeightedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c3WeightedCell);
            
            PdfPCell c4WeightedCell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C4") * 0.25), NORMAL_FONT));
            c4WeightedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c4WeightedCell);
            
            PdfPCell c5WeightedCell = new PdfPCell(new Phrase(String.format("%.4f", result.getNormalizedScores().get("C5") * 0.10), NORMAL_FONT));
            c5WeightedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c5WeightedCell);
            
            // Final score - center aligned
            PdfPCell finalScoreCell = new PdfPCell(new Phrase(String.format("%.4f", result.getFinalScore()), NORMAL_FONT));
            finalScoreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(finalScoreCell);
        }
        
        document.add(table);
        
        // Add conclusion
        document.add(new Paragraph(" ", NORMAL_FONT));
        Paragraph conclusion = new Paragraph("KESIMPULAN:", HEADER_FONT);
        conclusion.setSpacingAfter(10);
        document.add(conclusion);
        
        if (!results.isEmpty()) {
            NewSPKCalculator.SPKResult bestBook = results.get(0);
            Paragraph conclusionText = new Paragraph(
                "Berdasarkan analisis SPK yang telah dilakukan, buku dengan judul \"" + 
                bestBook.getBook().getTitle() + "\" menduduki peringkat pertama dengan nilai akhir " +
                String.format("%.4f", bestBook.getFinalScore()) + ". Buku ini direkomendasikan " +
                "sebagai pilihan terbaik berdasarkan kriteria yang telah ditentukan."
            );
            conclusionText.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(conclusionText);
        }
    }
    
    // Header and Footer Page Event
    private static class HeaderFooterPageEvent extends PdfPageEventHelper {
        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                // Header
                PdfContentByte cb = writer.getDirectContent();
                cb.beginText();
                cb.setFontAndSize(BaseFont.createFont(), 10);
                cb.showTextAligned(Element.ALIGN_LEFT, "Sistem Pendukung Keputusan - Analisis Buku", 50, 800, 0);
                cb.showTextAligned(Element.ALIGN_RIGHT, "Halaman " + writer.getPageNumber(), 550, 800, 0);
                cb.endText();
                
                // Header line
                cb.setLineWidth(1);
                cb.moveTo(50, 790);
                cb.lineTo(550, 790);
                cb.stroke();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                // Footer
                PdfContentByte cb = writer.getDirectContent();
                cb.beginText();
                cb.setFontAndSize(BaseFont.createFont(), 8);
                cb.showTextAligned(Element.ALIGN_LEFT, "Dokumen ini dibuat secara otomatis oleh Sistem SPK", 50, 50, 0);
                cb.showTextAligned(Element.ALIGN_RIGHT, "Tanggal: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), 550, 50, 0);
                cb.endText();
                
                // Footer line
                cb.setLineWidth(1);
                cb.moveTo(50, 60);
                cb.lineTo(550, 60);
                cb.stroke();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 