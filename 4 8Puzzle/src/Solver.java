/**
 * @author : Joey Huang
 * @since : 12/16/21, Thu
 **/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

public class Solver {
    private Node last;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<Node> PQ = new MinPQ<>(Comparator.comparingInt(o -> o.priority));
        PQ.insert(new Node(initial, 0, null));

        MinPQ<Node> twinPQ = new MinPQ<>(Comparator.comparingInt(o -> o.priority));
        twinPQ.insert(new Node(initial.twin(), 0, null));

        while (true) {
            last = move(PQ);
            if (last != null || move(twinPQ) != null) return;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private Node move(MinPQ<Node> PQ) {
        if (PQ.isEmpty()) return null;
        Node minNode = PQ.delMin();
        if (minNode.board.isGoal()) return minNode;
        for (Board b : minNode.board.neighbors()) {
            if (minNode.prevNode == null || !b.equals(minNode.prevNode.board)){
                PQ.insert(new Node(b, minNode.moves + 1, minNode));
            }
        }
        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return last != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.isSolvable() ? last.moves : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.isSolvable()) return null;
        Deque<Board> moves = new ArrayDeque<>();
        Node lastNode = last;
        while (lastNode != null) {
            moves.push(lastNode.board);
            lastNode = lastNode.prevNode;
        }
        return moves;
    }

    private class Node {
        private final Board board;
        private final int moves;
        private final int priority;
        private final Node prevNode;

        public Node(Board b, int moves, Node prevNode) {
            this.board = b;
            int manhattanDist = b.manhattan();
            this.moves = moves;
            this.prevNode = prevNode;
            this.priority = manhattanDist + this.moves;
        }
    }
}
