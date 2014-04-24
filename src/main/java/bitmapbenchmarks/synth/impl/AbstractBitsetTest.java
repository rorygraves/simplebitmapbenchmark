package bitmapbenchmarks.synth.impl;

import java.text.DecimalFormat;

public abstract class AbstractBitsetTest {
    public long test(int[][] data, int repeat, DecimalFormat df) {
        System.out.println(getDescription());
        System.out.println("# size, construction time, time to recover set bits, time to compute unions, intersections, xor ");

        testInternal(data,repeat,df);
        return 0L;
    }

    public String getDescription() { return ""; }

    public void testInternal(int[][] data, int repeat, DecimalFormat df) {
    }
}
