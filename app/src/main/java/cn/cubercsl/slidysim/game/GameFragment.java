package cn.cubercsl.slidysim.game;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import cn.cubercsl.slidysim.MyApplication;
import cn.cubercsl.slidysim.R;
import cn.cubercsl.slidysim.gen.ResultDao;
import cn.cubercsl.slidysim.results.Result;

public class GameFragment extends Fragment {

    private static Game game;
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
                refresh();
            }
        });
        refresh();
        return view;
    }

    /**
     * @description: refresh the time and move.
     */
    public void refresh() {
        timeView.setText(getTimeStr(game.getResult()));
        moveView.setText("Moves: " + game.getStepCount());
    }

    private class Game {
        private static final int FINISHED = 0;
        private static final int SCRAMBLED = 1;
        private static final int SOLVING = 2;

        private SquareView[] squareViews;
        private Integer[] currentState;
        private Integer stepCount = 0;
        private long startTime = 0L;
        private long endTime = 0L;
        private int state = FINISHED;

        private Handler handler = new Handler();
        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                refresh();
                handler.postDelayed(this, 20);
            }
        };


        /**
         * @param view the view of the game
         * @description: construct a new game instance.
         */
        Game(View view) {
            System.err.println("Start initialize...");
            currentState = new Integer[16];
            SquareListener squareListener = new SquareListener();
            System.arraycopy(Config.winState, 0, currentState, 0, 16);
            squareViews = new SquareView[16];
            try {
                for (int i = 0; i < 16; i++) {
                    squareViews[i] = view.findViewById(R.id.class.getDeclaredField("square" + i).getInt(R.id.class));
                    squareViews[i].setOnClickListener(squareListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            showCurrentState();
            System.err.println("ok");
        }

        /**
         * @param row the row number of the cell (0-indexed)
         * @param col the column number of the cell (0-indexed)
         * @return the index of the cell in the array.
         */
        private int indexOf(int row, int col) {
            return row * 4 + col;
        }

        /**
         * @description: refresh the UI.
         */
        private void showCurrentState() {
            for (int i = 0; i < 16; i++) {
                squareViews[i].setNum(currentState[i]);
            }
        }

        /**
         * @return whether the puzzle is solved.
         */
        private boolean isSolved() {
            return Arrays.equals(currentState, Config.winState);
        }

        /**
         * @return the step of the game.
         */
        int getStepCount() {
            return stepCount;
        }

        /**
         * @return the result or current time of the solving..
         */
        long getResult() {
            if (state == SOLVING) {
                return System.currentTimeMillis() - startTime;
            }
            if (state == SCRAMBLED) {
                return 0;
            }
            return endTime - startTime;
        }

        /**
         * @description: if solving,update the step.
         */
        private void updateStepCount() {
            if (state == SOLVING) {
                stepCount++;
            }
        }

        /**
         * @param num the permutation of the puzzle
         * @return whether it is solvable
         * @description: check whether the puzzle is solvable
         */
        private boolean isSolvable(Integer[] num) {
            int sum = 0;
            for (int i = 0; i < 16; i++) {
                if (num[i].equals(0)) {
                    sum += 3 - i / 4;
                } else {
                    for (int j = 0; j < i; j++) {
                        if (num[j] > num[i]) {
                            sum++;
                        }
                    }
                }
            }
//        Toast.makeText(MyApplication.getContext(), String.valueOf(sum), Toast.LENGTH_SHORT).show();
            return (sum & 1) == 0;
        }

        /**
         * @description: scramble the puzzle
         */
        public void scramble() {
            System.err.println("Scrambling...");
            Vector<Integer> vector = new Vector<>();
            for (int i = 0; i < 16; i++) {
                vector.add(i);
            }
            Random random = new Random();
            Collections.shuffle(vector, random);
            Integer[] num = new Integer[vector.size()];
            vector.toArray(num);

            /*
             * handle the parity.
             */
            if (!isSolvable(num)) {
                int r1 = random.nextInt(16);
                if (num[r1].equals(0)) {
                    r1 = (r1 + 1) % 16;
                }
                int r2 = random.nextInt(16);
                while (r1 == r2 || num[r2].equals(0)) {
                    r2 = (r2 + 1) % 16;
                }
                int t = num[r1];
                num[r1] = num[r2];
                num[r2] = t;
            }

            System.arraycopy(num, 0, currentState, 0, 16);

            showCurrentState();
            state = SCRAMBLED;
            stepCount = 0;
            if (isSolvable(num)) {
                // if the puzzle is solvable,..
                Toast.makeText(MyApplication.getContext(), "Scrambled!", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * @param num the number being moved.
         * @description: move one or more square.
         * Start game when the puzzle has been scrambled
         * *  and have made a valid move.
         */
        private void play(int num) {
            if (num == 0) return;
            int index = Arrays.asList(currentState).indexOf(num);
            int row = index / 4, col = index % 4;
            int zero = Arrays.asList(currentState).indexOf(0);
            int row0 = zero / 4, col0 = zero % 4;
            if (row != row0 && col != col0) {
                return;
            }
            if (state == SCRAMBLED) {
                state = SOLVING;
                startTime = System.currentTimeMillis();
                startTiming();
            }
            if (row == row0) {
                // vertical
                if (col > col0) {
                    for (int i = col0; i < col; i++) {
                        currentState[indexOf(row, i)] = currentState[indexOf(row, i + 1)];
                        updateStepCount();
                    }
                    currentState[indexOf(row, col)] = 0;
                } else {
                    for (int i = col0; i > col; i--) {
                        currentState[indexOf(row, i)] = currentState[indexOf(row, i - 1)];
                        updateStepCount();
                    }
                    currentState[indexOf(row, col)] = 0;
                }
            } else {
                // horizontal
                if (row > row0) {
                    for (int i = row0; i < row; i++) {
                        currentState[indexOf(i, col)] = currentState[indexOf(i + 1, col)];
                        updateStepCount();
                    }
                    currentState[indexOf(row, col)] = 0;
                } else {
                    for (int i = row0; i > row; i--) {
                        currentState[indexOf(i, col)] = currentState[indexOf(i - 1, col)];
                        updateStepCount();
                    }
                    currentState[indexOf(row, col)] = 0;
                }
            }

            showCurrentState();
            if (state == SOLVING && isSolved()) {
                Toast.makeText(MyApplication.getContext(), "Solved!", Toast.LENGTH_SHORT).show();
                endTime = System.currentTimeMillis();
                state = FINISHED;
                stopTiming();
                ResultDao resultDao = MyApplication.getInstance().getDaoSession().getResultDao();
                resultDao.insert(new Result(null, getResult(), getStepCount(), startTime));
            }

        }

        /**
         * @description: start Time Thread
         */
        private void startTiming() {
            handler.postDelayed(runnable, 20);
        }

        /**
         * @description: stop Time Thread
         * force refresh before stop!
         */
        private void stopTiming() {
            refresh();
            handler.removeCallbacks(runnable);
        }

        private class SquareListener implements AppCompatTextView.OnClickListener {

            /**
             * @param view the square being clicked
             * @description: listen and handle the move of square
             */
            @Override
            public void onClick(View view) {
                int num = ((SquareView) view).getNum();
                play(num);
            }
        }
    }
}
