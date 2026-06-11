package com.pug523.shelf;

import static com.pug523.shelf.ShelfTextUtil.categoryText;
import static com.pug523.shelf.ShelfTextUtil.confText;
import static com.pug523.shelf.ShelfTextUtil.optKey;
import static com.pug523.shelf.compat.JavaCompat.listOf;

import java.util.ArrayList;
import java.util.List;

import com.pug523.shelf.config.Option;
import com.pug523.shelf.config.Profile;
import com.pug523.shelf.gui.ConfigScreen;
import com.pug523.shelf.gui.OptionGroup;
import com.pug523.shelf.gui.TabNode;
import com.pug523.shelf.gui.layout.LayoutConfig;
import com.pug523.shelf.gui.widget.BooleanOptionWidget;
import com.pug523.shelf.gui.widget.SliderOptionWidget;

import net.minecraft.client.gui.screens.Screen;

public class ShelfConfigScreen {
    public static ConfigScreen createConfigScreen(Screen parent) {
        ShelfConfig config = Shelf.CONFIG.getConfig();
        ShelfConfig defaultConfig = ShelfConfig.createDefault();

        List<TabNode> roots = new ArrayList<>();

        TabNode masterRootNode = new TabNode(categoryText("all_settings"));

        // @formatter:off
        TabNode generalNode = new TabNode(categoryText("general"))
            .addGroup(new OptionGroup(categoryText("core"), listOf(
                new BooleanOptionWidget(new Option<>(optKey("general.auto_restock"), defaultConfig.autoRestock, () -> config.autoRestock, (v) -> config.autoRestock = v, listOf()), false),
                SliderOptionWidget.ofFloat(new Option<>(optKey("general.sensitivity"), defaultConfig.sensitivity, () -> config.sensitivity, (v) -> config.sensitivity = v, listOf()), 0.1f, 2.0f, 0.1f, false)
            )));

        TabNode layoutNode = layoutNode(config.layoutConfig);

        TabNode videoNode = new TabNode(categoryText("video"));

        TabNode displayNode = new TabNode(categoryText("display"))
            .addGroup(new OptionGroup(categoryText("screen"), listOf(
                SliderOptionWidget.ofInt(new Option<>(optKey("display.screen.fov"), defaultConfig.fov, () -> config.fov, (v) -> config.fov = v, listOf()), 30, 110, 1, false)
            )));

        TabNode performanceNode = new TabNode(categoryText("performance"))
            .addGroup(new OptionGroup(categoryText("graphics"), listOf(
                SliderOptionWidget.ofInt(new Option<>(optKey("performance.graphics.render_distance"), defaultConfig.renderDistance, () -> config.renderDistance, (v) -> config.renderDistance = v, listOf()), 2, 32, 1, true)
            )));

        TabNode advancedVideoTweaks = new TabNode(categoryText("advanced_tweaks"))
            .addGroup(new OptionGroup(categoryText("experimental_shaders"), listOf(
                new BooleanOptionWidget(new Option<>(optKey("advanced_tweaks.experimental_shaders.vsync"), defaultConfig.vsync, () -> config.vsync, (v) -> config.vsync = v, listOf()), true)
            )));
        // @formatter:on

        // Link them up recursively.
        performanceNode.addNode(advancedVideoTweaks);
        videoNode.addNode(displayNode);
        videoNode.addNode(performanceNode);

        masterRootNode.addNode(generalNode);
        masterRootNode.addNode(layoutNode);
        masterRootNode.addNode(videoNode);

        roots.add(masterRootNode);

        List<Profile> profiles = new ArrayList<>();
        return new ConfigScreen(confText("title"), parent, roots, profiles, Shelf.CONFIG::save, config.layoutConfig);
    }

