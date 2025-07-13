package com.bookspk;

import java.sql.Timestamp;

/**
 * Book model class for book selection system
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private int year;
    private int pages;
    private double rating;
    private double price;
    private String isbn;
    private String description;
    private Timestamp createdAt;
    
    public Book() {}
    
    public Book(String title, String author, String category, String publisher, 
                int year, int pages, double rating, double price, String isbn, String description) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.rating = rating;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
    }
    
    public Book(int id, String title, String author, String category, String publisher,
                int year, int pages, double rating, double price, String isbn, 
                String description, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.rating = rating;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                ", price=" + price +
                '}';
    }
} 