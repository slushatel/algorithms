import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        BinaryStdIn.close();

        CircularSuffixArray cfa = new CircularSuffixArray(s);
        int i = 0;
        while (cfa.index(i) != 0) i++;
        BinaryStdOut.write(i);

        int len = cfa.length();
        for (int j = 0; j < len; j++) {
            int charInd = (cfa.index(j) - 1 + len) % len;
            char ch = s.charAt(charInd);
            BinaryStdOut.write(ch);
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        BinaryStdIn.close();

        char[] sorted = t.toCharArray();
        Arrays.sort(sorted);

        int len = t.length();
        Map<Character, Queue<Integer>> nextMap = new HashMap<>();
        for (int i = 0; i < len; i++) {
            Character ch = t.charAt(i);
            Queue<Integer> queue = nextMap.get(ch);
            if (queue == null) {
                queue = new Queue<>();
                nextMap.put(ch, queue);
            }
            queue.enqueue(i);
        }

        int[] next = new int[len];
        for (int i = 0; i < len; i++) {
            next[i] = nextMap.get(sorted[i]).dequeue();
        }

        BinaryStdOut.write(sorted[first]);
        for (int i = 0; i < len-1; i++) {
            first = next[first];
            BinaryStdOut.write(sorted[first]);
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
//    public static void main(String[] args) throws IOException {
//        FileInputStream is = null;
//        FileOutputStream os = null;
//        is = new FileInputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra.txt"));
//        System.setIn(is);
//        os = new FileOutputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra_out.txt"));
//        System.setOut(new PrintStream(os));
//        transform();
//        is.close();
//        os.close();
//
//        is = new FileInputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra_out.txt"));
//        System.setIn(is);
//        os = new FileOutputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra_out_2.txt"));
//        System.setOut(new PrintStream(os));
//        inverseTransform();
//        is.close();
//        os.close();
//    }
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else {
            inverseTransform();
        }
    }

}