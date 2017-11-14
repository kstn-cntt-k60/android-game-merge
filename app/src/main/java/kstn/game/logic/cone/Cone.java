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
    private boolean hidden = true;
    private final EventManager eventManager;
    private final ProcessManager processManager;
    private final BaseTimeManager timeManager;

    private float angle = 0;
    private long timeToInitSpeed;

    // for handling touch events
    private float baseAngle = 0;
    private float startAngle = 0;
    private float endAngle = 0;
    private  float movenAngleBefore = 0;

    private boolean allowRotate = false;
    private boolean isRotating = false;
    private boolean isEnabled = true;
    private Needle gameNeedle;

    // Listeners
    EventListener moveEventListener;
    EventListener accelEventListener;

    public Cone(ProcessManager processManager_,
                AssetManager assetManager,
                EventManager eventManager_,
                BaseTimeManager timeManager,
                ViewGroup rootViewGroup) {
        this.rootViewGroup = rootViewGroup;
        this.eventManager = eventManager_;
        this.processManager = processManager_;
        this.timeManager = timeManager;
        this.gameNeedle = new Needle(this.processManager, assetManager, this.eventManager);
        Bitmap image = null;

        try {
            image = assetManager.getBitmap("non.png");
        } catch (IOException e) {
            Log.e("Cone", "Can't load non.png");
        }
        coneView = new ImageView(0, -1f, 1.2f, 1.2f, image);

        coneView.setTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View.TouchEvent event) {
                if (isRotating || !isEnabled)
                    return false;

                if (event.getType() == View.TouchEvent.TOUCH_DOWN) {
                    baseAngle = angle;
                    if (distance_from_event(event) >= 0.2f) {
                        allowRotate = true;
                        startAngle = event_to_angle(event);
                        Log.i("StartAngle", startAngle + " ");
                        Cone.this.timeToInitSpeed = Cone.this.timeManager.getCurrentMillis();
                    } else allowRotate = false;
                }
                else if (allowRotate && event.getType() == View.TouchEvent.TOUCH_MOVE) {
                    endAngle = event_to_angle(event);
                    eventManager.trigger(new ConeMoveEventData(
                            normalize(baseAngle + endAngle - startAngle)));

                }
                else if (allowRotate && event.getType() == View.TouchEvent.TOUCH_UP) {
                    timeToInitSpeed = Cone.this.timeManager.getCurrentMillis() - timeToInitSpeed;
                    endAngle = event_to_angle(event);
                    float deltaAngle = startAngle - endAngle;
                    float speedStart;
                    if (deltaAngle > 0 && deltaAngle < 180)
                        speedStart = deltaAngle/timeToInitSpeed;
                    else
                        speedStart = normalize(endAngle - startAngle)/timeToInitSpeed;
                    eventManager.trigger(new ConeAccelerateEventData(normalize(baseAngle + endAngle - startAngle), speedStart));
                }
                return true;
            }
        });

        moveEventListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                float angle = ((ConeMoveEventData) event).getAngle();
                float movenAngleAfter = normalize(baseAngle + endAngle - startAngle);
                Log.i("Result", "before :" + movenAngleBefore + "====== after: " + movenAngleAfter);
                if (movenAngleAfter > movenAngleBefore) {
                    Cone.this.rotate(angle);
                    movenAngleBefore = movenAngleAfter;
                }
            }
        };

        accelEventListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                isRotating = true;
                float angle = ((ConeAccelerateEventData) event).getAngle();
                Cone.this.rotate(angle);
                float speedStart = ((ConeAccelerateEventData) event).getSpeedStart();
                ConeProcess coneProcess = new ConeProcess(speedStart);
                processManager.attachProcess(coneProcess);
            }
        };

    }

    private void rotate(float angle) {
        coneView.rotate(angle);
        this.angle = angle;
        if (isCollision()) {
            eventManager.queue(new NeedleCollisonEventData(0));
        }
    }


    public void entry() {
        Log.i("Cone", "entry");
        rootViewGroup.addView(coneView);
        rootViewGroup.addView(gameNeedle.needleView);
        eventManager.addListener(ConeEventType.MOVE, moveEventListener);
        eventManager.addListener(ConeEventType.ACCELERATE, accelEventListener);
    }

    public void exit() {
        Log.i("Cone", "exit");
        rootViewGroup.removeView(coneView);
        rootViewGroup.removeView(gameNeedle.needleView);
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

        public ConeProcess(float speedStart) {
            this.speed = 2*speedStart;
            Log.i("Speed init", "" + speed);
        }


        @Override
        public void onUpdate(long deltaMs) {
            if (speed > 0) {
                speed -= deltaMs/3500f;
                if (isCollision()) {
                    eventManager.queue(new NeedleCollisonEventData(speed));
                    speed -= 0.005;
                }
                angle += speed;
                angle = normalize(angle);
                Cone.this.rotate(angle);
            }else {
                if (isCollision()){
                    angle -= 0.5;
                    Cone.this.rotate(angle);
                }
                succeed();
            }
        }

        @Override
        public void onSuccess() {
            result = getResult(angle);
            Log.i("Cone", "Show Result: " + result);
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

    private float normalize(float angle) {
        if (angle < 0)
            return angle + 360;
        else if (angle >= 360)
            return angle - 360;
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
        int result = getResult(angle);
        if ( gameNeedle.isStartCollison() && (result + 1)*18 - 9 - angle < 1.5){
            return true;
        }
        return false;
    }

    private int getResult(float angle) {
        return (int)(((int)angle - 9)/18f + 1)%20;
    }
}
