package bitmapbenchmarks.synth.impl;

import bitmapbenchmarks.synth.IntUtil;

import java.text.DecimalFormat;
import java.util.Arrays;

public class IntsTest extends AbstractBitsetTest {

    @Override
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# Ints");
        System.out.println("# size, construction time, time to recover set bits, time to compute unions and intersections ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        int size = 0;
        for (int k = 0; k < N; ++k) {
            size += data[k].length * 4;
        }

        aft = System.currentTimeMillis();
        line += "\t" + size / 1024;
        line += "\t" + df.format((aft - bef) / 1000.0);
        // uncompressing
        bef = System.currentTimeMillis();
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical or + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                int[][] b = Arrays.copyOf(data, k + 1);
                int[] union = IntUtil.unite(b);
                bogus += union[union.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical and + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                int[][] b = Arrays.copyOf(data, k + 1);
                int[] inter = IntUtil.intersect(b);
                if (inter.length > 0)
                    bogus += inter[inter.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        System.out.println(line);
        return bogus;
    }

}
