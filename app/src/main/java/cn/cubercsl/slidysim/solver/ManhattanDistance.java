package cn.cubercsl.slidysim.solver;

public class ManhattanDistance implements Heuristic {

    @Override
    public int calculate(int[] cells) {
        int counter = 0;
        for (int i = 0; i < cells.length; i++) {
            int value = cells[i];
            if (value == 0) {
                continue;
            }
            int row = i / 4;
            int col = i % 4;
            int expectRow = (value - 1) / 4;
            int expectCol = (value - 1) % 4;
            int difference = Math.abs(expectCol - col) + Math.abs(expectRow - row);
            counter += difference;
        }
        return counter;
    }
}
