package com.pug523.shelf.gui.overlay;

import com.pug523.shelf.compat.GuiCompat;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface ScreenOverlay {
    // Called when the overlay is formally attached to a screen environment.
    void init(int screenWidth, int screenHeight);

    // Layout calculations and custom framework rendering passes.
    void render(Font font, GuiCompat gui, int mouseX, int mouseY, float partialTicks);

    // Standard vanilla graphics state processing.
    default void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
    }

    // Input capture management (Return true to intercept and block underlying layers)
    boolean mouseClicked(double mouseX, double mouseY, int button);

    boolean mouseReleased(double mouseX, double mouseY, int button);

    boolean keyPressed(int keycode, int scancode, int modifiers);

    boolean charTyped(int codepoint, int modifiers);

    // Determines if the background screen should be dimmed down with a dark overlay.
    default boolean shouldDimBackground() {
        return true;
    }

    // Triggered automatically when the overlay is dismissed or replaced.
    default void onClose() {
    }
}
