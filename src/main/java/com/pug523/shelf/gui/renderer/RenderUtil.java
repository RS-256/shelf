package com.pug523.shelf.gui.renderer;

import com.pug523.shelf.compat.GuiCompat;
import com.pug523.shelf.gui.renderer.state.SdfRenderState;
import net.minecraft.client.gui.navigation.ScreenRectangle;

// TODO: cleanup

public class RenderUtil {
    private RenderUtil() {
    }

    public static void renderCircle(GuiCompat gui, float centerX, float centerY, float radius, int color) {
        float diameter = radius * 2.0f;
        float x = centerX - radius;
        float y = centerY - radius;
        addSdfRenderState(gui, x, y, diameter, diameter, radius, color);
    }

    public static void renderCapsule(GuiCompat gui, float x, float y, float width, float height, int color) {
        float radius = height / 2.0f;
        renderRoundedRect(gui, x, y, width, height, radius, color);
    }

    public static void renderRoundedRect(GuiCompat gui, float x, float y, float width, float height, float radius,
                                         int color) {
        addSdfRenderState(gui, x, y, width, height, radius, color);
    }

    private static void addSdfRenderState(GuiCompat gui, float x, float y, float width, float height, float radius,
                                          int color) {
        SdfRenderState state = new SdfRenderState(
            gui.getGraphics().pose(), x, y, x + width, y + height, width, height, radius,
            color, peekScissorStack(gui)
        );
        //#if MC >= 12106
        gui.getGraphics().guiRenderState.addGuiElement(state);
        //#else
        //$$ gui.getGraphics().drawSpecial(bufferSource -> {
        //$$     state.buildVertices(bufferSource.getBuffer(SdfRenderState.SDF_RENDER_TYPE));
        //$$ });
        //#endif
    }

    public static void renderOutline(GuiCompat gui, int x, int y, int width, int height, int borderThickness, int color) {
        gui.fill(x, y, x + borderThickness, y + height, color);
        gui.fill(x + width - borderThickness, y, x + width, y + height, color);
        gui.fill(x + borderThickness, y, x + width - borderThickness, y + borderThickness, color);
        gui.fill(x + borderThickness, y + height - borderThickness, x + width - borderThickness, y + height, color);
    }

    public static void renderInner(GuiCompat gui, int x, int y, int width, int height, int borderThickness, int color) {
        gui.fill(x + borderThickness, y + borderThickness, x + width - borderThickness, y + height - borderThickness, color);
    }

    public static void renderDownwardArrow(GuiCompat gui, int startX, int startY, int color) {
        gui.fill(startX, startY, startX + 5, startY + 1, color);
        gui.fill(startX + 1, startY + 1, startX + 4, startY + 2, color);
        gui.fill(startX + 2, startY + 2, startX + 3, startY + 3, color);
    }

    public static void renderRightwardArrow(GuiCompat gui, int startX, int startY, int color) {
        gui.fill(startX, startY, startX + 1, startY + 5, color);
        gui.fill(startX + 1, startY + 1, startX + 2, startY + 4, color);
        gui.fill(startX + 2, startY + 2, startX + 3, startY + 3, color);
    }

    public static void renderDebugRawQuad(GuiCompat gui, float x, float y, float width, float height) {
        float radius = Math.min(width, height) / 4.0f;
        gui.getGraphics().guiRenderState.addGuiElement(
            new SdfRenderState(gui.getGraphics().pose(), x,
                y, (x + width), (y + height), width, height, radius, 0x5500FF00, null));
    }

    public static ScreenRectangle peekScissorStack(GuiCompat gui) {
        //#if MC >= 12106
        return gui.getGraphics().scissorStack.peek();
        //#else
        //$$ return gui.getGraphics().scissorStack.stack.peekLast();
        //#endif
    }
}
