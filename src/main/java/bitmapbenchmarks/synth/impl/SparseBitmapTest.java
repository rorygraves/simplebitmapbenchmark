package bitmapbenchmarks.synth.impl;

import sparsebitmap.SparseBitmap;

import java.text.DecimalFormat;
import java.util.Arrays;

public class SparseBitmapTest extends AbstractBitsetTest {

    @Override
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# simple sparse bitmap implementation");
        System.out.println("# size, construction time, time to recover set bits, time to compute unions, intersections, xor ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        SparseBitmap[] bitmap = new SparseBitmap[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                bitmap[k] = new SparseBitmap();
                for (int x = 0; x < data[k].length; ++x) {
                    bitmap[k].set(data[k][x]);
                }
                size += bitmap[k].sizeInBytes();
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
                SparseBitmap bitmapor = SparseBitmap.or(Arrays.copyOfRange(
                        bitmap, 0, k + 1));
                int[] array = bitmapor.toArray();
                bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical and + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                SparseBitmap bitmapand = SparseBitmap.materialize(SparseBitmap
                        .fastand(Arrays.copyOfRange(bitmap, 0, k + 1)));
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
                SparseBitmap bitmapand = SparseBitmap.xor(Arrays.copyOfRange(bitmap, 0, k + 1));
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
