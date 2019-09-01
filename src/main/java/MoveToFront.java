import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final char[] DICT = new char[256];

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        initDict();
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            char i;
            for (i = 0; i < DICT.length; i++) {
                if (ch == DICT[i]) {
                    BinaryStdOut.write(i);
                    break;
                }
            }
            for (; i > 0; i--) {
                DICT[i] = DICT[i - 1];
            }
            DICT[0] = ch;
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        initDict();
        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            char ch = DICT[i];
            BinaryStdOut.write(ch);
            for (; i > 0; i--) {
                DICT[i] = DICT[i - 1];
            }
            DICT[0] = ch;
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    private static void initDict() {
        for (int i = 0; i < DICT.length; i++) {
            DICT[i] = (char) i;
        }
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
//    public static void main(String[] args) throws IOException {
//        FileInputStream is = null;
//        FileOutputStream os = null;
//        os = new FileOutputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra.txt"));
//        System.setOut(new PrintStream(os));
//        for (int i = 0; i < 256; i++) {
//            BinaryStdOut.write(i);
//        }
//        BinaryStdOut.close();
//
//        is = new FileInputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra.txt"));
//        System.setIn(is);
//        os = new FileOutputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra_out.txt"));
//        System.setOut(new PrintStream(os));
//        encode();
//        is.close();
//        os.close();
//
//        is = new FileInputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra_out.txt"));
//        System.setIn(is);
//        os = new FileOutputStream(new File("D:\\work\\algorithms\\src\\main\\java\\abra_out_2.txt"));
//        System.setOut(new PrintStream(os));
//        decode();
//        is.close();
//        os.close();
//    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else {
            decode();
        }
    }

}