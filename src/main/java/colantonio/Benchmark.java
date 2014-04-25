package colantonio;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Locale;

import com.googlecode.javaewah.EWAHCompressedBitmap;
import com.googlecode.javaewah32.EWAHCompressedBitmap32;

import org.roaringbitmap.RoaringBitmap;
import it.uniroma3.mat.extendedset.intset.ConciseSet;

/**
 * 
 * This a reproduction of the benchmark used by Colantonio and Di Pietro,
 * Concise: Compressed 'n' Composable Integer Set
 * 
 * While they report "Compression" as the ratio between the number of 32-bit words
 * required to represent the compressed bitmap and the cardinality
 * of the integer set, we report the number of bits per integer.
 * 
 *  Like them, we use "Density" mean  the ratio between the cardinality
 * of the set and the number range. 
 * 
 * Like them, we "Max/Cardinality" to mean the ratio
 * between the maximal value (i.e., the number range) and the cardinality
 * of the set that is, the inverse of the density.
 *
 *  
 * Time measurement are expressed in nanoseconds. Each experiment is
 * performed 100 times, and the average reported.
 *  
 * @author Daniel Lemire
 * 
 */
public class Benchmark {

        /**
         * @param a an array of integers
         * @return a bitset representing the provided integers
         */
        public static BitSet toBitSet(final int[] a) {
                BitSet bs = new BitSet();
                for (int x : a)
                        bs.set(x);
                return bs;
        }

        /**
         * @param a an array of integers
         * @return a ConciseSetTest representing the provided integers
         */
        public static ConciseSet toConciseSet(final int[] a) {
                ConciseSet cs = new ConciseSet();
                for (int x : a)
                        cs.add(x);
                return cs;
        }
        
        /**
         * @param a an array of integers
         * @return a RoaringBitmap representing the provided integers
         */
        public static RoaringBitmap toSpeedyRoaringBitmap(int[] a) {
                RoaringBitmap rr = new RoaringBitmap();
                for (int x : a)
                        rr.add(x);
                return rr;
        }

        /**
         * @param a an array of integers
         * @return a ConciseSetTest (in WAH mode) representing the provided integers
         */
        public static ConciseSet toWAHConciseSet(int[] a) {
                ConciseSet cs = new ConciseSet(true);
                for (int x : a)
                        cs.add(x);
                return cs;
        }

        /**
         * @param args command line arguments
         */
        public static void main(final String[] args) {
                Locale.setDefault(Locale.US);
                System.out.println("# This benchmark emulates what Colantonio and Di Pietro,");
                System.out.println("#  did in Concise: Compressed 'n' Composable Integer Set");
                System.out.println("########");
                System.out.println("# "+System.getProperty("java.vendor")+" "+System.getProperty("java.version")+" "+System.getProperty("java.vm.name"));
                System.out.println("# "+System.getProperty("os.name")+" "+System.getProperty("os.arch")+" "+System.getProperty("os.version"));
                System.out.println("# processors: "+Runtime.getRuntime().availableProcessors());
                System.out.println("# max mem.: "+Runtime.getRuntime().maxMemory());
                System.out.println("########");
                int N = 100000;
                
                DataGenerator gen = new DataGenerator(N);
                int TIMES = 10;
                gen.setUniform();
                test(gen,true, TIMES);
                System.out.println();
                gen.setZipfian();
                test(gen,true, TIMES);
                System.out.println();
       }

