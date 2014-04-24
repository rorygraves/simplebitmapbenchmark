package bitmapbenchmarks.synth.impl;

import it.uniroma3.mat.extendedset.intset.ConciseSet;

public class ConciseSetTest extends AbstractBitsetTest2<ConciseSet> {

    @Override
    public String getDescription() { return "# ConciseSetTest 32 bit using the extendedset_2.2 library"; }

    @Override
    public ConciseSet createEmpty() {
        return new ConciseSet();
    }

    @Override
    public void setBit(ConciseSet t, int bit) {
        t.add(bit);
    }

    @Override
    public int size(ConciseSet t) {
        return (int) (t.size() * t.collectionCompressionRatio()) * 4;
    }

    public int[] toArray(ConciseSet t) {
        return t.toArray();
    }

    public ConciseSet or(ConciseSet iBase,ConciseSet... additional) {
        ConciseSet current = iBase;
        for (ConciseSet a : additional) {
            current = current.union(a);
        }

        return current;
    }

    public ConciseSet and(ConciseSet first,ConciseSet second) {
        return first.intersection(second);
    }

    public ConciseSet xor(ConciseSet first,ConciseSet second) {
        return first.symmetricDifference(second);
    }
}
