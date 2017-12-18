package migemo;

import java.io.*;
import java.util.*;

public class MigemoCompactDictionary {
    private final LOUDSTrie keyTrie;
    private final LOUDSTrie valueTrie;
    private final BitVector mappingBitVector;
    private final int[] mapping;

    public MigemoCompactDictionary(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            this.keyTrie = readTrie(dis, true);
            this.valueTrie = readTrie(dis, false);
            int mappingBitVectorSize = dis.readInt();
            long[] mappingBitVectorWords = new long[(mappingBitVectorSize + 63) / 64];
            for (int i = 0; i < mappingBitVectorWords.length; i++) {
                mappingBitVectorWords[i] = dis.readLong();
            }
            this.mappingBitVector = new BitVector(mappingBitVectorWords, mappingBitVectorSize);
            int mappingSize = dis.readInt();
            this.mapping = new int[mappingSize];
            for (int i = 0; i < mappingSize; i++) {
                this.mapping[i] = dis.readInt();
            }
        }
    }

    private static LOUDSTrie readTrie(DataInputStream dis, boolean compactHiragana) throws IOException {
        int keyTrieEdgeSize = dis.readInt();
        char[] keyTrieEdges = new char[keyTrieEdgeSize];
        for (int i = 0; i < keyTrieEdges.length; i++) {
            char c = compactHiragana ? CompactHiraganaString.decode(dis.readByte()) : dis.readChar();
            keyTrieEdges[i] = c;
        }
        int keyTrieBitVectorSize = dis.readInt();
        long[] keyTrieBitVectorWords = new long[(keyTrieBitVectorSize + 63) / 64];
        for (int i = 0; i < keyTrieBitVectorWords.length; i++) {
            keyTrieBitVectorWords[i] = dis.readLong();
        }
        return new LOUDSTrie(new BitVector(keyTrieBitVectorWords, keyTrieBitVectorSize), keyTrieEdges);
    }

    public String[] search(String key) {
        int keyIndex = keyTrie.get(key);
        if (keyIndex != -1) {
            int valueStartPos = mappingBitVector.select(keyIndex, false);
            int valueEndPos = mappingBitVector.nextClearBit(valueStartPos + 1);
            int size = valueEndPos - valueStartPos - 1;
            if (size > 0) {
                int offset = mappingBitVector.rank(valueStartPos, false);
                String[] result = new String[size];
                for (int i = 0; i < result.length; i++) {
                    result[i] = valueTrie.getKey(mapping[valueStartPos - offset + i]);
                }
                return result;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String[] predictiveSearch(String key) {
        int keyIndex = keyTrie.get(key);
        if (keyIndex != -1) {
            List<String> result = new ArrayList<>();
            for (PrimitiveIterator.OfInt it = keyTrie.iterator(keyIndex); it.hasNext();) {
                int e = it.nextInt();
                int valueStartPos = mappingBitVector.select(e, false);
                int valueEndPos = mappingBitVector.nextClearBit(valueStartPos + 1);
                int size = valueEndPos - valueStartPos - 1;
                int offset = mappingBitVector.rank(valueStartPos, false);
                for (int i = 0; i < size; i++) {
                    result.add(valueTrie.getKey(mapping[valueStartPos - offset + i]));
                }
            }
            return result.toArray(new String[0]);
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        //build(new FileReader(new File(args[0])));
        long start = System.currentTimeMillis();
        MigemoCompactDictionary dic = new MigemoCompactDictionary(new BufferedInputStream(new FileInputStream("migemo-compact-dict")));
        long end = System.currentTimeMillis();
        System.out.println("load compact dictionary: " + (end - start));
        Scanner scanner = new Scanner(System.in);
        String line;
        while ((line = scanner.nextLine()) != null && !line.isEmpty()) {
            System.out.println(Arrays.toString(dic.predictiveSearch(line)));
        }
    }

    public static void build(Reader reader) throws IOException {
        long start, end;
        start = System.currentTimeMillis();
        TreeMap<String, String[]> tempDictionary = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(";") && !line.isEmpty()) {
                    int semicolonPos = line.indexOf('\t');
                    String key = line.substring(0, semicolonPos);
                    String value = line.substring(semicolonPos + 1, line.length());
                    try {
                        CompactHiraganaString.encode(key);
                    } catch (IllegalArgumentException e) {
                        System.out.println("skipped the word \"" + key + "\"");
                        continue;
                    }
                    tempDictionary.put(key, value.split("\t"));
                }
            }
        }
        end = System.currentTimeMillis();
        System.out.println("load dictionary: " + (end - start));

        // build key trie
        start = System.currentTimeMillis();
        String[] keys = tempDictionary.keySet().toArray(new String[tempDictionary.size()]);
        int[] generatedKeyIndex = new int[keys.length];
        LOUDSTrie keyTrie = LOUDSTrieBuilder.build(keys, generatedKeyIndex);
        end = System.currentTimeMillis();
        System.out.println("build key trie: " + (end - start));

        // build value trie
        start = System.currentTimeMillis();
        String[] values = tempDictionary.values().stream().flatMap(Arrays::stream).distinct().sorted().toArray(String[]::new);
        LOUDSTrie valueTrie = LOUDSTrieBuilder.build(values);
        end = System.currentTimeMillis();
        System.out.println("build value trie: " + (end - start));

        // build trie mapping
        start = System.currentTimeMillis();
        int mappingCount = (int)tempDictionary.values().stream().flatMap(Arrays::stream).count();
        int[] mapping = new int[mappingCount];
        int mappingIndex = 0;
        BitSet mappingBitSet = new BitSet();
        int mappingBitSetIndex = 0;
        for (int i = 1; i <= keyTrie.size(); i++) {
            String key = keyTrie.getKey(i);
            mappingBitSet.set(mappingBitSetIndex++, false);
            String[] value = tempDictionary.get(key);
            if (value != null) {
                for (int j = 0; j < value.length; j++) {
                    mappingBitSet.set(mappingBitSetIndex++, true);
                    mapping[mappingIndex++] = valueTrie.get(value[j]);
                }
            }
        }
        end = System.currentTimeMillis();
        System.out.println("build trie mapping: " + (end - start));

        start = System.currentTimeMillis();
        try (OutputStream os = new FileOutputStream(new File("migemo-compact-dict"));
             BufferedOutputStream bos = new BufferedOutputStream(os);
             DataOutputStream dos = new DataOutputStream(bos)) {
            // output key trie
            dos.writeInt(keyTrie.edges.length);
            for (int i = 0; i < keyTrie.edges.length; i++) {
                byte compactChar = CompactHiraganaString.encode(keyTrie.edges[i]);
                dos.write(compactChar);
            }
            dos.writeInt(keyTrie.bitVector.size());
            long[] keyTrieBitVectorWords = keyTrie.bitVector.toLongArray();
            for (int i = 0; i < keyTrieBitVectorWords.length ; i++) {
                dos.writeLong(keyTrieBitVectorWords[i]);
            }

            // output value trie
            dos.writeInt(valueTrie.edges.length);
            for (int i = 0; i < valueTrie.edges.length; i++) {
                dos.writeChar(valueTrie.edges[i]);
            }
            dos.writeInt(valueTrie.bitVector.size());
            long[] valueTrieBitVectorWords = valueTrie.bitVector.toLongArray();
            for (int i = 0; i < valueTrieBitVectorWords.length; i++) {
                dos.writeLong(valueTrieBitVectorWords[i]);
            }

            // output mapping trie
            dos.writeInt(mappingBitSetIndex);
            long[] mappingWords = mappingBitSet.toLongArray();
            for (int i = 0; i < mappingWords.length; i++) {
                dos.writeLong(mappingWords[i]);
            }
            dos.writeInt(mapping.length);
            for (int i = 0; i < mapping.length; i++) {
                dos.writeInt(mapping[i]);
            }
        }
        end = System.currentTimeMillis();
        System.out.println("output: " + (end - start));
    }
}
