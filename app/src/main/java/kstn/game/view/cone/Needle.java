package kstn.game.view.cone;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

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
 * Created by qi on 04/11/2017.
 */

public class Needle {
    private ViewGroup rootviewGroup;
    private ImageView needleView;
    private boolean isHidden = false;

    public Needle(final ProcessManager processManager,
                  AssetManager assetManager,
                  final EventManager eventManager,
                  ViewGroup rootviewGroup) {
        this.rootviewGroup = rootviewGroup;
        Bitmap image = null;
        try {
            image = assetManager.getBitmap("kim.png");
        } catch (IOException e) {
        }
        needleView = new ImageView(0.0f, 1.25f, 0.1f, 0.2f, image);

        needleView.setTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View.TouchEvent event) {
                if (event.getType() == View.TouchEvent.TOUCH_UP) {
                    Log.i(getClass().getName(), "Su kien va cham voi non");
//                    eventManager.trigger(new NeedleMoveEventData(10));
                }

                if (event.getType() == View.TouchEvent.TOUCH_MOVE) {
                    float y = event.getY();
                    float x = event.getX();

                    Log.i("Su kien:", "Cham kim");

                    eventManager.trigger(new NeedleMoveEventData(x, y));

                }
                return true;
            }
        });
        eventManager.addListener(NeedleEventType.COLLISION, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                //setgoc
//                float angle = ((NeedleMoveEventData) event).getAngle();
//                needleView.rotate(angle);
            }
        });

        eventManager.addListener(NeedleEventType.MOVE, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                event = (NeedleMoveEventData) event;
                needleView.rotate(30);
                needleView.setCenter(((NeedleMoveEventData) event).getX(), ((NeedleMoveEventData) event).getY());
                Log.i("Vi tri ", ((NeedleMoveEventData) event).getX() + " " + ((NeedleMoveEventData) event).getY());
//                processManager.attachProcess(new KimProcess(needleView));
            }
        });



    }

    class KimProcess extends Process {
        private final ImageView imageView;
        private float angle = 0;

        public KimProcess(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void onUpdate(long deltaMs) {
            angle += (deltaMs * 90) / 1000.0f;
            System.out.println("quay kim" + angle);
            imageView.rotate(angle);
        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onFail() {
        }

        @Override
        public void onAbort() {
        }


    }
    public void show() {
        if (!isHidden) rootviewGroup.addView(needleView);
    }
}
