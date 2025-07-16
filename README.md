# Book Selection SPK System

A modern Java GUI application for book management with Decision Support System (SPK) analysis. Built with Java Swing, MySQL database, and featuring a clean color palette design.

## 🚀 Features

### Core Features
- **Modern Login/Register System**: Clean authentication with gradient design
- **Book Management (CRUD)**: Complete Create, Read, Update, Delete operations
- **Advanced SPK Analysis**: Enhanced Decision Support System with new criteria
- **User Profile Management**: Edit profile and change password functionality
- **Dynamic Category Management**: 10+ book categories with filtering
- **Full Screen Interface**: Optimized for large displays
- **Toggle Features**: Hide/show panels for better workspace management

### Technical Features
- **Java 11+**: Modern Java with text blocks support
- **MySQL Integration**: Robust database connectivity
- **FlatLaf UI**: Modern look and feel
- **Gradient Design**: Beautiful color palette and animations
- **Responsive Layout**: Adaptive to different screen sizes
- **Input Validation**: Comprehensive form validation
- **Error Handling**: Graceful error handling and user feedback
- **Scrollable Panels**: Handle large forms and data sets

## 📋 Prerequisites

- **Java 11 or higher**
- **Maven 3.6 or higher**
- **MySQL Server 8.0 or higher**

## 🗄️ Database Setup

The application includes automatic database setup scripts:

### 1. Install MySQL Server
```bash
# macOS
brew install mysql
brew services start mysql

# Ubuntu/Debian
sudo apt install mysql-server
sudo systemctl start mysql

# Windows
# Download from https://dev.mysql.com/downloads/mysql/
```

### 2. Automatic Setup
The application automatically sets up the database using included SQL scripts:
- `setup_database.sql`: Creates database, tables, and sample data

## 🚀 Quick Start

### 1. Clone or Download Project
```bash
git clone https://github.com/wmasday/TugasJava
cd TugasJava
```

### 2. Run Application
```bash
# Make script executable
chmod +x run.sh

# Run the application
./run.sh
```

The `run.sh` script will:
- ✅ Check Java and Maven installation
- ✅ Verify MySQL service is running
- ✅ Compile the project
- ✅ Set up database automatically
- ✅ Launch the application

### 3. Alternative Manual Setup
```bash
# Compile project
mvn clean compile

# Run application
mvn exec:java -Dexec.mainClass="com.bookspk.LoginFrame"
```

## 👤 Default Login Credentials

The application creates a default admin user:
- **Username**: `admin`
- **Password**: `admin123`

## 📚 Application Modules

### 1. Login/Register System
- **LoginFrame**: Modern login interface with gradient design
- **RegisterFrame**: User registration with validation
- **EditProfileFrame**: Profile management and password change

### 2. Book Selection Dashboard
- **BookSelectionFrame**: Main dashboard with book listing
- **Category Filtering**: Filter books by category
- **Book Table**: Display all books with details
- **Book Details Modal**: Click any row to view detailed book information
- **Navigation**: Access to SPK Analysis and Book Management

### 3. Book Management (CRUD)
- **BookCRUDFrame**: Complete book management interface
- **Toggle Details**: Hide/show book details form for full table view
- **Add Books**: Create new books with validation
- **Edit Books**: Update existing book information
- **Delete Books**: Remove books with confirmation
- **Category Dropdown**: Dynamic category selection
- **Form Validation**: Comprehensive input validation
- **Scrollable Forms**: Handle large forms with scroll panes

### 4. Enhanced SPK Analysis
- **SPKFrame**: Decision Support System interface
- **Toggle Criteria**: Hide/show SPK criteria panel
- **Flexible Results**: Show top 3, 5, 10, 15, 20, 25, 30, 40, 50, 75, or 100 books
- **New SPK Criteria**: Enhanced with 4 new criteria:
  - **Jumlah Peminjam (1-100)**: Borrower count weight
  - **Kondisi Fisik Buku (1-5)**: Book condition weight
  - **Relevansi Isi Buku (1-5)**: Content relevance weight
  - **Durasi Peminjaman (1-5)**: Loan duration weight
- **Results Table**: Ranked book recommendations with SPK scores
- **Dynamic Layout**: Results panel expands when criteria is hidden

