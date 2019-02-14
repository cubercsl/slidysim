package cn.cubercsl.slidysim.game;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import cn.cubercsl.slidysim.MyApplication;
import cn.cubercsl.slidysim.R;

public class Game {

    public static int FINISHED = 0;
    public static int SCRAMBLED = 1;
    public static int SOLVING = 2;

    private SquareView[] squareViews;
    private Integer[] currentState;
    private Integer stepCount = 0;
    private long startTime = 0L;
    private long endTime = 0L;
    private int state = FINISHED;

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
    private static int indexOf(int row, int col) {
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
     * @return the state of the game.
     * @see Game#FINISHED,Game#SCRAMBLED.Game#SOLVING
     */
    int getState() {
        return state;
    }

    /**
     * @description: if solving,update the step.
     */
    private void updateStepCount() {
        if (getState() == SOLVING) {
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
        if (getState() == SOLVING && isSolved()) {
            Toast.makeText(MyApplication.getContext(), "Solved!", Toast.LENGTH_SHORT).show();
            endTime = System.currentTimeMillis();
            state = FINISHED;
        }

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
