import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;

/**
 * @author : Joey Huang
 * @since : 12/24/21, Fri
 **/

public class SAP {

    private final Digraph g;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        checkNull(G);
        this.g = G;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private void checkNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }

    private void checkIterables(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer vertex : v) {
            if (vertex == null || vertex >= this.g.V()) throw new IllegalArgumentException();
        }
        for (Integer vertex : w) {
            if (vertex == null || vertex >= this.g.V()) throw new IllegalArgumentException();
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v >= this.g.V() || w >= this.g.V()) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(this.g, w);
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < this.g.V(); i++) {
            if (p1.hasPathTo(i) && p2.hasPathTo(i)) {
                minLength = Math.min(minLength, p1.distTo(i) + p2.distTo(i));
            }
        }
        if (minLength == Integer.MAX_VALUE) return -1;
        return minLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v >= this.g.V() || w >= this.g.V()) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(this.g, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = 0;
        for (int i = 0; i < this.g.V(); i++) {
            if (p1.hasPathTo(i) && p2.hasPathTo(i)) {
                int dist = p1.distTo(i) + p2.distTo(i);
                if (dist < minLength) {
                    minLength = dist;
                    ancestor = i;
                }
            }
        }
        if (minLength == Integer.MAX_VALUE) return -1;
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        this.checkIterables(v, w);
        BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(this.g, w);
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < this.g.V(); i++) {
            if (p1.hasPathTo(i) && p2.hasPathTo(i)) {
                minLength = Math.min(minLength, p1.distTo(i) + p2.distTo(i));
            }
        }
        if (minLength == Integer.MAX_VALUE) return -1;
        return minLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        this.checkIterables(v, w);
        BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(this.g, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = 0;
        for (int i = 0; i < this.g.V(); i++) {
            if (p1.hasPathTo(i) && p2.hasPathTo(i)) {
                int dist = p1.distTo(i) + p2.distTo(i);
                if (dist < minLength) {
                    minLength = dist;
                    ancestor = i;
                }
            }
        }
        if (minLength == Integer.MAX_VALUE) return -1;
        return ancestor;
    }
}
