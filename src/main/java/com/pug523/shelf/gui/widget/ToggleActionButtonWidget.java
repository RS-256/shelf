package com.pug523.shelf.gui.widget;

import com.pug523.shelf.compat.ComponentCompat;
import com.pug523.shelf.compat.GuiCompat;
import com.pug523.shelf.config.Option;
import com.pug523.shelf.gui.layout.LayoutConfig;
import com.pug523.shelf.gui.layout.LayoutEngine;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class ToggleActionButtonWidget extends OptionWidget<Boolean> {
    private static final Component COMPONENT_TRUE = ComponentCompat.literal("true").withStyle(ChatFormatting.GREEN);
    private static final Component COMPONENT_FALSE = ComponentCompat.literal("false").withStyle(ChatFormatting.RED);

    private final ActionButtonWidget buttonDelegate;

    private int cachedBtnX;
    private int cachedBtnY;
    private int cachedBtnWidth;
    private int cachedBtnHeight;
    private boolean isBoundsCached = false;

    public ToggleActionButtonWidget(Option<Boolean> option) {
        super(option);
        this.buttonDelegate = new ActionButtonWidget(COMPONENT_FALSE, btn -> this.toggle());
    }

    @Override
    public void render(Font font, GuiCompat gui, LayoutEngine layout, int x, int y, int width, int height, int mouseX,
            int mouseY, int scissorX, int scissorY, int scissorMaxX, int scissorMaxY) {
        LayoutConfig cfg = layout.getConfig();

        this.cachedBtnWidth = cfg.toggleButtonWidth;
        this.cachedBtnHeight = cfg.toggleButtonHeight;
        this.cachedBtnX = x + width - this.cachedBtnWidth - cfg.toggleButtonRightPadding;
        this.cachedBtnY = y + (height - this.cachedBtnHeight) / 2;
        this.isBoundsCached = true;

        boolean pendingVal = option.getPendingValue().booleanValue();
        this.buttonDelegate.setLabel(pendingVal ? COMPONENT_TRUE : COMPONENT_FALSE);

        this.buttonDelegate.render(font, gui, layout, this.cachedBtnX, this.cachedBtnY, this.cachedBtnWidth,
                this.cachedBtnHeight, mouseX, mouseY, scissorX, scissorY, scissorMaxX, scissorMaxY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers) {
        if (!this.isBoundsCached) {
            return false;
        }

        return this.buttonDelegate.mouseClicked(mouseX, mouseY, button, modifiers);
    }

    private void toggle() {
        option.setPendingValue(!option.getPendingValue());
    }
}
