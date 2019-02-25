package cn.cubercsl.slidysim.solver;

import java.util.Vector;

/**
 * @description: Solve the puzzle by IDA*.
 * Heuristic Function: Manhattan Distance + Linear Conflict
 */
public class Solver {

    private static final int MAX = 65;
    private static final int dx[] = {-1, 1, 0, 0};
    private static final int dy[] = {0, 0, -1, 1};
    private static final Heuristic heuristic = new LinearConflict();
    private Vector<Integer> path = new Vector<>();
    private int[] puzzle = new int[16];

    public Solver(Integer[] puzzle) {
        for (int i = 0; i < 16; i++) {
            this.puzzle[i] = puzzle[i];
        }
    }

    private void swap(int x, int y) {
        int t = puzzle[x];
        puzzle[x] = puzzle[y];
        puzzle[y] = t;
    }

    private boolean depthFirstSearch(int curPos, int currentStep, int prevDirection, int bound) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException("User Interrupted.");
        }
        int limit = heuristic.calculate(puzzle);
        if (limit == 0) {
            return true;
        }
        if (currentStep + limit > bound) {
            return false;
        }
        int x = curPos / 4, y = curPos % 4;
        for (int i = 0; i < 4; i++) {
            if (i == (prevDirection ^ 1)) {
                continue;
            }
            int nx = x + dx[i], ny = y + dy[i];
            if (nx < 0 || ny < 0 || nx > 3 || ny > 3) {
                continue;
            }
            int nextPos = nx * 4 + ny;
            path.add(puzzle[nextPos]);
            swap(curPos, nextPos);
            if (depthFirstSearch(nextPos, currentStep + 1, i, bound)) {
                return true;
            }
            swap(curPos, nextPos);
            path.remove(path.size() - 1);
        }
        return false;
    }

    private void iterativeDeepeningAStar(int pos) throws RuntimeException, InterruptedException {
        if (pos == -1) {
            throw new RuntimeException("No Blank");
        }
        for (int i = heuristic.calculate(puzzle); i < MAX; i++) {
            System.out.println(i);
            if (depthFirstSearch(pos, 0, -1, i)) {
                return;
            }
        }
        throw new RuntimeException("Move Limit Exceed!");
    }

    /**
     * @return the solution path
     * @description: solve the puzzle
     */
    public Vector<Integer> solve() throws InterruptedException {
        int pos = -1;
        for (int i = 0; i < 16; i++) {
            if (puzzle[i] == 0) {
                pos = i;
            }
        }
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Start:");
            iterativeDeepeningAStar(pos);
            System.out.println("Time: " + (System.currentTimeMillis() - startTime));
        } catch (RuntimeException e) {
            return null;
        }
        return path;
    }

}
