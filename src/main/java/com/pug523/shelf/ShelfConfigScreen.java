package com.pug523.shelf;

import static com.pug523.shelf.ShelfTextUtil.categoryText;
import static com.pug523.shelf.ShelfTextUtil.confText;
import static com.pug523.shelf.ShelfTextUtil.optKey;
import static com.pug523.shelf.compat.JavaCompat.listOf;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.InputConstants;
import com.pug523.shelf.compat.ComponentCompat;
import com.pug523.shelf.config.Option;
import com.pug523.shelf.config.Profile;
import com.pug523.shelf.gui.ConfigScreen;
import com.pug523.shelf.gui.OptionGroup;
import com.pug523.shelf.gui.TabNode;
import com.pug523.shelf.gui.layout.LayoutConfig;
import com.pug523.shelf.gui.widget.*;

import net.minecraft.client.gui.screens.Screen;

public class ShelfConfigScreen {
    public static String test = "";
    private static TestEnum testEnum = TestEnum.AnEnumMember;
    private static List<InputConstants.Key> testKey = new ArrayList<>();

    enum TestEnum {
        AnEnumMember,
        AnotherEnumMember,
        AnotherEnumMember2,
        AnotherEnumMember3,
    }

    public static ConfigScreen createConfigScreen(Screen parent) {
        ShelfConfig config = Shelf.CONFIG.getConfig();
        ShelfConfig defaultConfig = ShelfConfig.createDefault();

        List<TabNode> roots = new ArrayList<>();

        TabNode masterRootNode = new TabNode(categoryText("all_settings"));

        // @formatter:off
        TabNode generalNode = new TabNode(categoryText("general"))
            .addGroup(new OptionGroup(categoryText("core"), listOf(
                new CyclingWidget<>(new Option<>(optKey("general.test_enum"), testEnum, () -> testEnum, v -> testEnum = v, listOf())),
                CyclingWidget.of(new Option<>(optKey("general.test_enum"), testEnum, () -> testEnum, v -> testEnum = v, listOf()), TestEnum.class, (e) -> ComponentCompat.literal(e.name())),
                new KeybindWidget(new Option<>(optKey("general.auto_eat"), testKey, () -> testKey, v -> testKey = v, listOf())),
                new ToggleBoxWidget(new Option<>(optKey("general.auto_eat"), defaultConfig.autoEat, () -> config.autoEat, v -> config.autoEat = v, listOf())),
                new ToggleActionButtonWidget(new Option<>(optKey("general.auto_eat"), defaultConfig.autoEat, () -> config.autoEat, v -> config.autoEat = v, listOf())),
                new ToggleCapsuleWidget(new Option<>(optKey("general.auto_restock"), defaultConfig.autoRestock, () -> config.autoRestock, v -> config.autoRestock = v, listOf())),
                new StringInputFieldWidget(new Option<>(optKey("general.restock_item"), "", () -> test, v -> test = v, listOf()), text -> true, text -> {}),
                new ItemInputFieldWidget(new Option<>(optKey("general.restock_item"), defaultConfig.restockItem, () -> config.restockItem, v -> config.restockItem = v, listOf()), null),
                SliderWidget.ofFloat(new Option<>(optKey("general.sensitivity"), defaultConfig.sensitivity, () -> config.sensitivity, v -> config.sensitivity = v, listOf()), 0.1f, 2.0f, 0.1f)
            )));

        TabNode layoutNode = layoutNode(config.layoutConfig);

        TabNode videoNode = new TabNode(categoryText("video"));

        TabNode displayNode = new TabNode(categoryText("display"))
            .addGroup(new OptionGroup(categoryText("screen"), listOf(
                SliderWidget.ofInt(new Option<>(optKey("display.screen.fov"), defaultConfig.fov, () -> config.fov, v -> config.fov = v, listOf()), 30, 110, 1)
            )));

        TabNode performanceNode = new TabNode(categoryText("performance"))
            .addGroup(new OptionGroup(categoryText("graphics"), listOf(
                SliderWidget.ofInt(new Option<>(optKey("performance.graphics.render_distance"), defaultConfig.renderDistance, () -> config.renderDistance, v -> config.renderDistance = v, listOf()), 2, 32, 1)
            )));

        TabNode advancedVideoTweaks = new TabNode(categoryText("advanced_tweaks"))
            .addGroup(new OptionGroup(categoryText("experimental_shaders"), listOf(
                new ToggleCapsuleWidget(new Option<>(optKey("advanced_tweaks.experimental_shaders.vsync"), defaultConfig.vsync, () -> config.vsync, v -> config.vsync = v, listOf()))
            )))
            .addGroup(new OptionGroup(categoryText("debug"), listOf(
                new ToggleBoxWidget(new Option<>(optKey("advanced_tweaks.debug.render_debug_circle"), defaultConfig.renderDebugCircle, () -> config.renderDebugCircle, v -> config.renderDebugCircle = v, listOf()))
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
                SliderWidget.ofInt(new Option<>(optKey("layout.top_bar"), d.topBarHeight, () -> c.topBarHeight, v -> c.topBarHeight = v, listOf()), 10, 60, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.bottom_bar"), d.bottomBarHeight, () -> c.bottomBarHeight, v -> c.bottomBarHeight = v, listOf()), 10, 60, 1),
                SliderWidget.ofFloat(new Option<>(optKey("layout.tab_width_pct"), (float)d.tabAreaWidthPercent, () -> (float)c.tabAreaWidthPercent, v -> c.tabAreaWidthPercent = v, listOf()), 0.1f, 0.4f, 0.01f),
                SliderWidget.ofFloat(new Option<>(optKey("layout.option_width_pct"), (float)d.optionAreaWidthPercent, () -> (float)c.optionAreaWidthPercent, v -> c.optionAreaWidthPercent = v, listOf()), 0.3f, 0.7f, 0.01f),
                SliderWidget.ofFloat(new Option<>(optKey("layout.tab_scroll_speed"), (float)d.tabScrollSpeed, () -> (float)c.tabScrollSpeed, v -> c.tabScrollSpeed = v, listOf()), 1.0f, 50.0f, 0.5f),
                SliderWidget.ofFloat(new Option<>(optKey("layout.option_scroll_speed"), (float)d.optionScrollSpeed, () -> (float)c.optionScrollSpeed, v -> c.optionScrollSpeed = v, listOf()), 1.0f, 50.0f, 0.5f)
            )))
            .addGroup(new OptionGroup(categoryText("layout.sizes"), listOf(
                SliderWidget.ofInt(new Option<>(optKey("layout.tab_height"), d.tabItemHeight, () -> c.tabItemHeight, v -> c.tabItemHeight = v, listOf()), 14, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.option_height"), d.optionItemHeight, () -> c.optionItemHeight, v -> c.optionItemHeight = v, listOf()), 14, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.indent"), d.tabTreeIndentation, () -> c.tabTreeIndentation, v -> c.tabTreeIndentation = v, listOf()), 0, 25, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.tab_start_offset_y"), d.tabItemStartOffsetY, () -> c.tabItemStartOffsetY, v -> c.tabItemStartOffsetY = v, listOf()), 0, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.option_start_offset_y"), d.optionItemStartOffsetY, () -> c.optionItemStartOffsetY, v -> c.optionItemStartOffsetY = v, listOf()), 0, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.text_padding_x"), d.textPaddingX, () -> c.textPaddingX, v -> c.textPaddingX = v, listOf()), 0, 30, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.reset_btn_width"), d.resetButtonWidth, () -> c.resetButtonWidth, v -> c.resetButtonWidth = v, listOf()), 10, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.reset_btn_height"), d.resetButtonHeight, () -> c.resetButtonHeight, v -> c.resetButtonHeight = v, listOf()), 5, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.reset_btn_padding_y"), d.resetButtonPaddingY, () -> c.resetButtonPaddingY, v -> c.resetButtonPaddingY = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.reset_btn_right_margin"), d.rightMarginFromResetButton, () -> c.rightMarginFromResetButton, v -> c.rightMarginFromResetButton = v, listOf()), 0, 30, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.scrollbar_width"), d.scrollbarWidth, () -> c.scrollbarWidth, v -> c.scrollbarWidth = v, listOf()), 1, 10, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.scrollbar_min_height"), d.scrollbarMinHeight, () -> c.scrollbarMinHeight, v -> c.scrollbarMinHeight = v, listOf()), 5, 50, 1),
                SliderWidget.ofFloat(new Option<>(optKey("layout.scrollbar_max_height_pct"), (float)d.scrollbarMaxHeightPercent, () -> (float)c.scrollbarMaxHeightPercent, v -> c.scrollbarMaxHeightPercent = v, listOf()), 0.05f, 1.0f, 0.01f),
                SliderWidget.ofInt(new Option<>(optKey("layout.option_text_offset_x"), d.optionTextOffsetX, () -> c.optionTextOffsetX, v -> c.optionTextOffsetX = v, listOf()), 0, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.option_header_offset_x"), d.optionHeaderOffsetX, () -> c.optionHeaderOffsetX, v -> c.optionHeaderOffsetX = v, listOf()), 0, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.option_header_offset_y"), d.optionHeaderOffsetY, () -> c.optionHeaderOffsetY, v -> c.optionHeaderOffsetY = v, listOf()), 0, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.desc_text_offset_x"), d.descTextOffsetX, () -> c.descTextOffsetX, v -> c.descTextOffsetX = v, listOf()), 0, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.desc_text_offset_y"), d.descTextOffsetY, () -> c.descTextOffsetY, v -> c.descTextOffsetY = v, listOf()), 0, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.desc_text_right_padding"), d.descTextRightPadding, () -> c.descTextRightPadding, v -> c.descTextRightPadding = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.footer_btn_width"), d.footerButtonWidth, () -> c.footerButtonWidth, v -> c.footerButtonWidth = v, listOf()), 20, 150, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.footer_btn_height"), d.footerButtonHeight, () -> c.footerButtonHeight, v -> c.footerButtonHeight = v, listOf()), 5, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.footer_padding_right"), d.footerPaddingRight, () -> c.footerPaddingRight, v -> c.footerPaddingRight = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.footer_btn_spacing"), d.footerButtonSpacing, () -> c.footerButtonSpacing, v -> c.footerButtonSpacing = v, listOf()), 0, 30, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.reset_icon_size"), d.resetIconSize, () -> c.resetIconSize, v -> c.resetIconSize = v, listOf()), 4, 30, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.tab_arrow_offset_x"), d.tabArrowOffsetX, () -> c.tabArrowOffsetX, v -> c.tabArrowOffsetX = v, listOf()), -10, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.tab_arrow_offset_y"), d.tabArrowOffsetY, () -> c.tabArrowOffsetY, v -> c.tabArrowOffsetY = v, listOf()), -10, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.tab_text_offset_x"), d.tabTextOffsetX, () -> c.tabTextOffsetX, v -> c.tabTextOffsetX = v, listOf()), 0, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.desc_title_spacing_y"), d.descTitleSpacingY, () -> c.descTitleSpacingY, v -> c.descTitleSpacingY = v, listOf()), 0, 40, 1)
            )))
            .addGroup(new OptionGroup(categoryText("layout.widgets"), listOf(
                SliderWidget.ofInt(new Option<>(optKey("layout.capsule_toggle_width"), d.capsuleToggleWidth, () -> c.capsuleToggleWidth, v -> c.capsuleToggleWidth = v, listOf()), 10, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.capsule_toggle_height"), d.capsuleToggleHeight, () -> c.capsuleToggleHeight, v -> c.capsuleToggleHeight = v, listOf()), 5, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.capsule_toggle_hitbox_padding"), d.capsuleToggleHitboxPadding, () -> c.capsuleToggleHitboxPadding, v -> c.capsuleToggleHitboxPadding = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.toggle_button_width"), d.toggleButtonWidth, () -> c.toggleButtonWidth, v -> c.toggleButtonWidth = v, listOf()), 20, 150, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.toggle_button_height"), d.toggleButtonHeight, () -> c.toggleButtonHeight, v -> c.toggleButtonHeight = v, listOf()), 5, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.box_toggle_width"), d.boxToggleWidth, () -> c.boxToggleWidth, v -> c.boxToggleWidth = v, listOf()), 4, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.box_toggle_height"), d.boxToggleHeight, () -> c.boxToggleHeight, v -> c.boxToggleHeight = v, listOf()), 4, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.box_toggle_outline"), d.boxToggleOutlineThickness, () -> c.boxToggleOutlineThickness, v -> c.boxToggleOutlineThickness = v, listOf()), 1, 5, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.box_toggle_padding"), d.boxToggleInnerPadding, () -> c.boxToggleInnerPadding, v -> c.boxToggleInnerPadding = v, listOf()), 0, 10, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.slider_width"), d.sliderWidth, () -> c.sliderWidth, v -> c.sliderWidth = v, listOf()), 30, 200, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.slider_height"), d.sliderHeight, () -> c.sliderHeight, v -> c.sliderHeight = v, listOf()), 1, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.slider_knob_size"), d.sliderKnobSize, () -> c.sliderKnobSize, v -> c.sliderKnobSize = v, listOf()), 2, 30, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.slider_text_padding"), d.sliderTextPadding, () -> c.sliderTextPadding, v -> c.sliderTextPadding = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.color_picker_padding"), d.colorPickerSquareRightPadding, () -> c.colorPickerSquareRightPadding, v -> c.colorPickerSquareRightPadding = v, listOf()), 0, 20, 1)
            )))
            .addGroup(new OptionGroup(categoryText("layout.popups"), listOf(
                SliderWidget.ofFloat(new Option<>(optKey("layout.picker.width_pct"), (float)d.pickerDialogWidthPercent, () -> (float)c.pickerDialogWidthPercent, v -> c.pickerDialogWidthPercent = v, listOf()), 0.1f, 0.9f, 0.01f),
                SliderWidget.ofFloat(new Option<>(optKey("layout.picker.height_pct"), (float)d.pickerDialogHeightPercent, () -> (float)c.pickerDialogHeightPercent, v -> c.pickerDialogHeightPercent = v, listOf()), 0.1f, 0.9f, 0.01f),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.min_width"), d.pickerDialogMinWidth, () -> c.pickerDialogMinWidth, v -> c.pickerDialogMinWidth = v, listOf()), 100, 600, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.min_height"), d.pickerDialogMinHeight, () -> c.pickerDialogMinHeight, v -> c.pickerDialogMinHeight = v, listOf()), 100, 500, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.max_width"), d.pickerDialogMaxWidth, () -> c.pickerDialogMaxWidth, v -> c.pickerDialogMaxWidth = v, listOf()), 200, 1000, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.max_height"), d.pickerDialogMaxHeight, () -> c.pickerDialogMaxHeight, v -> c.pickerDialogMaxHeight = v, listOf()), 200, 800, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.padding_inner"), d.pickerPaddingInner, () -> c.pickerPaddingInner, v -> c.pickerPaddingInner = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.slider_width"), d.pickerSliderWidth, () -> c.pickerSliderWidth, v -> c.pickerSliderWidth = v, listOf()), 1, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.slider_spacing"), d.pickerSliderSpacing, () -> c.pickerSliderSpacing, v -> c.pickerSliderSpacing = v, listOf()), 0, 30, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.rgb_slider_height"), d.pickerRgbSliderHeight, () -> c.pickerRgbSliderHeight, v -> c.pickerRgbSliderHeight = v, listOf()), 1, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.swat_width"), d.pickerSwatWidth, () -> c.pickerSwatWidth, v -> c.pickerSwatWidth = v, listOf()), 5, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.swat_height"), d.pickerSwatHeight, () -> c.pickerSwatHeight, v -> c.pickerSwatHeight = v, listOf()), 5, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.palette_box_size"), d.pickerPaletteBoxSize, () -> c.pickerPaletteBoxSize, v -> c.pickerPaletteBoxSize = v, listOf()), 4, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.palette_box_spacing"), d.pickerPaletteBoxSpacing, () -> c.pickerPaletteBoxSpacing, v -> c.pickerPaletteBoxSpacing = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.max_palette_colors"), d.pickerMaxPaletteColors, () -> c.pickerMaxPaletteColors, v -> c.pickerMaxPaletteColors = v, listOf()), 1, 30, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.title_offset_y"), d.pickerTitleOffsetY, () -> c.pickerTitleOffsetY, v -> c.pickerTitleOffsetY = v, listOf()), 0, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.swat_offset_y"), d.pickerSwatOffsetY, () -> c.pickerSwatOffsetY, v -> c.pickerSwatOffsetY = v, listOf()), 0, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.swat_spacing_x"), d.pickerSwatSpacingX, () -> c.pickerSwatSpacingX, v -> c.pickerSwatSpacingX = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.palette_label_offset_y"), d.pickerPaletteLabelOffsetY, () -> c.pickerPaletteLabelOffsetY, v -> c.pickerPaletteLabelOffsetY = v, listOf()), 0, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.recent_label_offset_y"), d.pickerRecentLabelOffsetY, () -> c.pickerRecentLabelOffsetY, v -> c.pickerRecentLabelOffsetY = v, listOf()), 0, 100, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.btn_width"), d.pickerButtonWidth, () -> c.pickerButtonWidth, v -> c.pickerButtonWidth = v, listOf()), 20, 150, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.btn_height"), d.pickerButtonHeight, () -> c.pickerButtonHeight, v -> c.pickerButtonHeight = v, listOf()), 5, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.x_btn_padding"), d.pickerXBtnPadding, () -> c.pickerXBtnPadding, v -> c.pickerXBtnPadding = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.metrics_offset_y"), d.pickerMetricsOffsetY, () -> c.pickerMetricsOffsetY, v -> c.pickerMetricsOffsetY = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.metrics_spacing_y"), d.pickerMetricsSpacingY, () -> c.pickerMetricsSpacingY, v -> c.pickerMetricsSpacingY = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.palette_text_spacing_y"), d.pickerPaletteTextSpacingY, () -> c.pickerPaletteTextSpacingY, v -> c.pickerPaletteTextSpacingY = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.clear_btn_padding_x"), d.pickerClearBtnPaddingX, () -> c.pickerClearBtnPaddingX, v -> c.pickerClearBtnPaddingX = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.clear_btn_padding_y"), d.pickerClearBtnPaddingY, () -> c.pickerClearBtnPaddingY, v -> c.pickerClearBtnPaddingY = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.slider_indicator_size"), d.pickerSliderIndicatorSize, () -> c.pickerSliderIndicatorSize, v -> c.pickerSliderIndicatorSize = v, listOf()), 1, 10, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.sb_indicator_size"), d.pickerSbSpaceIndicatorSize, () -> c.pickerSbSpaceIndicatorSize, v -> c.pickerSbSpaceIndicatorSize = v, listOf()), 1, 10, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.mode_toggle_width"), d.pickerModeToggleWidth, () -> c.pickerModeToggleWidth, v -> c.pickerModeToggleWidth = v, listOf()), 20, 150, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.picker.mode_toggle_height"), d.pickerModeToggleHeight, () -> c.pickerModeToggleHeight, v -> c.pickerModeToggleHeight = v, listOf()), 5, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.confirm.width"), d.confirmDialogWidth, () -> c.confirmDialogWidth, v -> c.confirmDialogWidth = v, listOf()), 100, 500, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.confirm.height"), d.confirmDialogHeight, () -> c.confirmDialogHeight, v -> c.confirmDialogHeight = v, listOf()), 50, 300, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.confirm.padding_inner"), d.confirmPaddingInner, () -> c.confirmPaddingInner, v -> c.confirmPaddingInner = v, listOf()), 0, 50, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.confirm.btn_width"), d.confirmButtonWidth, () -> c.confirmButtonWidth, v -> c.confirmButtonWidth = v, listOf()), 20, 150, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.confirm.btn_height"), d.confirmButtonHeight, () -> c.confirmButtonHeight, v -> c.confirmButtonHeight = v, listOf()), 5, 40, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.confirm.btn_spacing"), d.confirmButtonSpacing, () -> c.confirmButtonSpacing, v -> c.confirmButtonSpacing = v, listOf()), 0, 50, 1)
            )))
            .addGroup(new OptionGroup(categoryText("layout.visual_styles"), listOf(
                new ToggleBoxWidget(new Option<>(optKey("layout.style.rounded_capsule"), d.roundedCapsule, () -> c.roundedCapsule, v -> c.roundedCapsule = v, listOf())),
                new ToggleBoxWidget(new Option<>(optKey("layout.style.rounded_slider"), d.roundedSlider, () -> c.roundedSlider, v -> c.roundedSlider = v, listOf())),
                new ToggleBoxWidget(new Option<>(optKey("layout.style.action_btn_shadow"), d.actionButtonShadow, () -> c.actionButtonShadow, v -> c.actionButtonShadow = v, listOf())),
                new ToggleBoxWidget(new Option<>(optKey("layout.style.toggle_btn_shadow"), d.toggleButtonShadow, () -> c.toggleButtonShadow, v -> c.toggleButtonShadow = v, listOf()))
            )))
            .addGroup(new OptionGroup(categoryText("layout.colors.panels"), listOf(
                new ColorPickerWidget(new Option<>(optKey("layout.color.screen_bg"), d.colorScreenBaseBackground, () -> c.colorScreenBaseBackground, v -> c.colorScreenBaseBackground = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.header_bg"), d.colorHeaderBackground, () -> c.colorHeaderBackground, v -> c.colorHeaderBackground = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.footer_bg"), d.colorFooterBackground, () -> c.colorFooterBackground, v -> c.colorFooterBackground = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.tab_panel_bg"), d.colorTabPanelBackground, () -> c.colorTabPanelBackground, v -> c.colorTabPanelBackground = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.option_panel_bg"), d.colorOptionPanelBackground, () -> c.colorOptionPanelBackground, v -> c.colorOptionPanelBackground = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.desc_panel_bg"), d.colorDescriptionPanelBackground, () -> c.colorDescriptionPanelBackground, v -> c.colorDescriptionPanelBackground = v, listOf()))
            )))
            .addGroup(new OptionGroup(categoryText("layout.colors.elements"), listOf(
                new ColorPickerWidget(new Option<>(optKey("layout.color.text_primary"), d.colorTextPrimary, () -> c.colorTextPrimary, v -> c.colorTextPrimary = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.text_secondary"), d.colorTextSecondary, () -> c.colorTextSecondary, v -> c.colorTextSecondary = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.text_muted"), d.colorTextMuted, () -> c.colorTextMuted, v -> c.colorTextMuted = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.text_disabled"), d.colorTextDisabled, () -> c.colorTextDisabled, v -> c.colorTextDisabled = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.item_selected_text"), d.colorItemSelectedText, () -> c.colorItemSelectedText, v -> c.colorItemSelectedText = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.item_unselected_text"), d.colorItemUnselectedText, () -> c.colorItemUnselectedText, v -> c.colorItemUnselectedText = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.item_hover_bg"), d.colorItemHoverBackground, () -> c.colorItemHoverBackground, v -> c.colorItemHoverBackground = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.item_selected_bg"), d.colorItemSelectedBackground, () -> c.colorItemSelectedBackground, v -> c.colorItemSelectedBackground = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.scrollbar_track"), d.colorScrollBarTrack, () -> c.colorScrollBarTrack, v -> c.colorScrollBarTrack = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.scrollbar_thumb"), d.colorScrollBarThumb, () -> c.colorScrollBarThumb, v -> c.colorScrollBarThumb = v, listOf())),

                new ColorPickerWidget(new Option<>(optKey("layout.color.btn_border"), d.colorButtonBorder, () -> c.colorButtonBorder, v -> c.colorButtonBorder = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.btn_bg"), d.colorButtonBackground, () -> c.colorButtonBackground, v -> c.colorButtonBackground = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.btn_bg_hover"), d.colorButtonBackgroundHover, () -> c.colorButtonBackgroundHover, v -> c.colorButtonBackgroundHover = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.btn_bg_disabled"), d.colorButtonBackgroundDisabled, () -> c.colorButtonBackgroundDisabled, v -> c.colorButtonBackgroundDisabled = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.btn_text"), d.colorButtonText, () -> c.colorButtonText, v -> c.colorButtonText = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.btn_text_disabled"), d.colorButtonTextDisabled, () -> c.colorButtonTextDisabled, v -> c.colorButtonTextDisabled = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.toggle_bg_off"), d.colorToggleBgOff, () -> c.colorToggleBgOff, v -> c.colorToggleBgOff = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.toggle_bg_on"), d.colorToggleBgOn, () -> c.colorToggleBgOn, v -> c.colorToggleBgOn = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.toggle_knob"), d.colorToggleKnob, () -> c.colorToggleKnob, v -> c.colorToggleKnob = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.toggle_bg_off_hover"), d.colorToggleBgOffHover, () -> c.colorToggleBgOffHover, v -> c.colorToggleBgOffHover = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.toggle_bg_on_hover"), d.colorToggleBgOnHover, () -> c.colorToggleBgOnHover, v -> c.colorToggleBgOnHover = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.toggle_box"), d.colorToggleBox, () -> c.colorToggleBox, v -> c.colorToggleBox = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.toggle_box_hover"), d.colorToggleBoxHover, () -> c.colorToggleBoxHover, v -> c.colorToggleBoxHover = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.slider_track"), d.colorSliderTrack, () -> c.colorSliderTrack, v -> c.colorSliderTrack = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.slider_progress"), d.colorSliderProgress, () -> c.colorSliderProgress, v -> c.colorSliderProgress = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.slider_knob"), d.colorSliderKnob, () -> c.colorSliderKnob, v -> c.colorSliderKnob = v, listOf())),
                new ColorPickerWidget(new Option<>(optKey("layout.color.slider_text"), d.colorSliderText, () -> c.colorSliderText, v -> c.colorSliderText = v, listOf()))
            )))
            .addGroup(new OptionGroup(categoryText("layout.engine"), listOf(
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.min_tab_width"), d.engineMinTabAreaWidth, () -> c.engineMinTabAreaWidth, v -> c.engineMinTabAreaWidth = v, listOf()), 20, 200, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.min_option_width"), d.engineMinOptionAreaWidth, () -> c.engineMinOptionAreaWidth, v -> c.engineMinOptionAreaWidth = v, listOf()), 50, 400, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.min_desc_threshold"), d.engineMinDescAreaWidthThreshold, () -> c.engineMinDescAreaWidthThreshold, v -> c.engineMinDescAreaWidthThreshold = v, listOf()), 10, 200, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.track_padding_y"), d.engineTrackPaddingY, () -> c.engineTrackPaddingY, v -> c.engineTrackPaddingY = v, listOf()), 0, 10, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.text_height_offset"), d.engineTextHeightOffset, () -> c.engineTextHeightOffset, v -> c.engineTextHeightOffset = v, listOf()), -5, 10, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.reset_btn_offset_x"), d.engineResetButtonOffsetX, () -> c.engineResetButtonOffsetX, v -> c.engineResetButtonOffsetX = v, listOf()), -10, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.mode_toggle_spacing_x"), d.engineModeToggleSpacingX, () -> c.engineModeToggleSpacingX, v -> c.engineModeToggleSpacingX = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.top_section_height_offset"), d.engineTopSectionHeightOffset, () -> c.engineTopSectionHeightOffset, v -> c.engineTopSectionHeightOffset = v, listOf()), 0, 150, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.min_sb_size"), d.engineMinSbSize, () -> c.engineMinSbSize, v -> c.engineMinSbSize = v, listOf()), 10, 200, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.max_sb_size"), d.engineMaxSbSize, () -> c.engineMaxSbSize, v -> c.engineMaxSbSize = v, listOf()), 50, 500, 5),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.swat_text_spacing_y"), d.engineSwatTextSpacingY, () -> c.engineSwatTextSpacingY, v -> c.engineSwatTextSpacingY = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.min_palette_spacing"), d.engineMinPaletteBoxSpacing, () -> c.engineMinPaletteBoxSpacing, v -> c.engineMinPaletteBoxSpacing = v, listOf()), 0, 10, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.action_btn_spacing_y"), d.engineActionButtonSpacingY, () -> c.engineActionButtonSpacingY, v -> c.engineActionButtonSpacingY = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.action_btn_spacing_x"), d.engineActionButtonSpacingX, () -> c.engineActionButtonSpacingX, v -> c.engineActionButtonSpacingX = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.clear_btn_spacing_x"), d.engineClearButtonSpacingX, () -> c.engineClearButtonSpacingX, v -> c.engineClearButtonSpacingX = v, listOf()), 0, 20, 1),
                SliderWidget.ofInt(new Option<>(optKey("layout.engine.clear_btn_spacing_y"), d.engineClearButtonSpacingY, () -> c.engineClearButtonSpacingY, v -> c.engineClearButtonSpacingY = v, listOf()), 0, 20, 1)
            )));
        // @formatter:on
    }
}
