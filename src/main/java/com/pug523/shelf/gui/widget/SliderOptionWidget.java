package com.pug523.shelf.gui.widget;

import java.util.function.Function;

import com.pug523.shelf.config.Option;
import com.pug523.shelf.gui.Colors;
import com.pug523.shelf.gui.RenderUtil;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class SliderOptionWidget<N extends Number & Comparable<N>> extends OptionWidget<N> {
    // Dimensions & Layout
    private static final int SLIDER_WIDTH = 80;
    private static final int SLIDER_HEIGHT = 4;
    private static final int KNOB_SIZE = 8;
    private static final int PADDING_X = 25;
    private static final int TEXT_PADDING = 5;

    // Styling Colors
    private static final int COLOR_TRACK = Colors.THIN_WHITE2;
    private static final int COLOR_PROGRESS = Colors.INDIGO;
    private static final int COLOR_KNOB = Colors.WHITE;
    private static final int COLOR_TEXT = Colors.OFF_WHITE;

    private final double min;
    private final double max;
    private final double step;
    private final boolean round;
    private final Function<Double, N> typeConverter;

    private int cachedX, cachedY, cachedWidth, cachedHeight;
    private boolean isDragging = false;

    public SliderOptionWidget(Option<N> option, N min, N max, N step, boolean round, Function<Double, N> typeConverter) {
        super(option);
        this.min = min.doubleValue();
        this.max = max.doubleValue();
        this.step = step.doubleValue();
        this.round = round;
        this.typeConverter = typeConverter;
    }

    public static SliderOptionWidget<Integer> ofInt(Option<Integer> option, int min, int max, int step, boolean round) {
        return new SliderOptionWidget<Integer>(option, min, max, step, round, d -> (int) Math.round(d));
    }

    public static SliderOptionWidget<Double> ofDouble(Option<Double> option, double min, double max, double step, boolean round) {
        return new SliderOptionWidget<Double>(option, min, max, step, round, d -> d);
    }

    public static SliderOptionWidget<Float> ofFloat(Option<Float> option, float min, float max, float step, boolean round) {
        return new SliderOptionWidget<Float>(option, min, max, step, round, d -> (float) d.floatValue());
    }

    @Override
    public void render(Font font, GuiGraphicsExtractor gui, int x, int y, int width, int height, int mouseX, int mouseY) {
        this.cachedX = x;
        this.cachedY = y;
        this.cachedWidth = width;
        this.cachedHeight = height;

        int sliderX = x + width - SLIDER_WIDTH - PADDING_X;
        int sliderY = y + (height - SLIDER_HEIGHT) / 2;

        double currentValue = option.getPendingValue().doubleValue();
        double progress = Mth.clamp((currentValue - min) / (max - min), 0.0, 1.0);

        // Track
        gui.fill(sliderX, sliderY, sliderX + SLIDER_WIDTH, sliderY + SLIDER_HEIGHT, COLOR_TRACK);

        // Progress
        int fillEnd = sliderX + (int) (SLIDER_WIDTH * progress);
        gui.fill(sliderX, sliderY, fillEnd, sliderY + SLIDER_HEIGHT, COLOR_PROGRESS);

        if (round) {
            int centerX = fillEnd;
            int centerY = sliderY + (SLIDER_HEIGHT / 2);
            int radius = KNOB_SIZE / 2;
            RenderUtil.drawDynamicCircle(gui, centerX, centerY, radius, COLOR_KNOB);
        } else {
            int knobX = fillEnd - (KNOB_SIZE / 2);
            int knobY = sliderY + (SLIDER_HEIGHT / 2) - (KNOB_SIZE / 2);
            gui.fill(knobX, knobY, knobX + KNOB_SIZE, knobY + KNOB_SIZE, COLOR_KNOB);
        }

        // Text
        String valueText = formatValue(currentValue);
        Component textComponent = Component.literal(valueText);
        int textWidth = font.width(textComponent);
        int textX = sliderX - textWidth - TEXT_PADDING;
        int textY = y + (height - font.lineHeight) / 2;

        gui.text(font, textComponent, textX, textY, COLOR_TEXT, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int sliderX = cachedX + cachedWidth - SLIDER_WIDTH - PADDING_X;

            // Match the vertical box directly to the container bounds
            if (mouseX >= sliderX && mouseX <= sliderX + SLIDER_WIDTH && mouseY >= cachedY && mouseY <= cachedY + cachedHeight) {
                updateValueFromMouse(mouseX, sliderX, SLIDER_WIDTH);
                this.isDragging = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && this.isDragging) {
            int sliderX = cachedX + cachedWidth - SLIDER_WIDTH - PADDING_X;
            updateValueFromMouse(mouseX, sliderX, SLIDER_WIDTH);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isDragging = false;
        return true;
    }

    private void updateValueFromMouse(double mouseX, int sliderX, int sliderWidth) {
        double pct = (mouseX - sliderX) / (double) sliderWidth;
        pct = Mth.clamp(pct, 0.0, 1.0);

        double rawValue = min + (max - min) * pct;

        if (step > 0.0) {
            rawValue = Math.round(rawValue / step) * step;
        }

        rawValue = Mth.clamp(rawValue, min, max);

        N finalValue = typeConverter.apply(rawValue);
        option.setPendingValue(finalValue);
    }

    private String formatValue(double value) {
        if (step >= 1.0) {
            return String.valueOf((int) value);
        } else {
            return String.format("%.2f", value);
        }
    }
}
