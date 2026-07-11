package com.pug523.shelf.gui.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;

//#if MC >= 260200
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.PrimitiveTopology;
import net.minecraft.client.renderer.BindGroupLayouts;
//#else
//$$ import com.mojang.blaze3d.vertex.VertexFormat.Mode;
//#endif

//#if MC >= 260000
import com.mojang.blaze3d.pipeline.ColorTargetState;
//#else
//$$ import com.mojang.blaze3d.platform.DepthTestFunction;
//#endif

public class RenderPipelines {
    private RenderPipelines() {
    }

    public static final String SDF_PARAMS_UNIFORM_NAME = "SdfParams";
    // @formatter:off
    //#if MC >= 260200
    private static final BindGroupLayout SDF_PARAMS = BindGroupLayout.builder()
            .withUniform(SDF_PARAMS_UNIFORM_NAME, UniformType.UNIFORM_BUFFER).build();
    //#endif
    public static final RenderPipeline SDF_PIPELINE = RenderPipeline.builder().withLocation(ShaderIds.SDF)
            //#if MC >= 260200
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withBindGroupLayout(SDF_PARAMS)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            //#else
            //$$ .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            //$$ .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            //$$ .withUniform(SDF_PARAMS_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
            //$$ .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, Mode.QUADS)
                //#if MC >= 260000
                //$$ .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                //#else
                //$$ .withBlend(BlendFunction.TRANSLUCENT)
                //$$ .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                //#endif
            //#endif
            .withVertexShader(ShaderIds.SDF)
            .withFragmentShader(ShaderIds.SDF)
            .build();
    // @formatter:on
}
