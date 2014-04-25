package bitmapbenchmarks.synth;

import bitmapbenchmarks.synth.impl.*;

import java.text.DecimalFormat;

public class Benchmark {

    public static void main(String args[]) {
//        test(2, 10, 10);
        test(10, 18, 10);

//        System.out.println("Remaining tests are 'stress tests' over many bitmaps.");
//        test(10 * 16 * 4, 10, 2);

    }

    public static void test(int N, int nbr, int repeat) {
        System.out.println("# running test with N = " + N + " nbr = " + nbr + " repeat = " + repeat);
        DecimalFormat df = new DecimalFormat("0.###");
        ClusteredDataGenerator cdg = new ClusteredDataGenerator();
        System.out.println("# For each instance, we report the size, the construction time, ");
        System.out.println("# the time required to recover the set bits,");
        System.out.println("# and the time required to compute logical ors (unions) between lots of bitmaps.");
        for (int sparsity = 1; sparsity < 30 - nbr; sparsity += 4) {
            int[][] data = new int[N][];
            int Max = (1 << (nbr + sparsity));
            System.out.println("# generating random data...");
            int[] inter = cdg.generateClustered(1 << (nbr / 2), Max);
            int counter = 0;
            for (int k = 0; k < N; ++k) {
                data[k] = IntUtil.unite(inter, cdg.generateClustered(1 << nbr, Max));
                counter += data[k].length;
            }
            System.out.println("# generating random data... ok.");
            System.out.println("#  average set bit per 32-bit word = "
                    + df.format((counter / (data.length / 32.0 * Max))));

            // building
            (new RoaringBitmapTest()).test(data, repeat, df);
//            (new HashSetTest()).test(data, repeat, df);
//            (new JavaBitSetTest()).test(data, repeat, df);
            (new SparseBitSetTest()).test(data, repeat, df);
//            (new SparseBitmapTest()).test(data, repeat, df);
//            (new ConciseSetTest()).test(data, repeat, df);
//            (new WAHViaConciseSetTest()).test(data, repeat, df);
//            (new WAHBitset32Test()).test(data, repeat, df);
//            (new EWAH64Test()).test(data, repeat, df);
//            (new EWAH32Test()).test(data, repeat, df);
            System.out.println();
        }
    }
}
