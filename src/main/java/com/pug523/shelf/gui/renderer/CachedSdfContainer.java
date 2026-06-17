package com.pug523.shelf.gui.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class CachedSdfContainer {
    private int vaoId = -1;
    private int vboId = -1;
    private int vertexCount = 0;

    private static final int MAX_VERTICES = 2048;
    private float[] tempBuffer = new float[MAX_VERTICES];
    private int tempIdx = 0;

    public void beginRecording() {
        tempIdx = 0;
        vertexCount = 0;
    }

    public void addQuad(float x, float y, float w, float h, float radius, int color) {
        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        float[][] verts = { { x, y, 0f, 0f, r, g, b, a, radius }, // Top Left
                { x, y + h, 0f, 1f, r, g, b, a, radius }, // Bottom Left
                { x + w, y + h, 1f, 1f, r, g, b, a, radius }, // Bottom Right
                { x + w, y, 1f, 0f, r, g, b, a, radius } // Top Right
        };

        for (float[] v : verts) {
            if (tempIdx + 9 > tempBuffer.length) {
                float[] newBuffer = new float[tempBuffer.length * 2];
                System.arraycopy(tempBuffer, 0, newBuffer, 0, tempBuffer.length);
                tempBuffer = newBuffer;
            }
            System.arraycopy(v, 0, tempBuffer, tempIdx, 9);
            tempIdx += 9;
            vertexCount++;
        }
    }

    public void endRecording() {
        if (vertexCount == 0) {
            return;
        }

        if (vaoId == -1) {
            vaoId = GL30.glGenVertexArrays();
        }
        if (vboId == -1) {
            vboId = GL15.glGenBuffers();
        }

        GL30.glBindVertexArray(vaoId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);

        float[] finalData = new float[tempIdx];
        System.arraycopy(tempBuffer, 0, finalData, 0, tempIdx);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, finalData, GL15.GL_STATIC_DRAW);

        int stride = 9 * Float.BYTES;
        // Position
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, stride, 0);
        GL20.glEnableVertexAttribArray(0);
        // UV
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, stride, 2 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);
        // Color
        GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, stride, 4 * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);
        // Radius
        GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, stride, 8 * Float.BYTES);
        GL20.glEnableVertexAttribArray(3);

        GL30.glBindVertexArray(0);
    }

    public void render() {
        if (vertexCount == 0 || vaoId == -1) {
            return;
        }

        RenderUtil.beginRender();
        RenderUtil.drawCachedVao(vaoId, vertexCount);
        RenderUtil.endRender();
    }

    public void delete() {
        if (vboId != -1) {
            GL15.glDeleteBuffers(vboId);
        }
        if (vaoId != -1) {
            GL30.glDeleteVertexArrays(vaoId);
        }
    }
}
