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
    private int cachedX, cachedY, cachedWidth, cachedHeight;

    public ToggleCapsuleWidget(Option<Boolean> option) {
        super(option);
    }

    @Override
    public void render(Font font, GuiCompat gui, LayoutEngine layout, int x, int y, int width, int height, int mouseX, int mouseY) {
        this.cachedX = x;
        this.cachedY = y;
        this.cachedWidth = width;
        this.cachedHeight = height;
        LayoutConfig cfg = layout.getConfig();

        float switchX = getSwitchX(layout);
        float switchY = getSwitchY(layout);

        boolean val = option.getPendingValue();
        boolean isHovered = isGenerouslyHovered(mouseX, mouseY, layout);

        int bgBoxColor;
        if (val) {
            bgBoxColor = isHovered ? cfg.colorToggleBgOnHover : cfg.colorToggleBgOn;
        } else {
            bgBoxColor = isHovered ? cfg.colorToggleBgOffHover : cfg.colorToggleBgOff;
        }

        if (cfg.roundedCapsule) {
            RenderUtil.renderCapsule(gui, switchX, switchY, cfg.capsuleToggleWidth, cfg.capsuleToggleHeight, bgBoxColor);
        } else {
            gui.fill((int) switchX, (int) switchY, (int) (switchX + cfg.capsuleToggleWidth), (int) (switchY + cfg.capsuleToggleHeight), bgBoxColor);
        }

        float knobDiameter = cfg.capsuleToggleHeight * 1.0f;
        float knobY = switchY + (cfg.capsuleToggleHeight - knobDiameter) / 2.0f;
        float paddingX = (cfg.capsuleToggleHeight - knobDiameter) / 2.0f;
        float knobX = val ? (switchX + cfg.capsuleToggleWidth - knobDiameter - paddingX) : (switchX + paddingX);

        if (cfg.roundedCapsule) {
            float radius = knobDiameter / 2.0f;
            float centerX = knobX + radius;
            float centerY = knobY + radius;
            RenderUtil.renderCircle(gui, centerX, centerY, radius, cfg.colorToggleKnob);
        } else {
            int maxX = (int) (knobX + knobDiameter);
            int maxY = (int) (knobY + knobDiameter);
            gui.fill((int) knobX, (int) knobY, maxX, maxY, cfg.colorToggleKnob);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, LayoutEngine layout) {
        if (button == InputUtil.LEFT_MOUSE_BUTTON) {
            if (isGenerouslyHovered(mouseX, mouseY, layout)) {
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

    private boolean isGenerouslyHovered(double mouseX, double mouseY, LayoutEngine layout) {
        float sx = getSwitchX(layout);
        float sy = getSwitchY(layout);
        LayoutConfig cfg = layout.getConfig();
        return mouseX >= sx - cfg.capsuleToggleHitboxPadding && mouseX <= sx + cfg.capsuleToggleWidth + cfg.capsuleToggleHitboxPadding && mouseY >= sy - cfg.capsuleToggleHitboxPadding && mouseY <= sy + cfg.capsuleToggleHeight + cfg.capsuleToggleHitboxPadding;
    }

    private float getSwitchX(LayoutEngine layout) {
        return cachedX + cachedWidth - layout.getConfig().capsuleToggleWidth - layout.optionWidgetRightMargin;
    }

    private float getSwitchY(LayoutEngine layout) {
        return cachedY + (cachedHeight - layout.getConfig().capsuleToggleHeight) / 2.0f;
    }
}
