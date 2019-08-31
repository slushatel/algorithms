package boggle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleBoardClient {

    public static void main(String[] args) {
        {
            String dictionaryPath = "D:\\work\\algorithms\\src\\main\\java\\dictionary-algs4.txt";
            String boardPath = "D:\\work\\algorithms\\src\\main\\java\\board4x4.txt";
            process(dictionaryPath, boardPath);
        }
        {
            String dictionaryPath = "D:\\work\\algorithms\\src\\main\\java\\dictionary-algs4.txt";
            String boardPath = "D:\\work\\algorithms\\src\\main\\java\\board-q.txt";
            process(dictionaryPath, boardPath);
        }
        {
            String dictionaryPath = "D:\\work\\algorithms\\src\\main\\java\\dictionary-yawl.txt";
            String boardPath = "D:\\work\\algorithms\\src\\main\\java\\board4x4_2.txt";
            process(dictionaryPath, boardPath);
        }
        {
            String dictionaryPath = "D:\\work\\algorithms\\src\\main\\java\\dictionary-yawl.txt";
            String boardPath = "D:\\work\\algorithms\\src\\main\\java\\board1x29.txt";
            process(dictionaryPath, boardPath);
        }
    }

    public static void process(String dictionaryPath, String boardPath) {
        In in = new In(dictionaryPath);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(boardPath);
        int score = 0;
        int num = 0;
        for (String word : solver.getAllValidWords(board)) {
//            StdOut.println(word);
            score += solver.scoreOf(word);
            num++;
        }
        StdOut.println("Score = " + score + ", words: " + num);
    }
}