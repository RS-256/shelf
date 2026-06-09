package com.pug523.shelf.gui;

import net.minecraft.network.chat.Component;

import java.util.List;

public class OptionGroup {
    private final Component name;
    private final List<Option<?>> options;

    public OptionGroup(Component name, List<Option<?>> options) {
        this.name = name;
        this.options = options;
    }

    public Component getName() {
        return name;
    }

    public List<Option<?>> getOptions() {
        return options;
    }
}