    private static TabNode layoutNode(LayoutConfig c) {
        LayoutConfig d = LayoutConfig.createDefault();
        // @formatter:off
        return new TabNode(categoryText("layout_settings"))
            .addGroup(new OptionGroup(categoryText("layout.dimensions"), listOf(
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.top_bar"), d.topBarHeight, () -> c.topBarHeight, (v) -> c.topBarHeight = v, listOf()), 10, 60, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.bottom_bar"), d.bottomBarHeight, () -> c.bottomBarHeight, (v) -> c.bottomBarHeight = v, listOf()), 10, 60, 1, false),
                SliderOptionWidget.ofFloat(new Option<>(optKey("layout.tab_width_pct"), (float)d.tabAreaWidthPercent, () -> (float)c.tabAreaWidthPercent, (v) -> c.tabAreaWidthPercent = v, listOf()), 0.1f, 0.4f, 0.01f, false),
                SliderOptionWidget.ofFloat(new Option<>(optKey("layout.option_width_pct"), (float)d.optionAreaWidthPercent, () -> (float)c.optionAreaWidthPercent, (v) -> c.optionAreaWidthPercent = v, listOf()), 0.3f, 0.7f, 0.01f, false),
                SliderOptionWidget.ofFloat(new Option<>(optKey("layout.tab_scroll_speed"), (float)d.tabScrollSpeed, () -> (float)c.tabScrollSpeed, (v) -> c.tabScrollSpeed = v, listOf()), 1.0f, 50.0f, 0.5f, false),
                SliderOptionWidget.ofFloat(new Option<>(optKey("layout.option_scroll_speed"), (float)d.optionScrollSpeed, () -> (float)c.optionScrollSpeed, (v) -> c.optionScrollSpeed = v, listOf()), 1.0f, 50.0f, 0.5f, false)
            )))
            .addGroup(new OptionGroup(categoryText("layout.sizes"), listOf(
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.tab_height"), d.tabItemHeight, () -> c.tabItemHeight, (v) -> c.tabItemHeight = v, listOf()), 14, 40, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.option_height"), d.optionItemHeight, () -> c.optionItemHeight, (v) -> c.optionItemHeight = v, listOf()), 14, 40, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.indent"), d.tabTreeIndentation, () -> c.tabTreeIndentation, (v) -> c.tabTreeIndentation = v, listOf()), 0, 25, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.tab_start_offset_y"), d.tabItemStartOffsetY, () -> c.tabItemStartOffsetY, (v) -> c.tabItemStartOffsetY = v, listOf()), 0, 100, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.option_start_offset_y"), d.optionItemStartOffsetY, () -> c.optionItemStartOffsetY, (v) -> c.optionItemStartOffsetY = v, listOf()), 0, 100, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.text_padding_x"), d.textPaddingX, () -> c.textPaddingX, (v) -> c.textPaddingX = v, listOf()), 0, 30, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.reset_btn_width"), d.resetButtonWidth, () -> c.resetButtonWidth, (v) -> c.resetButtonWidth = v, listOf()), 10, 40, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.scrollbar_width"), d.scrollbarWidth, () -> c.scrollbarWidth, (v) -> c.scrollbarWidth = v, listOf()), 1, 10, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.scrollbar_min_height"), d.scrollbarMinHeight, () -> c.scrollbarMinHeight, (v) -> c.scrollbarMinHeight = v, listOf()), 5, 50, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.option_text_offset_x"), d.optionTextOffsetX, () -> c.optionTextOffsetX, (v) -> c.optionTextOffsetX = v, listOf()), 0, 40, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.option_header_offset_x"), d.optionHeaderOffsetX, () -> c.optionHeaderOffsetX, (v) -> c.optionHeaderOffsetX = v, listOf()), 0, 40, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.desc_text_offset_x"), d.descTextOffsetX, () -> c.descTextOffsetX, (v) -> c.descTextOffsetX = v, listOf()), 0, 40, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.desc_text_offset_y"), d.descTextOffsetY, () -> c.descTextOffsetY, (v) -> c.descTextOffsetY = v, listOf()), 0, 40, 1, false),
                SliderOptionWidget.ofInt(new Option<>(optKey("layout.desc_text_right_padding"), d.descTextRightPadding, () -> c.descTextRightPadding, (v) -> c.descTextRightPadding = v, listOf()), 0, 50, 1, false)
            )))
            .addGroup(new OptionGroup(categoryText("layout.colors.panels"), listOf(
                // TODO: ColorPickerWidget
                // new ColorPickerWidget(new Option<>(optKey("layout.color.screen_bg"), d.colorScreenBaseBackground, () -> c.colorScreenBaseBackground, (v) -> c.colorScreenBaseBackground = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.header_bg"), d.colorHeaderBackground, () -> c.colorHeaderBackground, (v) -> c.colorHeaderBackground = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.footer_bg"), d.colorFooterBackground, () -> c.colorFooterBackground, (v) -> c.colorFooterBackground = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.tab_panel_bg"), d.colorTabPanelBackground, () -> c.colorTabPanelBackground, (v) -> c.colorTabPanelBackground = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.option_panel_bg"), d.colorOptionPanelBackground, () -> c.colorOptionPanelBackground, (v) -> c.colorOptionPanelBackground = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.desc_panel_bg"), d.colorDescriptionPanelBackground, () -> c.colorDescriptionPanelBackground, (v) -> c.colorDescriptionPanelBackground = v, listOf()))
            )))
            .addGroup(new OptionGroup(categoryText("layout.colors.elements"), listOf(
                // TODO: ColorPickerWidget
                // new ColorPickerWidget(new Option<>(optKey("layout.color.text_primary"), d.colorTextPrimary, () -> c.colorTextPrimary, (v) -> c.colorTextPrimary = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.text_secondary"), d.colorTextSecondary, () -> c.colorTextSecondary, (v) -> c.colorTextSecondary = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.text_muted"), d.colorTextMuted, () -> c.colorTextMuted, (v) -> c.colorTextMuted = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.text_disabled"), d.colorTextDisabled, () -> c.colorTextDisabled, (v) -> c.colorTextDisabled = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.item_selected_text"), d.colorItemSelectedText, () -> c.colorItemSelectedText, (v) -> c.colorItemSelectedText = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.item_unselected_text"), d.colorItemUnselectedText, () -> c.colorItemUnselectedText, (v) -> c.colorItemUnselectedText = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.item_hover_bg"), d.colorItemHoverBackground, () -> c.colorItemHoverBackground, (v) -> c.colorItemHoverBackground = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.item_selected_bg"), d.colorItemSelectedBackground, () -> c.colorItemSelectedBackground, (v) -> c.colorItemSelectedBackground = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.scrollbar_track"), d.colorScrollBarTrack, () -> c.colorScrollBarTrack, (v) -> c.colorScrollBarTrack = v, listOf())),
                // new ColorPickerWidget(new Option<>(optKey("layout.color.scrollbar_thumb"), d.colorScrollBarThumb, () -> c.colorScrollBarThumb, (v) -> c.colorScrollBarThumb = v, listOf()))
                // TODO: Add action buttons color config here.
            )));
        // @formatter:on
    }
}
