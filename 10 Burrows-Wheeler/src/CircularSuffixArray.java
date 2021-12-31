import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * @author : Joey Huang
 * @since : 12/31/21, Fri
 **/

public class CircularSuffixArray {
    private final Integer[] indices;
    private final int length;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.length = s.length();
        this.indices = new Integer[length];
        for (int i = 0; i < length; i++) {
            this.indices[i] = i;
        }
        Arrays.sort(this.indices, (Integer t0, Integer t1) -> {
            for (int i = 0; i < length; i++) {
                char c0 = s.charAt((t0 + i) % length);
                char c1 = s.charAt((t1 + i) % length);

                if (c0 < c1) return -1;
                if (c1 < c0) return 1;
            }
            return t0.compareTo(t1);
        });
    }

    // length of s
    public int length() {
        return this.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > this.length) throw new IllegalArgumentException();
        return this.indices[i];
    }

    // unit testing (required)
    public static void main(String[] args){
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < c.length(); i++) {
            StdOut.println(c.index(i));
        }
    }
}
