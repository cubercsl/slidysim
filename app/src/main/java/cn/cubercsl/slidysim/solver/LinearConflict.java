package cn.cubercsl.slidysim.solver;

/**
 * @description: Manhattan Distance + Linear Conflict
 * O. Hansson, A. Mayer, and M. Yung, "Criticizing Solutions to Relaxed Models Yields Powerful Admissible Heuristics,"
 * Information Sciences, Vol. 63, Issue 3, pp. 207-227, 1992.
 */
public class LinearConflict extends ManhattanDistance implements Heuristic {

    @Override
    public int calculate(int[] cells) {
        int heuristic = super.calculate(cells);
        heuristic += linearVerticalConflict(cells);
        heuristic += linearHorizontalConflict(cells);
        return heuristic;
    }

    private int linearVerticalConflict(int[] cells) {
        int linearConflict = 0;
        for (int row = 0; row < 4; row++) {
            int max = -1;
            for (int col = 0; col < 4; col++) {
                int cellValue = cells[row * 4 + col];
                // is tile in its goal row ?
                if (cellValue != 0 && (cellValue - 1) / 4 == row) {
                    if (cellValue > max) {
                        max = cellValue;
                    } else {
                        // linear conflict, one tile must move up or down to allow the other to pass by and then back up
                        // add two moves to the manhattan distance
                        linearConflict += 2;
                    }
                }

            }

        }
        return linearConflict;
    }

    private int linearHorizontalConflict(int[] cells) {
        int linearConflict = 0;
        for (int col = 0; col < 4; col++) {
            int max = -1;
            for (int row = 0; row < 4; row++) {
                int cellValue = cells[row * 4 + col];
                // is tile in its goal row ?
                if (cellValue != 0 && cellValue % 4 == col + 1) {
                    if (cellValue > max) {
                        max = cellValue;
                    } else {
                        // linear conflict, one tile must move left or right to allow the other to pass by and then back up
                        // add two moves to the manhattan distance
                        linearConflict += 2;
                    }
                }
            }
        }
        return linearConflict;
    }

}
