package com.pug523.shelf.gui.widget;

import com.pug523.shelf.config.Option;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class OptionWidget<T> {
    protected final Option<T> option;

    public OptionWidget(Option<T> option) {
        this.option = option;
    }

    public abstract void render(Font font, GuiGraphicsExtractor gui, int x, int y, int width, int height, int mouseX, int mouseY);

    public Option<T> getOption() {
        return option;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }
}
