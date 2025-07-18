-- Database setup script for Java GUI Login System with Book Management and SPK Analysis
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

-- Create books table with new SPK criteria fields
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
    
    -- New SPK criteria fields
    borrower_count INT NOT NULL DEFAULT 0 COMMENT 'Jumlah Peminjam',
    book_condition VARCHAR(50) NOT NULL DEFAULT 'Baik' COMMENT 'Kondisi Fisik Buku',
    content_relevance VARCHAR(50) NOT NULL DEFAULT 'Relevan' COMMENT 'Relevansi Isi Buku',
    loan_duration INT NOT NULL DEFAULT 7 COMMENT 'Durasi Peminjaman (hari)',
    
    -- Constraints
    CHECK (year >= 1900 AND year <= 2024),
    CHECK (pages > 0 AND pages <= 5000),
    CHECK (rating >= 0.0 AND rating <= 5.0),
    CHECK (price >= 0.0),
    CHECK (LENGTH(isbn) >= 10),
    CHECK (borrower_count >= 0 AND borrower_count <= 1000),
    CHECK (loan_duration >= 1 AND loan_duration <= 30),
    CHECK (book_condition IN ('Rusak Berat', 'Rusak Ringan', 'Sedikit Baik', 'Baik', 'Sangat Baik')),
    CHECK (content_relevance IN ('Tidak Relevan', 'Kurang Relevan', 'Cukup Relevan', 'Relevan', 'Sangat Relevan'))
);

