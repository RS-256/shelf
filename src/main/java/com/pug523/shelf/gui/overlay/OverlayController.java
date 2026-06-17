package com.pug523.shelf.gui.overlay;

import org.jspecify.annotations.Nullable;

public class OverlayController {
    private @Nullable ScreenOverlay activeOverlay = null;
    private int cachedWidth;
    private int cachedHeight;

    public void open(@Nullable ScreenOverlay overlay) {
        if (this.activeOverlay != null) {
            this.activeOverlay.onClose();
        }
        this.activeOverlay = overlay;
        if (overlay != null) {
            overlay.init(this.cachedWidth, this.cachedHeight);
        }
    }

    public void closeActive() {
        this.open(null);
    }

    public boolean hasActiveOverlay() {
        return this.activeOverlay != null;
    }

    public @Nullable ScreenOverlay getActiveOverlay() {
        return this.activeOverlay;
    }

    public void updateDimensions(int width, int height) {
        this.cachedWidth = width;
        this.cachedHeight = height;
        if (this.activeOverlay != null) {
            this.activeOverlay.init(width, height);
        }
    }
}
