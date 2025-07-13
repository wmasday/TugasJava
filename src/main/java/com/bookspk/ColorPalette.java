package com.bookspk;

import java.awt.Color;

/**
 * Clean Color Palette for Modern UI Design
 * Minimalist and professional color scheme
 */
public class ColorPalette {

    // Primary Colors - Clean and Modern
    public static final Color PRIMARY_BLUE = new Color(59, 130, 246); // #3B82F6
    public static final Color PRIMARY_BLUE_DARK = new Color(37, 99, 235); // #2563EB
    public static final Color PRIMARY_GREEN = new Color(34, 197, 94); // #22C55E
    public static final Color PRIMARY_GREEN_DARK = new Color(22, 163, 74); // #16A34A
    public static final Color PRIMARY_PURPLE = new Color(147, 51, 234); // #9333EA
    public static final Color PRIMARY_PURPLE_DARK = new Color(126, 34, 206); // #7E22CE
    public static final Color PRIMARY_RED = new Color(239, 68, 68); // #EF4444
    public static final Color PRIMARY_RED_DARK = new Color(220, 38, 38); // #DC2626
    public static final Color PRIMARY_ORANGE = new Color(249, 115, 22); // #F97316
    public static final Color PRIMARY_ORANGE_DARK = new Color(234, 88, 12); // #EA580C
    public static final Color PRIMARY_GRAY = new Color(107, 114, 128); // #6B7280
    public static final Color PRIMARY_GRAY_DARK = new Color(75, 85, 99); // #4B5563

    // Secondary Colors - Neutral and Clean
    public static final Color SECONDARY_GRAY = new Color(107, 114, 128); // #6B7280
    public static final Color SECONDARY_GRAY_DARK = new Color(75, 85, 99); // #4B5563
    public static final Color SECONDARY_ORANGE = new Color(249, 115, 22); // #F97316
    public static final Color SECONDARY_ORANGE_DARK = new Color(234, 88, 12); // #EA580C
    public static final Color SECONDARY_GREEN = new Color(22, 163, 74); // #16A34A
    public static final Color SECONDARY_BLUE = new Color(37, 99, 235); // #2563EB
    public static final Color SECONDARY_RED = new Color(220, 38, 38); // #DC2626
    public static final Color SECONDARY_PURPLE = new Color(126, 34, 206); // #7E22CE

    // Accent Colors - For Special Actions
    public static final Color ACCENT_RED = new Color(239, 68, 68); // #EF4444
    public static final Color ACCENT_RED_DARK = new Color(220, 38, 38); // #DC2626
    public static final Color ACCENT_YELLOW = new Color(245, 158, 11); // #F59E0B
    public static final Color ACCENT_YELLOW_DARK = new Color(217, 119, 6); // #D97706

    // Background Colors - Clean and Light
    public static final Color BACKGROUND_PRIMARY = new Color(248, 250, 252); // #F8FAFC
    public static final Color BACKGROUND_SECONDARY = new Color(255, 255, 255); // #FFFFFF
    public static final Color BACKGROUND_TERTIARY = new Color(241, 245, 249); // #F1F5F9

    // Text Colors - High Contrast
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42); // #0F172A
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105); // #475569
    public static final Color TEXT_MUTED = new Color(148, 163, 184); // #94A3B8

    // Border Colors - Subtle
    public static final Color BORDER_PRIMARY = new Color(226, 232, 240); // #E2E8F0
    public static final Color BORDER_SECONDARY = new Color(203, 213, 225); // #CBD5E1

    // Success/Error Colors
    public static final Color SUCCESS = new Color(34, 197, 94); // #22C55E
    public static final Color ERROR = new Color(239, 68, 68); // #EF4444
    public static final Color WARNING = new Color(245, 158, 11); // #F59E0B
    public static final Color INFO = new Color(59, 130, 246); // #3B82F6

    // Gradient Combinations
    public static final Color[] GRADIENT_BLUE = { PRIMARY_BLUE, PRIMARY_BLUE_DARK };
    public static final Color[] GRADIENT_GREEN = { PRIMARY_GREEN, PRIMARY_GREEN_DARK };
    public static final Color[] GRADIENT_PURPLE = { PRIMARY_PURPLE, PRIMARY_PURPLE_DARK };
    public static final Color[] GRADIENT_ORANGE = { SECONDARY_ORANGE, SECONDARY_ORANGE_DARK };
    public static final Color[] GRADIENT_RED = { ACCENT_RED, ACCENT_RED_DARK };
    public static final Color[] GRADIENT_YELLOW = { ACCENT_YELLOW, ACCENT_YELLOW_DARK };
    public static final Color[] GRADIENT_GRAY = { SECONDARY_GRAY, SECONDARY_GRAY_DARK };

    // Background Gradients
    public static final Color[] GRADIENT_BACKGROUND = { BACKGROUND_PRIMARY, BACKGROUND_TERTIARY };
    public static final Color[] GRADIENT_CARD = { BACKGROUND_SECONDARY, BACKGROUND_PRIMARY };

    /**
     * Get gradient colors for button types
     */
    public static Color[] getButtonGradient(String type) {
        switch (type.toLowerCase()) {
            case "primary":
            case "login":
            case "register":
                return GRADIENT_BLUE;
            case "success":
            case "save":
            case "add":
            case "calculate":
                return GRADIENT_GREEN;
            case "warning":
            case "spk":
            case "password":
                return GRADIENT_ORANGE;
            case "danger":
            case "delete":
            case "logout":
                return GRADIENT_RED;
            case "info":
            case "edit":
            case "profile":
                return GRADIENT_PURPLE;
            case "neutral":
            case "back":
            case "clear":
            case "secondary":
                return GRADIENT_GRAY;
            default:
                return GRADIENT_BLUE;
        }
    }

    /**
     * Get background gradient
     */
    public static Color[] getBackgroundGradient() {
        return GRADIENT_BACKGROUND;
    }

    /**
     * Get card gradient
     */
    public static Color[] getCardGradient() {
        return GRADIENT_CARD;
    }
}