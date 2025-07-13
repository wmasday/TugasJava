package com.bookspk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class for MySQL
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/tugas_java";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Try empty password first

    private static Connection connection = null;

    /**
     * Get database connection
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Initialize database tables
     */
    public static void initializeDatabase() {
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "email VARCHAR(100)," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String createBooksTableSQL = "CREATE TABLE IF NOT EXISTS books (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "author VARCHAR(255) NOT NULL," +
                "category VARCHAR(100) NOT NULL," +
                "publisher VARCHAR(255)," +
                "year INT," +
                "pages INT," +
                "rating DECIMAL(3,1) DEFAULT 0.0," +
                "price DECIMAL(10,2) DEFAULT 0.00," +
                "isbn VARCHAR(20)," +
                "description TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String insertDefaultUser = "INSERT IGNORE INTO users (username, password, email) " +
                "VALUES ('admin', 'admin123', 'admin@example.com')";

        // Sample books data
        String[] insertSampleBooks = {
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', 'Novel', 'Scribner', 1925, 180, 4.2, 150000, '978-0743273565', 'A story of the fabulously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('To Kill a Mockingbird', 'Harper Lee', 'Novel', 'Grand Central', 1960, 281, 4.5, 120000, '978-0446310789', 'The story of young Scout Finch and her father Atticus in a racially divided Alabama town.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('1984', 'George Orwell', 'Fiksi', 'Penguin Books', 1949, 328, 4.3, 180000, '978-0451524935', 'A dystopian novel about totalitarianism and surveillance society.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('Pride and Prejudice', 'Jane Austen', 'Novel', 'Penguin Classics', 1813, 432, 4.4, 160000, '978-0141439518', 'A romantic novel of manners that follows the emotional development of Elizabeth Bennet.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('The Hobbit', 'J.R.R. Tolkien', 'Fiksi', 'Houghton Mifflin', 1937, 366, 4.6, 200000, '978-0547928241', 'A fantasy novel about Bilbo Baggins journey with thirteen dwarves.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('The Catcher in the Rye', 'J.D. Salinger', 'Novel', 'Little, Brown', 1951, 277, 4.1, 140000, '978-0316769488', 'A novel about teenage alienation and loss of innocence in post-World War II America.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('Lord of the Flies', 'William Golding', 'Fiksi', 'Penguin Books', 1954, 224, 4.0, 130000, '978-0399501487', 'A novel about the dark side of human nature through the story of boys stranded on an island.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('Animal Farm', 'George Orwell', 'Fiksi', 'Penguin Books', 1945, 112, 4.2, 110000, '978-0451526342', 'An allegorical novella about farm animals who rebel against their human farmer.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('The Alchemist', 'Paulo Coelho', 'Pengembangan Diri', 'HarperOne', 1988, 208, 4.3, 170000, '978-0062315007', 'A novel about a young Andalusian shepherd who dreams of finding a worldly treasure.')",
                "INSERT IGNORE INTO books (title, author, category, publisher, year, pages, rating, price, isbn, description) VALUES ('Rich Dad Poor Dad', 'Robert Kiyosaki', 'Bisnis', 'Warner Books', 1997, 336, 4.1, 250000, '978-0446677455', 'A personal finance book about the difference in mindset between the rich and poor.')"
        };

        try (Connection conn = getConnection()) {
            // Create tables
            conn.createStatement().execute(createUsersTableSQL);
            conn.createStatement().execute(createBooksTableSQL);

            // Insert default user
            conn.createStatement().execute(insertDefaultUser);

            // Insert sample books
            for (String insertBook : insertSampleBooks) {
                conn.createStatement().execute(insertBook);
            }

            System.out.println("Database initialized successfully with sample books");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}