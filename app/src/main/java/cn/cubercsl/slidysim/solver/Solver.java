package cn.cubercsl.slidysim.solver;

import java.util.Vector;

/**
 * @description: Solve the puzzle by IDA*. Too slow for java, just for test.
 * @deprecated
 */
public class Solver {

    private static final int MAX = 60;
    private static final int dx[] = {-1, 1, 0, 0};
    private static final int dy[] = {0, 0, -1, 1};
    private Vector<Integer> path = new Vector<>();
    private int[] puzzle = new int[16];

    public Solver(Integer[] puzzle) {
        for (int i = 0; i < 16; i++) {
            this.puzzle[i] = puzzle[i];
        }
    }

    public static void main(String[] args) {
        Integer[] test = {1, 2, 3, 4, 6, 7, 8, 0, 5, 10, 11, 12, 9, 13, 14, 15};
        Solver solver = new Solver(test);
        System.out.println(solver.solve().toString());
    }

    private int h() {
        int h = 0;
        for (int i = 0; i < 16; i++) {
            if (puzzle[i] != 0) {
                h += Math.abs(i / 4 - (puzzle[i] - 1) / 4) + Math.abs(i % 4 - (puzzle[i] - 1) % 4);
            }
        }
        return h;
    }

    private void swap(int x, int y) {
        int t = puzzle[x];
        puzzle[x] = puzzle[y];
        puzzle[y] = t;
    }

    private boolean dfs(int curPos, int currentStep, int prevDirection, int bound) {
        int limit = h();
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
            if (dfs(nextPos, currentStep + 1, i, bound)) {
                return true;
            }
            swap(curPos, nextPos);
            path.remove(path.size() - 1);
        }
        return false;
    }

    private boolean idaStar(int pos) {
        for (int i = h(); i < MAX; i++) {
            if (dfs(pos, 0, -1, i)) {
                return true;
            }
        }
        return false;
    }

    public Vector<Integer> solve() {
        int pos = -1;
        for (int i = 0; i < 16; i++) {
            if (puzzle[i] == 0) {
                pos = i;
            }
        }
        if (pos != -1) {
            idaStar(pos);
        }
        return path;
    }

}
