/**
 * @author : Joey Huang
 * @since : 12/31/21, Fri
 **/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    private static final int R = 256;
    private static StringBuilder generateSymTable() {
        StringBuilder symTable = new StringBuilder();
        for (int i = 0; i < R; i++) {
            symTable.append((char) i);
        }
        return symTable;
    }

    public static void encode() {
        StringBuilder symTable = generateSymTable();
        while (!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();
            int idx = symTable.indexOf("" + c);
            BinaryStdOut.write(idx, 8);
            symTable.deleteCharAt(idx).insert(0, c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        StringBuilder symTable = generateSymTable();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char cAt = symTable.charAt(c);
            BinaryStdOut.write(cAt);
            symTable.deleteCharAt(c).insert(0, cAt);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        } else if (args[0].equals("+")) {
            MoveToFront.decode();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
