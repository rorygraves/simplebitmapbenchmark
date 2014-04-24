package bitmapbenchmarks.synth.impl;

import com.googlecode.javaewah.EWAHCompressedBitmap;

import java.text.DecimalFormat;
import java.util.Arrays;

public class EWAH64Test extends AbstractBitsetTest {

    @Override
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# EWAH using the javaewah library");
        System.out.println("# size, construction time, time to recover set bits, time to compute unions, intersections, xor");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        EWAHCompressedBitmap[] ewah = new EWAHCompressedBitmap[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                ewah[k] = new EWAHCompressedBitmap();
                for (int x = 0; x < data[k].length; ++x) {
                    ewah[k].set(data[k][x]);
                }
                ewah[k].trim();
                size += ewah[k].sizeInBytes();
            }
        }
        aft = System.currentTimeMillis();
        line += "\t" + size / 1024;
        line += "\t" + df.format((aft - bef) / 1000.0);
        // uncompressing
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                int[] array = ewah[k].toArray();
                bogus += array.length;
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        // fast logical or + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                EWAHCompressedBitmap bitmapor = EWAHCompressedBitmap.or(Arrays
                        .copyOf(ewah, k + 1));
                int[] array = bitmapor.toArray();
                bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        // fast logical and + retrieval 2-by-2
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                EWAHCompressedBitmap bitmapand = EWAHCompressedBitmap
                        .and(Arrays.copyOf(ewah, k + 1));
                int[] array = bitmapand.toArray();
                if (array.length > 0)
                    bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        // fast logical xor + retrieval 2-by-2
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                EWAHCompressedBitmap bitmapand = EWAHCompressedBitmap
                        .xor(Arrays.copyOf(ewah, k + 1));
                int[] array = bitmapand.toArray();
                if (array.length > 0)
                    bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        System.out.println(line);
        return bogus;
    }
}
