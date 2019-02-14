package cn.cubercsl.slidysim.game;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

public class SquareView extends AppCompatTextView {

    private int num;

    public SquareView(Context context, AttributeSet attr) {
        super(context, attr);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
        setGravity(Gravity.CENTER);
    }

    /**
     * @return the number of the square.
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num the number to set
     * @description: set the number for the Square and change the color.
     * @see Config#color
     */
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
}