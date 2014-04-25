package bitmapbenchmarks.synth.impl;

import org.brettw.SparseBitSet;

import java.util.ArrayList;

public class SparseBitSetTest extends AbstractBitsetTest2<SparseBitSet> {

    @Override
    public String getDescription() {
        return "# SparseBitSet";
    }

    @Override
    public SparseBitSet createEmpty() {
        return new SparseBitSet();
    }

    @Override
    public void setBit(SparseBitSet sparseBitSet, int bit) {
        sparseBitSet.set(bit);
    }

    @Override
    public int[] toArray(SparseBitSet sparseBitSet) {
        int[] array = new int[sparseBitSet.cardinality()];
        int pos = 0;
        for (int i = sparseBitSet.nextSetBit(0); i >= 0; i = sparseBitSet.nextSetBit(i + 1)) {
            array[pos++] = i;
        }
        return array;
    }

    @Override
    public SparseBitSet or(SparseBitSet first, SparseBitSet second) {
        SparseBitSet result = first.clone();
        result.or(second);
        return result;
    }

    @Override
    public SparseBitSet orAll(SparseBitSet... set) {
        SparseBitSet result = set[0].clone();
        for (int j = 1; j < set.length; ++j) {
            result.or(set[j]);
        }
        return result;
    }

    @Override
    public SparseBitSet and(SparseBitSet first, SparseBitSet second) {
        SparseBitSet result = first.clone();
        result.and(second);
        return result;
    }

    @Override
    public SparseBitSet andAll(SparseBitSet... set) {
        SparseBitSet result = set[0].clone();
        for (int j = 1; j < set.length; ++j) {
            result.and(set[j]);
        }
        return result;
    }

    @Override
    public SparseBitSet xor(SparseBitSet first, SparseBitSet second) {
        SparseBitSet result = first.clone();
        result.xor(second);
        return result;
    }

    @Override
    public SparseBitSet xorAll(SparseBitSet... set) {
        SparseBitSet result = set[0].clone();
        for (int j = 1; j < set.length; ++j) {
            result.xor(set[j]);
        }
        return result;
    }

    @Override
    public SparseBitSet[] convertToArray(ArrayList<SparseBitSet> l, int n) {
        return l.toArray(new SparseBitSet[n]);
    }
}
