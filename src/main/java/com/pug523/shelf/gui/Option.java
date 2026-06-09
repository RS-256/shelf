package com.pug523.shelf.gui;

import net.minecraft.network.chat.Component;

import java.util.List;

import com.pug523.shelf.gui.widget.OptionWidget;

public class Option<T> {
    private final Component name;
    private final String descriptionKey;
    private final OptionWidget widget;
    private final List<Tag> tags;


    public Option(Component name, String descriptionKey, OptionWidget widget, List<Tag> tags) {
        this.name = name;
        this.descriptionKey = descriptionKey;
        this.widget = widget;
		this.tags = tags;
    }

    public Option(Component name, String descriptionKey, OptionWidget widget) {
        this(name, descriptionKey, widget, List.of());
    }

    public Option(String nameKey, OptionWidget widget, List<Tag> tags) {
        this.name = Component.translatable(nameKey);
        this.descriptionKey = nameKey + ".desc";
        this.widget = widget;
		this.tags = tags;
    }

    public Option(String nameKey, OptionWidget widget) {
        this(nameKey, widget, List.of());
    }

    public Component getName() {
        return name;
    }

    public Component getDescription() {
        return Component.translatable(this.descriptionKey);
    }

    public OptionWidget getWidget() {
        return widget;
    }

    public List<Tag> tags() {
        return tags;
    }
}
