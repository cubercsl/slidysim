package cn.cubercsl.slidysim;

import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Game {

    public static int FINISHED = 0;
    public static int SCRAMBLED = 1;
    public static int SOLVING = 2;


    private static SquareView[] squareViews;
    private static Integer[] currentState;
    private static Integer stepCount = 0;
    private static Long startTime = 0L;
    private static Long endTime = 0L;
    private static int state = FINISHED;

    private Game() {

    }

    public static void setSquareViews(SquareView[] squareViews) {
        Game.squareViews = squareViews;
    }

    public static void initialize() {
        System.err.println("Start initialize...");
        currentState = new Integer[16];
        System.arraycopy(Config.winState, 0, currentState, 0, 16);
        showCurrentState();
        System.err.println("ok");
    }

    public static void showCurrentState() {
        for (int i = 0; i < 16; i++) {
            squareViews[i].setNum(currentState[i]);
        }
    }

    public static boolean isWin() {
        return Arrays.equals(currentState, Config.winState);
    }

    public static int getStepCount() {
        return stepCount;
    }

    public static void setStepCount(int stepCount) {
        Game.stepCount = stepCount;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        Game.startTime = startTime;
    }

    public static Long getEndTime() {
        return endTime;
    }

    public static void setEndTime(Long endTime) {
        Game.endTime = endTime;
    }

    public static int getState() {
        return state;
    }

    public static void setState(int state) {
        Game.state = state;
    }

    private static void updateStepCount() {
        if (getState() == SOLVING) {
            stepCount++;
        }
    }

    public static void scramble() {
        System.err.println("Scrambling...");
        Vector<Integer> vector = new Vector<>();
        for (int i = 0; i < 16; i++) {
            vector.add(i);
        }
        Random random = new Random();
        Collections.shuffle(vector, random);
        Integer[] num = new Integer[vector.size()];
        vector.toArray(num);
        int sum = 0;
        for (int i = 0; i < 16; i++) {
            if (num[i] == 0) {
                sum += i / 4 + (i + 1) % 4;
            } else {
                for (int j = 0; j < i; j++) {
                    if (num[j] < num[i]) {
                        sum++;
                    }
                }
            }
        }
        // parity
        if ((sum & 1) != 0) {
            int r1 = random.nextInt(16);
            if (num[r1] == 0) {
                r1 = (r1 + 1) % 16;
            }
            int r2 = random.nextInt(16);
            while (r1 == r2 && num[r2] == 0) {
                r2 = (r2 + 1) % 16;
            }
            // swap
            int t = num[r1];
            num[r1] = num[r2];
            num[r2] = t;
        }

        System.arraycopy(num, 0, currentState, 0, 16);

        showCurrentState();
        setState(SCRAMBLED);
        setStepCount(0);
        System.err.println("Finished.");
    }

    private static int indexOf(int row, int col) {
        return row * 4 + col;
    }

    public static void play(int num) {
        System.out.println("Click on " + num);
        if (num == 0) return;
        if (getState() == SCRAMBLED) {
            // Start game when the puzzle has been scrambled.
            setState(SOLVING);
            setStartTime(System.currentTimeMillis());
        }
        int index = Arrays.asList(currentState).indexOf(num);
        int row = index / 4, col = index % 4;
        int zero = Arrays.asList(currentState).indexOf(0);
        int row0 = zero / 4, col0 = zero % 4;
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
        } else if (col == col0) {
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
        if (getState() == SOLVING && isWin()) {
            Toast.makeText(MyApplication.getContext(), "Solved!", Toast.LENGTH_SHORT).show();
            setEndTime(System.currentTimeMillis());
            setState(FINISHED);
        }

    }
}
