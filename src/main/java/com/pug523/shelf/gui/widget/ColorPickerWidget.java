package com.pug523.shelf.gui.widget;

import java.util.Locale;

import com.pug523.shelf.compat.ComponentCompat;
import com.pug523.shelf.compat.GuiCompat;
import com.pug523.shelf.compat.ScreenCompat;
import com.pug523.shelf.config.Option;
import com.pug523.shelf.gui.ConfigScreen;
import com.pug523.shelf.gui.layout.Bounds;
import com.pug523.shelf.gui.layout.LayoutEngine;
import com.pug523.shelf.gui.overlay.ColorPickerOverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;

public class ColorPickerWidget extends OptionWidget<Integer> {
    // TODO: move them to LayoutConfig
    private final int SQUARE_SIZE = 14;
    private final int TEXT_WIDTH = 55;
    private Bounds cachedSquareBounds;

    public ColorPickerWidget(Option<Integer> option) {
        super(option);
    }

    @Override
    public void render(Font font, GuiCompat gui, LayoutEngine layout, int x, int y, int width, int height, int mouseX, int mouseY) {
        int argb = this.option.getPendingValue();

        int textX = x + width - layout.optionWidgetRightMargin - TEXT_WIDTH;
        int textY = y + (height - font.lineHeight) / 2 + 1;
        int squareX = textX - SQUARE_SIZE - layout.getConfig().colorPickerSquareRightPadding;
        int squareY = y + (height - SQUARE_SIZE) / 2;
        this.cachedSquareBounds = new Bounds(squareX, squareY, SQUARE_SIZE, SQUARE_SIZE);

        int borderColor = cachedSquareBounds.contains(mouseX, mouseY) ? 0xFFFFFFFF : 0x33FFFFFF;

        gui.fill(squareX, squareY, squareX + SQUARE_SIZE, squareY + SQUARE_SIZE, borderColor);
        gui.fill(squareX + 1, squareY + 1, squareX + SQUARE_SIZE - 1, squareY + SQUARE_SIZE - 1, argb);

        String hexString = String.format(Locale.ROOT, "#%08X", argb);
        gui.text(font, ComponentCompat.literal(hexString), textX, textY, 0xFF9CA3AF, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, LayoutEngine layout) {
        Screen screen = ScreenCompat.getScreen(Minecraft.getInstance());
        if (screen instanceof ConfigScreen && cachedSquareBounds != null && cachedSquareBounds.contains(mouseX, mouseY)) {
            ConfigScreen configScreen = (ConfigScreen) screen;
            ColorPickerOverlay pickerOverlay = new ColorPickerOverlay(this.option, finalColor -> {
                configScreen.getOverlayController().closeActive();
                if (option.isPendingModifiedFromActual()) {
                    configScreen.getChangeController().markDirty();
                }
            });
            configScreen.getOverlayController().open(pickerOverlay);
        }
        return false;
    }
}
