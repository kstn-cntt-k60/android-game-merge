package kstn.game.logic.cone;

import android.graphics.Bitmap;
import android.util.Log;
import java.io.IOException;

import kstn.game.app.root.BaseTimeManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.process.Process;
import kstn.game.logic.process.ProcessManager;
import kstn.game.view.asset.AssetManager;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewGroup;

/**
 * Created by qi on 16/10/2017.
 */

public class Cone {
    private final  ViewGroup rootViewGroup;
    private final ImageView coneView;
    private final ImageView needleView;
    private boolean hidden = true;
    private final EventManager eventManager;
    private final ProcessManager processManager;
    private final BaseTimeManager timeManager;
    private ConeProcess coneProcess;

    private float angleCone = 0;
    private long timeToInitSpeed;
    private float realSpeedCone = 0;
    int count = 0;

    private float angleNeedle = 0;
    // for handling touch events
    private float baseAngle = 0;
    private float startAngle = 0;
    private float endAngle = 0;
    private float touchStartX = 0;


    private boolean allowRotate = false;
    private boolean isRotating = false;
    private boolean isEnabled = true;

    // Listeners
    EventListener moveEventListener;
    EventListener accelEventListener;

    private final float xBias = 0.05f;
    private final float minRadius = 0.2f;
    private final float minVelocity = 180f; // degree / seconds
    private final float decreaseAccel = -50f;

