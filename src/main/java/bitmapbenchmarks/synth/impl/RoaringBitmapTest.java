package bitmapbenchmarks.synth.impl;

import me.lemire.roaringbitmap.SpeedyRoaringBitmap;

import java.text.DecimalFormat;

public class RoaringBitmapTest extends AbstractBitsetTest {

    @Override
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# RoaringBitmap");
        System.out.println("# size, construction time, time to recover set bits, time to compute unions, intersections and xor ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        SpeedyRoaringBitmap[] bitmap = new SpeedyRoaringBitmap[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                bitmap[k] = new SpeedyRoaringBitmap();
                for (int x = 0; x < data[k].length; ++x) {
                    bitmap[k].set(data[k][x]);
                }
                size += bitmap[k].getSizeInBytes();
            }
        }
        aft = System.currentTimeMillis();
        line += "\t" + size / 1024;
        line += "\t" + df.format((aft - bef) / 1000.0);
        // uncompressing
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                int[] array = bitmap[k].getIntegers();
                bogus += array.length;
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical or + extraction
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                SpeedyRoaringBitmap bitmapor = bitmap[0];
                for (int j = 1; j < k + 1; ++j) {
                    bitmapor = SpeedyRoaringBitmap.or(bitmapor, bitmap[j]);
                }

                int[] array = bitmapor.getIntegers();
                bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        // logical and + extraction
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                SpeedyRoaringBitmap bitmapand = bitmap[0];
                for (int j = 1; j < k + 1; ++j) {
                    bitmapand = SpeedyRoaringBitmap.and(bitmapand, bitmap[j]);
                }

                int[] array = bitmapand.getIntegers();
                if (array.length > 0)
                    bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);


        // logical xor + extraction
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                SpeedyRoaringBitmap bitmapxor = bitmap[0];
                for (int j = 1; j < k + 1; ++j) {
                    bitmapxor = SpeedyRoaringBitmap.xor(bitmapxor, bitmap[j]);
                }
                int[] array = bitmapxor.getIntegers();
                if (array.length > 0)
                    bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);


        System.out.println(line);
        return bogus;
    }

}
