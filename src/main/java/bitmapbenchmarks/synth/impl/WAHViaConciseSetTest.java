package bitmapbenchmarks.synth.impl;

import it.uniroma3.mat.extendedset.intset.ConciseSet;

import java.text.DecimalFormat;

public class WAHViaConciseSetTest extends AbstractBitsetTest {

    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out
                .println("# WAH 32 bit using the extendedset_2.2 library");
        System.out
                .println("# size, construction time, time to recover set bits, time to compute unions, intersections, xor ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        ConciseSet[] bitmap = new ConciseSet[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                bitmap[k] = new ConciseSet(true);
                for (int x = 0; x < data[k].length; ++x) {
                    bitmap[k].add(data[k][x]);
                }
                size += (int) (bitmap[k].size() * bitmap[k]
                        .collectionCompressionRatio()) * 4;
            }
        }
        aft = System.currentTimeMillis();
        line += "\t" + size / 1024;
        line += "\t" + df.format((aft - bef) / 1000.0);
        // uncompressing
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                int[] array = bitmap[k].toArray();
                bogus += array.length;
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical or + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                ConciseSet bitmapor = bitmap[0];
                for (int j = 1; j < k + 1; ++j) {
                    bitmapor = bitmapor.union(bitmap[j]);
                }
                int[] array = bitmapor.toArray();
                bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical and + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                ConciseSet bitmapand = bitmap[0];
                for (int j = 1; j < k + 1; ++j) {
                    bitmapand = bitmapand.intersection(bitmap[j]);
                }
                int[] array = bitmapand.toArray();
                if (array != null)
                    if (array.length > 0)
                        bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical xor + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                ConciseSet bitmapand = bitmap[0];
                for (int j = 1; j < k + 1; ++j) {
                    bitmapand = bitmapand.symmetricDifference(bitmap[j]);
                }
                int[] array = bitmapand.toArray();
                if (array != null)
                    if (array.length > 0)
                        bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        System.out.println(line);
        return bogus;
    }
}
