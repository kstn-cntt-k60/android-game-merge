package kstn.game.app.screen;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import kstn.game.app.event.LLBaseEventType;
import kstn.game.app.event.LLEventData;
import kstn.game.app.event.LLEventManager;
import kstn.game.app.event.LLListener;
import kstn.game.app.event.LLTouchEvent;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewGroup;

public class GameAnimationView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private GameViewClient viewClient = null;
    // root view group
    private ViewGroup viewGroup = new ViewGroup();
    private float width = 0, height = 0;
    private LLEventManager llEventManager = null;

    private ShaderProgram program = null;
    private final float[] projectionMatrix = new float[16];
    private int projectionMatrixLocation = -1;

    private View viewOnTouchMove = null;

    public GameAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        this.setRenderer(this);
    }

    public ViewGroup getRootViewGroup() {
        return viewGroup;
    }

    public void setProgram(ShaderProgram program) {
        this.program = program;
    }

    public void setViewClient(GameViewClient viewClient) {
        this.viewClient = viewClient;
    }

    public void setLLEventManager(LLEventManager llEventManager) {
        this.llEventManager = llEventManager;
        this.llEventManager.addListener(LLBaseEventType.TOUCH_EVENT, new LLListener() {
            @Override
            public void onEvent(LLEventData event) {
                handleTouchEvent((View.TouchEvent) event);
            }
        });
    }

    private void handleTouchEvent(View.TouchEvent event) {
        if (viewOnTouchMove == null && event.getType() == View.TouchEvent.TOUCH_DOWN) {
            viewOnTouchMove = viewGroup.onTouch(event);
        }
        else if (viewOnTouchMove != null) {
            viewOnTouchMove.onTouch(event);
            if (event.getType() == View.TouchEvent.TOUCH_UP)
                viewOnTouchMove = null;
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        SurfaceTick.increase();
        program.onSurfaceCreated();
        projectionMatrixLocation = program.getUniformLocation("projectionMatrix");
        assert (projectionMatrixLocation == 0);
        viewGroup.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
        float ratio = (float)height / (float)width;
        Matrix.orthoM(projectionMatrix, 0, -1, 1, -ratio, ratio, -1, 1);
        viewGroup.onSurfaceChanged(2, ratio * 2);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        viewClient.onDrawFrame();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        program.use();
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glEnableVertexAttribArray(1);

        // Projection matrix
        GLES20.glUniformMatrix4fv(projectionMatrixLocation,
                1, false, projectionMatrix, 0);

        viewGroup.onDraw();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewClient.onResume();
    }

    @Override
    public void onPause() {
        viewClient.onPause();
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (width == 0 || height == 0)
            return false;
        if (llEventManager == null)
            return false;
        if (event.getActionIndex() != 0)
            return true;

        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            if (event.getPointerId(i) == 0) {
                float x = (event.getX(i) - halfWidth) / halfWidth;
                float y = -(event.getY(i) - halfHeight) / halfWidth;
                int type;
                int actionMasked = event.getActionMasked();
                if (actionMasked == MotionEvent.ACTION_DOWN) {
                    type = View.TouchEvent.TOUCH_DOWN;
                }
                else if (actionMasked == MotionEvent.ACTION_UP) {
                    type = View.TouchEvent.TOUCH_UP;
                }
                else {
                    type = View.TouchEvent.TOUCH_MOVE;
                }

                llEventManager.queue(new LLTouchEvent(type, x, y));
                return true;
            }
        }
        return true;
    }
}
