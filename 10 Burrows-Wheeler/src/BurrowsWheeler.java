/**
 * @author : Joey Huang
 * @since : 12/31/21, Fri
 **/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String str = BinaryStdIn.readString();
        int length = str.length();
        CircularSuffixArray c = new CircularSuffixArray(str);
        StringBuilder sb = new StringBuilder();
        int first = -1;
        for (int i = 0; i < length; i++) {
            if (c.index(i) == 0) {
                sb.append(str.charAt(length - 1));
                first = i;
            } else {
                sb.append(str.charAt(c.index(i) - 1));
            }
        }
        if (first < 0) throw new IllegalArgumentException();
        BinaryStdOut.write(first);
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        int R = 256;
        int[] count = new int[R + 1];

        String str = BinaryStdIn.readString();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            count[c + 1]++;
        }

        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }

        char[] auxArr = new char[length];
        int[] next = new int[length];
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            auxArr[count[c]] = c;
            next[count[c]] = i;
            count[c]++;
        }

        int travPtr = first;
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(auxArr[travPtr]);
            travPtr = next[travPtr];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")){
            BurrowsWheeler.transform();
        } else if (args[0].equals("+")) {
            BurrowsWheeler.inverseTransform();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
