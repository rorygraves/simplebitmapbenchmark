package bitmapbenchmarks.synth.impl;

import com.googlecode.javaewah32.EWAHCompressedBitmap32;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class EWAH32Test extends AbstractBitsetTest2<EWAHCompressedBitmap32> {


    @Override
    public String getDescription() { return "# EWAH 32-bit using the javaewah library"; }

    @Override
    public EWAHCompressedBitmap32 createEmpty() { return new EWAHCompressedBitmap32(); }

    @Override
    public void setBit(EWAHCompressedBitmap32 cb, int bit) { cb.set(bit); }

    @Override
    public int[] toArray(EWAHCompressedBitmap32 cb) {
        return cb.toArray();
    }

    @Override
    public EWAHCompressedBitmap32 or(EWAHCompressedBitmap32 first, EWAHCompressedBitmap32 second) {
        return first.or(second);
    }

    @Override
    public EWAHCompressedBitmap32 orAll(EWAHCompressedBitmap32... set) {
        return EWAHCompressedBitmap32.or(set);
    }

    @Override
    public EWAHCompressedBitmap32 and(EWAHCompressedBitmap32 first, EWAHCompressedBitmap32 second) {
        return first.and(second);
    }

    @Override
    public EWAHCompressedBitmap32 andAll(EWAHCompressedBitmap32... set) {
        return EWAHCompressedBitmap32.and(set);
    }

    @Override
    public EWAHCompressedBitmap32 xor(EWAHCompressedBitmap32 first, EWAHCompressedBitmap32 second) {
        return first.xor(second);
    }

    @Override
    public EWAHCompressedBitmap32 xorAll(EWAHCompressedBitmap32... set) {
        return EWAHCompressedBitmap32.xor(set);
    }

    @Override
    public EWAHCompressedBitmap32[] convertToArray(ArrayList<EWAHCompressedBitmap32> l, int n) {
        return l.toArray(new EWAHCompressedBitmap32[n]);
    }
}
