package kstn.game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import kstn.game.app.root.Root;
import kstn.game.app.screen.GameAnimationView;
import kstn.game.view.thang.fragment.AFragment;

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

        AddFragment(R.id.anotherMyLayout, new AFragment());
    }

    public void AddFragment(int id, Fragment fragment){
        String name = fragment.getClass().getName();
        FragmentManager quanlyFragment = getSupportFragmentManager();
        FragmentTransaction transaction = quanlyFragment.beginTransaction();
        transaction.replace(id,fragment);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.myLayout);
        if(!(fragment instanceof AFragment)){
            super.onBackPressed();
        }
        else{
            super.onBackPressed();
            System.exit(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }

    @Override
    protected void onPause() {
        gameView.onPause();
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