        /**
         * @param gen data generator
         * @param verbose whether to print out the result
         * @param TIMES how many times should we run each test
         */
        public static void test(final DataGenerator gen, final boolean verbose, final int TIMES) {
                if (!verbose)
                        System.out
                                .println("# running a dry run (can take a long time)");
                int bogus = 0;
                long bef, aft;
                DecimalFormat df = new DecimalFormat("0.000E0");
                DecimalFormat dfb = new DecimalFormat("000.0");
                if (verbose)
                        if(gen.is_zipfian())
                                System.out
                                .println("### zipfian test");
                         else
                              System.out
                                .println("### uniform test");
                if (verbose)
                        System.out
                                .println("# first columns are timings [intersection times in ns], then union times in ns, " +
                                    "then then bits/int");
                if (verbose)
                        System.out
                                .print("# density\tbitset\t\tconcise\t\twah\t\tewah32\t\tewah64\t\troar"+
                                    "\t\tbitset\t\tconcise\t\twah\t\tewah32\t\tewah64\t\troar");
                if(verbose)
                                System.out
                                .println("\t\tbitset\t\tconcise\t\twah\t\tewah32\t\tewah64\t\troar");
                for (double d = 0.0009765625; d < 1.000; d *= 2) {
                        double[] timingsand = new double[6];
                        double[] timingsor = new double[6];
                        double[] storageinbits = new double[6];
                        
                        for (int times = 0; times < TIMES; ++times) {
                                int[] v1 = gen.getRandomArray(d);
                                int[] v2 = gen.getRandomArray(d);
                                //BitSet
                                //Append times
                                BitSet b1 = toBitSet(v1); // we will clone it
                                BitSet b2 = toBitSet(v2);
                                //Storage
                                storageinbits[0] += b1.size() + b2.size();
                                //And times.
                                bef = System.nanoTime();
                                BitSet band = (BitSet) b1.clone(); // for fair comparison (not inplace)
                                band.and(b2);
                                aft = System.nanoTime();
                                timingsand[0] += aft - bef;
                                bogus += b1.length();
                                bef = System.nanoTime();
                                BitSet bor = (BitSet) b1.clone(); // for fair comparison (not inplace)
                                bor.or(b2);
                                aft = System.nanoTime();
                                timingsor[0] += aft - bef;
                                b1 = null;
                                b2 = null;
                                int[] trueintersection = toArray(band);
                                int[] trueunion = toArray(bor);
                                bor = null;
                                band = null;
                                /////////////////////////////////////////////////
                                // Concise
                                ConciseSet cs1 = toConciseSet(v1);
                                ConciseSet cs2 = toConciseSet(v2);
                                storageinbits[1] += cs1.size()
                                        * cs1.collectionCompressionRatio() * 4
                                        * 8;
                                storageinbits[1] += cs2.size()
                                        * cs2.collectionCompressionRatio() * 4
                                        * 8;
                                bef = System.nanoTime();
                                ConciseSet csand = cs1.intersection(cs2);
                                aft = System.nanoTime();
                                // we verify the answer
                                if(!Arrays.equals(csand.toArray(), trueintersection))
                                        throw new RuntimeException("bug");
                                bogus += csand.size();
                                timingsand[1] += aft - bef;
                                bef = System.nanoTime();
                                ConciseSet csor = cs1.union(cs2);
                                aft = System.nanoTime();
                                // we verify the answer
                                if(!Arrays.equals(csor.toArray(), trueunion))
                                        throw new RuntimeException("bug");
                                bogus += csor.size();
                                timingsor[1] += aft - bef;
                                bogus += cs1.size();
                                cs1 = null;
                                cs2 = null;
                                csand = null;
                                csor = null;
                                ///////////////////////////////////////////////
                                //WAHConcise 
                                ConciseSet wah1 = toWAHConciseSet(v1);
                                ConciseSet wah2 = toWAHConciseSet(v2);
                                //Storage
                                storageinbits[2] += wah1.size()
                                        * wah1.collectionCompressionRatio() * 4
                                        * 8;
                                storageinbits[2] += wah2.size()
                                        * wah2.collectionCompressionRatio() * 4
                                        * 8;
                                //Intersect times
                                bef = System.nanoTime();
                                ConciseSet wahand = wah1.intersection(wah2);
                                aft = System.nanoTime();
                                //we verify the answer
                                if(!Arrays.equals(wahand.toArray(), trueintersection))
                                        throw new RuntimeException("bug");
                                bogus += wahand.size();
                                timingsand[2] += aft - bef;
                                //Intersect times
                                bef = System.nanoTime();
                                ConciseSet wahor = wah1.union(wah2);
                                aft = System.nanoTime();
                                //we verify the answer
                                if(!Arrays.equals(wahor.toArray(), trueunion))
                                        throw new RuntimeException("bug");
                                bogus += wahand.size();
                                timingsor[2] += aft - bef;
                                bogus += wahor.size();
                                wah1 = null;
                                wah2 = null;
                                wahand = null;
                                wahor = null;
                                ///////////////////////////////////////////////
                                EWAHCompressedBitmap32 ewah1 = EWAHCompressedBitmap32.bitmapOf(v1);
                                EWAHCompressedBitmap32 ewah2 = EWAHCompressedBitmap32.bitmapOf(v2);
                                //Storage
                                storageinbits[3] += ewah1.sizeInBytes()*8;
                                storageinbits[3] += ewah2.sizeInBytes()*8;
                                //Intersect times
                                bef = System.nanoTime();
                                
                                EWAHCompressedBitmap32 ewahand = ewah1.and(ewah2);
                                aft = System.nanoTime();
                                //we verify the answer
                                if(!Arrays.equals(ewahand.toArray(), trueintersection))
                                        throw new RuntimeException("bug");
                                bogus += ewahand.sizeInBytes();
                                timingsand[3] += aft - bef;
                                //Intersect times
                                bef = System.nanoTime();
                                EWAHCompressedBitmap32 ewahor = ewah1.or(ewah2);
                                aft = System.nanoTime();
                                //we verify the answer
                                if(!Arrays.equals(ewahor.toArray(), trueunion))
                                        throw new RuntimeException("bug");
                                timingsor[3] += aft - bef;
                                bogus += ewahor.sizeInBytes();
                                ewahand = null;
                                ewahor = null;
                                ///////////////////////////////////////////////
                                EWAHCompressedBitmap ewah641 = EWAHCompressedBitmap.bitmapOf(v1);
                                EWAHCompressedBitmap ewah642 = EWAHCompressedBitmap.bitmapOf(v2);
                                //Storage
                                storageinbits[4] += ewah641.sizeInBytes()*8;
                                storageinbits[4] += ewah642.sizeInBytes()*8;
                                //Intersect times
                                bef = System.nanoTime();
                                
                                EWAHCompressedBitmap ewahands64 = ewah641.and(ewah642);
                                aft = System.nanoTime();
                                //we verify the answer
                                if(!Arrays.equals(ewahands64.toArray(), trueintersection))
                                        throw new RuntimeException("bug");
                                bogus += ewahands64.sizeInBytes();
                                timingsand[4] += aft - bef;
                                //Intersect times
                                bef = System.nanoTime();
                                EWAHCompressedBitmap ewahors64 = ewah641.or(ewah642);
                                aft = System.nanoTime();
                                //we verify the answer
                                if(!Arrays.equals(ewahors64.toArray(), trueunion))
                                        throw new RuntimeException("bug");
                                timingsor[4] += aft - bef;
                                bogus += ewahors64.sizeInBytes();
                                ewah1 = null;
                                ewah2 = null;
                                ewahands64 = null;
                                ewahors64 = null;

                                /////////////////////////////////////////////////
                                //RoaringBitmap
                                RoaringBitmap rb1 = toSpeedyRoaringBitmap(v1);
                                RoaringBitmap rb2 = toSpeedyRoaringBitmap(v2);
                                //Storage
                                storageinbits[5] += rb1.getSizeInBytes() * 8;
                                storageinbits[5] += rb2.getSizeInBytes() * 8;
                                //Intersect times
                                bef = System.nanoTime();
                                RoaringBitmap rband = RoaringBitmap.and(rb1,rb2);
                                aft = System.nanoTime();
                                // we verify the answer
                                if(!Arrays.equals(rband.toArray(), trueintersection))
                                        throw new RuntimeException("bug");
                                bogus += rb1.getCardinality();
                                timingsand[5] += aft - bef;
                                bef = System.nanoTime();
                                RoaringBitmap rbor = RoaringBitmap.or(rb1,rb2);
                                aft = System.nanoTime();
                                // we verify the answer
                                if(!Arrays.equals(rbor.toArray(), trueunion))
                                        throw new RuntimeException("bug");
                                bogus += rb1.getCardinality();
                                timingsor[5] += aft - bef;
                               
                                //Remove times
                                bogus += rb1.getCardinality();
                                rb1 = null;
                                rb2 = null;
                                rband = null;
                                rbor = null;
                        }
                        if (verbose) {
                                System.out.print(df.format(d)+"\t"
                                        + df.format(timingsand[0] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsand[1] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsand[2] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsand[3] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsand[4] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsand[5] / TIMES));
                        }
                        if (verbose) {
                                System.out.print("\t|\t"
                                        + df.format(timingsor[0] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsor[1] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsor[2] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsor[3] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsor[4] / TIMES)
                                        + "\t\t"
                                        + df.format(timingsor[5] / TIMES));
                        }

                        if (verbose)
                                        System.out.println("\t|\t"
                                                + dfb.format(storageinbits[0]
                                                        / (2 * TIMES * gen.N))
                                                + "\t\t"
                                                + dfb.format(storageinbits[1]
                                                        / (2 * TIMES * gen.N))
                                                + "\t\t"
                                                + dfb.format(storageinbits[2]
                                                        / (2 * TIMES * gen.N))
                                                + "\t\t"
                                                + dfb.format(storageinbits[3]
                                                        / (2 * TIMES * gen.N))
                                                + "\t\t"
                                                + dfb.format(storageinbits[4]
                                                        / (2 * TIMES * gen.N))
                                                + "\t\t"
                                                + dfb.format(storageinbits[5]
                                                        / (2 * TIMES * gen.N)));
                }
                System.out.println("#ignore = " + bogus);
        }
        
        private static int[] toArray(final BitSet bs) {
                int[] a = new int[bs.cardinality()];
                int pos = 0;
                for (int x = bs.nextSetBit(0); x >= 0; x = bs.nextSetBit(x + 1))
                        a[pos++] = x;
                return a;
        }

}

