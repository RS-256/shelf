package com.pug523.shelf.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface OptionWidget {
    void render(Font font, GuiGraphicsExtractor gui, int x, int y, int width, int height, int mouseX, int mouseY);

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @FunctionalInterface
    interface Memento {
        void restore();
    }

    // Captures a snapshot that can restore this widget's exact current state.
    default Memento captureSnapshot() {
        // Default to no-op
        return () -> {};
    }
}

