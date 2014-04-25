package bitmapbenchmarks.synth.impl;

import com.googlecode.javaewah.EWAHCompressedBitmap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class EWAH64Test extends AbstractBitsetTest2<EWAHCompressedBitmap> {
    @Override
    public String getDescription() {
        return "# EWAH using the javaewah library";
    }

    @Override
    public EWAHCompressedBitmap createEmpty() {
        return new EWAHCompressedBitmap();
    }

    @Override
    public void setBit(EWAHCompressedBitmap cb, int bit) {
        cb.set(bit);
    }

    @Override
    public int[] toArray(EWAHCompressedBitmap cb) {
        return cb.toArray();
    }

    @Override
    public EWAHCompressedBitmap or(EWAHCompressedBitmap first, EWAHCompressedBitmap second) {
        return first.or(second);
    }

    @Override
    public EWAHCompressedBitmap orAll(EWAHCompressedBitmap... set) {
        return EWAHCompressedBitmap.or(set);
    }

    @Override
    public EWAHCompressedBitmap and(EWAHCompressedBitmap first, EWAHCompressedBitmap second) {
        return first.and(second);
    }

    @Override
    public EWAHCompressedBitmap andAll(EWAHCompressedBitmap... set) {
        return EWAHCompressedBitmap.and(set);
    }

    @Override
    public EWAHCompressedBitmap xor(EWAHCompressedBitmap first, EWAHCompressedBitmap second) {
        return first.xor(second);
    }

    @Override
    public EWAHCompressedBitmap xorAll(EWAHCompressedBitmap... set) {
        return EWAHCompressedBitmap.xor(set);
    }

    @Override
    public EWAHCompressedBitmap[] convertToArray(ArrayList<EWAHCompressedBitmap> l, int n) {
        return l.toArray(new EWAHCompressedBitmap[n]);
    }
}
