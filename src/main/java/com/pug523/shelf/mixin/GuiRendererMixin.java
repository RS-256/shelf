package com.pug523.shelf.mixin;

import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12106
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.pug523.shelf.gui.renderer.shader.UniformRegistry;
import com.pug523.shelf.gui.renderer.state.SdfRenderState;
import com.pug523.shelf.gui.renderer.RenderPipelines;
import com.pug523.shelf.gui.renderer.SdfParamBufferPool;
import java.util.List;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
//$$ import com.pug523.shelf.Shelf;
//#endif

//#if MC >= 12106
@Mixin(GuiRenderer.class)
//#else
//$$ @Mixin(Shelf.class)
//#endif
public class GuiRendererMixin {
    //#if MC >= 12106
    @Shadow
    @Final
    private List<GuiRenderer.Draw> draws;

    @Shadow
    private RenderPipeline previousPipeline;

    // @formatter:off
    //#if MC >= 260200
    @Inject(method = "addElementToMesh", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    //#else
    //$$ @Inject(method = "addElementToMesh", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;recordMesh(Lcom/mojang/blaze3d/vertex/BufferBuilder;Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/gui/render/TextureSetup;Lnet/minecraft/client/gui/navigation/ScreenRectangle;)V"))
    //#endif
    //#if MC >= 12109
    private void registerUniformApplier(GuiElementRenderState elementState, CallbackInfo ci) {
    //#else
    //$$ private void registerUniformApplier(GuiElementRenderState elementState, int i, CallbackInfo ci) {
    //#endif
    // @formatter:on
        if (elementState.pipeline() == RenderPipelines.SDF_PIPELINE) {
            UniformRegistry.put(this.draws.size(), ((SdfRenderState) elementState));
            this.previousPipeline = null;
        }
    }

    @Inject(method = "executeDrawRange", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", shift = At.Shift.AFTER))
    private void applyUniforms(CallbackInfo ci, @Local(name = "renderPass") RenderPass renderPass,
                               @Local(name = "i") int i) {
        UniformRegistry.applyAndRemove(i, renderPass);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void tryShrinkSdfParams(CallbackInfo ci) {
        SdfParamBufferPool.tryShrink();
    }

    // @Inject(method = "executeDraw", at = @At("HEAD"))
    // private void applyUniforms(GuiRenderer.Draw draw, RenderPass renderPass, CallbackInfo ci) {
    //     if (draw.pipeline() ==  RenderPipelines.SDF_PIPELINE) {
    //         // UniformRegistry.applyIfPresent(renderPass);
    //     }
    // }
    //#endif
}
