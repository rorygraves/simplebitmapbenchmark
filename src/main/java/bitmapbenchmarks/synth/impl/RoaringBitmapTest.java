package bitmapbenchmarks.synth.impl;

import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;

import java.util.ArrayList;

public class RoaringBitmapTest extends AbstractBitsetTest2<RoaringBitmap> {


    @Override
    public String getDescription() {
        return "# RoaringBitmap";
    }

    @Override
    public RoaringBitmap createEmpty() {
        return new RoaringBitmap();
    }

    @Override
    public void setBit(RoaringBitmap rb, int bit) {
        rb.add(bit);
    }

    @Override
    public int[] toArray(RoaringBitmap rb) {
        return rb.toArray();
    }

    @Override
    public RoaringBitmap or(RoaringBitmap first, RoaringBitmap second) {
        return RoaringBitmap.or(first,second);
    }

    @Override
    public RoaringBitmap orAll(RoaringBitmap... set) {
        return FastAggregation.or(set);
    }

    @Override
    public RoaringBitmap and(RoaringBitmap first, RoaringBitmap second) {
        return RoaringBitmap.and(first,second);
    }

    @Override
    public RoaringBitmap andAll(RoaringBitmap... set) {
        return FastAggregation.and(set);
    }

    @Override
    public RoaringBitmap xor(RoaringBitmap first, RoaringBitmap second) {
        return RoaringBitmap.xor(first, second);
    }

    @Override
    public RoaringBitmap xorAll(RoaringBitmap... set) {
        return FastAggregation.xor(set);
    }

    @Override
    public RoaringBitmap[] convertToArray(ArrayList<RoaringBitmap> l, int n) {
        return l.toArray(new RoaringBitmap[n]);
    }
}
