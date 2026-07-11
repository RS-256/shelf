package com.pug523.shelf.gui.renderer.state;

import org.joml.Matrix3x2f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pug523.shelf.compat.GuiCompat;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderPipelines;

public class ColorGradientRenderState implements ShelfGuiElementRenderState {
    private final Matrix3x2f pose;
    @Nullable
    private final ScreenRectangle bounds;
    @Nullable
    private final ScreenRectangle scissorArea;

    public final int x0;
    public final int x1;
    public final int y0;
    public final int y1;

    public final int x0y0Color;
    public final int x0y1Color;
    public final int x1y0Color;
    public final int x1y1Color;

    public ColorGradientRenderState(GuiCompat gui, int x0, int x1, int y0, int y1, int x0y0Color, int x0y1Color,
            int x1y0Color, int x1y1Color) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;

        this.x0y0Color = x0y0Color;
        this.x0y1Color = x0y1Color;
        this.x1y0Color = x1y0Color;
        this.x1y1Color = x1y1Color;

        GuiGraphicsExtractor graphics = gui.getGraphics();
        this.scissorArea = graphics.scissorStack.peek();
        this.pose = new Matrix3x2f(graphics.pose());
        int width = x1 - x0;
        int height = y1 - y0;
        ScreenRectangle b = new ScreenRectangle(x0, y0, width, height).transformMaxBounds(this.pose);
        if (scissorArea == null) {
            bounds = b;
        } else {
            bounds = scissorArea.intersection(b);
        }
    }

    @Override
    // @formatter:off
    //#if MC >= 12106 && MC <= 12108
    //$$ public void buildVertices(@NonNull VertexConsumer vertices, float depth) {
    //#else
    public void buildVertices(@NonNull VertexConsumer vertices) {
    //#endif
    // @formatter:on
        RenderStateUtil.addVertexWith2DPose(vertices, this.pose, this.x1, this.y0).setColor(this.x1y0Color);
        RenderStateUtil.addVertexWith2DPose(vertices, this.pose, this.x0, this.y0).setColor(this.x0y0Color);
        RenderStateUtil.addVertexWith2DPose(vertices, this.pose, this.x0, this.y1).setColor(this.x0y1Color);
        RenderStateUtil.addVertexWith2DPose(vertices, this.pose, this.x1, this.y1).setColor(this.x1y1Color);
    }

    @Override
    public @Nullable ScreenRectangle bounds() {
        return bounds;
    }

    @Override
    public @NonNull RenderPipeline pipeline() {
        return RenderPipelines.GUI;
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return scissorArea;
    }
}
