package com.pug523.shelf.config;

import net.minecraft.network.chat.Component;

public class Profile {
    private final Component name;

    public Profile(Component name) {
        this.name = name;
    }

    public Component getName() {
        return name;
    }
}