## 🎨 Design Features

### Color Palette
- **Primary Colors**: Modern blue, green, orange, purple, red
- **Background**: Gradient backgrounds
- **Text**: High contrast for readability
- **Borders**: Subtle borders for component separation
- **Card Background**: Dedicated color for modal dialogs

### UI Components
- **Gradient Buttons**: Animated buttons with hover effects
- **Modern Input Fields**: Clean text fields with proper spacing
- **Responsive Tables**: Sortable and filterable data tables
- **Split Panes**: Efficient use of screen space
- **Scroll Panes**: Handle large datasets gracefully
- **Modal Dialogs**: Book details modal with gradient design
- **Toggle Buttons**: Show/hide functionality for better UX

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Enhanced Books Table
```sql
CREATE TABLE books (
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
    borrower_count INT DEFAULT 0,
    book_condition VARCHAR(50),
    content_relevance VARCHAR(50),
    loan_duration INT DEFAULT 7
);
```

## 🔧 Project Structure

```
src/main/java/com/bookspk/
├── LoginFrame.java              # Login interface
├── RegisterFrame.java           # Registration interface
├── EditProfileFrame.java        # Profile management
├── BookSelectionFrame.java      # Main dashboard with modal
├── BookCRUDFrame.java          # Book management with toggle
├── SPKFrame.java               # Enhanced SPK analysis
├── MainApplicationFrame.java    # Legacy main frame
├── DatabaseConnection.java      # Database utility
├── User.java                   # User model
├── UserDAO.java                # User data access
├── Book.java                   # Enhanced book model
├── BookDAO.java                # Enhanced book data access
├── GradientButton.java         # Custom button component
└── ColorPalette.java           # Enhanced color definitions
```

## 🎯 Enhanced SPK Analysis Algorithm

The Decision Support System uses weighted criteria with new SPK factors:

### New SPK Criteria Weights
1. **Jumlah Peminjam (1-100)**: Borrower count - higher is better
2. **Kondisi Fisik Buku (1-5)**: Book condition - better condition preferred
3. **Relevansi Isi Buku (1-5)**: Content relevance - more relevant preferred
4. **Durasi Peminjaman (1-5)**: Loan duration - shorter duration preferred

### Enhanced Calculation Method
```java
SPK Score = (BorrowerCount_Normalized × BorrowerCount_Weight) + 
            (BookCondition_Normalized × BookCondition_Weight) + 
            (ContentRelevance_Normalized × ContentRelevance_Weight) + 
            (LoanDuration_Normalized × LoanDuration_Weight)
```

### Normalization Ranges
- **Borrower Count**: 1-20 (0.2), 21-40 (0.4), 41-60 (0.6), 61-80 (0.8), 81-100 (1.0)
- **Book Condition**: Rusak Berat (0.2), Rusak Ringan (0.4), Sedikit Baik (0.6), Baik (0.8), Sangat Baik (1.0)
- **Content Relevance**: Tidak Relevan (0.2), Kurang Relevan (0.4), Cukup Relevan (0.6), Relevan (0.8), Sangat Relevan (1.0)
- **Loan Duration**: <3 days (1.0), 3-6 days (0.8), 7-10 days (0.6), 11-14 days (0.4), >14 days (0.2)

## 📚 Book Categories

The system now supports 10+ categories:
- **Fiksi**: Fiction books
- **Non-Fiksi**: Non-fiction books
- **Komik & Manga**: Comics and manga
- **Pendidikan**: Educational books
- **Ensiklopedia**: Encyclopedia and reference
- **Teknologi**: Technology and programming
- **Bisnis**: Business and economics
- **Psikologi**: Psychology and self-help
- **Sains**: Science and research
- **Sejarah**: History and culture

## 🛠️ Customization

### Adding New Book Categories
1. Add categories to the database
2. The application automatically loads new categories
3. No code changes required

### Modifying SPK Criteria
Edit `BookDAO.java` in the `calculateSPKScore` method:
```java
private double calculateSPKScore(Book book, double[] weights) {
    // Modify calculation logic here
}
```

