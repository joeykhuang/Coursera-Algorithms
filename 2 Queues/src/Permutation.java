/**
 * @author : Joey Huang
 * @since : 12/12/21, Sun
 **/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rqueue = new RandomizedQueue<>();
        final int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            rqueue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(rqueue.dequeue());
        }
    }
}
