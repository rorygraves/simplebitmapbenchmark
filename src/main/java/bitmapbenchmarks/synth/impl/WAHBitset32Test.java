package bitmapbenchmarks.synth.impl;

import org.devbrat.util.WAHBitSet;

import java.text.DecimalFormat;
import java.util.Iterator;

public class WAHBitset32Test extends AbstractBitsetTest {
    @Override
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# WAH 32 bit using the compressedbitset library");
        System.out
                .println("# size, construction time, time to recover set bits, time to compute unions  and intersections ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        WAHBitSet[] bitmap = new WAHBitSet[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                bitmap[k] = new WAHBitSet();
                for (int x = 0; x < data[k].length; ++x) {
                    bitmap[k].set(data[k][x]);
                }
                size += bitmap[k].memSize() * 4;
            }
        }
        aft = System.currentTimeMillis();
        line += "\t" + size / 1024;
        line += "\t" + df.format((aft - bef) / 1000.0);
        // uncompressing
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                int[] array = new int[bitmap[k].cardinality()];
                int c = 0;
                for (@SuppressWarnings("unchecked")
                     Iterator<Integer> i = bitmap[k].iterator(); i.hasNext(); array[c++] = i.next()) {
                }
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical or + extraction
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                WAHBitSet bitmapor = bitmap[0];
                for (int j = 1; j < k + 1; ++j) {
                    bitmapor = bitmapor.or(bitmap[j]);
                }
                int[] array = new int[bitmapor.cardinality()];
                int c = 0;
                for (@SuppressWarnings("unchecked")
                     Iterator<Integer> i = bitmapor.iterator(); i.hasNext(); array[c++] = i.next()) {
                }
                bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        // logical and + extraction
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                WAHBitSet bitmapand = bitmap[0];
                for (int j = 1; j < k + 1; ++j) {
                    bitmapand = bitmapand.and(bitmap[j]);
                }
                int[] array = new int[bitmapand.cardinality()];
                int c = 0;
                for (@SuppressWarnings("unchecked")
                     Iterator<Integer> i = bitmapand.iterator(); i.hasNext(); array[c++] = i.next()) {
                }
                if (array.length > 0)
                    bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        System.out.println(line);
        return bogus;
    }
}
