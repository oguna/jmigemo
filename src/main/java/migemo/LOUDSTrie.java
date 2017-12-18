package migemo;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;

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

    public PrimitiveIterator.OfInt iterator(int index) {
        // upper 32 bit = node index
        // lower 32 bit = bit position
        int pos = bitVector.select(index, true);
        long[] initialStack = new long[16];
        initialStack[0] = (long) index << 32 | pos;
        return new PrimitiveIterator.OfInt() {
            private long[] stack = initialStack;
            private int stackPointer = 0;
            @Override
            public int nextInt() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                long pair = stack[stackPointer];
                int currentIndex = (int)(pair >> 32);
                int currentPos = (int)pair;
                int fistChild = firstChild(currentIndex);
                if (fistChild != -1) {
                    if (stackPointer >= stack.length - 1) {
                        stack = Arrays.copyOf(stack, stack.length * 2);
                    }
                    int fistChildPos = bitVector.select(fistChild, true);
                    stack[++stackPointer] = (long)fistChild << 32 | fistChildPos;
                    return currentIndex;
                }
                if (bitVector.get(currentPos + 1) && stackPointer > 0) {
                    stack[stackPointer] = (long)(currentIndex + 1) << 32 | (currentPos + 1);
                    return currentIndex;
                }
                while (stackPointer > 1) {
                    long parentPair = stack[--stackPointer];
                    int parentIndex = (int)(parentPair >> 32);
                    int parentPos = (int)parentPair;
                    if (bitVector.get(parentPos + 1)) {
                        stack[stackPointer] = (long)(parentIndex + 1) << 32 | (parentPos + 1);
                        return currentIndex;
                    }
                }
                stackPointer = -1;
                return currentIndex;
            }

            @Override
            public boolean hasNext() {
                return stackPointer >= 0;
            }
        };
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
