package cn.cubercsl.slidysim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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
}