-- Insert sample book data with new SPK criteria
INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description, borrower_count, book_condition, content_relevance, loan_duration) VALUES
('Laskar Pelangi', 'Andrea Hirata', 'Novel', 'Bentang Pustaka', 2005, 529, 4.5, 85000, '978-602-291-123-4', 'Novel inspiratif tentang perjuangan anak-anak di Belitung untuk mendapatkan pendidikan.', 85, 'Sangat Baik', 'Sangat Relevan', 5),
('Bumi Manusia', 'Pramoedya Ananta Toer', 'Sejarah', 'Hasta Mitra', 1980, 535, 4.8, 95000, '978-602-291-234-5', 'Novel sejarah yang mengisahkan perjuangan Minke di era kolonial Belanda.', 92, 'Baik', 'Sangat Relevan', 8),
('Filosofi Teras', 'Henry Manampiring', 'Pengembangan Diri', 'Kompas', 2018, 320, 4.3, 75000, '978-602-291-345-6', 'Buku pengenalan filsafat Stoa untuk kehidupan sehari-hari.', 78, 'Sangat Baik', 'Relevan', 6),
('Atomic Habits', 'James Clear', 'Pengembangan Diri', 'Gramedia Pustaka Utama', 2018, 320, 4.7, 120000, '978-602-291-456-7', 'Buku tentang membangun kebiasaan kecil untuk perubahan besar.', 95, 'Sangat Baik', 'Sangat Relevan', 4),
('Rich Dad Poor Dad', 'Robert T. Kiyosaki', 'Bisnis', 'Gramedia Pustaka Utama', 1997, 336, 4.4, 110000, '978-602-291-567-8', 'Buku tentang mindset keuangan dan investasi.', 88, 'Baik', 'Relevan', 7),
('The Psychology of Money', 'Morgan Housel', 'Bisnis', 'Gramedia Pustaka Utama', 2020, 256, 4.6, 130000, '978-602-291-678-9', 'Buku tentang psikologi dalam pengambilan keputusan keuangan.', 82, 'Sangat Baik', 'Sangat Relevan', 5),
('Clean Code', 'Robert C. Martin', 'Teknologi', 'Prentice Hall', 2008, 464, 4.5, 150000, '978-602-291-789-0', 'Buku tentang menulis kode yang bersih dan mudah dipahami.', 75, 'Baik', 'Relevan', 10),
('Design Patterns', 'Erich Gamma', 'Teknologi', 'Addison-Wesley', 1994, 416, 4.3, 180000, '978-602-291-890-1', 'Buku tentang pola desain dalam pengembangan software.', 68, 'Sedikit Baik', 'Cukup Relevan', 12),
('The Martian', 'Andy Weir', 'Fiksi', 'Crown Publishing', 2011, 384, 4.4, 140000, '978-602-291-901-2', 'Novel fiksi ilmiah tentang astronot yang terdampar di Mars.', 90, 'Sangat Baik', 'Relevan', 6),
('Dune', 'Frank Herbert', 'Fiksi', 'Chilton Books', 1965, 688, 4.6, 160000, '978-602-291-012-3', 'Novel fiksi ilmiah epik tentang planet Arrakis dan spice melange.', 87, 'Baik', 'Sangat Relevan', 8),
('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', 'Fiksi', 'Scholastic', 1997, 223, 4.8, 125000, '978-602-291-023-4', 'Novel fantasi tentang penyihir muda Harry Potter.', 98, 'Sangat Baik', 'Sangat Relevan', 3),
('The Lord of the Rings', 'J.R.R. Tolkien', 'Fiksi', 'Allen & Unwin', 1954, 1216, 4.9, 200000, '978-602-291-034-5', 'Epik fantasi tentang perjalanan Frodo untuk menghancurkan cincin.', 94, 'Baik', 'Relevan', 15),
('To Kill a Mockingbird', 'Harper Lee', 'Novel', 'J.B. Lippincott', 1960, 281, 4.7, 95000, '978-602-291-045-6', 'Novel klasik tentang rasisme dan ketidakadilan di Amerika Selatan.', 89, 'Sangat Baik', 'Sangat Relevan', 7),
('1984', 'George Orwell', 'Fiksi', 'Secker & Warburg', 1949, 328, 4.6, 110000, '978-602-291-056-7', 'Novel distopia tentang totalitarianisme dan pengawasan negara.', 86, 'Baik', 'Relevan', 9),
('The Great Gatsby', 'F. Scott Fitzgerald', 'Novel', 'Scribner', 1925, 180, 4.5, 85000, '978-602-291-067-8', 'Novel klasik tentang impian Amerika dan dekadensi era Jazz.', 72, 'Sedikit Baik', 'Cukup Relevan', 11),
('Pride and Prejudice', 'Jane Austen', 'Novel', 'T. Egerton', 1813, 432, 4.4, 90000, '978-602-291-078-9', 'Novel romantis klasik tentang Elizabeth Bennet dan Mr. Darcy.', 81, 'Baik', 'Relevan', 8),
('The Catcher in the Rye', 'J.D. Salinger', 'Novel', 'Little, Brown', 1951, 277, 4.3, 95000, '978-602-291-089-0', 'Novel tentang remaja Holden Caulfield dan pencarian identitas.', 76, 'Sedikit Baik', 'Cukup Relevan', 10),
('The Hobbit', 'J.R.R. Tolkien', 'Fiksi', 'Allen & Unwin', 1937, 310, 4.5, 120000, '978-602-291-090-1', 'Novel fantasi tentang petualangan Bilbo Baggins.', 88, 'Sangat Baik', 'Relevan', 6),
('Animal Farm', 'George Orwell', 'Fiksi', 'Secker & Warburg', 1945, 112, 4.4, 75000, '978-602-291-101-2', 'Alegori politik tentang revolusi dan korupsi kekuasaan.', 79, 'Baik', 'Relevan', 7),
('The Alchemist', 'Paulo Coelho', 'Novel', 'HarperCollins', 1988, 208, 4.2, 85000, '978-602-291-112-3', 'Novel spiritual tentang Santiago dan pencarian harta karun.', 83, 'Sangat Baik', 'Relevan', 5),
-- New books with new categories
('Naruto Vol. 1', 'Masashi Kishimoto', 'Komik & Manga', 'Shueisha', 1999, 208, 4.5, 45000, '978-602-291-113-4', 'Manga populer tentang ninja muda Naruto Uzumaki dan perjuangannya untuk menjadi Hokage.', 95, 'Sangat Baik', 'Sangat Relevan', 4),
('One Piece Vol. 1', 'Eiichiro Oda', 'Komik & Manga', 'Shueisha', 1997, 208, 4.7, 48000, '978-602-291-114-5', 'Manga epik tentang petualangan bajak laut Monkey D. Luffy mencari harta karun.', 92, 'Sangat Baik', 'Sangat Relevan', 3),
('Dragon Ball Vol. 1', 'Akira Toriyama', 'Komik & Manga', 'Shueisha', 1984, 208, 4.6, 42000, '978-602-291-115-6', 'Manga klasik tentang petualangan Son Goku dan pencarian Dragon Balls.', 88, 'Baik', 'Relevan', 5),
('Matematika Dasar', 'Prof. Dr. Bambang', 'Pendidikan', 'Erlangga', 2020, 350, 4.3, 85000, '978-602-291-116-7', 'Buku pelajaran matematika dasar untuk siswa SMA dengan contoh dan latihan.', 75, 'Sangat Baik', 'Sangat Relevan', 7),
('Fisika Modern', 'Prof. Dr. Surya', 'Pendidikan', 'Grasindo', 2019, 420, 4.4, 95000, '978-602-291-117-8', 'Buku pelajaran fisika modern dengan konsep dan aplikasi terkini.', 68, 'Baik', 'Relevan', 8),
('Biologi Kelas XI', 'Prof. Dr. Rina', 'Pendidikan', 'Penerbit Nasional', 2021, 380, 4.2, 88000, '978-602-291-118-9', 'Buku pelajaran biologi untuk kelas XI dengan gambar dan diagram lengkap.', 72, 'Sangat Baik', 'Relevan', 6),
('Ensiklopedia Indonesia', 'Tim Redaksi', 'Ensiklopedia', 'Balai Pustaka', 2018, 1200, 4.8, 250000, '978-602-291-119-0', 'Ensiklopedia lengkap tentang Indonesia dari sejarah hingga budaya.', 82, 'Sangat Baik', 'Sangat Relevan', 10),
('Ensiklopedia Sains', 'Dr. Ahmad', 'Ensiklopedia', 'Penerbit Ilmiah', 2020, 800, 4.6, 180000, '978-602-291-120-1', 'Ensiklopedia sains modern dengan ilustrasi dan penjelasan detail.', 78, 'Baik', 'Relevan', 9),
('Ensiklopedia Dunia', 'Tim Internasional', 'Ensiklopedia', 'Global Publishing', 2019, 1500, 4.7, 300000, '978-602-291-121-2', 'Ensiklopedia dunia terlengkap dengan informasi dari berbagai negara.', 85, 'Sangat Baik', 'Sangat Relevan', 12),
('Sapiens', 'Yuval Noah Harari', 'Non-Fiksi', 'Gramedia Pustaka Utama', 2014, 464, 4.8, 150000, '978-602-291-122-3', 'Buku non-fiksi tentang sejarah manusia dari zaman purba hingga modern.', 96, 'Sangat Baik', 'Sangat Relevan', 5),
('Homo Deus', 'Yuval Noah Harari', 'Non-Fiksi', 'Gramedia Pustaka Utama', 2016, 448, 4.6, 160000, '978-602-291-123-4', 'Buku non-fiksi tentang masa depan manusia dan teknologi.', 89, 'Sangat Baik', 'Relevan', 6),
('21 Lessons for the 21st Century', 'Yuval Noah Harari', 'Non-Fiksi', 'Gramedia Pustaka Utama', 2018, 400, 4.5, 140000, '978-602-291-124-5', 'Buku non-fiksi tentang tantangan dan pelajaran untuk abad 21.', 84, 'Baik', 'Relevan', 7),
('The Subtle Art of Not Giving a F*ck', 'Mark Manson', 'Non-Fiksi', 'Gramedia Pustaka Utama', 2016, 224, 4.4, 120000, '978-602-291-125-6', 'Buku non-fiksi tentang filosofi hidup yang kontroversial.', 91, 'Sangat Baik', 'Sangat Relevan', 4),
('Thinking, Fast and Slow', 'Daniel Kahneman', 'Non-Fiksi', 'Farrar, Straus and Giroux', 2011, 499, 4.7, 180000, '978-602-291-126-7', 'Buku non-fiksi tentang psikologi kognitif dan pengambilan keputusan.', 87, 'Sangat Baik', 'Relevan', 8),
('The Power of Habit', 'Charles Duhigg', 'Non-Fiksi', 'Random House', 2012, 371, 4.5, 130000, '978-602-291-127-8', 'Buku non-fiksi tentang sains di balik kebiasaan dan perubahan.', 79, 'Baik', 'Relevan', 6),
('Malcolm X: A Life of Reinvention', 'Manning Marable', 'Non-Fiksi', 'Viking Press', 2011, 608, 4.6, 200000, '978-602-291-128-9', 'Biografi non-fiksi tentang kehidupan Malcolm X dan perjuangan hak sipil.', 73, 'Sangat Baik', 'Sangat Relevan', 9),
('Steve Jobs', 'Walter Isaacson', 'Non-Fiksi', 'Simon & Schuster', 2011, 656, 4.8, 220000, '978-602-291-129-0', 'Biografi non-fiksi tentang Steve Jobs dan revolusi teknologi.', 94, 'Sangat Baik', 'Sangat Relevan', 7),
('The Innovators', 'Walter Isaacson', 'Non-Fiksi', 'Simon & Schuster', 2014, 560, 4.5, 190000, '978-602-291-130-1', 'Buku non-fiksi tentang sejarah revolusi digital dan para inovatornya.', 81, 'Baik', 'Relevan', 8),
-- Additional dummy data books
('Honesty Mitha Juniar', 'Mitha Juniar', 'Fiksi', 'PT. Grasindo', 2014, 280, 4.2, 75000, '978-602-291-131-2', 'Novel fiksi tentang kejujuran dan perjuangan hidup.', 78, 'Sangat Baik', 'Relevan', 6),
('Tanjong Cinta', 'Heriyati', 'Fiksi', 'Republika', 2010, 320, 4.0, 65000, '978-602-291-132-3', 'Novel romantis fiksi tentang cinta di Tanjong.', 72, 'Baik', 'Cukup Relevan', 7),
('Kupu-kupu Cantik', 'Nuning Widowati', 'Non-Fiksi', 'Transmedia Pustaka', 2016, 240, 4.3, 80000, '978-602-291-133-4', 'Buku non-fiksi tentang transformasi dan pertumbuhan pribadi.', 85, 'Sangat Baik', 'Relevan', 5),
('Yuk Jadi Pelajar Full Prestasi', 'Husni Mubarok', 'Non-Fiksi', 'Noktah', 2019, 200, 4.4, 70000, '978-602-291-134-5', 'Buku motivasi non-fiksi untuk pelajar yang ingin berprestasi.', 90, 'Sangat Baik', 'Sangat Relevan', 4),
('Detektif Conan', 'Juan Hadrianus', 'Komik & Manga', 'PT Gramedia', 2020, 180, 4.6, 55000, '978-602-291-135-6', 'Komik detektif populer tentang Conan Edogawa.', 88, 'Sangat Baik', 'Sangat Relevan', 3),
('Dragon Ball Super', 'Milka Ivana', 'Komik & Manga', 'PT Gramedia', 2021, 200, 4.5, 58000, '978-602-291-136-7', 'Komik aksi Dragon Ball Super dengan pertarungan epik.', 92, 'Sangat Baik', 'Sangat Relevan', 4),
('Selangkah Lebih Dekat Dengan Soekarno', 'Adji Nugroho', 'Pendidikan', 'Roemah Soekarno', 2017, 350, 4.1, 95000, '978-602-291-137-8', 'Buku pendidikan tentang sejarah dan pemikiran Soekarno.', 68, 'Baik', 'Relevan', 8),
('Jendral Bersenjata Nurani', 'Retno Kustiati', 'Pendidikan', 'PT Pustaka Sinar Harapan', 2004, 400, 4.0, 85000, '978-602-291-138-9', 'Buku pendidikan tentang kepemimpinan dan nilai-nilai moral.', 65, 'Sedikit Baik', 'Cukup Relevan', 9),
('Pahlawan Indonesia Jilid 2', 'Jendela Puspita', 'Ensiklopedia', 'Jendela Puspita', 2024, 600, 4.7, 120000, '978-602-291-139-0', 'Ensiklopedia lengkap tentang pahlawan-pahlawan Indonesia.', 82, 'Sangat Baik', 'Sangat Relevan', 10),
('Tokoh Pendidikan (Edisi Revisi)', 'Abendanon & K.H Imam Zarkasyi', 'Ensiklopedia', 'Nuansa Cendekia', 2013, 450, 4.3, 100000, '978-602-291-140-1', 'Ensiklopedia tokoh-tokoh pendidikan Indonesia yang berpengaruh.', 75, 'Baik', 'Relevan', 7);

