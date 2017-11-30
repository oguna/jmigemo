package migemo;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;

public class LOUDSTrieBuilder {
    public static LOUDSTrie build(String[] keys) {
        return build(keys, null);
    }

    public static LOUDSTrie build(String[] keys, int[] generatedIndex) {
        Objects.requireNonNull(keys);
        int[] memo;
        if (generatedIndex == null) {
            memo = new int[keys.length];
        } else if (generatedIndex.length == keys.length) {
            memo = generatedIndex;
        } else {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null) {
                throw new IllegalArgumentException();
            }
            if (i > 0 && keys[i - 1].compareTo(keys[i]) >= 0) {
                throw new IllegalArgumentException();
            }
        }
        Arrays.fill(memo, 1);
        int offset = 0;
        int currentNode = 1;
        StringBuilder edges = new StringBuilder();
        edges.append(0);
        edges.append(0);
        int[] childSizes = new int[128];
        while (true) {
            char lastChar = 0;
            int lastParent = 0;
            int restKeys = 0;
            for (int i = 0; i < keys.length; i++) {
                if (memo[i] < 0) {
                    continue;
                }
                if (keys[i].length() <= offset) {
                    memo[i] = -memo[i];
                    continue;
                }
                char currentChar = keys[i].charAt(offset);
                int currentParent = memo[i];
                if (lastChar != currentChar || lastParent != currentParent) {
                    if (childSizes.length <= memo[i]) {
                        childSizes = Arrays.copyOf(childSizes, childSizes.length * 2);
                    }
                    childSizes[memo[i]]++;
                    currentNode++;
                    edges.append(currentChar);
                    lastChar = currentChar;
                    lastParent = currentParent;
                }
                memo[i] = currentNode;
                restKeys++;
            }
            if (restKeys == 0) {
                break;
            }
            offset++;
        }
        //System.out.println(Arrays.toString(memo));
        //System.out.println(edges.toString());
        //System.out.println(childSizes.toString());
        for (int i = 0; i < memo.length; i++) {
            memo[i] = -memo[i];
        }
        BitSet bitSet = new BitSet();
        bitSet.set(0, true);
        int bitVectorSize = 1;
        for (int i = 1; i <= currentNode; i++) {
            bitSet.set(bitVectorSize, false);
            bitVectorSize++;
            int childSize = childSizes[i];
            for (int j = 0; j < childSize; j++) {
                bitSet.set(bitVectorSize, true);
                bitVectorSize++;
            }
        }
        long[] bitVectorWords = bitSet.toLongArray();
        if ((bitVectorSize + 63) / 64 != bitVectorWords.length) {
            bitVectorWords = Arrays.copyOf(bitVectorWords, (bitVectorSize + 63) / 64);
        }
        BitVector bitVector = new BitVector(bitVectorWords, bitVectorSize);
        return new LOUDSTrie(bitVector, edges.toString().toCharArray());
    }
}
