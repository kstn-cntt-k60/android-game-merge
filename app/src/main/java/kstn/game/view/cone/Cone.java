package kstn.game.view.cone;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.process.ProcessManager;
import kstn.game.view.asset.AssetManager;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewGroup;

/**
 * Created by qi on 16/10/2017.
 */

public class Cone {
    private ViewGroup rootViewGroup;
    private ImageView coneView;
    private boolean hidden = true;

    public Cone(ProcessManager processManager,
                AssetManager assetManager,
                final EventManager eventManager,
                ViewGroup rootViewGroup) {
        this.rootViewGroup = rootViewGroup;
        Bitmap image = null;
        try {
            image = assetManager.getBitmap("cute.png");
        } catch (IOException e) {
            Log.e("Cone", "");
        }
        coneView = new ImageView(0, 0.6f, 1.2f, 1.2f, image);

        coneView.setTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View.TouchEvent event) {
                if (event.getType() == View.TouchEvent.TOUCH_DOWN) {
                    float x2 = event.getX() * event.getX();
                    float y2 = event.getY() * event.getY();
                    float len = (float) Math.sqrt(x2 + y2);
                    if (len > 0.6f)
                        return false;

                    float cosP = event.getY() / len;
                    float phi = (float) Math.acos(cosP);
                    if (event.getX() > 0) {
                        phi = -phi;
                    }
                    eventManager.trigger(new ConeMoveEventData((float) Math.toDegrees(phi)));
                }
                return true;
            }
        });

        eventManager.addListener(ConeEventType.MOVE, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                float angle = ((ConeMoveEventData) event).getAngle();
                coneView.rotate(angle);
            }
        });


        // rootViewGroup.addView(coneView);
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
}
