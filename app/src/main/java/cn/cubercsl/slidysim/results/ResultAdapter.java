package cn.cubercsl.slidysim.results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cubercsl.slidysim.R;

public class ResultAdapter extends ArrayAdapter<Result> {

    private int resource;

    public ResultAdapter(Context context, int resource, List<Result> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    /**
     * @param time the time to format
     * @return the format time string
     * @description: format the time to string.
     */
    private static String getTimeStr(long time) {
        if (time < 0) {
            return "N/A";
        }
        int ms = (int) (time % 1000);
        int sec = (int) (time / 1000);
        int min = sec / 60;
        sec %= 60;
        return String.format("%1$02d:%2$02d.%3$03d", min, sec, ms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Result result = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource, null);

        TextView time = view.findViewById(R.id.time);
        time.setText(getTimeStr(result.getTime()));

        TextView moves = view.findViewById(R.id.moves);
        moves.setText(String.valueOf(result.getMoves()) + " moves");

//        TextView timeStamp = view.findViewById(R.id.time_stamp);
//        timeStamp.setText(new Date(result.getTimeStamp()).toString());
        return view;
    }
}
