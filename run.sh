#!/bin/bash

# Book Selection SPK System - Run Script
# Modern Java GUI application with MySQL database

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${CYAN}================================${NC}"
echo -e "${CYAN}  Book Selection SPK System${NC}"
echo -e "${CYAN}  Modern Java GUI Application${NC}"
echo -e "${CYAN}================================${NC}"
echo ""

# Check if Java is installed
echo -e "${BLUE}🔍 Checking Java installation...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java is not installed or not in PATH${NC}"
    echo -e "${YELLOW}Please install Java 11 or higher and try again${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo -e "${GREEN}✅ Java version: $JAVA_VERSION${NC}"

if [ "$JAVA_VERSION" -lt 11 ]; then
    echo -e "${RED}❌ Java 11 or higher is required${NC}"
    echo -e "${YELLOW}Current version: $JAVA_VERSION${NC}"
    exit 1
fi

# Check if Maven is installed
echo -e "${BLUE}🔍 Checking Maven installation...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven is not installed or not in PATH${NC}"
    echo -e "${YELLOW}Please install Maven and try again${NC}"
    exit 1
fi

MAVEN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f3)
echo -e "${GREEN}✅ Maven version: $MAVEN_VERSION${NC}"

# Check if MySQL is running
echo -e "${BLUE}🔍 Checking MySQL service...${NC}"
if ! pgrep -x "mysqld" > /dev/null; then
    echo -e "${YELLOW}⚠️  MySQL service is not running${NC}"
    echo -e "${YELLOW}Please start MySQL service before running the application${NC}"
    echo -e "${CYAN}On macOS: brew services start mysql${NC}"
    echo -e "${CYAN}On Ubuntu: sudo systemctl start mysql${NC}"
    echo -e "${CYAN}On Windows: Start MySQL service from Services${NC}"
    echo ""
    read -p "Do you want to continue anyway? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
else
    echo -e "${GREEN}✅ MySQL service is running${NC}"
fi

# Clean and compile
echo -e "${BLUE}🔨 Building project...${NC}"
if mvn clean compile; then
    echo -e "${GREEN}✅ Project compiled successfully${NC}"
else
    echo -e "${RED}❌ Compilation failed${NC}"
    exit 1
fi

# Initialize database
echo -e "${BLUE}🗄️  Initializing database...${NC}"
echo -e "${YELLOW}This will create the database tables and insert sample data${NC}"

# Check if setup_database.sql exists
if [ ! -f "setup_database.sql" ]; then
    echo -e "${RED}❌ setup_database.sql not found${NC}"
    echo -e "${YELLOW}Please ensure setup_database.sql is in the current directory${NC}"
    exit 1
fi

# Try to run setup_database.sql
echo -e "${BLUE}📊 Setting up database schema and sample data...${NC}"
if mysql -u root < setup_database.sql 2>/dev/null; then
    echo -e "${GREEN}✅ Database setup completed successfully${NC}"
else
    echo -e "${YELLOW}⚠️  Could not run setup_database.sql with root user${NC}"
    echo -e "${YELLOW}Trying with default MySQL settings...${NC}"
    
    # Try alternative MySQL connection methods
    if mysql < setup_database.sql 2>/dev/null; then
        echo -e "${GREEN}✅ Database setup completed with default settings${NC}"
    else
        echo -e "${YELLOW}⚠️  Could not run setup_database.sql${NC}"
        echo -e "${YELLOW}The application will try to connect with default settings${NC}"
        echo -e "${YELLOW}You may need to manually run setup_database.sql${NC}"
    fi
fi

# Run the application
echo -e "${BLUE}🚀 Starting Book Selection SPK System...${NC}"
echo -e "${CYAN}================================${NC}"
echo -e "${CYAN}  Application Features:${NC}"
echo -e "${CYAN}  • Modern Login/Register System${NC}"
echo -e "${CYAN}  • Book Management (CRUD)${NC}"
echo -e "${CYAN}  • SPK Analysis (Decision Support)${NC}"
echo -e "${CYAN}  • User Profile Management${NC}"
echo -e "${CYAN}  • Clean Color Palette Design${NC}"
echo -e "${CYAN}================================${NC}"
echo ""

# Run with Maven
if mvn exec:java -Dexec.mainClass="com.bookspk.LoginFrame"; then
    echo -e "${GREEN}✅ Application closed successfully${NC}"
else
    echo -e "${RED}❌ Application failed to start${NC}"
    echo -e "${YELLOW}Please check the error messages above${NC}"
    exit 1
fi

echo ""
echo -e "${GREEN}🎉 Thank you for using Book Selection SPK System!${NC}" 