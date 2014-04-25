package bitmapbenchmarks.synth.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class HashSetTest extends AbstractBitsetTest2<HashSet<Integer>> {
    @Override
    public String getDescription() {
        return "# Hash Set";
    }

    @Override
    public HashSet<Integer> createEmpty() {
        return new HashSet<Integer>();
    }

    @Override
    public void setBit(HashSet<Integer> hs, int bit) {
        hs.add(bit);
    }

    @Override
    public int[] toArray(HashSet<Integer> hs) {

        int[] array = new int[hs.size()];
        int idx = 0;
        for(int i : hs)
            array[idx++] = i;
        Arrays.sort(array);
        return array;
    }

    @Override
    public HashSet<Integer> or(HashSet<Integer> first, HashSet<Integer> second) {
        HashSet<Integer> result = new HashSet<Integer>();
        result.addAll(first);
        result.addAll(second);
        return result;
    }

    @Override
    public HashSet<Integer> orAll(HashSet<Integer>... set) {
        HashSet<Integer> result = new HashSet<Integer>();
        for (HashSet<Integer> s : set) {
            result.addAll(s);
        }
        return result;
    }

    @Override
    public HashSet<Integer> and(HashSet<Integer> first, HashSet<Integer> second) {
        HashSet<Integer> result = (HashSet<Integer>) first.clone();
        result.retainAll(second);
        return result;
    }

    @Override
    public HashSet<Integer> andAll(HashSet<Integer>... set) {
        HashSet<Integer> result = null;
        for(HashSet<Integer> s : set) {
            if(result == null)
                result = (HashSet<Integer>) s.clone();
            else
                result.retainAll(s);
        }
        return result;
    }

    @Override
    public HashSet<Integer> xor(HashSet<Integer> first, HashSet<Integer> second) {
        // take a copy of first and remove all the elements in second
        HashSet<Integer> result = (HashSet<Integer>) first.clone();
        result.removeAll(second);

        // take a clone of second and remove all elements in first collection
        HashSet<Integer> secondOnly = (HashSet<Integer>) second.clone();
        secondOnly.removeAll(first);

        // combine result
        result.addAll(secondOnly);
        return result;
    }

    @Override
    public HashSet<Integer> xorAll(HashSet<Integer>... set) {
        HashSet<Integer> seen = new HashSet<Integer>();
        HashSet<Integer> result = new HashSet<Integer>();

        for(HashSet<Integer> s : set) {
            for(Integer i : s) {
                // if we have seen it already its not exclusive so remove from results
                if(seen.contains(i))
                    result.remove(i);
                else {
                    seen.add(i);
                    result.add(i);
                }
            }
        }

        return result;
    }

    @Override
    public HashSet<Integer>[] convertToArray(ArrayList<HashSet<Integer>> l, int n) {
        return (HashSet<Integer>[]) l.toArray(new HashSet<?>[n]);
    }
}
