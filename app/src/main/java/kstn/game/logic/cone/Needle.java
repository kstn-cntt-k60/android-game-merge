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

/**
 * Created by qi on 04/11/2017.
 */
///sai mai sant sua

public class Needle {

    public ImageView needleView;

    private float speed;
    public float angle = 0;

    private final float deltaAngle = 1.8f;

    private final ProcessManager processManager;
    public Needle(final ProcessManager processManager,
                  AssetManager assetManager,
                  final EventManager eventManager) {
        this.processManager = processManager;
        final NeedleProcess needleProcess = new NeedleProcess();
        Bitmap image = null;
        try {
            image = assetManager.getBitmap("kim.png");
        } catch (IOException e) {
        }
        needleView = new ImageView(0.0f, -0.75f, 0.1f, 0.2f, image);
        eventManager.addListener(NeedleEventType.COLLISION, new EventListener() {
            @Override
            public void onEvent(EventData event) {

            }
        });
    }

    class NeedleProcess extends Process {
        private long currentTime = 0;
        public NeedleProcess() {

        }

        @Override
        public void onUpdate(long deltaMs) {


        }

        @Override
        public void onSuccess() {
            needleView.rotate(0);
            angle = 0;
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
    public float getAngle() {
        return angle;
    }

}
