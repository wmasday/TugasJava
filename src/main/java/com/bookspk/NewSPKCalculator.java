package com.bookspk;

import java.util.*;

public class NewSPKCalculator {
    
    public static class SPKResult {
        private Book book;
        private Map<String, Integer> criteriaScores;
        private Map<String, Double> normalizedScores;
        private double finalScore;
        private int rank;
        
        public SPKResult(Book book) {
            this.book = book;
            this.criteriaScores = new HashMap<>();
            this.normalizedScores = new HashMap<>();
        }
        
        public Book getBook() { return book; }
        public Map<String, Integer> getCriteriaScores() { return criteriaScores; }
        public Map<String, Double> getNormalizedScores() { return normalizedScores; }
        public double getFinalScore() { return finalScore; }
        public int getRank() { return rank; }
        
        public void setFinalScore(double finalScore) { this.finalScore = finalScore; }
        public void setRank(int rank) { this.rank = rank; }
    }
    
    public List<SPKResult> calculateSPK(List<Book> books) {
        if (books == null || books.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<SPKResult> results = new ArrayList<>();
        
        // Step 1: Calculate criteria scores for each book
        for (Book book : books) {
            SPKResult result = new SPKResult(book);
            
            // C1: Jumlah Peminjam
            int c1Score = SPKDataConverter.convertBorrowCountToWeight(book.getBorrowerCount());
            result.getCriteriaScores().put("C1", c1Score);
            
            // C2: Kategori dan Rating
            int c2Score = SPKDataConverter.convertCategoryAndRatingToWeight(book.getCategory(), book.getRating());
            result.getCriteriaScores().put("C2", c2Score);
            
            // C3: Kondisi Fisik
            int c3Score = SPKDataConverter.convertPhysicalConditionToWeight(book.getBookCondition());
            result.getCriteriaScores().put("C3", c3Score);
            
            // C4: Relevansi Isi
            int c4Score = SPKDataConverter.convertContentRelevanceToWeight(book.getContentRelevance());
            result.getCriteriaScores().put("C4", c4Score);
            
            // C5: Durasi Peminjaman
            int c5Score = SPKDataConverter.convertBorrowDurationToWeight(book.getLoanDuration());
            result.getCriteriaScores().put("C5", c5Score);
            
            results.add(result);
        }
        
        // Step 2: Normalize scores
        Map<String, Integer> maxScores = new HashMap<>();
        maxScores.put("C1", 5);
        maxScores.put("C2", 5);
        maxScores.put("C3", 5);
        maxScores.put("C4", 5);
        maxScores.put("C5", 5);
        
        for (SPKResult result : results) {
            for (String criteria : Arrays.asList("C1", "C2", "C3", "C4", "C5")) {
                int score = result.getCriteriaScores().get(criteria);
                int maxScore = maxScores.get(criteria);
                double normalizedScore = (double) score / maxScore;
                result.getNormalizedScores().put(criteria, normalizedScore);
            }
        }
        
        // Step 3: Calculate final scores with weights
        for (SPKResult result : results) {
            double finalScore = 0.0;
            
            finalScore += result.getNormalizedScores().get("C1") * SPKDataConverter.getCriteriaWeight("C1");
            finalScore += result.getNormalizedScores().get("C2") * SPKDataConverter.getCriteriaWeight("C2");
            finalScore += result.getNormalizedScores().get("C3") * SPKDataConverter.getCriteriaWeight("C3");
            finalScore += result.getNormalizedScores().get("C4") * SPKDataConverter.getCriteriaWeight("C4");
            finalScore += result.getNormalizedScores().get("C5") * SPKDataConverter.getCriteriaWeight("C5");
            
            result.setFinalScore(finalScore);
        }
        
        // Step 4: Sort by final score (descending)
        results.sort((a, b) -> Double.compare(b.getFinalScore(), a.getFinalScore()));
        
        // Step 5: Assign ranks
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setRank(i + 1);
        }
        
        return results;
    }
    
    public Map<String, Object> getSPKMatrix(List<SPKResult> results) {
        Map<String, Object> matrix = new HashMap<>();
        
        // Decision Matrix
        List<List<Object>> decisionMatrix = new ArrayList<>();
        List<Object> headers = new ArrayList<>();
        headers.add("Judul Buku");
        headers.add("C1");
        headers.add("C2");
        headers.add("C3");
        headers.add("C4");
        headers.add("C5");
        decisionMatrix.add(headers);
        
        for (SPKResult result : results) {
            List<Object> row = new ArrayList<>();
            row.add(result.getBook().getTitle());
            row.add(result.getCriteriaScores().get("C1"));
            row.add(result.getCriteriaScores().get("C2"));
            row.add(result.getCriteriaScores().get("C3"));
            row.add(result.getCriteriaScores().get("C4"));
            row.add(result.getCriteriaScores().get("C5"));
            decisionMatrix.add(row);
        }
        
        // Normalized Matrix
        List<List<Object>> normalizedMatrix = new ArrayList<>();
        List<Object> normHeaders = new ArrayList<>();
        normHeaders.add("Judul Buku");
        normHeaders.add("C1");
        normHeaders.add("C2");
        normHeaders.add("C3");
        normHeaders.add("C4");
        normHeaders.add("C5");
        normHeaders.add("Nilai Akhir");
        normalizedMatrix.add(normHeaders);
        
        for (SPKResult result : results) {
            List<Object> row = new ArrayList<>();
            row.add(result.getBook().getTitle());
            row.add(String.format("%.4f", result.getNormalizedScores().get("C1")));
            row.add(String.format("%.4f", result.getNormalizedScores().get("C2")));
            row.add(String.format("%.4f", result.getNormalizedScores().get("C3")));
            row.add(String.format("%.4f", result.getNormalizedScores().get("C4")));
            row.add(String.format("%.4f", result.getNormalizedScores().get("C5")));
            row.add(String.format("%.4f", result.getFinalScore()));
            normalizedMatrix.add(row);
        }
        
        // Final Results with Ranking
        List<List<Object>> finalResults = new ArrayList<>();
        List<Object> finalHeaders = new ArrayList<>();
        finalHeaders.add("Ranking");
        finalHeaders.add("Judul Buku");
        finalHeaders.add("C1 (30%)");
        finalHeaders.add("C2 (20%)");
        finalHeaders.add("C3 (15%)");
        finalHeaders.add("C4 (25%)");
        finalHeaders.add("C5 (10%)");
        finalHeaders.add("Nilai Akhir");
        finalResults.add(finalHeaders);
        
        for (SPKResult result : results) {
            List<Object> row = new ArrayList<>();
            row.add(result.getRank());
            row.add(result.getBook().getTitle());
            row.add(String.format("%.4f", result.getNormalizedScores().get("C1") * SPKDataConverter.getCriteriaWeight("C1")));
            row.add(String.format("%.4f", result.getNormalizedScores().get("C2") * SPKDataConverter.getCriteriaWeight("C2")));
            row.add(String.format("%.4f", result.getNormalizedScores().get("C3") * SPKDataConverter.getCriteriaWeight("C3")));
            row.add(String.format("%.4f", result.getNormalizedScores().get("C4") * SPKDataConverter.getCriteriaWeight("C4")));
            row.add(String.format("%.4f", result.getNormalizedScores().get("C5") * SPKDataConverter.getCriteriaWeight("C5")));
            row.add(String.format("%.4f", result.getFinalScore()));
            finalResults.add(row);
        }
        
        matrix.put("decisionMatrix", decisionMatrix);
        matrix.put("normalizedMatrix", normalizedMatrix);
        matrix.put("finalResults", finalResults);
        
        return matrix;
    }
} 