-- Create indexes for better performance
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_books_rating ON books(rating DESC);
CREATE INDEX idx_books_year ON books(year DESC);
CREATE INDEX idx_books_price ON books(price);
CREATE INDEX idx_books_borrower_count ON books(borrower_count DESC);
CREATE INDEX idx_books_book_condition ON books(book_condition);
CREATE INDEX idx_books_content_relevance ON books(content_relevance);
CREATE INDEX idx_books_loan_duration ON books(loan_duration);

-- Tabel untuk kriteria SPK
CREATE TABLE IF NOT EXISTS criteria (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    bobot FLOAT NOT NULL
);

-- Dummy data untuk tabel criteria
INSERT INTO criteria (code, name, bobot) VALUES
('C1', 'Jumlah Peminjaman', 0.30),
('C2', 'Kategori Jenis Buku', 0.20),
('C3', 'Kondisi Fisik Buku', 0.15),
('C4', 'Relevansi Isi Buku', 0.25),
('C5', 'Durasi Peminjaman', 0.10);

-- Create a dedicated user for the application (optional)
-- CREATE USER IF NOT EXISTS 'loginapp'@'localhost' IDENTIFIED BY 'your_password';
-- GRANT ALL PRIVILEGES ON tugas_java.* TO 'loginapp'@'localhost';
-- FLUSH PRIVILEGES;

-- Show the created data
SELECT 'Users Table:' as table_name;
SELECT * FROM users;

SELECT 'Books Table with SPK Criteria:' as table_name;
SELECT id, title, author, category, rating, price, isbn, borrower_count, book_condition, content_relevance, loan_duration FROM books ORDER BY borrower_count DESC;

-- Show SPK criteria summary
SELECT 'SPK Criteria Summary:' as summary;
SELECT 
    'Jumlah Peminjam' as criteria,
    MIN(borrower_count) as min_value,
    MAX(borrower_count) as max_value,
    AVG(borrower_count) as avg_value
FROM books
UNION ALL
SELECT 
    'Durasi Peminjaman (hari)' as criteria,
    MIN(loan_duration) as min_value,
    MAX(loan_duration) as max_value,
    AVG(loan_duration) as avg_value
FROM books;

SELECT 'Book Conditions Distribution:' as distribution;
SELECT book_condition, COUNT(*) as count FROM books GROUP BY book_condition ORDER BY count DESC;

SELECT 'Content Relevance Distribution:' as distribution;
SELECT content_relevance, COUNT(*) as count FROM books GROUP BY content_relevance ORDER BY count DESC; 