# Book Selection SPK System

A modern Java GUI application for book management with Decision Support System (SPK) analysis. Built with Java Swing, MySQL database, and featuring a clean color palette design.

## 🚀 Features

### Core Features
- **Modern Login/Register System**: Clean authentication with gradient design
- **Book Management (CRUD)**: Complete Create, Read, Update, Delete operations
- **SPK Analysis**: Decision Support System for book selection
- **User Profile Management**: Edit profile and change password functionality
- **Category Management**: Dynamic dropdown with database-driven categories
- **Full Screen Interface**: Optimized for large displays

### Technical Features
- **Java 11+**: Modern Java with text blocks support
- **MySQL Integration**: Robust database connectivity
- **FlatLaf UI**: Modern look and feel
- **Gradient Design**: Beautiful color palette and animations
- **Responsive Layout**: Adaptive to different screen sizes
- **Input Validation**: Comprehensive form validation
- **Error Handling**: Graceful error handling and user feedback

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
- `test_database.sql`: Verifies database setup and constraints

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
- **Navigation**: Access to SPK Analysis and Book Management

### 3. Book Management (CRUD)
- **BookCRUDFrame**: Complete book management interface
- **Add Books**: Create new books with validation
- **Edit Books**: Update existing book information
- **Delete Books**: Remove books with confirmation
- **Category Dropdown**: Dynamic category selection
- **Form Validation**: Comprehensive input validation

### 4. SPK Analysis
- **SPKFrame**: Decision Support System interface
- **Criteria Weights**: Adjustable sliders for:
  - Rating (0-5)
  - Price (0-1M)
  - Year (1900-2024)
  - Pages (0-1000)
- **Results Table**: Ranked book recommendations
- **50%-50% Layout**: Balanced criteria and results panels

## 🎨 Design Features

### Color Palette
- **Primary Colors**: Modern blue, green, orange, purple
- **Background**: Gradient backgrounds
- **Text**: High contrast for readability
- **Borders**: Subtle borders for component separation

### UI Components
- **Gradient Buttons**: Animated buttons with hover effects
- **Modern Input Fields**: Clean text fields with proper spacing
- **Responsive Tables**: Sortable and filterable data tables
- **Split Panes**: Efficient use of screen space
- **Scroll Panes**: Handle large datasets gracefully

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

### Books Table
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 🔧 Project Structure

```
src/main/java/com/bookspk/
├── LoginFrame.java              # Login interface
├── RegisterFrame.java           # Registration interface
├── EditProfileFrame.java        # Profile management
├── BookSelectionFrame.java      # Main dashboard
├── BookCRUDFrame.java          # Book management
├── SPKFrame.java               # SPK analysis
├── MainApplicationFrame.java    # Legacy main frame
├── DatabaseConnection.java      # Database utility
├── User.java                   # User model
├── UserDAO.java                # User data access
├── Book.java                   # Book model
├── BookDAO.java                # Book data access
├── GradientButton.java         # Custom button component
└── ColorPalette.java           # Color definitions
```

## 🎯 SPK Analysis Algorithm

The Decision Support System uses weighted criteria:

### Criteria Weights
1. **Rating (0-5)**: Book quality assessment
2. **Price (0-1M)**: Affordability factor
3. **Year (1900-2024)**: Publication recency
4. **Pages (0-1000)**: Content depth

### Calculation Method
```java
SPK Score = (Rating × Rating_Weight) + 
            (Price_Normalized × Price_Weight) + 
            (Year_Normalized × Year_Weight) + 
            (Pages_Normalized × Pages_Weight)
```

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

## 📝 Sample Data

The application includes 10 sample books:
- **Laskar Pelangi** (Andrea Hirata)
- **Bumi Manusia** (Pramoedya Ananta Toer)
- **Filosofi Teras** (Henry Manampiring)
- **Atomic Habits** (James Clear)
- **Rich Dad Poor Dad** (Robert T. Kiyosaki)
- **The Psychology of Money** (Morgan Housel)
- **Clean Code** (Robert C. Martin)
- **Design Patterns** (Erich Gamma)
- **The Martian** (Andy Weir)
- **Dune** (Frank Herbert)

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