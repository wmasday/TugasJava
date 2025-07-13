package com.bookspk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern Gradient Button with animations and shadows
 */
public class GradientButton extends JButton {
    private Color startColor;
    private Color endColor;
    private Color shadowColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private float shadowOpacity = 0.3f;
    private int borderRadius = 12;
    private int shadowOffset = 4;
    private Timer animationTimer;
    private float animationProgress = 0.0f;

    public GradientButton(String text, Color startColor, Color endColor) {
        super(text);
        this.startColor = startColor;
        this.endColor = endColor;
        this.shadowColor = new Color(0, 0, 0, (int) (255 * shadowOpacity));

        setupButton();
        setupAnimations();
    }

    private void setupButton() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setPreferredSize(new Dimension(140, 40));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add mouse listeners for hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                startHoverAnimation();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                startHoverAnimation();
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    private void setupAnimations() {
        animationTimer = new Timer(16, e -> {
            if (isHovered) {
                animationProgress = Math.min(1.0f, animationProgress + 0.1f);
            } else {
                animationProgress = Math.max(0.0f, animationProgress - 0.1f);
            }

            if (animationProgress == 0.0f || animationProgress == 1.0f) {
                animationTimer.stop();
            }

            repaint();
        });
    }

    private void startHoverAnimation() {
        if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();

        // Calculate colors based on state
        Color currentStartColor = getCurrentStartColor();
        Color currentEndColor = getCurrentEndColor();

        // Draw shadow
        if (!isPressed) {
            drawShadow(g2d, width, height);
        }

        // Create rounded rectangle
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                isPressed ? 2 : 0,
                isPressed ? 2 : 0,
                width - (isPressed ? 4 : 0),
                height - (isPressed ? 4 : 0),
                borderRadius,
                borderRadius);

        // Draw gradient background
        GradientPaint gradient = new GradientPaint(
                0, 0, currentStartColor,
                0, height, currentEndColor);
        g2d.setPaint(gradient);
        g2d.fill(roundedRect);

        // Draw text
        g2d.setColor(getForeground());
        g2d.setFont(getFont());

        FontMetrics fm = g2d.getFontMetrics();
        int textX = (width - fm.stringWidth(getText())) / 2;
        int textY = (height - fm.getHeight()) / 2 + fm.getAscent();

        g2d.drawString(getText(), textX, textY);

        g2d.dispose();
    }

    private void drawShadow(Graphics2D g2d, int width, int height) {
        // Create shadow effect
        int shadowSize = (int) (shadowOffset * (1 + animationProgress * 0.5));
        Color shadow = new Color(
                shadowColor.getRed(),
                shadowColor.getGreen(),
                shadowColor.getBlue(),
                (int) (shadowColor.getAlpha() * (0.3 + animationProgress * 0.4)));

        g2d.setColor(shadow);
        RoundRectangle2D shadowRect = new RoundRectangle2D.Float(
                shadowSize, shadowSize,
                width - shadowSize * 2,
                height - shadowSize * 2,
                borderRadius, borderRadius);
        g2d.fill(shadowRect);
    }

    private Color getCurrentStartColor() {
        if (isPressed) {
            return endColor;
        } else if (isHovered) {
            return startColor.brighter();
        } else {
            return startColor;
        }
    }

    private Color getCurrentEndColor() {
        if (isPressed) {
            return startColor;
        } else if (isHovered) {
            return endColor.brighter();
        } else {
            return endColor;
        }
    }

    // Setter methods for customization
    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
    }

    public void setShadowOffset(int offset) {
        this.shadowOffset = offset;
        repaint();
    }

    public void setShadowOpacity(float opacity) {
        this.shadowOpacity = opacity;
        this.shadowColor = new Color(0, 0, 0, (int) (255 * shadowOpacity));
        repaint();
    }

    public void setGradientColors(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        repaint();
    }
}