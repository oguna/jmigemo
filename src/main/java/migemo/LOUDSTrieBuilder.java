package migemo;

import java.util.Arrays;
import java.util.BitSet;

public class LOUDSTrieBuilder {
    public static LOUDSTrie build(String[] keys) {
        return build(keys, new int[keys.length]);
    }

    public static LOUDSTrie build(String[] keys, int[] nodes) {
        if (keys == null || nodes == null || nodes.length != keys.length) {
            throw new RuntimeException();
        }
        for (int i = 0; i < keys.length - 1; i++) {
            if (keys[i].compareTo(keys[i + 1]) >= 0) {
                throw new RuntimeException();
            }
        }
        Arrays.fill(nodes, 1);
        int cursor = 0;
        int currentNode = 1;
        StringBuilder edges = new StringBuilder();
        edges.append(' ');
        edges.append(' ');
        BitSet louds = new BitSet();
        int loudsIndex = 0;
        louds.set(loudsIndex++, true);
        while (true) {
            char lastChar = 0;
            int lastParent = 0;
            int restKeys = 0;
            for (int i = 0; i < keys.length; i++) {
                if (keys[i].length() < cursor) {
                    continue;
                }
                if (keys[i].length() == cursor) {
                    louds.set(loudsIndex++, false);
                    lastParent = nodes[i];
                    lastChar = 0;
                    continue;
                }
                char currentChar = keys[i].charAt(cursor);
                int currentParent = nodes[i];
                if (lastParent != currentParent) {
                    louds.set(loudsIndex++, false);
                    louds.set(loudsIndex++, true);
                    edges.append(currentChar);
                    currentNode = currentNode + 1;
                } else if (lastChar != currentChar) {
                    louds.set(loudsIndex++, true);
                    edges.append(currentChar);
                    currentNode = currentNode + 1;
                }
                nodes[i] = currentNode;
                lastChar = currentChar;
                lastParent = currentParent;
                restKeys++;
            }
            if (restKeys == 0) {
                break;
            }
            cursor++;
        }
        long[] words = Arrays.copyOf(louds.toLongArray(), (loudsIndex + 63) / 64);
        BitVector bitVector = new BitVector(words, loudsIndex);
        return new LOUDSTrie(bitVector, edges.toString().toCharArray());
    }
}
