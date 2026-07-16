package com.pug523.shelf.gui;

import java.util.List;

import com.pug523.shelf.gui.widget.option.OptionWidget;

import net.minecraft.network.chat.Component;

public class OptionGroup {
    private final Component name;
    private final List<OptionWidget<?>> optionWidgets;

    public OptionGroup(Component name, List<OptionWidget<?>> optionWidgets) {
        this.name = name;
        this.optionWidgets = optionWidgets;
    }

    public Component getName() {
        return name;
    }

    public List<OptionWidget<?>> getOptionWidgets() {
        return optionWidgets;
    }
}
