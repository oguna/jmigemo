package migemo;

import java.util.Arrays;

public class BitVector {
    private final static int LB = 512;
    private final static int SB = 64;

    private final long[] words;
    private final int sizeInBits;
    private final int[] lb;
    private final short[] sb;

    public BitVector(long[] words, int sizeInBits) {
        if (words == null) {
            throw new IllegalArgumentException();
        }
        if ((sizeInBits + 63 ) / 64 != words.length) {
            throw new IllegalArgumentException();
        }
        this.words = words;
        this.sizeInBits = sizeInBits;
        this.lb = new int[(sizeInBits + 511) / 512];
        this.sb = new short[this.lb.length * 8];
        int sum = 0;
        short sumInLb = 0;
        for (int i = 0; i < sb.length; i++) {
            short bitCount = i < this.words.length ? (short) Long.bitCount(this.words[i]) : 0;
            this.sb[i] = sumInLb;
            sumInLb += bitCount;
            if ((i & 7) == 7) {
                this.lb[i >> 3] = sum;
                sum += sumInLb;
                sumInLb = 0;
            }
        }
    }

    public BitVector(boolean[] bits) {
        this(convertBits(bits), bits.length);
    }

    private static long[] convertBits(boolean[] bits) {
        if (bits == null) {
            return null;
        }
        long[] words = new long[(bits.length + 63) / 64];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                words[i / 64] |= 1L << (i % 64);
            }
        }
        return words;
    }

    public int rank(int pos, boolean b) {
        if (pos < 0 && sizeInBits <= pos) {
            throw new IndexOutOfBoundsException();
        }
        int count1 = this.sb[pos / SB] + this.lb[pos / LB];
        long word = this.words[pos / SB];
        count1 += Long.bitCount(word & ((1L << (pos & 63)) - 1));
        return b ? count1 : (pos - count1);
    }

    public int select(int count, boolean b) {
        int lbIndex = lowerBoundBinarySearchLB(count, b) - 1;
        int countInLb = count - (b ? this.lb[lbIndex] : (512 * lbIndex - this.lb[lbIndex]));
        int sbIndex = lowerBoundBinarySearchSB(countInLb, lbIndex * 8, lbIndex * 8 + 8, b) - 1;
        int countInSb = countInLb - (b ? this.sb[sbIndex] : (64 * (sbIndex % 8) - this.sb[sbIndex]));
        long word = this.words[sbIndex];
        if (!b) {
            word = ~word;
        }
        int lowerBitCount = Integer.bitCount((int)word);
        int i = 0;
        if (countInSb > lowerBitCount) {
            word >>>= 32;
            countInSb -= lowerBitCount;
            i = 32;
        }
        while (countInSb > 0) {
            countInSb -= word & 1;
            word >>>= 1;
            i++;
        }
        return sbIndex * 64 + (i - 1);
    }

    private int lowerBoundBinarySearchLB(int key, boolean b) {
        int high = lb.length;
        int low = -1;
        while (high - low > 1) {
            int mid = (high + low) / 2;
            if ((b ? lb[mid] : 512 * mid - lb[mid]) < key) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return high;
    }

    private int lowerBoundBinarySearchSB(int key, int fromIndex, int toIndex, boolean b) {
        int high = toIndex;
        int low = fromIndex - 1;
        while (high - low > 1) {
            int mid = (high + low) / 2;
            if ((b ? sb[mid] : 64 * (mid % 8) - sb[mid]) < key) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return high;
    }

    public int nextClearBit(int fromIndex) {
        int u = fromIndex >> 6;
        long word = ~words[u] & (0xffffffffffffffffL  << fromIndex);
        while (true) {
            if (word != 0)
                return (u * 64) + Long.numberOfTrailingZeros(word);
            if (++u == words.length)
                return -1;
            word = ~words[u];
        }
    }

    public long[] toLongArray() {
        return Arrays.copyOf(this.words, this.words.length);
    }

    public int size() {
        return this.sizeInBits;
    }

    public boolean get(int pos) {
        if (pos < 0 && sizeInBits <= pos) {
            throw new IndexOutOfBoundsException();
        }
        return ((this.words[pos >>> 6] >>> (pos & 63)) & 1) == 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.sizeInBits + this.sizeInBits >>> 6);
        for (int i = 0; i < this.sizeInBits; i++) {
            boolean bit = ((this.words[i >>> 6] >>> (i & 63)) & 1) == 1;
            sb.append(bit ? '1' : '0');
            if ((i & 63) == 63) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
}
