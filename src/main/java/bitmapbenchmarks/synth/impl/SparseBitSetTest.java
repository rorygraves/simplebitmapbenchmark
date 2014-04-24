package bitmapbenchmarks.synth.impl;

import org.brettw.SparseBitSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

public class SparseBitSetTest extends AbstractBitsetTest {

    public static long estimateSizeInBytes(SparseBitSet sbs) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // Note: you could use a file output steam instead of ByteArrayOutputStream
        ObjectOutputStream oo;
        try {
            oo = new ObjectOutputStream(bos);
            oo.writeObject(sbs);
            oo.close();
            return bos.toByteArray().length;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;

    }

    @Override
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# SparseBitSet");
        System.out
                .println("# size, construction time, time to recover set bits, time to compute unions, intersections and xor ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        SparseBitSet[] bitmap = new SparseBitSet[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                bitmap[k] = new SparseBitSet();
                for (int x = 0; x < data[k].length; ++x) {
                    bitmap[k].set(data[k][x]);
                }
                size += estimateSizeInBytes(bitmap[k]);// no straight-forward way to estimate memory usage?
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
                int pos = 0;
                for (int i = bitmap[k].nextSetBit(0); i >= 0; i = bitmap[k]
                        .nextSetBit(i + 1)) {
                    array[pos++] = i;
                }
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                SparseBitSet bitmapor = bitmap[0].clone();
                for (int j = 1; j < k + 1; ++j) {
                    bitmapor.or(bitmap[j]);
                }
                int[] array = new int[bitmapor.cardinality()];
                int pos = 0;
                for (int i = bitmapor.nextSetBit(0); i >= 0; i = bitmapor
                        .nextSetBit(i + 1)) {
                    array[pos++] = i;
                }
                bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical and + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                SparseBitSet bitmapand = bitmap[0].clone();
                for (int j = 1; j < k + 1; ++j) {
                    bitmapand.and(bitmap[j]);
                }
                int[] array = new int[bitmapand.cardinality()];
                int pos = 0;
                for (int i = bitmapand.nextSetBit(0); i >= 0; i = bitmapand
                        .nextSetBit(i + 1)) {
                    array[pos++] = i;
                }
                if (array.length > 0)
                    bogus += array[array.length - 1];
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical xor + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                SparseBitSet bitmapand = bitmap[0].clone();
                for (int j = 1; j < k + 1; ++j) {
                    bitmapand.xor(bitmap[j]);
                }
                int[] array = new int[bitmapand.cardinality()];
                int pos = 0;
                for (int i = bitmapand.nextSetBit(0); i >= 0; i = bitmapand
                        .nextSetBit(i + 1)) {
                    array[pos++] = i;
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
