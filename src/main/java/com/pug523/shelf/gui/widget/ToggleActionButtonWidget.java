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

    public ToggleActionButtonWidget(Option<Boolean> option) {
        super(option);
        this.buttonDelegate = new ActionButtonWidget(COMPONENT_FALSE, btn -> this.toggle());
    }

    @Override
    public void render(Font font, GuiCompat gui, LayoutEngine layout, int x, int y, int width, int height, int mouseX, int mouseY) {
        LayoutConfig cfg = layout.getConfig();

        boolean pendingVal = option.getPendingValue();
        this.buttonDelegate.setLabel(pendingVal ? COMPONENT_TRUE : COMPONENT_FALSE);

        int btnX = x + width - cfg.toggleButtonWidth - layout.optionWidgetRightMargin;
        int btnY = y + (height - cfg.toggleButtonHeight) / 2;
        this.buttonDelegate.render(font, gui, layout, btnX, btnY, cfg.toggleButtonWidth, cfg.toggleButtonHeight, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, LayoutEngine layout) {
        return this.buttonDelegate.mouseClicked(mouseX, mouseY, button, modifiers, layout);
    }

    private void toggle() {
        option.setPendingValue(!option.getPendingValue());
    }
}
