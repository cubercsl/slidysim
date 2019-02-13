package cn.cubercsl.slidysim;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuAbout:
                Toast.makeText(this, "Course Project of Java.\nAuthor: cubercsl", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mainMenuExit:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TimeView timeView = findViewById(R.id.timeView1);
        final TextView textView = findViewById(R.id.textView1);
        initSquareView();
        textView.setText("Moves:" + Game.getStepCount());
        timeView.refresh();

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    timeView.refresh();
                    textView.setText("Moves:" + Game.getStepCount());
                }
                super.handleMessage(msg);
            }
        };

        Thread thread = new Thread() {

            @Override
            public void run() {
                int preState = Game.FINISHED;
                while (true) {
                    int curState = Game.getState();
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
                    }
                }
            }
        };

        thread.start();
    }

    private void initSquareView() {
        SquareView[] squareViews = new SquareView[16];
        for (int i = 0; i < 16; i++) {
            try {
                squareViews[i] = findViewById(R.id.class.getDeclaredField("square" + i).getInt(R.id.class));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        Game.setSquareViews(squareViews);
        Game.initialize();
    }

    public void scrambleOnClick(View view) {
        Game.scramble();
    }
}
