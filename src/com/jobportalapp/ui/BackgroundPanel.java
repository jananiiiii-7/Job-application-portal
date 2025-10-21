package com.jobportalapp.ui;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    // Constructor with image path
    public BackgroundPanel(String imagePath) {
        System.out.println("Loading image from: " + imagePath);
        backgroundImage = new ImageIcon(imagePath).getImage();
        System.out.println("Image loaded: " + (backgroundImage != null));
    }

    // Default constructor with a fallback image (optional)
    public BackgroundPanel() {
        this("images/default.jpg");  // Use a default background image if no path is provided
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Stretch the image to fit the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
