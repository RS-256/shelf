package com.pug523.shelf.gui.renderer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;

public class SdfRenderQueue {
    private static final int MAX_QUADS = 1024;

    // x, y, w, h, radius
    private static final float[] VERTEX_DATA = new float[MAX_QUADS * 5];
    private static final int[] COLOR_DATA = new int[MAX_QUADS];
    private static final boolean[] USE_SCISSOR_DATA = new boolean[MAX_QUADS];
    private static final int[] SCISSOR_DATA = new int[MAX_QUADS * 4];

    private static int taskCount = 0;
    private static final float PADDING = 1.5f;
    private static boolean isBuffering = false;

    public static void startBuffering() {
        taskCount = 0;
        isBuffering = true;
    }

    public static void queueSdfQuad(float x, float y, float w, float h, float radius, int color, boolean addPadding) {
        queueSdfQuad(x, y, w, h, radius, color, addPadding, false, 0, 0, 0, 0);
    }

    public static void queueSdfQuad(float x, float y, float w, float h, float radius, int color, boolean addPadding,
            boolean useScissor, int scissorX, int scissorY, int scissorMaxX, int scissorMaxY) {
        if (addPadding) {
            x -= PADDING;
            y -= PADDING;
            w += PADDING * 2;
            h += PADDING * 2;
        }

        if (!isBuffering) {
            RenderUtil.beginRender();
            applyScissorState(useScissor, scissorX, scissorY, scissorMaxX, scissorMaxY);
            RenderUtil.drawSdfQuadImmediate(x, y, w, h, radius, color);
            RenderUtil.endRender();
            return;
        }

        if (taskCount >= MAX_QUADS) {
            flushAll();
            startBuffering();
        }

        int vIdx = taskCount * 5;
        VERTEX_DATA[vIdx] = x;
        VERTEX_DATA[vIdx + 1] = y;
        VERTEX_DATA[vIdx + 2] = w;
        VERTEX_DATA[vIdx + 3] = h;
        VERTEX_DATA[vIdx + 4] = radius;
        int sIdx = taskCount * 4;
        SCISSOR_DATA[sIdx] = scissorX;
        SCISSOR_DATA[sIdx + 1] = scissorY;
        SCISSOR_DATA[sIdx + 2] = scissorMaxX;
        SCISSOR_DATA[sIdx + 3] = scissorMaxY;

        COLOR_DATA[taskCount] = color;
        USE_SCISSOR_DATA[taskCount] = useScissor;

        taskCount++;
    }

    public static void flushAll() {
        if (taskCount == 0) {
            isBuffering = false;
            return;
        }

        RenderUtil.beginRender();

        for (int i = 0; i < taskCount; i++) {
            int vIdx = i * 5;
            int sIdx = i * 4;
            applyScissorState(USE_SCISSOR_DATA[i], SCISSOR_DATA[sIdx], // scissorX
                    SCISSOR_DATA[sIdx + 1], // scissorY
                    SCISSOR_DATA[sIdx + 2], // scissorMaxX
                    SCISSOR_DATA[sIdx + 3] // scissorMaxY
            );
            RenderUtil.drawSdfQuadImmediate(VERTEX_DATA[vIdx], // x
                    VERTEX_DATA[vIdx + 1], // y
                    VERTEX_DATA[vIdx + 2], // w
                    VERTEX_DATA[vIdx + 3], // h
                    VERTEX_DATA[vIdx + 4], // radius
                    COLOR_DATA[i] // color
            );
        }

        RenderUtil.endRender();

        taskCount = 0;
        isBuffering = false;
    }

    private static void applyScissorState(boolean useScissor, int x, int y, int maxX, int maxY) {
        if (useScissor) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            Minecraft mc = Minecraft.getInstance();
            //#if MC >= 11500
            Window window = mc.getWindow();
            //#else
            //$$ Window window = mc.window;
            //#endif
            double scale = window.getGuiScale();

            int screenX = (int) Math.round(x * scale);
            int screenY = (int) Math.round((window.getGuiScaledHeight() - maxY) * scale);

            int screenWidth = (int) Math.round((maxX - x) * scale);
            int screenHeight = (int) Math.round((maxY - y) * scale);

            GL11.glScissor(Math.max(0, screenX), Math.max(0, screenY), Math.max(0, screenWidth),
                    Math.max(0, screenHeight));
        } else {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }
}
