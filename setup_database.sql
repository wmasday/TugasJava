-- Database setup script for Java GUI Login System with Book Management
-- Run this script in MySQL to create the database and tables

-- Create database
CREATE DATABASE IF NOT EXISTS tugas_java;

-- Use the database
USE tugas_java;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user
INSERT IGNORE INTO users (username, password, email) 
VALUES ('admin', 'admin123', 'admin@example.com');

-- Create books table with ISBN uniqueness constraint
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    pages INT NOT NULL,
    rating DOUBLE NOT NULL DEFAULT 0.0,
    price DOUBLE NOT NULL DEFAULT 0.0,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Constraints
    CHECK (year >= 1900 AND year <= 2024),
    CHECK (pages > 0 AND pages <= 5000),
    CHECK (rating >= 0.0 AND rating <= 5.0),
    CHECK (price >= 0.0),
    CHECK (LENGTH(isbn) >= 10)
);

-- Insert sample book data
INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES
('Laskar Pelangi', 'Andrea Hirata', 'Novel', 'Bentang Pustaka', 2005, 529, 4.5, 85000, '978-602-291-123-4', 'Novel inspiratif tentang perjuangan anak-anak di Belitung untuk mendapatkan pendidikan.'),
('Bumi Manusia', 'Pramoedya Ananta Toer', 'Sejarah', 'Hasta Mitra', 1980, 535, 4.8, 95000, '978-602-291-234-5', 'Novel sejarah yang mengisahkan perjuangan Minke di era kolonial Belanda.'),
('Filosofi Teras', 'Henry Manampiring', 'Pengembangan Diri', 'Kompas', 2018, 320, 4.3, 75000, '978-602-291-345-6', 'Buku pengenalan filsafat Stoa untuk kehidupan sehari-hari.'),
('Atomic Habits', 'James Clear', 'Pengembangan Diri', 'Gramedia Pustaka Utama', 2018, 320, 4.7, 120000, '978-602-291-456-7', 'Buku tentang membangun kebiasaan kecil untuk perubahan besar.'),
('Rich Dad Poor Dad', 'Robert T. Kiyosaki', 'Bisnis', 'Gramedia Pustaka Utama', 1997, 336, 4.4, 110000, '978-602-291-567-8', 'Buku tentang mindset keuangan dan investasi.'),
('The Psychology of Money', 'Morgan Housel', 'Bisnis', 'Gramedia Pustaka Utama', 2020, 256, 4.6, 130000, '978-602-291-678-9', 'Buku tentang psikologi dalam pengambilan keputusan keuangan.'),
('Clean Code', 'Robert C. Martin', 'Teknologi', 'Prentice Hall', 2008, 464, 4.5, 150000, '978-602-291-789-0', 'Buku tentang menulis kode yang bersih dan mudah dipahami.'),
('Design Patterns', 'Erich Gamma', 'Teknologi', 'Addison-Wesley', 1994, 416, 4.3, 180000, '978-602-291-890-1', 'Buku tentang pola desain dalam pengembangan software.'),
('The Martian', 'Andy Weir', 'Fiksi', 'Crown Publishing', 2011, 384, 4.4, 140000, '978-602-291-901-2', 'Novel fiksi ilmiah tentang astronot yang terdampar di Mars.'),
('Dune', 'Frank Herbert', 'Fiksi', 'Chilton Books', 1965, 688, 4.6, 160000, '978-602-291-012-3', 'Novel fiksi ilmiah epik tentang planet Arrakis dan spice melange.');

-- Create indexes for better performance
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_books_rating ON books(rating DESC);
CREATE INDEX idx_books_year ON books(year DESC);
CREATE INDEX idx_books_price ON books(price);

-- Create a dedicated user for the application (optional)
-- CREATE USER IF NOT EXISTS 'loginapp'@'localhost' IDENTIFIED BY 'your_password';
-- GRANT ALL PRIVILEGES ON tugas_java.* TO 'loginapp'@'localhost';
-- FLUSH PRIVILEGES;

-- Show the created data
SELECT 'Users Table:' as table_name;
SELECT * FROM users;

SELECT 'Books Table:' as table_name;
SELECT id, title, author, category, rating, price, isbn FROM books ORDER BY rating DESC; 