    public Cone(ProcessManager processManager_,
                AssetManager assetManager,
                EventManager eventManager_,
                BaseTimeManager timeManager,
                ViewGroup rootViewGroup) {
        this.rootViewGroup = rootViewGroup;
        this.eventManager = eventManager_;
        this.processManager = processManager_;
        this.timeManager = timeManager;
        Bitmap imageCone = null;
        Bitmap imageNeedle = null;

        try {
            imageCone = assetManager.getBitmap("non.png");
            imageNeedle = assetManager.getBitmap("kim.png");
        } catch (IOException e) {
            Log.e("Cone", "Can't load non.png");
        }
        coneView = new ImageView(0, -1.75f, 1.9f, 1.9f, imageCone);
        needleView = new ImageView(0.0f, -0.75f, 0.1f, 0.2f, imageNeedle);


        coneView.setTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View.TouchEvent event) {
                if (isRotating || !isEnabled)
                    return false;

                if (event.getType() == View.TouchEvent.TOUCH_DOWN) {
                    baseAngle = angleCone;
                    if (distance_from_event(event) >= minRadius) {
                        allowRotate = true;
                        touchStartX = event.getX();
                        startAngle = event_to_angle(event);
                        Cone.this.timeToInitSpeed = Cone.this.timeManager.getCurrentMillis();
                    }
                    else
                        allowRotate = false;
                }
                else if (allowRotate && event.getType() == View.TouchEvent.TOUCH_MOVE) {
                    endAngle = event_to_angle(event);
                    float touchCurrentX = event.getX();
                    if (touchCurrentX - xBias < touchStartX) {
                        eventManager.trigger(new ConeMoveEventData(normalize(baseAngle + endAngle - startAngle)));
                        touchStartX = touchCurrentX;
                    }
                    else {
                        allowRotate = false;
                    }
                }
                else if (allowRotate && event.getType() == View.TouchEvent.TOUCH_UP) {
                    long timeDelta = Cone.this.timeManager.getCurrentMillis() - timeToInitSpeed;
                    endAngle = event_to_angle(event);
                    float speedStart;
                    speedStart = normalize(endAngle - startAngle) * 1000 / timeDelta;

                    if (speedStart >= minVelocity) {
                        eventManager.trigger(new ConeAccelerateEventData(normalize(baseAngle + endAngle - startAngle), speedStart));
                    }
                }
                return true;
            }
        });

        moveEventListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                float angle = ((ConeMoveEventData) event).getAngle();
                Cone.this.rotate(angle);
            }
        };

        accelEventListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                isRotating = true;
                float angle = ((ConeAccelerateEventData) event).getAngle();
                Cone.this.rotate(angle);
                float speedStart = ((ConeAccelerateEventData) event).getSpeedStart();
                coneProcess = new ConeProcess(speedStart);
                processManager.attachProcess(coneProcess);
            }
        };


    }

    private void rotate(float angle) {
        coneView.rotate(angle);
        this.angleCone = angle;
        if (isCollision()) {
            angleNeedle = -5f;
            needleView.rotate(angleNeedle);
        }
    }


    public void entry() {
        angleCone = 0f;
        coneView.rotate(angleCone);
        rootViewGroup.addView(coneView);
        rootViewGroup.addView(needleView);
        eventManager.addListener(ConeEventType.MOVE, moveEventListener);
        eventManager.addListener(ConeEventType.ACCELERATE, accelEventListener);
    }

    public void exit() {
        rootViewGroup.removeView(coneView);
        rootViewGroup.removeView(needleView);
        eventManager.removeListener(ConeEventType.MOVE, moveEventListener);
        eventManager.removeListener(ConeEventType.ACCELERATE, accelEventListener);
    }

    public void disable() {
        isEnabled = false;
    }

    public void enable() {
        isEnabled = true;
    }

    public void hide() {
        if (!hidden) {
            rootViewGroup.removeView(coneView);
            hidden = true;
        }
    }

    public void show() {
        if (hidden) {
            rootViewGroup.addView(coneView);
            hidden = false;
        }
    }

    class ConeProcess extends Process {
        private float speed;
        private int result;
        private float endTime;
        private long currentTime;
        private float startAngle;
        private float realSpeed;

        public ConeProcess(float speedStart) {
            this.speed = speedStart;
            this.endTime = - speed * 1000 / decreaseAccel;
            currentTime = 0;
            startAngle = Cone.this.angleCone;
         }

        @Override
        public void onUpdate(long deltaMs) {
            currentTime += deltaMs;
            realSpeedCone = speed + decreaseAccel * currentTime;
            if (currentTime <= endTime) {
                float angle = startAngle + speed * currentTime / 1000.0f
                        + decreaseAccel * currentTime * currentTime / (2 * 1000 * 1000);
                if (isCollision()) {
                    Log.i("Vacham", "" + count++);
                    angleNeedle = -9f;
                    needleView.rotate(-angleNeedle);
                }
                Cone.this.rotate(normalize(angle));
            }
            else {
                float angle = startAngle + speed * endTime / 1000.0f
                        + decreaseAccel * endTime * endTime / (2 * 1000 * 1000);
                Cone.this.rotate(angle);
                if (isCollision()){
                    Cone.this.rotate(angle);
                }
                succeed();
            }
        }

        @Override
        public void onSuccess() {
            angleNeedle = 0;
            needleView.rotate(angleNeedle);
            result = getResult(angleCone);
            eventManager.trigger(new ConeStopEventData(result));
            Cone.this.isRotating = false;
        }

        @Override
        public void onFail() {
            onSuccess();
        }

        @Override
        public void onAbort() {
        }
    }

    class NeedleProcess extends Process {
        private long currentTime = 0;
        public NeedleProcess() {

        }

        @Override
        public void onUpdate(long deltaMs) {
            currentTime += deltaMs;
            Log.i("Collision", currentTime + " maxTime " + realSpeedCone );
            if (currentTime < 18/realSpeedCone * 1000 && realSpeedCone > 20) {
                angleNeedle -= 0.3;
//                Log.i("Collison", "" + normalize(angle));
                needleView.rotate(normalize(angleNeedle));
            } else {
                angleNeedle += 0.06;
                if (angleNeedle > 0.01) {
                    succeed();
                }
            }
        }

        @Override
        public void onSuccess() {
            needleView.rotate(0);
            angleNeedle = 0;
        }

        @Override
        public void onFail() {

        }

        @Override
        public void onAbort() {

        }
    }

    public static float normalize(float angle) {
        double ratio = angle / 360f;
        angle -= Math.floor(ratio) * 360;
        return angle;
    }

    private float event_to_angle(View.TouchEvent event) {
        float len = distance_from_event(event);
        float cosP = event.getY() / len;
        float phi = (float) Math.acos(cosP);
        if (event.getX() > 0) {
            phi = -phi;
        }
        return normalize((float)Math.toDegrees(phi));
    }

    private float distance_from_event(View.TouchEvent event) {
        float x = event.getX();
        float y = event.getY();
        float d2 = x*x + y*y;
        return (float)Math.sqrt(d2);
    }

    private boolean isCollision() {
        int result = getResult(angleCone);
        float angleCollison = -angleNeedle + ((result + 1)*18 - 9 - angleCone) ;

        if ( angleCollison < 1.0f) {
            count ++;
            return true;

        } else {
            angleNeedle += 0.01;
            needleView.rotate(angleNeedle);
        }

        return false;
    }

    private int getResult(float angle) {
        return (int)(((int)angle - 9)/18f + 1)%20;
    }
}
