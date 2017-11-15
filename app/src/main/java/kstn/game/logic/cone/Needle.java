package kstn.game.logic.cone;

import android.graphics.Bitmap;

import java.io.IOException;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.process.Process;
import kstn.game.logic.process.ProcessManager;
import kstn.game.view.asset.AssetManager;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.ViewGroup;

/**
 * Created by qi on 04/11/2017.
 */

public class Needle {

    private ViewGroup rootviewGroup;
    public ImageView needleView;

    private final ProcessManager processManager;
    private boolean isStartCollison = true;
    public Needle(final ProcessManager processManager,
                  AssetManager assetManager,
                  final EventManager eventManager) {
        this.rootviewGroup = rootviewGroup;
        this.processManager = processManager;
        Bitmap image = null;
        try {
            image = assetManager.getBitmap("kim.png");
        } catch (IOException e) {
        }
        needleView = new ImageView(0.0f, -0.95f, 0.1f, 0.2f, image);
        eventManager.addListener(NeedleEventType.COLLISION, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                float angleStop = ((NeedleCollisonEventData) event).getAngle();
                processManager.attachProcess(new NeedleProcess(angleStop));
            }
        });


    }

    class NeedleProcess extends Process {
        private float time = 0;
        private float angle = 0;
        private float angleStop;
        public NeedleProcess(float angelStop) {
            this.angleStop = angelStop;
        }

        @Override
        public void onUpdate(long deltaMs) {
            isStartCollison = false;
            if (time < 1) {
                time += 0.1;
                angle += 0.9;
                needleView.rotate(-angle);
            } else {
                angle -= 0.06;
                if (angle < 0.01) {
                    succeed();
                }
            }

        }

        @Override
        public void onSuccess() {
            needleView.rotate(0);
            isStartCollison = true;
        }

        @Override
        public void onFail() {
        }

        @Override
        public void onAbort() {
        }


    }

    public boolean isStartCollison() {
        return isStartCollison;
    }


}
