package com.pug523.shelf.gui;

public class ScreenLayout {
    // private final int width;
    private final int height;
    private final LayoutConfig config;

    // Derived layout boundaries
    public final int tabAreaWidth;
    public final int optionAreaWidth;
    public final int descAreaX;
    public final int mainContentHeight;

    public ScreenLayout(int width, int height, LayoutConfig config) {
        // this.width = width;
        this.height = height;
        this.config = config;

        this.tabAreaWidth = (int) (width * config.tabAreaWidthPercent);
        this.optionAreaWidth = (int) (width * config.optionAreaWidthPercent);
        this.descAreaX = this.tabAreaWidth + this.optionAreaWidth;
        this.mainContentHeight = height - config.topBarHeight - config.bottomBarHeight;
    }

    public boolean isWithinContentArea(double mouseY) {
        return mouseY > config.topBarHeight && mouseY < (height - config.bottomBarHeight);
    }

    public boolean isMouseOverTabs(double mouseX) {
        return mouseX < tabAreaWidth;
    }

    public boolean isMouseOverOptions(double mouseX) {
        return mouseX >= tabAreaWidth && mouseX < descAreaX;
    }
}
