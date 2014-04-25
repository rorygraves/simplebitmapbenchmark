package bitmapbenchmarks.synth.impl;

import objectexplorer.MemoryMeasurer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

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
    public abstract int[] toArray(T t);
    /** logical or two bitsets - must not modify the original bitmaps */
    public abstract T or(T first,T second);
    /** logical or a collection, must not modify the original bitsets */
    public abstract T orAll(T... set);
    public abstract T and(T first,T second);
    public abstract T andAll(T... set);
    public abstract T xor(T first,T second);
    public abstract T xorAll(T... set);

    /** convert the arraylist to a correctly typed array (exists because java arrays and type erasure don't play nice)
     *
     * @param l The list of elements
     * @param n The number of elements to copy
     * @return
     */
    public abstract T[] convertToArray(ArrayList<T> l,int n);

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
                size += MemoryMeasurer.measureBytes(bitmap.get(k));
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

                T[] subArray = convertToArray(bitmap,k+1);
                T bitmapOr = orAll(subArray);
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
