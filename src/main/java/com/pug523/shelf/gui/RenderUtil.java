package com.pug523.shelf.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class RenderUtil {
    public static void drawDynamicCircle(GuiGraphicsExtractor gui, int centerX, int centerY, int radius, int color) {
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                // Check if the current pixel is inside the circle boundary.
                // Adding 0.5 helps smooth out pixel aliasing for a rounder look.
                if ((x * x) + (y * y) <= (radius * radius) + radius) {
                    int pixelX = centerX + x;
                    int pixelY = centerY + y;
                    // Draw a single 1x1 pixel block.
                    gui.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, color);
                }
            }
        }
    }

    public static void drawDynamicCapsule(GuiGraphicsExtractor gui, int x, int y, int width, int height, int color) {
        int radius = height / 2;

        float leftCenterX = x + radius;
        float rightCenterX = x + width - radius;
        float centerY = y + radius;

        float maxDistanceSq = (radius * radius) - 0.5f;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixelX = x + col;
                int pixelY = y + row;

                float sampleX = pixelX + 0.5f;
                float sampleY = pixelY + 0.5f;

                boolean inside = false;

                if (sampleX < leftCenterX) {
                    // Left circle cap math
                    float dx = sampleX - leftCenterX;
                    float dy = sampleY - centerY;
                    if ((dx * dx) + (dy * dy) < maxDistanceSq) inside = true;
                } else if (sampleX > rightCenterX) {
                    // Right circle cap math
                    float dx = sampleX - rightCenterX;
                    float dy = sampleY - centerY;
                    if ((dx * dx) + (dy * dy) < maxDistanceSq) inside = true;
                } else {
                    // Middle body rectangle math
                    inside = true;
                }

                if (inside) {
                    gui.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, color);
                }
            }
        }
    }

    public static void renderDownwardArrow(GuiGraphicsExtractor gui, int startX, int startY, int color) {
        gui.fill(startX, startY, startX + 5, startY + 1, color);
        gui.fill(startX + 1, startY + 1, startX + 4, startY + 2, color);
        gui.fill(startX + 2, startY + 2, startX + 3, startY + 3, color);
    }

    public static void renderRightwardArrow(GuiGraphicsExtractor gui, int startX, int startY, int color) {
        gui.fill(startX, startY, startX + 1, startY + 5, color);
        gui.fill(startX + 1, startY + 1, startX + 2, startY + 4, color);
        gui.fill(startX + 2, startY + 2, startX + 3, startY + 3, color);
    }
}
