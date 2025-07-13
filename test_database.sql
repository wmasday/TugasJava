-- Test script for database setup and ISBN uniqueness
-- Run this after setup_database.sql to verify everything works

USE tugas_java;

-- Test 1: Check if tables exist
SELECT 'Testing table existence...' as test;
SHOW TABLES;

-- Test 2: Check users table
SELECT 'Testing users table...' as test;
SELECT * FROM users;

-- Test 3: Check books table structure
SELECT 'Testing books table structure...' as test;
DESCRIBE books;

-- Test 4: Check books data
SELECT 'Testing books data...' as test;
SELECT id, title, author, isbn, rating FROM books ORDER BY rating DESC;

-- Test 5: Test ISBN uniqueness constraint
SELECT 'Testing ISBN uniqueness...' as test;

-- Try to insert a book with existing ISBN (should fail)
INSERT INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) 
VALUES ('Test Book', 'Test Author', 'Test Category', 'Test Publisher', 2024, 300, 4.0, 100000, '978-602-291-123-4', 'Test description');

-- Test 6: Check constraints
SELECT 'Testing constraints...' as test;
SELECT COUNT(*) as total_books FROM books;
SELECT COUNT(DISTINCT isbn) as unique_isbns FROM books;

-- Test 7: Check indexes
SELECT 'Testing indexes...' as test;
SHOW INDEX FROM books;

-- Test 8: Test valid book insertion
SELECT 'Testing valid book insertion...' as test;
INSERT INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) 
VALUES ('Test Book Valid', 'Test Author Valid', 'Test Category', 'Test Publisher', 2024, 300, 4.0, 100000, '978-602-291-999-9', 'Test description valid');

-- Verify the new book was added
SELECT 'Verifying new book...' as test;
SELECT id, title, author, isbn FROM books WHERE isbn = '978-602-291-999-9';

-- Clean up test data
DELETE FROM books WHERE isbn = '978-602-291-999-9';

SELECT 'Database test completed successfully!' as result; 