package bitmapbenchmarks.synth.impl;

import java.text.DecimalFormat;
import java.util.TreeSet;

public class TreeSetTest extends AbstractBitsetTest {
    @SuppressWarnings("unchecked")
    @Override
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# Tree Set");
        System.out
                .println("# size, construction time, time to recover set bits, time to compute unions  and intersections ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        TreeSet<Integer>[] bitmap = new TreeSet[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                bitmap[k] = new TreeSet<Integer>();
                for (int x = 0; x < data[k].length; ++x) {
                    bitmap[k].add(data[k][x]);
                }
            }
        }
        aft = System.currentTimeMillis();
        line += "\t" + size / 1024;
        line += "\t" + df.format((aft - bef) / 1000.0);
        // uncompressing
        bef = System.currentTimeMillis();
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical or
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                TreeSet<Integer> bitmapor = new TreeSet<Integer>();
                for (int j = 0; j < k + 1; ++j) {
                    bitmapor.addAll(bitmap[k]);
                }
                bogus += bitmapor.size();
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical and + extraction
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                TreeSet<Integer> bitmapand = (TreeSet<Integer>) bitmap[0].clone();
                for (int j = 1; j < k; ++j) {
                    bitmapand.retainAll(bitmap[j]);
                }
                bogus += bitmapand.size();
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        System.out.println(line);
        return bogus;
    }

}
