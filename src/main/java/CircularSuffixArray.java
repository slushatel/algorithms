import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.TrieST;

import java.util.LinkedList;
import java.util.List;

public class CircularSuffixArray {
    private final int n;
    private final int[] indexes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        n = s.length();
        char[] chars = s.toCharArray();

        TrieST<List<Integer>> trie = new TrieST<>();
        Queue<String> nonUniqueKeys = new Queue<>();
        for (int i = 0; i < n; i++) {
            addKey("", chars, i, trie);
        }
        for (String key : trie.keys()) {
            if (trie.get(key).size() > 1) nonUniqueKeys.enqueue(key);
        }
        while (!nonUniqueKeys.isEmpty()) {
            String key = nonUniqueKeys.dequeue();
            List<Integer> list = trie.get(key);
            trie.put(key, null);
            for (Integer lastCharIndex : list) {
                addKey(key, chars, lastCharIndex + 1, trie);
            }
            for (String subKey : trie.keysWithPrefix(key)) {
                if (subKey.equals(key)) continue;
                if (trie.get(subKey).size() > 1) nonUniqueKeys.enqueue(subKey);
            }
        }

        indexes = new int[n];
        int i = 0;
        for (String key : trie.keys()) {
            indexes[i++] = (trie.get(key).get(0) - key.length() + 1 + n) % n;
//            indexes[trie.get(key).get(0) - key.length() + 1] = i++;
        }
    }

    private void addKey(String currentKey, char[] chars, int newCharIndex, TrieST<List<Integer>> trie) {
        char ch = chars[newCharIndex % n];
        String newKey = currentKey + ch;
        List<Integer> newKeyList = trie.get(newKey);
        if (newKeyList == null) {
            newKeyList = new LinkedList<>();
            trie.put(newKey, newKeyList);
        }
        newKeyList.add(newCharIndex % n);
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return indexes[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
//        new CircularSuffixArray("abracadabra!");
        new CircularSuffixArray("AABBBBBBAB");
    }

}