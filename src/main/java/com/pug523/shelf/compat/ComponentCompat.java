package com.pug523.shelf.compat;

import net.minecraft.network.chat.Component;

//#if MC < 11900
//$$ import net.minecraft.network.chat.TextComponent;
//$$ import net.minecraft.network.chat.TranslatableComponent;
//#endif

//#if MC >= 11600
import net.minecraft.network.chat.MutableComponent;
//#endif

public class ComponentCompat {
    // @formatter:off
    //#if MC >= 11600
    public static MutableComponent literal(String message) {
    //#else
    //$$ public static Component literal(String message) {
    //#endif
    // @formatter:on
        //#if MC >= 11900
        return Component.literal(message);
        //#else
        //$$ return new TextComponent(message);
        //#endif
    }

    // @formatter:off
    //#if MC >= 11600
    public static MutableComponent translatable(String message) {
    //#else
    //$$ public static Component translatable(String message) {
    //#endif
    // @formatter:on
        //#if MC >= 11900
        return Component.translatable(message);
        //#else
        //$$ return new TranslatableComponent(message);
        //#endif
    }
}
