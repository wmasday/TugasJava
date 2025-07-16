package com.bookspk;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Book operations with SPK calculations
 */
public class BookDAO {
    
    /**
     * Get all books
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getString("publisher"),
                    rs.getInt("year"),
                    rs.getInt("pages"),
                    rs.getDouble("rating"),
                    rs.getDouble("price"),
                    rs.getString("isbn"),
                    rs.getString("description"),
                    rs.getTimestamp("created_at"),
                    rs.getInt("borrower_count"),
                    rs.getString("book_condition"),
                    rs.getString("content_relevance"),
                    rs.getInt("loan_duration")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting books: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Get books by category
     * @param category category to filter
     * @return List of books in the category
     */
    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE category = ? ORDER BY rating DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("publisher"),
                        rs.getInt("year"),
                        rs.getInt("pages"),
                        rs.getDouble("rating"),
                        rs.getDouble("price"),
                        rs.getString("isbn"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("borrower_count"),
                        rs.getString("book_condition"),
                        rs.getString("content_relevance"),
                        rs.getInt("loan_duration")
                    );
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting books by category: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Get all categories
     * @return List of all categories
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM books ORDER BY category";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }
        return categories;
    }
    
    /**
     * Add a new book
     * @param book Book object to add
     * @return true if successful, false otherwise
     */
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description, borrower_count, book_condition, content_relevance, loan_duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getYear());
            pstmt.setInt(6, book.getPages());
            pstmt.setDouble(7, book.getRating());
            pstmt.setDouble(8, book.getPrice());
            pstmt.setString(9, book.getIsbn());
            pstmt.setString(10, book.getDescription());
            pstmt.setInt(11, book.getBorrowerCount());
            pstmt.setString(12, book.getBookCondition());
            pstmt.setString(13, book.getContentRelevance());
            pstmt.setInt(14, book.getLoanDuration());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing book
     * @param book Book object to update
     * @return true if successful, false otherwise
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, publisher = ?, year = ?, pages = ?, rating = ?, price = ?, isbn = ?, description = ?, borrower_count = ?, book_condition = ?, content_relevance = ?, loan_duration = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getYear());
            pstmt.setInt(6, book.getPages());
            pstmt.setDouble(7, book.getRating());
            pstmt.setDouble(8, book.getPrice());
            pstmt.setString(9, book.getIsbn());
            pstmt.setString(10, book.getDescription());
            pstmt.setInt(11, book.getBorrowerCount());
            pstmt.setString(12, book.getBookCondition());
            pstmt.setString(13, book.getContentRelevance());
            pstmt.setInt(14, book.getLoanDuration());
            pstmt.setInt(15, book.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a book
     * @param bookId ID of the book to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get book by ID
     * @param bookId ID of the book to get
     * @return Book object if found, null otherwise
     */
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("publisher"),
                        rs.getInt("year"),
                        rs.getInt("pages"),
                        rs.getDouble("rating"),
                        rs.getDouble("price"),
                        rs.getString("isbn"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("borrower_count"),
                        rs.getString("book_condition"),
                        rs.getString("content_relevance"),
                        rs.getInt("loan_duration")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get book by title and author
     * @param title title of the book
     * @param author author of the book
     * @return Book object if found, null otherwise
     */
    public Book getBookByTitleAndAuthor(String title, String author) {
        String sql = "SELECT * FROM books WHERE title = ? AND author = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("publisher"),
                        rs.getInt("year"),
                        rs.getInt("pages"),
                        rs.getDouble("rating"),
                        rs.getDouble("price"),
                        rs.getString("isbn"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("borrower_count"),
                        rs.getString("book_condition"),
                        rs.getString("content_relevance"),
                        rs.getInt("loan_duration")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by title and author: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Check if ISBN exists
     * @param isbn ISBN to check
     * @return true if ISBN exists, false otherwise
     */
    public boolean isbnExists(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking ISBN: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get top books using SPK (Simple Additive Weighting) with new criteria
     * @param limit number of top books to return
     * @param weights array of weights for criteria [borrowerCount, bookCondition, contentRelevance, loanDuration]
     * @return List of BookSPKResult objects
     */
    public List<BookSPKResult> getTopBooksSPK(int limit, double[] weights) {
        List<BookSPKResult> results = new ArrayList<>();
        List<Book> allBooks = getAllBooks();
        
        if (allBooks.isEmpty()) {
            return results;
        }
        
        // Normalize weights
        double totalWeight = 0;
        for (double weight : weights) {
            totalWeight += weight;
        }
        
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] / totalWeight;
        }
        
        // Calculate SPK scores
        for (Book book : allBooks) {
            double spkScore = calculateSPKScore(book, weights);
            results.add(new BookSPKResult(book, spkScore));
        }
        
        // Sort by SPK score (descending)
        results.sort((a, b) -> Double.compare(b.getSpkScore(), a.getSpkScore()));
        
        // Return top results
        return results.subList(0, Math.min(limit, results.size()));
    }
    
    /**
     * Calculate SPK score for a book using new criteria
     * @param book Book object
     * @param weights array of weights for criteria [borrowerCount, bookCondition, contentRelevance, loanDuration]
     * @return SPK score
     */
    private double calculateSPKScore(Book book, double[] weights) {
        // Calculate normalized scores for each criterion (0-1 scale)
        
        // 1. Jumlah Peminjam (Borrower Count) - Higher is better
        double normalizedBorrowerCount = calculateBorrowerCountScore(book.getBorrowerCount());
        
        // 2. Kondisi Fisik Buku (Book Condition) - Higher is better
        double normalizedBookCondition = calculateBookConditionScore(book.getBookCondition());
        
        // 3. Relevansi Isi Buku (Content Relevance) - Higher is better
        double normalizedContentRelevance = calculateContentRelevanceScore(book.getContentRelevance());
        
        // 4. Durasi Peminjaman (Loan Duration) - Lower is better (shorter duration is preferred)
        double normalizedLoanDuration = calculateLoanDurationScore(book.getLoanDuration());
        
        // Calculate weighted sum
        double score = (normalizedBorrowerCount * weights[0]) +
                     (normalizedBookCondition * weights[1]) +
                     (normalizedContentRelevance * weights[2]) +
                     (normalizedLoanDuration * weights[3]);
        
        return score;
    }
    
    /**
     * Calculate borrower count score based on ranges
     * @param borrowerCount number of borrowers
     * @return normalized score (0-1)
     */
    private double calculateBorrowerCountScore(int borrowerCount) {
        if (borrowerCount >= 1 && borrowerCount <= 20) return 0.2; // Bobot 1
        else if (borrowerCount >= 21 && borrowerCount <= 40) return 0.4; // Bobot 2
        else if (borrowerCount >= 41 && borrowerCount <= 60) return 0.6; // Bobot 3
        else if (borrowerCount >= 61 && borrowerCount <= 80) return 0.8; // Bobot 4
        else if (borrowerCount >= 81 && borrowerCount <= 100) return 1.0; // Bobot 5
        else return 0.0; // Default for out of range
    }
    
    /**
     * Calculate book condition score
     * @param bookCondition condition of the book
     * @return normalized score (0-1)
     */
    private double calculateBookConditionScore(String bookCondition) {
        if (bookCondition == null) return 0.0;
        
        switch (bookCondition.toLowerCase()) {
            case "rusak berat": return 0.2; // Bobot 1
            case "rusak ringan": return 0.4; // Bobot 2
            case "sedikit baik": return 0.6; // Bobot 3
            case "baik": return 0.8; // Bobot 4
            case "sangat baik": return 1.0; // Bobot 5
            default: return 0.0;
        }
    }
    
    /**
     * Calculate content relevance score
     * @param contentRelevance relevance of the book content
     * @return normalized score (0-1)
     */
    private double calculateContentRelevanceScore(String contentRelevance) {
        if (contentRelevance == null) return 0.0;
        
        switch (contentRelevance.toLowerCase()) {
            case "tidak relevan": return 0.2; // Bobot 1
            case "kurang relevan": return 0.4; // Bobot 2
            case "cukup relevan": return 0.6; // Bobot 3
            case "relevan": return 0.8; // Bobot 4
            case "sangat relevan": return 1.0; // Bobot 5
            default: return 0.0;
        }
    }
    
    /**
     * Calculate loan duration score (shorter duration is better)
     * @param loanDuration duration in days
     * @return normalized score (0-1)
     */
    private double calculateLoanDurationScore(int loanDuration) {
        if (loanDuration < 3) return 1.0; // Sangat Singkat - Bobot 5
        else if (loanDuration >= 3 && loanDuration <= 6) return 0.8; // Singkat - Bobot 4
        else if (loanDuration >= 7 && loanDuration <= 10) return 0.6; // Sedang - Bobot 3
        else if (loanDuration >= 11 && loanDuration <= 14) return 0.4; // Lama - Bobot 2
        else if (loanDuration > 14) return 0.2; // Sangat Lama - Bobot 1
        else return 0.0; // Default
    }
    
    /**
     * Inner class to hold SPK results
     */
    public static class BookSPKResult {
        private Book book;
        private double spkScore;
        
        public BookSPKResult(Book book, double spkScore) {
            this.book = book;
            this.spkScore = spkScore;
        }
        
        public Book getBook() {
            return book;
        }
        
        public double getSpkScore() {
            return spkScore;
        }
    }
} 