package kstn.game.view.state.singleplayer;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import kstn.game.MainActivity;
import kstn.game.R;

/**
 * Created by tung on 15/11/2017.
 */

public class DialogManager {
    private Dialog hopthoai;

    public DialogManager(MainActivity activity, View v, int height){
        this.hopthoai = new Dialog(activity, R.style.Theme_Dialog);
        hopthoai.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        hopthoai.setContentView(v);
        WindowManager.LayoutParams w = hopthoai.getWindow().getAttributes();
        w.gravity = Gravity.BOTTOM;
        w.x = 0;
        w.height = (int) (height * 0.53);
        hopthoai.getWindow().setAttributes(w);
    }

    public Dialog getHopthoai() {
        return hopthoai;
    }
}
