package bitmapbenchmarks.synth.impl;

import java.text.DecimalFormat;
import java.util.BitSet;

public class JavaBitSetTest extends AbstractBitsetTest {

    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println("# BitSet");
        System.out
                .println("# size, construction time, time to recover set bits, time to compute unions, intersections, xor ");
        long bef, aft;
        String line = "";
        long bogus = 0;
        int N = data.length;
        bef = System.currentTimeMillis();
        BitSet[] bitmap = new BitSet[N];
        int size = 0;
        for (int r = 0; r < repeat; ++r) {
            size = 0;
            for (int k = 0; k < N; ++k) {
                bitmap[k] = new BitSet();
                for (int x = 0; x < data[k].length; ++x) {
                    bitmap[k].set(data[k][x]);
                }
                size += bitmap[k].size() / 8;
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
                for (int i = bitmap[k].nextSetBit(0); i >= 0; i = bitmap[k].nextSetBit(i + 1)) {
                    array[pos++] = i;
                }
            }
        aft = System.currentTimeMillis();
        line += "\t" + df.format((aft - bef) / 1000.0);
        // logical or + retrieval
        bef = System.currentTimeMillis();
        for (int r = 0; r < repeat; ++r)
            for (int k = 0; k < N; ++k) {
                BitSet bitmapor = (BitSet) bitmap[0].clone();
                for (int j = 1; j < k + 1; ++j) {
                    bitmapor.or(bitmap[j]);
                }
                int[] array = new int[bitmapor.cardinality()];
                int pos = 0;
                for (int i = bitmapor.nextSetBit(0); i >= 0; i = bitmapor.nextSetBit(i + 1)) {
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
                BitSet bitmapand = (BitSet) bitmap[0].clone();
                for (int j = 1; j < k + 1; ++j) {
                    bitmapand.and(bitmap[j]);
                }
                int[] array = new int[bitmapand.cardinality()];
                int pos = 0;
                for (int i = bitmapand.nextSetBit(0); i >= 0; i = bitmapand.nextSetBit(i + 1)) {
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
                BitSet bitmapand = (BitSet) bitmap[0].clone();
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
