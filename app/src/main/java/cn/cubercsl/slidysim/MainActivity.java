package cn.cubercsl.slidysim;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import cn.cubercsl.slidysim.game.Game;
import cn.cubercsl.slidysim.game.GameFragment;
import cn.cubercsl.slidysim.results.ResultFragment;

public class MainActivity extends AppCompatActivity {

    private GameFragment gameFragment = new GameFragment();
    private ResultFragment resultFragment = new ResultFragment();
    private AboutFragment aboutFragment = new AboutFragment();
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        replaceFragment(gameFragment);
                        return true;
                    case R.id.navigation_dashboard:
                        replaceFragment(resultFragment);
                        return true;
                    case R.id.navigation_notifications:
                        replaceFragment(aboutFragment);
                        return true;
                }
                return false;
            }
        });
        replaceFragment(gameFragment);
        final MyHandler handler = new MyHandler(this);

        Thread thread = new Thread() {

            @Override
            public void run() {
                int preState = Game.FINISHED;
                while (true) {
                    int curState = gameFragment.getState();
                    if (curState == Game.SOLVING ||
                            preState == Game.SOLVING && curState == Game.SCRAMBLED ||
                            preState == Game.FINISHED && curState == Game.SCRAMBLED ||
                            preState == Game.SOLVING && curState == Game.FINISHED) {
                        Message msg = new Message();
                        msg.what = 1;
                        preState = curState;
                        handler.handleMessage(msg);
                    }

                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        };

        thread.start();
    }

    private void refresh() {
        gameFragment.refresh();
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment == currentFragment) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (currentFragment != null)
            transaction.hide(currentFragment);
        if (!fragment.isAdded()) {
            transaction.add(R.id.mainview, fragment);
        }
        currentFragment = fragment;
        transaction.show(fragment).commit();
    }

    static class MyHandler extends Handler {
        private WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = weakReference.get();
            if (activity != null) {
                if (msg.what == 1) {
                    ((MainActivity) activity).refresh();
                }
            }
        }
    }
}

