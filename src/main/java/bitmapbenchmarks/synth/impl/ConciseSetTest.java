package bitmapbenchmarks.synth.impl;

import it.uniroma3.mat.extendedset.intset.ConciseSet;

import java.util.ArrayList;

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

    public int[] toArray(ConciseSet t) {
        return t.toArray();
    }

    @Override
    public ConciseSet or(ConciseSet first, ConciseSet second) {
        return first.union(second);
    }

    @Override
    public ConciseSet orAll(ConciseSet... set) {
        ConciseSet current = set[0];
        for(int i=1;i<set.length;i++)
            current = current.union(set[i]);
        return current;
    }

    @Override
    public ConciseSet and(ConciseSet first,ConciseSet second) {
        return first.intersection(second);
    }

    @Override
    public ConciseSet andAll(ConciseSet... set) {
        ConciseSet current = set[0];
        for(int i=1;i<set.length;i++)
            current = current.intersection(set[i]);
        return current;
    }

    public ConciseSet xor(ConciseSet first,ConciseSet second) {
        return first.symmetricDifference(second);
    }

    @Override
    public ConciseSet xorAll(ConciseSet... set) {
        ConciseSet current = set[0];
        for(int i=1;i<set.length;i++)
            current = current.symmetricDifference(set[i]);
        return current;
    }

    @Override
    public ConciseSet[] convertToArray(ArrayList<ConciseSet> l, int n) {
        return l.toArray(new ConciseSet[n]);
    }
}
