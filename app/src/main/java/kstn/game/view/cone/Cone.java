package kstn.game.view.cone;

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
    private float speedStart;
    private long timeToInitSpeed;

    // for handling touch events
    private float baseAngle = 0;
    private float startAngle = 0;
    private float endAngle = 0;
    private boolean allowRotate = false;
    private boolean isRotating = false;
    private boolean isEnabled = true;
    private Needle gameNeedle;

    public Cone(ProcessManager processManager_,
                AssetManager assetManager,
                EventManager eventManager_,
                final BaseTimeManager timeManager,
                Needle gameNeedle,
                ViewGroup rootViewGroup) {
        this.rootViewGroup = rootViewGroup;
        this.eventManager = eventManager_;
        this.processManager = processManager_;
        this.timeManager = timeManager;
        this.gameNeedle = gameNeedle;
        Bitmap image = null;

        try {
            image = assetManager.getBitmap("non.png");
        } catch (IOException e) {
            Log.e("Cone", "Can't load non.png");
        }
        coneView = new ImageView(0, 0.6f, 1.2f, 1.2f, image);


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
                        Cone.this.timeToInitSpeed = timeManager.getCurrentMillis();
                    } else allowRotate = false;
                }
                else if (allowRotate && event.getType() == View.TouchEvent.TOUCH_MOVE) {
                    endAngle = event_to_angle(event);
                    eventManager.trigger(new ConeMoveEventData(
                            normalize(baseAngle + endAngle - startAngle)));

                }
                else if (allowRotate && event.getType() == View.TouchEvent.TOUCH_UP) {
                    timeToInitSpeed = timeManager.getCurrentMillis() - timeToInitSpeed;
                    endAngle = event_to_angle(event);
                    eventManager.trigger(new ConeAccelerateEventData(
                            normalize(baseAngle + endAngle - startAngle)));
                    speedStart = normalize((baseAngle + endAngle - startAngle)/timeToInitSpeed);
                    Log.i("SpeedStart ", speedStart + " " );
                    Log.i("Time " , timeToInitSpeed + " ");
                }
                return true;
            }
        });

        eventManager.addListener(ConeEventType.MOVE, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                float angle = ((ConeMoveEventData) event).getAngle();
                coneView.rotate(angle);
                Cone.this.angle = angle;
            }
        });
        eventManager.addListener(ConeEventType.ACCELERATE, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                isRotating = true;
                float angle = ((ConeAccelerateEventData) event).getAngle();
                coneView.rotate(angle);
                Cone.this.angle = angle;
                ConeProcess coneProcess = new ConeProcess();
                processManager.attachProcess(coneProcess);
            }
        });
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
        private long time = 0;
        private int result;

        public ConeProcess() {
            this.speed = speedStart;
        }

        @Override
        public void onUpdate(long deltaMs) {
            if (speed > 0) {
                time += 1;
                Log.i("Time", "" + speed + " " + time+ " " + angle);
                if (time < speedStart/0.75) {
                    angle += 0.5*time/100;
                }
                else if (time < 900) angle += 0.5*speedStart/75;
                else if ((time - speedStart/0.5)%10 < 5 ) {
                    speed -= 0.5;
                    angle += speed/100;
                }

                angle = normalize(angle);
                coneView.rotate(angle);
            }else {
                succeed();
            }
        }

        @Override
        public void onSuccess() {
            result = (int)(((int)angle - 9)/18f + 1)%20;
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
        return false;
    }
}
