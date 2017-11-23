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

public class Cone {
    private final  ViewGroup rootViewGroup;
    private final ImageView coneView;
    private final ImageView needleView;
    private boolean hidden = true;
    private final EventManager eventManager;
    private final ProcessManager processManager;
    private final BaseTimeManager timeManager;
    private ConeProcess coneProcess = null;

    private double angleCone = 0;
    private long timeToInitSpeed;
    private double realSpeedCone = 0;
    private double speed;


    private double angleNeedle = 0;
    // for handling touch events
    private double baseAngle = 0;
    private double startAngle = 0;
    private double endAngle = 0;
    private double touchStartX = 0;


    private boolean allowRotate = false;
    private boolean isRotating = false;
    private boolean isEnabled = true;
    private boolean isMoveEvent = false;


    private EventListener moveEventListener;
    private EventListener accelEventListener;

    private final double xBias = 0.05f;
    private final double minRadius = 0.2f;
    private final double minVelocity = 180f; // degree / seconds
    private final double decreaseAccel = -50f;

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
            imageCone = assetManager.getBitmap("non1.png");
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
                    isMoveEvent = true;
                    endAngle = event_to_angle(event);
                    double touchCurrentX = event.getX();
                    if (touchCurrentX - xBias < touchStartX) {
                        eventManager.trigger(new ConeMoveEventData((float) normalize(baseAngle + endAngle - startAngle)));
                        touchStartX = touchCurrentX;
                    }
                    else {
                        allowRotate = false;
                    }
                }
                else if (allowRotate && event.getType() == View.TouchEvent.TOUCH_UP) {
                    isMoveEvent = false;
                    long timeDelta = Cone.this.timeManager.getCurrentMillis() - timeToInitSpeed;
                    endAngle = event_to_angle(event);
                    double speedStart;
                    speedStart = normalize(endAngle - startAngle) * 1000 / timeDelta;

                    if (speedStart >= minVelocity) {
                        eventManager.trigger(
                                new ConeAccelerateEventData(
                                        (float) normalize(baseAngle + endAngle - startAngle), (float) speedStart));
                    }
                }
                return true;
            }
        });

        moveEventListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                double angle = ((ConeMoveEventData) event).getAngle();
                Cone.this.rotate(angle);
            }
        };

        accelEventListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                isRotating = true;
                double angle = ((ConeAccelerateEventData) event).getAngle();
                Cone.this.rotate(angle);
                double speedStart = ((ConeAccelerateEventData) event).getSpeedStart();
                coneProcess = new ConeProcess(speedStart);
                processManager.attachProcess(coneProcess);
            }
        };

    }

    private void rotate(double angle) {
        if (isCollision()) {
            angleNeedle = -20;
        } else {
            if (isMoveEvent) {
                angleNeedle += 2;
                if (angleNeedle > 0.01) angleNeedle = 0;
            }else {
                angleNeedle += 3;
                if (angleNeedle > 0.01) {
                    angleNeedle = 0;
                }
            }
        }
        needleView.rotate((float) angleNeedle);

        this.angleCone = angle;
        coneView.rotate((float) angleCone);
    }


    public void entry() {
        angleCone = 0f;
        coneView.rotate((float) angleCone);
        rootViewGroup.addView(coneView);
        rootViewGroup.addView(needleView);
        eventManager.addListener(ConeEventType.MOVE, moveEventListener);
        eventManager.addListener(ConeEventType.ACCELERATE, accelEventListener);
    }

    public void exit() {
        if (coneProcess != null && coneProcess.isAlive())
            coneProcess.fail();

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
        private int result;
        private double endTime;
        private long currentTime;
        private double startAngle;

        public ConeProcess(double speedStart) {
            speed = speedStart;
            this.endTime = - speed * 1000 / decreaseAccel;
            currentTime = 0;
            startAngle = Cone.this.angleCone;
         }

        @Override
        public void onUpdate(long deltaMs) {
            currentTime += deltaMs;
            realSpeedCone = speed + decreaseAccel * currentTime / ( 1000);
            if (currentTime <= endTime) {
                double angle = startAngle + speed * currentTime / 1000.0f
                        + decreaseAccel * currentTime * currentTime / (2 * 1000 * 1000);
                Cone.this.rotate(normalize(angle));
            }
            else {
                double angle = startAngle + speed * endTime / 1000.0f
                        + decreaseAccel * endTime * endTime / (2 * 1000 * 1000);
                Cone.this.rotate(normalize(angle));
                succeed();
            }
        }

        @Override
        public void onSuccess() {
            angleNeedle = 0;
            needleView.rotate((float) angleNeedle);
            result = getResult(angleCone);
            eventManager.trigger(new ConeStopEventData(result));
            Cone.this.isRotating = false;
            coneProcess = null;
        }

        @Override
        public void onFail() {
            onSuccess();
        }

        @Override
        public void onAbort() {
        }
    }

    public static double normalize(double angle) {
        double ratio = angle / 360f;
        angle -= Math.floor(ratio) * 360;
        return angle;
    }

    private double event_to_angle(View.TouchEvent event) {
        double len = distance_from_event(event);
        double cosP = event.getY() / len;
        double phi = (double) Math.acos(cosP);
        if (event.getX() > 0) {
            phi = -phi;
        }
        return normalize((double)Math.toDegrees(phi));
    }

    private double distance_from_event(View.TouchEvent event) {
        double x = event.getX();
        double y = event.getY();
        double d2 = x*x + y*y;
        return (double)Math.sqrt(d2);
    }

    private boolean isCollision() {
        int result = getResult(angleCone);
        double angleCollison = -angleNeedle + ((result + 1)*18 - 9 - angleCone) ;
        if ( angleCollison < 1.0f) {
            return true;
        }
        return false;
    }

    private int getResult(double angle) {
        return (int)(((int)angle - 9)/18f + 1)%20;
    }
}
