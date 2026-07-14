package com.pug523.shelf.mixin;

import org.spongepowered.asm.mixin.Mixin;

//#if MC <= 12105
//$$ import com.mojang.blaze3d.vertex.BufferBuilder;
//$$ import net.minecraft.client.renderer.MultiBufferSource;
//$$ import net.minecraft.client.renderer.RenderType;
//$$ import com.pug523.shelf.gui.renderer.shader.UniformRegistry;
//$$ import com.pug523.shelf.gui.renderer.shader.SdfRenderType;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import com.pug523.shelf.Shelf;
//#endif

//#if MC >= 12106
@Mixin(Shelf.class)
//#else
//$$ @Mixin(MultiBufferSource.BufferSource.class)
//#endif
public class BufferSourceMixin {
    //#if MC <= 12105
    //$$ @Inject(method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V", at = @At("HEAD"))
    //$$ private void captureSdfState(RenderType renderType, BufferBuilder bufferBuilder, CallbackInfo ci) {
    //$$     if (renderType instanceof SdfRenderType sdfType) {
    //$$         UniformRegistry.push(sdfType.getSdfState());
    //$$     }
    //$$ }

    //$$ @Inject(method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V", at = @At("RETURN"))
    //$$ private void clearSdfState(RenderType renderType, BufferBuilder bufferBuilder, CallbackInfo ci) {
    //$$     if (renderType instanceof SdfRenderType) {
    //$$         UniformRegistry.pop();
    //$$     }
    //$$ }
    //#endif
}
