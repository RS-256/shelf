package com.pug523.shelf.gui.renderer.state;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pug523.shelf.gui.renderer.RenderPipelines;
import com.pug523.shelf.gui.renderer.shader.UniformApplier;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryStack;

//#if MC >= 12106
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;

import java.nio.ByteBuffer;
//#else
//$$ import com.mojang.blaze3d.buffers.BufferType;
//$$ import com.mojang.blaze3d.buffers.BufferUsage;
//$$ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
//$$ import com.mojang.blaze3d.vertex.VertexFormat;
//$$ import net.minecraft.client.renderer.RenderStateShard;
//$$ import net.minecraft.client.renderer.RenderType;
//#endif

import static com.pug523.shelf.gui.renderer.RenderPipelines.SDF_PARAMS_UNIFORM_NAME;

public class SdfRenderState implements ShelfGuiElementRenderState, UniformApplier {
    //#if MC >= 12106
    public static final int USAGE = GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_UNIFORM;
    public static final int SDF_PARAMS_UBO_SIZE = new Std140SizeCalculator().putVec4().get();
    public static final GpuBuffer sdfParamsBuffer = RenderSystem.getDevice().createBuffer(() -> "SDF params UBO", USAGE, SDF_PARAMS_UBO_SIZE);
    public static final GpuBufferSlice sdfParamsBufferSlice = sdfParamsBuffer.slice();
    //#else
    //$$ public static final int SDF_PARAMS_UBO_SIZE = 16; // vec4 = 16 bytes (width, height, radius, 0.0f)
    //$$ public static final GpuBuffer sdfParamsBuffer = RenderSystem.getDevice().createBuffer(
    //$$     () -> "SDF Params UBO",
    //$$     BufferType.UNIFORM,
    //$$     BufferUsage.DYNAMIC_WRITE,
    //$$     SDF_PARAMS_UBO_SIZE
    //$$ );
    //$$ public static final RenderType SDF_RENDER_TYPE = RenderType.create(
    //$$     "shelf_sdf",
    //$$     DefaultVertexFormat.POSITION_TEX_COLOR,
    //$$     VertexFormat.Mode.QUADS,
    //$$     1536,
    //$$     false,
    //$$     false,
    //$$     RenderType.CompositeState.builder()
    //$$         .setShaderState(new RenderStateShard.ShaderStateShard(() -> RenderSystem.getDevice().getShaderProgram(SHADER_ID)))
    //$$         .setOutputState(new RenderStateShard.OutputStateShard("setup_sdf_ubo", () -> {
    //$$             RenderSystem.getDevice().setUniformBuffer(0, sdfParamsBuffer);
    //$$         }, () -> {}))
    //$$         .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
    //$$         .createCompositeState(false)
    //$$ );
    //#endif

    public final Matrix3x2f pose;
    public final float x0;
    public final float y0;
    public final float x1;
    public final float y1;
    public final float width;
    public final float height;
    public final float radius;
    public final int color;
    private final @Nullable ScreenRectangle scissorArea;
    private final @Nullable ScreenRectangle bounds;

    public SdfRenderState(Matrix3x2fc pose, float x0, float y0,
                          float x1, float y1, float width, float height, float radius, int color,
                          @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) {
        this.pose = new Matrix3x2f(pose);
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.color = color;
        this.scissorArea = scissorArea;
        this.bounds = bounds;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            //#if MC >= 12106
            ByteBuffer byteBuffer = Std140Builder.onStack(stack, SDF_PARAMS_UBO_SIZE)
                .putVec4(this.width, this.height, this.radius, 0.0f).get();
            //#else
            //$$ ByteBuffer buffer = stack.malloc(SDF_PARAMS_UBO_SIZE);
            //$$ buffer.putFloat(width);
            //$$ buffer.putFloat(height);
            //$$ buffer.putFloat(radius);
            //$$ buffer.putFloat(0.0f);
            //$$ buffer.flip();
            //#endif
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(sdfParamsBufferSlice, byteBuffer);
        }
    }

    public SdfRenderState(Matrix3x2fc pose, float x0, float y0,
                          float x1, float y1, float width, float height, float radius, int color,
                          @Nullable ScreenRectangle scissorArea) {
        this(pose, x0, y0, x1, y1, width, height, radius, color, scissorArea,
            RenderStateUtil.bounds((int) x0, (int) y0, (int) x1, (int) y1, pose, scissorArea));
    }

    @Override
    public @NonNull RenderPipeline pipeline() {
        return RenderPipelines.SDF_PIPELINE;
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return this.scissorArea;
    }

    @Override
    public @Nullable ScreenRectangle bounds() {
        return this.bounds;
    }

    @Override
    // @formatter:off
    //#if MC >= 12106 && MC <= 12108
    //$$ public void buildVertices(@NonNull VertexConsumer vertices, float depth) {
    //#else
    public void buildVertices(@NonNull VertexConsumer vertices) {
    //#endif
    // @formatter:on
        writeVertex(vertices, x0, y0, 0.0f, 0.0f);
        writeVertex(vertices, x0, y1, 0.0f, 1.0f);
        writeVertex(vertices, x1, y1, 1.0f, 1.0f);
        writeVertex(vertices, x1, y0, 1.0f, 0.0f);
    }

    private void writeVertex(@NonNull VertexConsumer vertices, float x, float y, float u, float v) {
        RenderStateUtil.addVertexWith2DPose(vertices, this.pose, x, y).setUv(u, v).setColor(this.color);
    }

    @Override
    public void applyUniforms(RenderPass renderPass) {
        renderPass.setUniform(SDF_PARAMS_UNIFORM_NAME, sdfParamsBufferSlice);
    }
}
