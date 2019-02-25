package cn.cubercsl.slidysim.solver;

/**
 * @description: An Improved Admissible Heuristic
 * http://www.aaai.org/Papers/AAAI/1996/AAAI96-178.pdf
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
        for (int row = 0; row < SIZE; row++) {
            int max = -1;
            for (int col = 0; col < SIZE; col++) {
                int cellValue = cells[row * SIZE + col];
                // is tile in its goal row ?
                if (cellValue != 0 && (cellValue - 1) / SIZE == row) {
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
        for (int col = 0; col < SIZE; col++) {
            int max = -1;
            for (int row = 0; row < SIZE; row++) {
                int cellValue = cells[row * SIZE + col];
                // is tile in its goal row ?
                if (cellValue != 0 && cellValue % SIZE == col + 1) {
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
