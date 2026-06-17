package com.pug523.shelf.gui.widget;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import com.pug523.shelf.compat.ComponentCompat;
import com.pug523.shelf.compat.GuiCompat;
import com.pug523.shelf.config.Option;
import com.pug523.shelf.gui.Colors;
import com.pug523.shelf.gui.overlay.ScreenOverlay;
import com.pug523.shelf.gui.sound.SoundUtil;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ColorPickerOverlay implements ScreenOverlay {
    private final Option<Integer> targetOption;
    private final Consumer<Integer> onConfirm;

    private int x, y;
    private final int width = 340;
    private final int height = 300;

    private float hue = 0.0f;
    private float saturation = 1.0f;
    private float brightness = 1.0f;
    private float alpha = 1.0f;

    private final int originalColor;
    private final List<Integer> presetColors = new ArrayList<>();
    private final List<Integer> recentColors = new ArrayList<>();

    private boolean isDraggingSBSpace = false;
    private boolean isDraggingHueSlider = false;
    private boolean isDraggingAlphaSlider = false;

    private static final Component TITLE_TEXT = ComponentCompat.translatable("Select Color");
    private static final Component LABEL_HEX = ComponentCompat.translatable("HEX");
    private static final Component LABEL_NEW = ComponentCompat.translatable("New");
    private static final Component LABEL_CURRENT = ComponentCompat.translatable("Current");
    private static final Component PRESET_TEXT = ComponentCompat.translatable("Presets");
    private static final Component RECENT_TEXT = ComponentCompat.translatable("Recent Colors");
    private static final Component BTN_CANCEL = ComponentCompat.translatable("Cancel");
    private static final Component BTN_OK = ComponentCompat.translatable("OK");
    private static final Component BTN_X = ComponentCompat.literal("X");

    public ColorPickerOverlay(Option<Integer> targetOption, Consumer<Integer> onConfirm) {
        this.targetOption = targetOption;
        this.onConfirm = onConfirm;

        this.originalColor = targetOption.getPendingValue();
        int r = (originalColor >> 16) & 0xFF;
        int g = (originalColor >> 8) & 0xFF;
        int b = originalColor & 0xFF;
        int a = (originalColor >> 24) & 0xFF;
        this.alpha = a / 255.0f;

        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];

        presetColors.add(0xFF2563EB);
        presetColors.add(0xFF7C3AED);
        presetColors.add(0xFF8B5CF6);
        presetColors.add(0xFFEC4899);
        presetColors.add(0xFFEF4444);
        presetColors.add(0xFFF59E0B);
        presetColors.add(0xFFFBBF24);
        presetColors.add(0xFF22C55E);
        presetColors.add(0xFF10B981);
        presetColors.add(0xFF60A5FA);
        presetColors.add(0xFFE5E7EB);
    }

    @Override
    public void init(int screenWidth, int screenHeight) {
        this.x = (screenWidth - this.width) / 2;
        this.y = (screenHeight - this.height) / 2;
    }

    @Override
    public void render(Font font, GuiCompat gui, int mouseX, int mouseY, float partialTicks) {
        updateDragStates(mouseX, mouseY);

        gui.fill(x, y, x + width, y + height, 0xFF11131E);
        gui.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF161923);

        // Header Title Sequence
        gui.text(font, TITLE_TEXT, x + 16, y + 14, Colors.WHITE, false);
        // Window close text tag symbol 'X' at top right
        gui.text(font, BTN_X, x + width - 24, y + 14, 0xFF6B7280, false);

        int sbX = x + 16;
        int sbY = y + 36;
        int sbSize = 120;

        // Render 2D Saturation/Brightness
        int segmentWidth = 1;
        for (int row = 0; row < sbSize; row++) {
            float currentBrightness = 1.0f - (row / (float) sbSize);

            for (int col = 0; col < sbSize; col += segmentWidth) {
                float currentSaturation = col / (float) sbSize;
                int pixelColor = Color.HSBtoRGB(hue, currentSaturation, currentBrightness);
                gui.fill(sbX + col, sbY + row, sbX + col + segmentWidth, sbY + row + 1, pixelColor);
            }
        }

        // Target handle cursor positioning circle rings
        int handleX = sbX + (int) (this.saturation * sbSize);
        int handleY = sbY + (int) ((1.0f - this.brightness) * sbSize);
        gui.fill(handleX - 3, handleY - 3, handleX + 3, handleY + 3, 0xFFFFFFFF);
        gui.fill(handleX - 1, handleY - 1, handleX + 1, handleY + 1, Color.HSBtoRGB(hue, saturation, brightness));

        // Render Hue vertical slider bar tracking path
        int hueX = sbX + sbSize + 12;
        int hueY = sbY;
        int hueWidth = 10;
        for (int i = 0; i < sbSize; i++) {
            float sliceHue = i / (float) sbSize;
            int sliceColor = Color.HSBtoRGB(sliceHue, 1.0f, 1.0f);
            gui.fill(hueX, hueY + i, hueX + hueWidth, hueY + i + 1, sliceColor);
        }
        int hueThumbY = hueY + (int) (this.hue * sbSize);
        gui.fill(hueX - 2, hueThumbY - 2, hueX + hueWidth + 2, hueThumbY + 2, Colors.WHITE);

        // Render Alpha vertical slider tracking path
        int alphaX = hueX + hueWidth + 10;
        int alphaY = sbY;
        int alphaWidth = 10;
        int baseRgb = Color.HSBtoRGB(hue, saturation, brightness) & 0xFFFFFF;
        for (int i = 0; i < sbSize; i++) {
            float stepAlpha = 1.0f - (i / (float) sbSize);
            int argbStep = ((int) (stepAlpha * 255.0f) << 24) | baseRgb;
            gui.fill(alphaX, alphaY + i, alphaX + alphaWidth, alphaY + i + 1, argbStep);
        }
        int alphaThumbY = alphaY + (int) ((1.0f - this.alpha) * sbSize);
        gui.fill(alphaX - 2, alphaThumbY - 2, alphaX + alphaWidth + 2, alphaThumbY + 2, Colors.WHITE);

        // Build dynamic ARGB calculation sequences based on operational parameters
        int currentRgb = Color.HSBtoRGB(hue, saturation, brightness);
        int finalArgb = ((int) (alpha * 255.0f) << 24) | (currentRgb & 0xFFFFFF);
        this.targetOption.setPendingValue(finalArgb);

        // Color comparison swath layouts ("New Color" vs "Current Color")
        int metaX = alphaX + alphaWidth + 16;
        gui.text(font, LABEL_NEW, metaX, sbY, 0xFF9CA3AF, false);
        gui.fill(metaX, sbY + 12, metaX + 32, sbY + 36, finalArgb);

        gui.text(font, LABEL_CURRENT, metaX + 44, sbY, 0xFF9CA3AF, false);
        gui.fill(metaX + 44, sbY + 12, metaX + 76, sbY + 36, originalColor);

        // Digital numeric statistics display blocks
        int inputBlockY = sbY + 44;
        gui.text(font, LABEL_HEX, metaX, inputBlockY + 4, 0xFF6B7280, false);
        String hexStr = String.format(Locale.ROOT, "#%06X", (finalArgb & 0xFFFFFF));
        gui.text(font, ComponentCompat.literal(hexStr), metaX + 30, inputBlockY + 4, Colors.WHITE, false);

        // Display individual channel numerical strings matching mock components
        int r = (currentRgb >> 16) & 0xFF;
        int g = (currentRgb >> 8) & 0xFF;
        int b = currentRgb & 0xFF;
        String rgbMetrics = String.format(Locale.ROOT, "R %d   G %d   B %d", r, g, b);
        gui.text(font, ComponentCompat.literal(rgbMetrics), metaX, inputBlockY + 20, 0xFF9CA3AF, false);

        String hsvMetrics = String.format(Locale.ROOT, "H %d  S %d%%  V %d%%", (int) (hue * 360),
                (int) (saturation * 100), (int) (brightness * 100));
        gui.text(font, ComponentCompat.literal(hsvMetrics), metaX, inputBlockY + 34, 0xFF9CA3AF, false);

        // Draw Palette Structures (Presets & Recents)
        int paletteY = sbY + sbSize + 12;
        gui.text(font, PRESET_TEXT, x + 16, paletteY, 0xFF6B7280, false);
        renderColorRow(gui, x + 16, paletteY + 12, presetColors);

        int recentY = paletteY + 34;
        gui.text(font, RECENT_TEXT, x + 16, recentY, 0xFF6B7280, false);
        renderColorRow(gui, x + 16, recentY + 12, recentColors);
        // Optional trash icon placeholder string next to the recents header row boundary
        gui.text(font, ComponentCompat.literal("clear"), x + width - 50, recentY, 0xFF4B5563, false);

        // Base dialogue confirmation trigger targets
        int btnWidth = 55;
        int btnHeight = 18;
        int closeX = x + width - (btnWidth * 2) - 16;
        int okX = x + width - btnWidth - 12;
        int btnY = y + height - 26;

        gui.fill(closeX, btnY, closeX + btnWidth, btnY + btnHeight, 0x1FFFFFFF);
        gui.text(font, BTN_CANCEL, closeX + btnWidth / 2 + 1, btnY + btnHeight / 2 + 1, Colors.WHITE, false);

        gui.fill(okX, btnY, okX + btnWidth, btnY + btnHeight, 0xFF2563EB);
        gui.text(font, BTN_OK, okX + btnWidth / 2 + 1, btnY + btnHeight / 2 + 1, Colors.WHITE, false);
    }

    private void renderColorRow(GuiCompat gui, int startX, int startY, List<Integer> colors) {
        int itemSize = 12;
        int gap = 4;
        for (int i = 0; i < colors.size(); i++) {
            int cx = startX + i * (itemSize + gap);
            gui.fill(cx, startY, cx + itemSize, startY + itemSize, colors.get(i));
        }
    }

    private void updateDragStates(int mouseX, int mouseY) {
        int sbX = x + 16;
        int sbY = y + 36;
        int sbSize = 120;
        // int hueX = sbX + sbSize + 12;
        // int alphaX = hueX + 10 + 10;

        if (this.isDraggingSBSpace) {
            this.saturation = Mth.clamp((mouseX - sbX) / (float) sbSize, 0.0f, 1.0f);
            this.brightness = Mth.clamp(1.0f - ((mouseY - sbY) / (float) sbSize), 0.0f, 1.0f);
        }

        if (this.isDraggingHueSlider) {
            this.hue = Mth.clamp((mouseY - sbY) / (float) sbSize, 0.0f, 1.0f);
        }

        if (this.isDraggingAlphaSlider) {
            this.alpha = Mth.clamp(1.0f - ((mouseY - sbY) / (float) sbSize), 0.0f, 1.0f);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int sbX = x + 16;
        int sbY = y + 36;
        int sbSize = 120;
        int hueX = sbX + sbSize + 12;
        int alphaX = hueX + 20;

        // Interactive validation bounds over the main 2D grid matrix spectrum block
        if (mouseX >= sbX && mouseX < sbX + sbSize && mouseY >= sbY && mouseY < sbY + sbSize) {
            this.isDraggingSBSpace = true;
            SoundUtil.clickSound();
            return true;
        }

        // Interactive validation bounds over vertical hue spectrum column bars
        if (mouseX >= hueX && mouseX < hueX + 10 && mouseY >= sbY && mouseY < sbY + sbSize) {
            this.isDraggingHueSlider = true;
            SoundUtil.clickSound();
            return true;
        }

        // Interactive validation bounds over vertical alpha spectrum column bars
        if (mouseX >= alphaX && mouseX < alphaX + 10 && mouseY >= sbY && mouseY < sbY + sbSize) {
            this.isDraggingAlphaSlider = true;
            SoundUtil.clickSound();
            return true;
        }

        // Grid selection interceptions within Presets Row
        int paletteY = sbY + sbSize + 12;
        if (mouseY >= paletteY + 12 && mouseY < paletteY + 24) {
            if (checkAndApplyPaletteClick(mouseX, x + 16, presetColors))
                return true;
        }

        // Grid selection interceptions within Recents Row
        int recentY = paletteY + 34;
        if (mouseY >= recentY + 12 && mouseY < recentY + 24) {
            if (checkAndApplyPaletteClick(mouseX, x + 16, recentColors))
                return true;
        }

        // Confirmation operations closures targets bounding check
        int btnWidth = 55;
        int closeX = x + width - (btnWidth * 2) - 16;
        int okX = x + width - btnWidth - 12;
        int btnY = y + height - 26;

        if (mouseX >= okX && mouseX < okX + btnWidth && mouseY >= btnY && mouseY < btnY + 18) {
            int currentRgb = Color.HSBtoRGB(hue, saturation, brightness);
            int finalSelectedColor = ((int) (alpha * 255.0f) << 24) | (currentRgb & 0xFFFFFF);

            this.onConfirm.accept(finalSelectedColor);
            SoundUtil.clickSound();
            return true;
        }

        // Cancel target or upper 'X' close box window boundaries click action
        boolean isCloseBoxClicked = mouseX >= x + width - 28 && mouseX < x + width - 12 && mouseY >= y + 10
                && mouseY < y + 24;
        if ((mouseX >= closeX && mouseX < closeX + btnWidth && mouseY >= btnY && mouseY < btnY + 18)
                || isCloseBoxClicked) {
            // Rollback the backing parameters directly to cached state upon closures
            this.targetOption.setPendingValue(originalColor);
            this.onConfirm.accept(originalColor);
            SoundUtil.clickSound();
            return true;
        }

        return false;
    }

    private boolean checkAndApplyPaletteClick(double mouseX, int startX, List<Integer> colors) {
        int itemSize = 12;
        int gap = 4;
        for (int i = 0; i < colors.size(); i++) {
            int cx = startX + i * (itemSize + gap);
            if (mouseX >= cx && mouseX < cx + itemSize) {
                int selected = colors.get(i);
                int r = (selected >> 16) & 0xFF;
                int g = (selected >> 8) & 0xFF;
                int b = selected & 0xFF;
                int a = (selected >> 24) & 0xFF;

                this.alpha = a / 255.0f;
                float[] hsb = Color.RGBtoHSB(r, g, b, null);
                this.hue = hsb[0];
                this.saturation = hsb[1];
                this.brightness = hsb[2];

                SoundUtil.clickSound();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Resets operational tracking flags seamlessly to secure drag lock isolations
        this.isDraggingSBSpace = false;
        this.isDraggingHueSlider = false;
        this.isDraggingAlphaSlider = false;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean charTyped(int codePoint, int modifiers) {
        return false;
    }
}
