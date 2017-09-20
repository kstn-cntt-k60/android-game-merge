package kstn.game.view.screen;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public final class ImageView extends View implements ViewRotatable {
    private float angle = 0;
    private final Bitmap bitmap;
    private final float[] rotateMatrix = new float[16];
    private int imageTextureId = -1;

    private float[] textureCoordArray = new float[8];
    private FloatBuffer textureCoordBuffer = null;

    public ImageView(float centerX, float centerY,
                     float width, float height, Bitmap bitmap) {
        super();
        this.bitmap = bitmap;
        initMatrices();
        initBuffers();

        setSize(width, height);
        setCenter(centerX, centerY);
    }

    protected void initMatrices() {
        super.initMatrices();
        Matrix.setIdentityM(rotateMatrix, 0);
    }

    protected void initBuffers() {
        super.initBuffers();

        // Texture Coordinate Buffer
        textureCoordArray[0] = 0;
        textureCoordArray[1] = 1;

        textureCoordArray[2] = 1;
        textureCoordArray[3] = 1;

        textureCoordArray[4] = 1;
        textureCoordArray[5] = 0;

        textureCoordArray[6] = 0;
        textureCoordArray[7] = 0;

        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 4 * 2);
        buffer.order(ByteOrder.nativeOrder());
        textureCoordBuffer = buffer.asFloatBuffer();

        textureCoordBuffer.clear();
        textureCoordBuffer.put(textureCoordArray, 0, 8);
        textureCoordBuffer.flip();
    }


    @Override
    public void rotate(float angle) {
        this.angle = angle;
        Matrix.setRotateM(rotateMatrix, 0, angle, 0, 0, 1);
        updateModelMatrix();
    }

    @Override
    protected void updateModelMatrix() {
        Matrix.multiplyMM(modelMatrix, 0, translationMatrix, 0, rotateMatrix, 0);
    }

    @Override
    public void onSurfaceCreated() {
        // Initialize texture
        final int tex[] = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        imageTextureId = tex[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTextureId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        // Back to no texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        // Matrix location
        modelMatrixLocation = 1;
    }

    @Override
    public void onDraw() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTextureId);

        // Location 0, 1 is position, texCoord, respectively
        assert (positionBuffer != null);
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 0, positionBuffer);
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 0, textureCoordBuffer);

        // Uniform
        GLES20.glUniformMatrix4fv(modelMatrixLocation, 1, false, modelMatrix, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
