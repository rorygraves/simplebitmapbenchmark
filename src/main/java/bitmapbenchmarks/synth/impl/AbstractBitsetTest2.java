package bitmapbenchmarks.synth.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class AbstractBitsetTest2<T> {
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println(getDescription());
        System.out.println("# size, construction time, time to recover set bits, time to compute unions, intersections, xor ");

        testInternal(data,repeat,df);
        return 0L;
    }

    public abstract String getDescription();

    public abstract T createEmpty();
    public abstract void setBit(T t,int bit);
    public abstract int size(T t);
    public abstract int[] toArray(T t);
    public abstract T or(T base,T... additional);
    public abstract T and(T first,T second);
    public abstract T xor(T first,T second);

    public void testInternal(int[][] data, int repeat, DecimalFormat df) {
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        ArrayList<T> bitmap = new ArrayList<T>(N);
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                bitmap.add(createEmpty());
                for (int x = 0; x < data[k].length; ++x) {
                    setBit(bitmap.get(k), data[k][x]);
                }
                size += size(bitmap.get(k));
            }
        }
        aft = System.currentTimeMillis();
        line += "\t" + size / 1024;
        line += "\t" + df.format((aft - bef) / 1000.0);
        // uncompressing
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                int[] array = toArray(bitmap.get(k));
                bogus += array.length;
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical or + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                T bitmapOr = bitmap.get(0);
                for (int j = 1; j < k + 1; ++j) {
                    bitmapOr = or(bitmapOr,bitmap.get(j));
                }
                int[] array = toArray(bitmapOr);
                bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical and + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                T bitmapAnd = bitmap.get(0);
                for (int j = 1; j < k + 1; ++j) {
                    bitmapAnd = and(bitmapAnd, bitmap.get(j));
                }
                int[] array = toArray(bitmapAnd);
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
                T bitmapXor = bitmap.get(0);
                for (int j = 1; j < k + 1; ++j) {
                    bitmapXor = xor(bitmapXor,bitmap.get(j));
                }
                int[] array = toArray(bitmapXor);
                if (array != null)
                    if (array.length > 0)
                        bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);

        System.out.println(line);
    }
}
