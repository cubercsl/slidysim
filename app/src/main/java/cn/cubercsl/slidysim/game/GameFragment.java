package cn.cubercsl.slidysim.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cubercsl.slidysim.R;

public class GameFragment extends Fragment {

    private Game game;
    private TextView timeView, moveView;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game, container, false);
        game = new Game(view);
        timeView = view.findViewById(R.id.timeView);
        moveView = view.findViewById(R.id.moveView);
        view.findViewById(R.id.scramble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.scramble();
            }
        });
        refresh();
        return view;
    }

    /**
     * @return the current game state
     */
    public int getState() {
        if (game == null) {
            return Game.FINISHED;
        }
        return game.getState();
    }

    /**
     * @description: refresh the time and move.
     */
    public void refresh() {
        timeView.setText(getTimeStr(game.getResult()));
        moveView.setText("Moves: " + game.getStepCount());
    }
}
