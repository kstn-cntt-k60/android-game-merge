package kstn.game;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import kstn.game.app.root.Root;
import kstn.game.app.screen.GameAnimationView;
import kstn.game.view.thang.fragment.MenuFragment;

public class MainActivity extends AppCompatActivity {
    private GameAnimationView gameView;
    private Root root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set FullScreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        gameView = (GameAnimationView) findViewById(R.id.game_animation);
        root = new Root(this, gameView);
        root.init();

        // MenuFragment.uiEventManager = root.getUiEventManager();
    }

    public void addFragment(int id, Fragment fragment){
        FragmentManager quanlyFragment = getSupportFragmentManager();
        FragmentTransaction transaction = quanlyFragment.beginTransaction();
        transaction.replace(id,fragment);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        root.onBack();
    }

    @Override
    public void onPause() {
        gameView.onPause();
        super.onPause();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        gameView.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        gameView.clearAnimation();
        super.onDestroy();
    }
}