### Changing Color Palette
Edit `ColorPalette.java`:
```java
public static final Color PRIMARY_BLUE = new Color(52, 152, 219);
public static final Color SECONDARY_BLUE = new Color(41, 128, 185);
public static final Color CARD_BACKGROUND = new Color(255, 255, 255);
```

## 🔍 Troubleshooting

### Database Connection Issues
1. **MySQL not running**: Start MySQL service
2. **Permission denied**: Check MySQL user permissions
3. **Database not found**: Run `./run.sh` to auto-setup

### Compilation Issues
1. **Java version**: Ensure Java 11+ is installed
2. **Maven issues**: Run `mvn clean install`
3. **Dependencies**: Check `pom.xml` for correct versions

### GUI Issues
1. **FlatLaf error**: Application falls back to system look
2. **Display issues**: Check screen resolution settings
3. **Performance**: Ensure adequate system resources

## 📝 Enhanced Sample Data

The application includes 25+ sample books across all categories:

### Fiksi
- **Laskar Pelangi** (Andrea Hirata)
- **Bumi Manusia** (Pramoedya Ananta Toer)
- **Perahu Kertas** (Dee Lestari)

### Non-Fiksi
- **Filosofi Teras** (Henry Manampiring)
- **Atomic Habits** (James Clear)
- **Rich Dad Poor Dad** (Robert T. Kiyosaki)

### Komik & Manga
- **One Piece Vol. 1** (Eiichiro Oda)
- **Naruto Vol. 1** (Masashi Kishimoto)
- **Dragon Ball Vol. 1** (Akira Toriyama)

### Pendidikan
- **Matematika Dasar** (Prof. Dr. Budi Santoso)
- **Fisika Modern** (Dr. Siti Rahayu)
- **Kimia Organik** (Prof. Ahmad Hidayat)

### Ensiklopedia
- **Ensiklopedia Indonesia** (Tim Redaksi)
- **World Atlas** (National Geographic)
- **Encyclopedia Britannica** (Britannica)

### Teknologi
- **Clean Code** (Robert C. Martin)
- **Design Patterns** (Erich Gamma)
- **The Pragmatic Programmer** (Andrew Hunt)

### Bisnis
- **The Psychology of Money** (Morgan Housel)
- **Zero to One** (Peter Thiel)
- **Good to Great** (Jim Collins)

### Psikologi
- **Thinking, Fast and Slow** (Daniel Kahneman)
- **The Power of Habit** (Charles Duhigg)
- **Mindset** (Carol S. Dweck)

### Sains
- **A Brief History of Time** (Stephen Hawking)
- **The Selfish Gene** (Richard Dawkins)
- **Sapiens** (Yuval Noah Harari)

### Sejarah
- **Guns, Germs, and Steel** (Jared Diamond)
- **The Rise and Fall of the Third Reich** (William L. Shirer)
- **A People's History of the United States** (Howard Zinn)

## 🆕 Recent Updates

### Version 2.0 Features
- ✅ **Enhanced SPK Criteria**: Added 4 new SPK factors
- ✅ **Toggle Features**: Hide/show panels for better workspace
- ✅ **Book Details Modal**: Click rows to view detailed information
- ✅ **Extended Categories**: 10+ book categories with sample data
- ✅ **Flexible Results**: Show top 3-100 books in SPK analysis
- ✅ **Scrollable Forms**: Handle large forms with scroll panes
- ✅ **Enhanced UI**: Improved color palette and component design

## 🔒 Security Notes

- **Development Use**: This is a demonstration application
- **Password Storage**: Passwords stored in plain text (not for production)
- **Input Validation**: Basic validation implemented
- **Production Recommendations**:
  - Implement password hashing (BCrypt)
  - Add input sanitization
  - Use connection pooling
  - Implement proper session management

## 📄 License

This project is for educational purposes.

## 🤝 Contributing

Feel free to submit issues and enhancement requests!

## 📞 Support

For issues or questions:
1. Check the troubleshooting section
2. Review the console output for error messages
3. Ensure all prerequisites are met
4. Try running `./run.sh` for automatic setup

---

**Built with ❤️ using Java Swing, MySQL, and modern UI design principles.**

**Version 2.0 - Enhanced with advanced SPK analysis, toggle features, and comprehensive book management system.** 