package com.pug523.shelf.gui.renderer.shader;

//#if MC >= 12104
import com.mojang.blaze3d.systems.RenderPass;
//#else
//$$ import com.mojang.blaze3d.shaders.Uniform;
//#endif

public interface UniformApplier {
    //#if MC >= 12104
    void applyUniforms(RenderPass renderPass);
    //#else
    //$$ void applyUniforms(Uniform uniform);
    //#endif
}
