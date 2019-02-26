package cn.cubercsl.slidysim.solver;

import android.content.res.Resources;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.cubercsl.slidysim.MyApplication;
import cn.cubercsl.slidysim.R;

/**
 * @description: Additive Pattern Database Heuristic with Static 6-6-3 Partitioning
 * A. Felner, R. Korf, and S. Hanan, "Additive Pattern Database Heuristics,"
 * Journal of AI Research, Vol. 22, pp. 279-318, 2004.
 */

public class PatternDataBase implements Heuristic {

    @Override
    public int calculate(int[] cells) {

        int index_1 = 0;
        int index_2 = 0;
        int index_3 = 0;

        for (int i = 0; i < cells.length; i++) {
            byte value = (byte) (cells[i] & 0xff);
            int position = -1;
            if ((position = PatternDatabaseType.fifteen_3_6_6_1.getPatternPosition(value)) != -1) {
                index_1 |= i << (position << 2);
            } else if ((position = PatternDatabaseType.fifteen_3_6_6_2.getPatternPosition(value)) != -1) {
                index_2 |= i << (position << 2);
            } else if ((position = PatternDatabaseType.fifteen_3_6_6_3.getPatternPosition(value)) != -1) {
                index_3 |= i << (position << 2);
            }
        }
        return PatternDatabaseType.fifteen_3_6_6_1.getCost(index_1)
                + PatternDatabaseType.fifteen_3_6_6_2.getCost(index_2)
                + PatternDatabaseType.fifteen_3_6_6_3.getCost(index_3);
    }

    public enum PatternDatabaseType {
        fifteen_3_6_6_1(new byte[]{2, 3, 4}),
        fifteen_3_6_6_2(new byte[]{1, 5, 6, 9, 10, 13}),
        fifteen_3_6_6_3(new byte[]{7, 8, 11, 12, 14, 15});

        private byte[] costTable;
        private byte[] tilesInPattern;

        private PatternDatabaseType(byte[] tilesInPattern) {
            this.tilesInPattern = tilesInPattern;
            costTable = new byte[(int) Math.pow(2, tilesInPattern.length * 4)];
            loadTableCost();
        }

        private void loadTableCost() {
            System.err.println("Load database for " + name());
            long startTime = System.currentTimeMillis();
            try {
                loadFile(name());
            } catch (IOException e) {
                throw new RuntimeException("Impossible to load databasepattern", e);
            }
            System.err.println("Database loaded in "
                    + (System.currentTimeMillis() - startTime));
        }

        private void loadFile(String fileName) throws IOException {
            try {
                Resources resources = MyApplication.getContext().getResources();
                InputStream is = resources.openRawResource(R.raw.class.getDeclaredField(fileName).getInt(R.raw.class));
                DataInputStream inputStream = new DataInputStream(
                        new BufferedInputStream(is));
                inputStream.read(costTable);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }

        public byte getCost(int index) {
            return costTable[index];
        }

        public int getPatternPosition(byte value) {
            for (int i = 0; i < tilesInPattern.length; i++) {
                byte tile = tilesInPattern[i];
                if (tile == value) {
                    return i;
                }
            }
            return -1; //not found
        }
    }

}

