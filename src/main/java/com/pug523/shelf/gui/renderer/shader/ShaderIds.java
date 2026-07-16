package com.pug523.shelf.gui.renderer.shader;

import com.pug523.shelf.compat.IdentifierCompat;

import net.minecraft.resources.Identifier;

public class ShaderIds {
    private ShaderIds() {
    }

    public static final Identifier SDF = IdentifierCompat.ofShelf(shaderPath("sdf"));

    private static String shaderPath(String s) {
        //#if MC >= 12104
        return "core/" + s;
        //#elseif MC >= 12102
        //$$ return "core/" + s + "_1.21.3";
        //#elseif MC >= 12100
        //$$ return s + "_1.21.1";
        //#else
        //$$ return s;
        //#endif
    }
}
