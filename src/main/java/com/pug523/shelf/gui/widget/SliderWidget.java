package com.pug523.shelf.gui.widget;

import java.util.function.Function;

import com.pug523.shelf.compat.ComponentCompat;
import com.pug523.shelf.compat.GuiCompat;
import com.pug523.shelf.config.Option;
import com.pug523.shelf.gui.input.InputUtil;
import com.pug523.shelf.gui.layout.Bounds;
import com.pug523.shelf.gui.layout.LayoutConfig;
import com.pug523.shelf.gui.layout.LayoutEngine;
import com.pug523.shelf.gui.renderer.RenderUtil;
import com.pug523.shelf.gui.text.TextUtil;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class SliderWidget<N extends Number & Comparable<N>> extends OptionWidget<N> {
    private final double min;
    private final double max;
    private final double step;
    private final Function<Double, N> typeConverter;

    private Bounds cachedWidgetBounds;
    private LayoutEngine cachedEngine;
    private boolean isDragging = false;

    public SliderWidget(Option<N> option, N min, N max, N step, Function<Double, N> typeConverter) {
        super(option);
        this.min = min.doubleValue();
        this.max = max.doubleValue();
        this.step = step.doubleValue();
        this.typeConverter = typeConverter;
    }

    public static SliderWidget<Integer> ofInt(Option<Integer> option, int min, int max, int step) {
        return new SliderWidget<>(option, min, max, step, d -> (int) Math.round(d));
    }

    public static SliderWidget<Double> ofDouble(Option<Double> option, double min, double max, double step) {
        return new SliderWidget<>(option, min, max, step, d -> d);
    }

    public static SliderWidget<Float> ofFloat(Option<Float> option, float min, float max, float step) {
        return new SliderWidget<>(option, min, max, step, Double::floatValue);
    }

    @Override
    public void render(Font font, GuiCompat gui, LayoutEngine layout, int x, int y, int width, int height, int mouseX, int mouseY) {
        this.cachedWidgetBounds = new Bounds(x, y, width, height);
        this.cachedEngine = layout;

        LayoutConfig cfg = layout.getConfig();

        int sliderX = x + width - cfg.sliderWidth - layout.optionWidgetRightMargin;
        int sliderY = y + (height - cfg.sliderHeight) / 2;

        double currentValue = option.getPendingValue().doubleValue();
        double progress = Mth.clamp((currentValue - min) / (max - min), 0.0, 1.0);

        // Track
        gui.fill(sliderX, sliderY, sliderX + cfg.sliderWidth, sliderY + cfg.sliderHeight, cfg.colorSliderTrack);

        // Progress
        int fillEnd = sliderX + (int) (cfg.sliderWidth * progress);
        gui.fill(sliderX, sliderY, fillEnd, sliderY + cfg.sliderHeight, cfg.colorSliderProgress);

        if (cfg.roundedSlider) {
            RenderUtil.renderCircle(gui, fillEnd, sliderY + (cfg.sliderHeight / 2.0f), cfg.sliderKnobSize / 2.25f, cfg.colorSliderKnob);
        } else {
            int knobX = fillEnd - (cfg.sliderKnobSize / 2);
            int knobY = sliderY + (cfg.sliderHeight / 2) - (cfg.sliderKnobSize / 2);
            gui.fill(knobX, knobY, knobX + cfg.sliderKnobSize, knobY + cfg.sliderKnobSize, cfg.colorSliderKnob);
        }

        // Text
        Component textComponent = ComponentCompat.literal(formatValue(currentValue));
        int textX = sliderX - TextUtil.width(font, textComponent) - cfg.sliderTextPadding;
        int textY = y + (height - font.lineHeight) / 2 + 1;

        gui.text(font, textComponent, textX, textY, cfg.colorSliderText, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, LayoutEngine layout) {
        if (button == InputUtil.LEFT_MOUSE_BUTTON && cachedWidgetBounds != null) {
            LayoutConfig cfg = cachedEngine.getConfig();
            int sliderX = cachedWidgetBounds.x + cachedWidgetBounds.width - cfg.sliderWidth - layout.optionWidgetRightMargin;

            if (mouseX >= sliderX && mouseX <= sliderX + cfg.sliderWidth && mouseY >= cachedWidgetBounds.y && mouseY <= cachedWidgetBounds.maxY) {
                updateValueFromMouse(mouseX);
                this.isDragging = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY, LayoutEngine layout) {
        if (button == 0 && this.isDragging && cachedWidgetBounds != null) {
            updateValueFromMouse(mouseX);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button, LayoutEngine layout) {
        this.isDragging = false;
        return true;
    }

    private void updateValueFromMouse(double mouseX) {
        double pct = cachedEngine.getSliderProgressFromMouse(mouseX, cachedWidgetBounds);
        double rawValue = min + (max - min) * pct;
        if (step > 0.0) {
            rawValue = Math.round(rawValue / step) * step;
        }
        rawValue = Mth.clamp(rawValue, min, max);

        option.setPendingValue(typeConverter.apply(rawValue));
    }

    private String formatValue(double value) {
        return step >= 1.0 ? String.valueOf((int) value) : String.format("%.2f", value);
    }
}
