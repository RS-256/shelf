package com.pug523.shelf.gui;

public class LayoutConfig {
    // Structural layout defaults
    public final int topBarHeight;
    public final int bottomBarHeight;
    public final double tabAreaWidthPercent;
    public final double optionAreaWidthPercent;
    public final double tabScrollSpeed;
    public final double optionScrollSpeed;

    // Sizing constants
    public final int tabItemHeight;
    public final int optionItemHeight;
    public final int tabTreeIndentation;

    // Color tokens
    public final int colorScreenBaseBackground;
    public final int colorHeaderBackground;
    public final int colorFooterBackground;
    public final int colorTabPanelBackground;
    public final int colorOptionPanelBackground;
    public final int colorDescriptionPanelBackground;
    public final int colorTextPrimary;
    public final int colorTextSecondary;
    public final int colorTextMuted;
    public final int colorTextDisabled;
    public final int colorItemSelectedText;
    public final int colorItemUnselectedText;
    public final int colorItemHoverBackground;
    public final int colorItemSelectedBackground;
    public final int colorScrollBarTrack;
    public final int colorScrollBarThumb;

    // Default layout
    public LayoutConfig() {
        this.topBarHeight = 30;
        this.bottomBarHeight = 30;
        this.tabAreaWidthPercent = 0.20;
        this.optionAreaWidthPercent = 0.55;
        this.tabScrollSpeed = 15.0;
        this.optionScrollSpeed = 20.0;

        this.tabItemHeight = 22;
        this.optionItemHeight = 22;
        this.tabTreeIndentation = 10;

        this.colorScreenBaseBackground = 0xA0181818;
        this.colorHeaderBackground = 0x15FFFFFF;
        this.colorFooterBackground = 0x22000000;
        this.colorTabPanelBackground = 0x33000000;
        this.colorOptionPanelBackground = 0x44000000;
        this.colorDescriptionPanelBackground = 0x55000000;
        this.colorTextPrimary = 0xFFFFFFFF;
        this.colorTextSecondary = 0xFFB0B0B2;
        this.colorTextMuted = 0xFFDDDDDD;
        this.colorTextDisabled = 0x88DDDDDD;
        this.colorItemSelectedText = 0xFFF5F5F5;
        this.colorItemUnselectedText = 0x99DDDDDD;
        this.colorItemHoverBackground = 0x22FFFFFF;
        this.colorItemSelectedBackground = 0x45FFFFFF;
        this.colorScrollBarTrack = 0x88000000;
        this.colorScrollBarThumb = 0xFFDDDDDD;
    }

    public LayoutConfig(
            int topBarHeight,
            int bottomBarHeight,
            double tabAreaWidthPercent,
            double optionAreaWidthPercent,
            double tabScrollSpeed,
            double optionScrollSpeed,
            int tabItemHeight,
            int optionItemHeight,
            int tabTreeIndentation,
            int colorScreenBaseBackground,
            int colorHeaderBackground,
            int colorFooterBackground,
            int colorTabPanelBackground,
            int colorOptionPanelBackground,
            int colorDescriptionPanelBackground,
            int colorTextPrimary,
            int colorTextSecondary,
            int colorTextMuted,
            int colorTextDisabled,
            int colorItemSelectedText,
            int colorItemUnselectedText,
            int colorItemHoverBackground,
            int colorItemSelectedBackground,
            int colorScrollBarTrack,
            int colorScrollBarThumb) {
        this.topBarHeight = topBarHeight;
        this.bottomBarHeight = bottomBarHeight;
        this.tabAreaWidthPercent = tabAreaWidthPercent;
        this.optionAreaWidthPercent = optionAreaWidthPercent;
        this.tabScrollSpeed = tabScrollSpeed;
        this.optionScrollSpeed = optionScrollSpeed;
        this.tabItemHeight = tabItemHeight;
        this.optionItemHeight = optionItemHeight;
        this.tabTreeIndentation = tabTreeIndentation;
        this.colorScreenBaseBackground = colorScreenBaseBackground;
        this.colorHeaderBackground = colorHeaderBackground;
        this.colorFooterBackground = colorFooterBackground;
        this.colorTabPanelBackground = colorTabPanelBackground;
        this.colorOptionPanelBackground = colorOptionPanelBackground;
        this.colorDescriptionPanelBackground = colorDescriptionPanelBackground;
        this.colorTextPrimary = colorTextPrimary;
        this.colorTextSecondary = colorTextSecondary;
        this.colorTextMuted = colorTextMuted;
        this.colorTextDisabled = colorTextDisabled;
        this.colorItemSelectedText = colorItemSelectedText;
        this.colorItemUnselectedText = colorItemUnselectedText;
        this.colorItemHoverBackground = colorItemHoverBackground;
        this.colorItemSelectedBackground = colorItemSelectedBackground;
        this.colorScrollBarTrack = colorScrollBarTrack;
        this.colorScrollBarThumb = colorScrollBarThumb;
    }
}
