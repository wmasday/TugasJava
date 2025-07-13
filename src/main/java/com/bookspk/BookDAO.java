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
                    rs.getTimestamp("created_at")
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
                        rs.getTimestamp("created_at")
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
        String sql = "INSERT INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
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
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, publisher = ?, year = ?, pages = ?, rating = ?, price = ?, isbn = ?, description = ? WHERE id = ?";
        
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
            pstmt.setInt(11, book.getId());
            
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
                        rs.getTimestamp("created_at")
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
                        rs.getTimestamp("created_at")
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
     * Get top books using SPK (Simple Additive Weighting)
     * @param limit number of top books to return
     * @param weights array of weights for criteria [rating, price, year, pages]
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
     * Calculate SPK score for a book
     * @param book Book object
     * @param weights array of weights for criteria
     * @return SPK score
     */
    private double calculateSPKScore(Book book, double[] weights) {
        // Normalize values (0-1 scale)
        double normalizedRating = book.getRating() / 5.0; // Rating is 0-5
        double normalizedPrice = 1.0 - (book.getPrice() / 1000000.0); // Price is 0-1M, lower is better
        double normalizedYear = (book.getYear() - 1900) / (2024 - 1900); // Year is 1900-2024
        double normalizedPages = book.getPages() / 1000.0; // Pages is 0-1000
        
        // Calculate weighted sum
        double score = (normalizedRating * weights[0]) +
                     (normalizedPrice * weights[1]) +
                     (normalizedYear * weights[2]) +
                     (normalizedPages * weights[3]);
        
        return score;
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