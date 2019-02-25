package cn.cubercsl.slidysim.solver;

public class ManhattanDistance implements Heuristic {

    protected static final int SIZE = 4;

    @Override
    public int calculate(int[] cells) {
        int counter = 0;
        for (int i = 0; i < cells.length; i++) {
            int value = cells[i];
            if (value == 0) {
                continue;
            }
            int row = i / SIZE;
            int col = i % SIZE;
            int expectRow = (value - 1) / SIZE;
            int expectCol = (value - 1) % SIZE;
            int difference = Math.abs(expectCol - col) + Math.abs(expectRow - row);
            counter += difference;
        }
        return counter;
    }
}
