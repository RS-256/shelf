package com.pug523.shelf.gui.renderer.shader;

import java.util.HashMap;
import java.util.Map;

//#if MC >= 12104
import com.mojang.blaze3d.systems.RenderPass;
//#else
//$$ import com.mojang.blaze3d.shaders.Uniform;
//#endif

public final class UniformRegistry {
    //#if MC >= 12106
    private static final Map<Integer, UniformApplier> REGISTRY = new HashMap<>();

    public static void put(int i, UniformApplier applier) {
        REGISTRY.put(i, applier);
    }

    public static UniformApplier get(int i) {
        return REGISTRY.get(i);
    }

    public static void remove(int i) {
        REGISTRY.remove(i);
    }

    public static void clear() {
        REGISTRY.clear();
    }

    public static void applyAndRemove(int i, RenderPass pass) {
        UniformApplier e = get(i);
        if (e != null) {
            e.applyUniforms(pass);
            remove(i);
        }
    }
    //#else
    //$$ private static final ThreadLocal<UniformApplier> CURRENT_APPLIER = new ThreadLocal<>();

    //$$ public static void push(UniformApplier applier) {
    //$$     CURRENT_APPLIER.set(applier);
    //$$ }

    //$$ public static void pop() {
    //$$     CURRENT_APPLIER.remove();
    //$$ }

    //#if MC >= 12104
    //$$ public static void applyIfPresent(RenderPass pass) {
    //$$     UniformApplier applier = CURRENT_APPLIER.get();
    //$$     if (applier != null) {
    //$$         applier.applyUniforms(pass);
    //$$     }
    //$$ }
    //#else
    //$$ public static void applyIfPresent(Uniform uniform) {
    //$$     UniformApplier applier = CURRENT_APPLIER.get();
    //$$     if (applier != null) {
    //$$         applier.applyUniforms(uniform);
    //$$     }
    //$$ }
    //#endif
    //#endif
}
