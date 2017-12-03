package migemo;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

public class LOUDSTrie {
    public final BitVector bitVector;
    public final char[] edges;

    public LOUDSTrie(BitVector bitVector, char[] edges) {
        this.bitVector = bitVector;
        this.edges = edges;
    }

    public String getKey(int index) {
        if (index <= 0 || edges.length <= index) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (index > 1) {
            sb.append(edges[index]);
            index = parent(index);
        }
        return sb.reverse().toString();
    }

    public int parent(int x) {
        return bitVector.rank(bitVector.select(x, true), false);
    }

    public int firstChild(int x) {
        int y = bitVector.select(x, false) + 1;
        if (bitVector.get(y)) {
            return bitVector.rank(y, true) + 1;
        } else {
            return -1;
        }
    }

    public int traverse(int index, char c) {
        int firstChild = firstChild(index);
        if (firstChild == -1) {
            return -1;
        }
        int childStartBit = bitVector.select(firstChild, true);
        int childEndBit = bitVector.nextClearBit(childStartBit);
        int childSize = childEndBit - childStartBit;
        int result = Arrays.binarySearch(edges, firstChild, firstChild + childSize, c);
        return result >= 0 ? result : -1;
    }

    public int get(CharSequence key) {
        int nodeIndex = 1;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            nodeIndex = traverse(nodeIndex, c);
            if (nodeIndex == -1) {
                break;
           }
        }
        return (nodeIndex >= 0) ? nodeIndex : -1;
   }

    public void visitDepthFirst(int index, IntConsumer visitor) {
        visitor.accept(index);
        int child = firstChild(index);
        if (child  == -1) {
            return;
        }
        int childPos = bitVector.select(child, true);
        while (bitVector.get(childPos)) {
            visitDepthFirst(child, visitor);
            child++;
            childPos++;
        }
    }

    public PrimitiveIterator.OfInt commonPrefixSearch(String key) {
        Objects.requireNonNull(key);
        return new PrimitiveIterator.OfInt() {
            int offset = 0;
            int index = 1;
            @Override
            public int nextInt() {
                if (index == -1) {
                    throw new NoSuchElementException();
                }
                int lastIndex = index;
                if (offset < key.length()) {
                    char c = key.charAt(offset);
                    index = traverse(index, c);
                } else {
                    index = -1;
                }
                offset++;
                return lastIndex;
            }

            @Override
            public boolean hasNext() {
                return index != -1;
            }
        };
    }

    public int size() {
        return this.edges.length - 2;
    }
}
