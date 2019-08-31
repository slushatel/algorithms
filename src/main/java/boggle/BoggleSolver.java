package boggle;

import edu.princeton.cs.algs4.TST;

import java.util.LinkedList;
import java.util.List;

public class BoggleSolver {

    private final TST<SearchValue> tst = new TST<>();
    private TST<Boolean> tstFound;

    private class SearchValue {
        final boolean isWord;

        public SearchValue(boolean isWord) {
            this.isWord = isWord;
        }

        @Override
        public String toString() {
            return "SearchValue{" +
                    "isWord=" + isWord +
                    '}';
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String s : dictionary) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < s.length() - 1; i++) {
                str.append(s.charAt(i));
                if (!tst.contains(str.toString()))
                    tst.put(str.toString(), new SearchValue(false));
            }
            tst.put(s, new SearchValue(true));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        tstFound = new TST<>();
        List<String> list = new LinkedList<>();
        StringBuilder letters = new StringBuilder();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(list, i, j, visited, board, letters);
                assert letters.length() == 0;
                assert checkArray(visited);
            }
        }
        return list;
    }

    private boolean checkArray(boolean[][] arr) {
        for (boolean[] booleans : arr) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) return false;
            }
        }
        return true;
    }

    private void dfs(List<String> list, int row, int col, boolean[][] visited, BoggleBoard board, StringBuilder letters) {
        if (row < 0 || row >= board.rows() || col < 0 || col >= board.cols()) return;
        if (visited[row][col]) return;
        // no prefix in the dictionary
        if (!tst.contains(letters.toString() + board.getLetter(row, col))) return;

        visited[row][col] = true;
        char ch = board.getLetter(row, col);
        letters.append(ch);
        if (ch == 'Q') letters.append('U');
        String word = letters.toString();
        addWordToResult(word, list);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                dfs(list, row + i, col + j, visited, board, letters);
            }
        }

//        if (ch == 'Q') {
//            letters.append('U');
//            word = letters.toString();
//            addWordToResult(word, list);
//            for (int i = -1; i <= 1; i++) {
//                for (int j = -1; j <= 1; j++) {
//                    dfs(list, row + i, col + j, visited, board, letters);
//                }
//            }
//            letters.deleteCharAt(letters.length() - 1);
//        }

        visited[row][col] = false;
        letters.deleteCharAt(letters.length() - 1);
        if (ch == 'Q') {
            letters.deleteCharAt(letters.length() - 1);
        }
    }

    private void addWordToResult(String word, List<String> list) {
        if (word.length() > 2 &&
                tst.contains(word) && tst.get(word).isWord && !tstFound.contains(word)) {
            list.add(word);
            tstFound.put(word, Boolean.TRUE);
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int len = word.length();

        if (len < 3 || !tst.contains(word) || !tst.get(word).isWord) return 0;
        if (len <= 4) return 1;
        if (len <= 5) return 2;
        if (len <= 6) return 3;
        if (len <= 7) return 5;
        return 11;
    }
}