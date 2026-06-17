package com.pug523.shelf.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.pug523.shelf.gui.renderer.SdfRenderQueue;

@Mixin(RenderTarget.class)
public class RenderTargetMixin {

    //#if MC >= 12104
    @Inject(method = "blitToScreen", at = @At("TAIL"))
    private void flushOnBlitToScreen(CallbackInfo ci) {
        SdfRenderQueue.flushAll();
    }
    //#endif
}
