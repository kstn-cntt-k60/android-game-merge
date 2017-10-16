package kstn.game.view.screen;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class View {
    protected float width, height;
    protected float centerX, centerY;
    protected OnTouchListener onTouchListener = null;

    // For OpenGL ES
    protected final float[] translationMatrix = new float[16];
    protected final float[] modelMatrix = new float[16];
    protected final float[] positionArray = new float[8];
    protected final short[] indexArray = new short[6];
    protected int modelMatrixLocation = -1;

    protected ShortBuffer indexBuffer = null;
    protected FloatBuffer positionBuffer = null;

    public View() {
        this.width = 0;
        this.height = 0;
        this.centerX = 0;
        this.centerY = 0;
    }

    public View(float centerX, float centerY, float width, float height) {
        this.width = width;
        this.height = height;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    protected void initMatrices() {
        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.setIdentityM(modelMatrix, 0);
    }

    protected void initBuffers() {
        // Index Buffer
        indexArray[0] = 0;
        indexArray[1] = 1;
        indexArray[2] = 2;

        // Another triangle
        indexArray[3] = 0;
        indexArray[4] = 2;
        indexArray[5] = 3;

        ByteBuffer buffer = ByteBuffer.allocateDirect(6 * 2);
        buffer.order(ByteOrder.nativeOrder());
        indexBuffer = buffer.asShortBuffer();

        indexBuffer.clear();
        indexBuffer.put(indexArray, 0, 6);
        indexBuffer.flip();

        // Position Buffer
        int vertexSize = 4 * 2;
        buffer = ByteBuffer.allocateDirect(4 * vertexSize);
        buffer.order(ByteOrder.nativeOrder());
        positionBuffer = buffer.asFloatBuffer();
    }

    public void onSurfaceCreated() {}
    public void onSurfaceChanged(float screenWidth, float screenHeight) {}

    public void onDraw() {}

    public View onTouch(TouchEvent event) {
        if (onTouchListener != null) {
            float x = event.getX();
            float y = event.getY();

            if (event.getType() == TouchEvent.TOUCH_DOWN) {
                if (x > centerX + width / 2 || x < centerX - width / 2)
                    return null;
                if (y > centerY + height / 2 || y < centerY - height / 2)
                    return null;

                TouchEvent newEvent = event.setXY(x - centerX, y - centerY);
                boolean isProcessed = onTouchListener.onTouch(newEvent);
                if (isProcessed)
                    return this;
                else
                    return null;
            }
            else {
                TouchEvent newEvent = event.setXY(x - centerX, y - centerY);
                boolean isProcessed = onTouchListener.onTouch(newEvent);
                if (isProcessed)
                    return this;
                else
                    return null;
            }
        }
        return null;
    }

    public final void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updatePositionArray();
    }

    public final void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.translateM(translationMatrix, 0, centerX, centerY, 0);
        updateModelMatrix();
    }

    private void updatePositionArray() {
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        positionArray[0] = -halfWidth;
        positionArray[1] = -halfHeight;

        positionArray[2] = halfWidth;
        positionArray[3] = -halfHeight;

        positionArray[4] = halfWidth;
        positionArray[5] = halfHeight;

        positionArray[6] = -halfWidth;
        positionArray[7] = halfHeight;
        updatePositionBuffer();
    }

    private void updatePositionBuffer() {
        positionBuffer.clear();
        positionBuffer.put(positionArray, 0, 8);
        positionBuffer.flip();
    }

    protected void updateModelMatrix() {
        Matrix.translateM(modelMatrix, 0, centerX, centerY, 0);
    }

    public void setTouchListener(OnTouchListener listener) {
        this.onTouchListener = listener;
    }

    public interface TouchEvent {
        int TOUCH_DOWN = 0;
        int TOUCH_UP = 1;
        int TOUCH_MOVE = 2;

        int getType();

        float getX();

        float getY();

        TouchEvent setXY(float x, float y);
    }

    public interface OnTouchListener {
        // True if processed it, false otherwise
        boolean onTouch(TouchEvent event);
    }

}
