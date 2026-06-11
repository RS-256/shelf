package com.pug523.shelf.gui.controller;

import com.pug523.shelf.compat.JavaCompat;
import com.pug523.shelf.gui.model.OptionContext;

public final class OptionContextController {

    private OptionContext context = new OptionContext(JavaCompat.listOf());

    public OptionContext getContext() {
        return context;
    }

    public void setContext(OptionContext context) {
        this.context = context;
    }
}
