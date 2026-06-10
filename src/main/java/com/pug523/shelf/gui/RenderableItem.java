package com.pug523.shelf.gui;

import com.pug523.shelf.gui.widget.OptionWidget;

import net.minecraft.network.chat.Component;

public record RenderableItem(Component text, OptionWidget<?> widget, boolean isHeader) {
    public static RenderableItem createHeader(Component text) {
        return new RenderableItem(text, null, true);
    }

    public static RenderableItem createOption(OptionWidget<?> widget) {
        return new RenderableItem(widget.getOption().getName(), widget, false);
    }
}
