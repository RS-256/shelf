package com.pug523.shelf.gui.widget;

import java.util.Locale;

import com.pug523.shelf.compat.ComponentCompat;
import com.pug523.shelf.compat.GuiCompat;
import com.pug523.shelf.config.Option;
import com.pug523.shelf.gui.ConfigScreen;
import com.pug523.shelf.gui.layout.LayoutEngine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class ColorPickerWidget extends OptionWidget<Integer> {
    private final int squareSize = 14;
    private final int textWidth = 55;

    private int textX;
    private int textY;
    private int squareX;
    private int squareY;

    public ColorPickerWidget(Option<Integer> option) {
        super(option);
    }

    @Override
    public void render(Font font, GuiCompat gui, LayoutEngine layout, int x, int y, int width, int height, int mouseX,
            int mouseY, int scissorX, int scissorY, int scissorMaxX, int scissorMaxY) {

        int argb = this.option.getPendingValue();

        // Align metrics to the right side of the option row definition
        this.textX = x + width - textWidth - 10;
        this.textY = y + (height - font.lineHeight) / 2 + 1;

        this.squareX = textX - squareSize - 8;
        this.squareY = y + (height - squareSize) / 2;

        // Render external boundary highlights based on cursor interaction states
        int borderColor = isSquareHovered(mouseX, mouseY) ? 0xFFFFFFFF : 0x33FFFFFF;

        gui.fill(squareX, squareY, squareX + squareSize, squareY + squareSize, borderColor);

        // Render actual inner color value swatches securely
        // Separating the internal fill to display alpha accurately over flat rows
        gui.fill(squareX + 1, squareY + 1, squareX + squareSize - 1, squareY + squareSize - 1, argb);

        // Render raw descriptive HEX string configurations (e.g., #2563EB)
        String hexString = String.format(Locale.ROOT, "#%06X", (argb & 0xFFFFFF));
        gui.text(font, ComponentCompat.literal(hexString), textX, textY, 0xFF9CA3AF, false);
    }

    private boolean isSquareHovered(double mouseX, double mouseY) {
        return mouseX >= squareX && mouseX < squareX + squareSize && mouseY >= squareY && mouseY < squareY + squareSize;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof ConfigScreen && isSquareHovered(mouseX, mouseY)) {
            ConfigScreen configScreen = (ConfigScreen) mc.screen;
            ColorPickerOverlay pickerOverlay = new ColorPickerOverlay(this.option, finalColor -> {
                configScreen.getOverlayController().closeActive();
            });

            configScreen.getOverlayController().open(pickerOverlay);
            return true;
        }

        return false;
    }
}
