package com.pug523.shelf.gui.widget;

import com.pug523.shelf.compat.GuiCompat;
import com.pug523.shelf.config.Option;
import com.pug523.shelf.gui.input.InputUtil;
import com.pug523.shelf.gui.layout.LayoutConfig;
import com.pug523.shelf.gui.layout.LayoutEngine;
import com.pug523.shelf.gui.renderer.RenderUtil;
import com.pug523.shelf.gui.sound.SoundUtil;

import net.minecraft.client.gui.Font;

public class ToggleCapsuleWidget extends OptionWidget<Boolean> {
    private LayoutConfig cachedConfig;
    // TODO: move round to layout config as `roundedToggleCapsule`
    private final boolean round;
    private int cachedX, cachedY, cachedWidth, cachedHeight;

    public ToggleCapsuleWidget(Option<Boolean> option, boolean round) {
        super(option);
        this.round = round;
    }

    @Override
    public void render(Font font, GuiCompat gui, LayoutEngine layout, int x, int y, int width, int height, int mouseX,
            int mouseY, int scissorX, int scissorY, int scissorMaxX, int scissorMaxY) {
        this.cachedX = x;
        this.cachedY = y;
        this.cachedWidth = width;
        this.cachedHeight = height;
        this.cachedConfig = layout.getConfig();

        LayoutConfig cfg = layout.getConfig();

        int switchX = getSwitchX(cfg);
        int switchY = getSwitchY(cfg);

        boolean val = option.getPendingValue().booleanValue();
        boolean isHovered = isGenerouslyHovered(mouseX, mouseY, cfg);

        int bgBoxColor;
        if (val) {
            bgBoxColor = isHovered ? cfg.colorToggleBgOnHover : cfg.colorToggleBgOn;
        } else {
            bgBoxColor = isHovered ? cfg.colorToggleBgOffHover : cfg.colorToggleBgOff;
        }

        if (round) {
            RenderUtil.drawCapsule(gui, switchX, switchY, cfg.capsuleToggleWidth, cfg.capsuleToggleHeight, bgBoxColor,
                    scissorX, scissorY, scissorMaxX, scissorMaxY);
        } else {
            gui.fill(switchX, switchY, switchX + cfg.capsuleToggleWidth, switchY + cfg.capsuleToggleHeight, bgBoxColor);
        }

        int knobHeight = cfg.capsuleToggleHeight - 6;
        int knobWidth = knobHeight;
        int knobY = switchY + (cfg.capsuleToggleHeight - knobHeight) / 2;
        int paddingX = (cfg.capsuleToggleHeight - knobHeight) / 2;
        int knobX = val ? (switchX + cfg.capsuleToggleWidth - knobWidth - paddingX) : (switchX + paddingX);

        if (round) {
            int radius = knobWidth / 2;
            int centerX = knobX + radius;
            int centerY = knobY + radius;
            RenderUtil.drawCircle(gui, centerX, centerY, radius, cfg.colorToggleKnob, scissorX, scissorY, scissorMaxX,
                    scissorMaxY);
        } else {
            gui.fill(knobX, knobY, knobX + knobWidth, knobY + knobHeight, cfg.colorToggleKnob);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers) {
        if (button == InputUtil.LEFT_MOUSE_BUTTON && cachedConfig != null) {
            if (isGenerouslyHovered(mouseX, mouseY, cachedConfig)) {
                toggle();
                SoundUtil.clickSound();
                return true;
            }
        }
        return false;
    }

    private void toggle() {
        option.setPendingValue(!option.getPendingValue());
    }

    private boolean isGenerouslyHovered(double mouseX, double mouseY, LayoutConfig cfg) {
        int sx = getSwitchX(cfg);
        int sy = getSwitchY(cfg);
        return mouseX >= sx - cfg.capsuleToggleHitboxPadding
                && mouseX <= sx + cfg.capsuleToggleWidth + cfg.capsuleToggleHitboxPadding
                && mouseY >= sy - cfg.capsuleToggleHitboxPadding
                && mouseY <= sy + cfg.capsuleToggleHeight + cfg.capsuleToggleHitboxPadding;
    }

    private int getSwitchX(LayoutConfig cfg) {
        return cachedX + cachedWidth - cfg.capsuleToggleWidth - cfg.capsuleTogglePaddingRight;
    }

    private int getSwitchY(LayoutConfig cfg) {
        return cachedY + (cachedHeight - cfg.capsuleToggleHeight) / 2;
    }
}
