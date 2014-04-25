package bitmapbenchmarks.synth.impl;

import java.util.ArrayList;
import java.util.BitSet;

public class JavaBitSetTest extends AbstractBitsetTest2<BitSet> {

    @Override
    public String getDescription() {
        return "Java BitSet";
    }

    @Override
    public BitSet createEmpty() {
        return new BitSet();
    }

    @Override
    public void setBit(BitSet bitSet, int bit) {
        bitSet.set(bit);
    }

    @Override
    public int[] toArray(BitSet bitSet) {
        int[] array = new int[bitSet.cardinality()];
        int pos = 0;
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            array[pos++] = i;
        }

        return array;
    }

    @Override
    public BitSet or(BitSet first, BitSet second) {
        BitSet result = (BitSet) first.clone();
        result.or(second);
        return result;
    }

    @Override
    public BitSet orAll(BitSet... set) {
        BitSet result = (BitSet) set[0].clone();
        for (int j = 1; j < set.length; ++j) {
            result.or(set[j]);
        }
        return result;
    }

    @Override
    public BitSet and(BitSet first, BitSet second) {
        BitSet result = (BitSet) first.clone();
        result.and(second);
        return result;
    }

    @Override
    public BitSet andAll(BitSet... set) {
        BitSet result = (BitSet) set[0].clone();
        for (int j = 1; j < set.length; ++j) {
            result.and(set[j]);
        }
        return result;
    }

    @Override
    public BitSet xor(BitSet first, BitSet second) {
        BitSet result = (BitSet) first.clone();
        result.xor(second);
        return result;
    }

    @Override
    public BitSet xorAll(BitSet... set) {
        BitSet result = (BitSet) set[0].clone();
        for (int j = 1; j < set.length; ++j) {
            result.xor(set[j]);
        }
        return result;
    }

    @Override
    public BitSet[] convertToArray(ArrayList<BitSet> l, int n) {
        return l.toArray(new BitSet[n]);
    }
}
