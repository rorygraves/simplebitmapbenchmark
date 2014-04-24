package bitmapbenchmarks.synth.impl;

import com.googlecode.javaewah32.EWAHCompressedBitmap32;

import java.text.DecimalFormat;
import java.util.Arrays;

public class EWAH32Test extends AbstractBitsetTest2<EWAHCompressedBitmap32> {


    @Override
    public String getDescription() { return "# EWAH 32-bit using the javaewah library"; }

    @Override
    public EWAHCompressedBitmap32 createEmpty() { return new EWAHCompressedBitmap32(); }

    @Override
    public void setBit(EWAHCompressedBitmap32 cb, int bit) { cb.set(bit); }

    @Override
    public int size(EWAHCompressedBitmap32 cb) {
        return cb.sizeInBytes();
    }

    @Override
    public int[] toArray(EWAHCompressedBitmap32 cb) {
        return cb.toArray();
    }

    @Override
    public EWAHCompressedBitmap32 or(EWAHCompressedBitmap32 first, EWAHCompressedBitmap32 second) {
        return first.or(second);
    }

    @Override
    public EWAHCompressedBitmap32 and(EWAHCompressedBitmap32 first, EWAHCompressedBitmap32 second) {
        return null;
    }

    @Override
    public EWAHCompressedBitmap32 xor(EWAHCompressedBitmap32 first, EWAHCompressedBitmap32 second) {
        return null;
    }

    @Override
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# EWAH 32-bit using the javaewah library");
        System.out.println("# size, construction time, time to recover set bits, time to compute unions, intersections, xor ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        EWAHCompressedBitmap32[] ewah = new EWAHCompressedBitmap32[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                ewah[k] = new EWAHCompressedBitmap32();
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
                EWAHCompressedBitmap32 bitmapor = EWAHCompressedBitmap32
                        .or(Arrays.copyOf(ewah, k + 1));
                int[] array = bitmapor.toArray();
                bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        // fast logical and + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                EWAHCompressedBitmap32 bitmapand = EWAHCompressedBitmap32
                        .and(Arrays.copyOf(ewah, k + 1));
                int[] array = bitmapand.toArray();
                if (array.length > 0)
                    bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        // fast logical xor + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                EWAHCompressedBitmap32 bitmapand = EWAHCompressedBitmap32
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
