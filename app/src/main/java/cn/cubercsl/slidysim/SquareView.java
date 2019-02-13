package cn.cubercsl.slidysim;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

public class SquareView extends AppCompatTextView implements AppCompatTextView.OnClickListener {

    public int num;

    public SquareView(Context context, AttributeSet attr) {
        super(context, attr);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
        setGravity(Gravity.CENTER);
        setOnClickListener(this);
    }

    public void setNum(int num) {
        this.num = num;
        setText(String.valueOf(num));
        if (num == 0) {
            setTextColor(0x00000000);
            setBackgroundColor(0x00000000);
        } else {
            setTextColor(Color.BLACK);
            try {
                setBackgroundResource(Config.color[num]);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        Game.play(num);
    }
}
