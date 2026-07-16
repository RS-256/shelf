package com.pug523.shelf.gui.widget.overlay;

import com.pug523.shelf.gui.widget.ClickableWidget;

public interface OverlayWidget extends ClickableWidget {
    /// Determines if the background screen should be dimmed down with a dark overlay.
    default boolean shouldDimBackground() {
        return false;
    }

    /// Triggered automatically when the overlay is dismissed or replaced.
    default void onClose() {
    }
}
