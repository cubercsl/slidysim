package cn.cubercsl.slidysim;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;

public class TimeView extends AppCompatTextView {

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void refresh() {
        long time = 0;
        int state = Game.getState();
        if (state == Game.SOLVING) {
            time = System.currentTimeMillis() - Game.getStartTime();
        } else if (state == Game.FINISHED) {
            time = Game.getEndTime() - Game.getStartTime();
        }
        time = Math.min(3600000, time);
        setText(new SimpleDateFormat("mm:ss.SSS").format(time));
    }
}
