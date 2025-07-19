package com.bookspk;

public class SPKDataConverter {
    
    // C1. Jumlah Peminjam dan Bobot
    public static int convertBorrowCountToWeight(int borrowCount) {
        if (borrowCount >= 1 && borrowCount <= 20) return 1;
        if (borrowCount >= 21 && borrowCount <= 40) return 2;
        if (borrowCount >= 41 && borrowCount <= 60) return 3;
        if (borrowCount >= 61 && borrowCount <= 80) return 4;
        if (borrowCount >= 81 && borrowCount <= 100) return 5;
        return 1; // default
    }
    
    // C2. Kategori Jenis Buku, Rata Rata Rating, Bobot
    public static int convertCategoryAndRatingToWeight(String category, double rating) {
        if ("Ensiklopedia".equalsIgnoreCase(category) && rating >= 1.0 && rating <= 1.9) return 1;
        if ("Komik & Manga".equalsIgnoreCase(category) && rating >= 2.0 && rating <= 3.1) return 2;
        if ("Non-Fiksi".equalsIgnoreCase(category) && rating >= 3.1 && rating <= 4.0) return 3;
        if ("Fiksi".equalsIgnoreCase(category) && rating >= 4.1 && rating <= 4.5) return 4;
        if ("Pendidikan".equalsIgnoreCase(category) && rating >= 4.6 && rating <= 5.0) return 5;
        
        // Fallback based on category only
        if ("Ensiklopedia".equalsIgnoreCase(category)) return 1;
        if ("Komik & Manga".equalsIgnoreCase(category)) return 2;
        if ("Non-Fiksi".equalsIgnoreCase(category)) return 3;
        if ("Fiksi".equalsIgnoreCase(category)) return 4;
        if ("Pendidikan".equalsIgnoreCase(category)) return 5;
        
        return 3; // default
    }
    
    // C3. Kondisi Fisik Buku, bobot
    public static int convertPhysicalConditionToWeight(String condition) {
        if ("Rusak Berat".equalsIgnoreCase(condition)) return 1;
        if ("Rusak Ringan".equalsIgnoreCase(condition)) return 2;
        if ("Sedikit Baik".equalsIgnoreCase(condition)) return 3;
        if ("Baik".equalsIgnoreCase(condition)) return 4;
        if ("Sangat Baik".equalsIgnoreCase(condition)) return 5;
        return 3; // default
    }
    
    // C4. Relevansi Isi Buku, Bobot
    public static int convertContentRelevanceToWeight(String relevance) {
        if ("Tidak Relevan".equalsIgnoreCase(relevance)) return 1;
        if ("Kurang Relevan".equalsIgnoreCase(relevance)) return 2;
        if ("Cukup Relevan".equalsIgnoreCase(relevance)) return 3;
        if ("Relevan".equalsIgnoreCase(relevance)) return 4;
        if ("Sangat Relevan".equalsIgnoreCase(relevance)) return 5;
        return 3; // default
    }
    
    // C5. Durasi Peminjaman, Keterangan, Bobot
    public static int convertBorrowDurationToWeight(int duration) {
        if (duration < 3) return 1; // Sangat Singkat
        if (duration >= 3 && duration <= 6) return 2; // Singkat
        if (duration >= 7 && duration <= 10) return 3; // Sedang
        if (duration >= 11 && duration <= 14) return 4; // Lama
        if (duration > 14) return 5; // Sangat Lama
        return 3; // default
    }
    
    // Get bobot kriteria
    public static double getCriteriaWeight(String criteriaCode) {
        switch (criteriaCode) {
            case "C1": return 0.30; // Jumlah Peminjaman 30%
            case "C2": return 0.20; // Kategori Jenis Buku 20%
            case "C3": return 0.15; // Kondisi Fisik Buku 15%
            case "C4": return 0.25; // Relevansi Isi Buku 25%
            case "C5": return 0.10; // Durasi Peminjaman 10%
            default: return 0.0;
        }
    }
    
    // Get nama kriteria
    public static String getCriteriaName(String criteriaCode) {
        switch (criteriaCode) {
            case "C1": return "Jumlah Peminjaman";
            case "C2": return "Kategori Jenis Buku";
            case "C3": return "Kondisi Fisik Buku";
            case "C4": return "Relevansi Isi Buku";
            case "C5": return "Durasi Peminjaman";
            default: return "Unknown";
        }
    }
} 