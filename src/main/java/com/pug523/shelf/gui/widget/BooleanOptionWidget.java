package com.pug523.shelf.gui.widget;

import com.pug523.shelf.config.Option;
import com.pug523.shelf.gui.Colors;
import com.pug523.shelf.gui.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class BooleanOptionWidget extends OptionWidget {
    public static final int COLOR_TOGGLE_BG_OFF = Colors.MIDDLE_GRAY;
    public static final int COLOR_TOGGLE_BG_ON = Colors.GREEN2;
    public static final int COLOR_KNOB = Colors.WHITE;
    public static final int COLOR_TOGGLE_BG_OFF_HOVER = Colors.MIDDLE_GRAY2;
    public static final int COLOR_TOGGLE_BG_ON_HOVER  = Colors.GREEN3;

    // Dimensions & Layout
    private static final int SWITCH_WIDTH = 30;
    private static final int SWITCH_HEIGHT = 14;
    private static final int PADDING_RIGHT = 25;
    private static final int HITBOX_PADDING = 4;

    private final boolean round;

    private int cachedX, cachedY, cachedWidth, cachedHeight;

    public BooleanOptionWidget(Option<Boolean> option, boolean round) {
        this.round = round;
        super(option);
    }

    public static OptionWidget<Boolean> of(Option<Boolean> option, boolean round) {
        return new BooleanOptionWidget(option, round);
    }

    @Override
    public void render(Font font, GuiGraphicsExtractor gui, int x, int y, int width, int height, int mouseX, int mouseY) {
        this.cachedX = x;
        this.cachedY = y;
        this.cachedWidth = width;
        this.cachedHeight = height;

        int switchX = x + width - SWITCH_WIDTH - PADDING_RIGHT;
        int switchY = y + (height - SWITCH_HEIGHT) / 2;

        boolean val = ((Option<Boolean>)option).getPendingValue().booleanValue();
        boolean isHovered = mouseX >= switchX - HITBOX_PADDING && mouseX <= switchX + SWITCH_WIDTH + HITBOX_PADDING &&
                            mouseY >= switchY - HITBOX_PADDING && mouseY <= switchY + SWITCH_HEIGHT + HITBOX_PADDING;

        int bgBoxColor;
        if (val) {
            bgBoxColor = isHovered ? COLOR_TOGGLE_BG_ON_HOVER : COLOR_TOGGLE_BG_ON;
        } else {
            bgBoxColor = isHovered ? COLOR_TOGGLE_BG_OFF_HOVER : COLOR_TOGGLE_BG_OFF;
        }

        if (round) {
            RenderUtil.drawDynamicCapsule(gui, switchX, switchY, SWITCH_WIDTH, SWITCH_HEIGHT, bgBoxColor);
        } else {
            gui.fill(switchX, switchY, switchX + SWITCH_WIDTH, switchY + SWITCH_HEIGHT, bgBoxColor);
        }

        int knobHeight = SWITCH_HEIGHT - 6;
        int knobWidth = knobHeight;
        int knobY = switchY + (SWITCH_HEIGHT - knobHeight) / 2;
        int paddingX = (SWITCH_HEIGHT - knobHeight) / 2;
        int knobX = val ? (switchX + SWITCH_WIDTH - knobWidth - paddingX) : (switchX + paddingX);

        if (round) {
            int radius = knobWidth / 2;
            int centerX = knobX + radius;
            int centerY = knobY + radius;
            RenderUtil.drawDynamicCircle(gui, centerX, centerY, radius, COLOR_KNOB);
        } else {
            gui.fill(knobX, knobY, knobX + knobWidth, knobY + knobHeight, COLOR_KNOB);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Left click
        if (button == 0) {
            int switchX = cachedX + cachedWidth - SWITCH_WIDTH - PADDING_RIGHT;
            int switchY = cachedY + (cachedHeight - SWITCH_HEIGHT) / 2;

            // Generous click box for small elements.
            if (mouseX >= switchX - HITBOX_PADDING && mouseX <= switchX + SWITCH_WIDTH + HITBOX_PADDING &&
                mouseY >= switchY - HITBOX_PADDING && mouseY <= switchY + SWITCH_HEIGHT + HITBOX_PADDING) {

                // Play vanilla UI click sound.
                Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                );

                toggle();
                return true;
            }
        }
        return false;
    }

    private void toggle() {
        option.setPendingValue(!((Option<Boolean>)option).getPendingValue());
    }
}
