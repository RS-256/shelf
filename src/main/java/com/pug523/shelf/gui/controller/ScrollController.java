package com.pug523.shelf.gui.controller;

import com.pug523.shelf.compat.JavaCompat;

public final class ScrollController {
    // TODO: description scroll

    private double tabScroll;
    private double optionScroll;

    public double getTabScroll() {
        return tabScroll;
    }

    public double getOptionScroll() {
        return optionScroll;
    }

    public void scrollTabs(double amount, int contentHeight, int viewportHeight) {
        tabScroll = JavaCompat.clamp(tabScroll + amount, 0.0d, Math.max(0.0d, contentHeight - viewportHeight));
    }

    public void scrollOptions(double amount, int contentHeight, int viewportHeight) {
        optionScroll = JavaCompat.clamp(optionScroll + amount, 0.0d, Math.max(0.0d, contentHeight - viewportHeight));
    }

    public void setTabScroll(double value, int contentHeight, int viewportHeight) {
        tabScroll = JavaCompat.clamp(value, 0.0d, Math.max(0.0d, contentHeight - viewportHeight));
    }

    public void setOptionScroll(double value, int contentHeight, int viewportHeight) {
        optionScroll = JavaCompat.clamp(value, 0.0d, Math.max(0.0d, contentHeight - viewportHeight));
    }

    public void reset() {
        this.tabScroll = 0;
        this.optionScroll = 0;
    }
}
