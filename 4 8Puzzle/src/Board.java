/**
 * @author : Joey Huang
 * @since : 12/16/21, Thu
 **/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private final int n;
    private int[][] board;
    private int zeroPosRow;
    private int zeroPosCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.board = new int[n][n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (tiles[i][j] == 0) {
                    this.zeroPosRow = i;
                    this.zeroPosCol = j;
                }
                this.board[i][j] = tiles[i][j];
            }
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            StdOut.println(initial.hamming());
            StdOut.println("____");
            StdOut.println(initial.manhattan());
            StdOut.println("____");
            StdOut.println(initial.isGoal());
            StdOut.println("____");
            Iterable<Board> neighborBoards = initial.neighbors();
            for (Board b : neighborBoards) {
                StdOut.println(b);
            }
            StdOut.println("____");
            StdOut.println(initial.twin());
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder(this.n + "\n");
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                result.append(" ").append(this.board[i][j]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDist = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != i * this.n + j + 1 && this.board[i][j] != 0) {
                    hammingDist++;
                }
            }
        }
        return hammingDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDist = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] == 0) continue;
                int row = (this.board[i][j] - 1) / this.n;
                int col = (this.board[i][j] - 1) % this.n;
                int dis = Math.abs(row - i) + Math.abs(col - j);
                manhattanDist += dis;
            }
        }
        return manhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (!(y instanceof Board)) return false;
        Board yBoard = (Board) y;
        if (this.n != yBoard.dimension()) return false;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != yBoard.board[i][j]) return false;
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard = new Board(this.board);
        outer:
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n - 1; j++) {
                if (!(twinBoard.board[i][j] == 0) && !(twinBoard.board[i][j + 1] == 0)) {
                    int temp = twinBoard.board[i][j];
                    twinBoard.board[i][j] = twinBoard.board[i][j + 1];
                    twinBoard.board[i][j + 1] = temp;
                    break outer;
                }
            }
        }
        return twinBoard;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Board[] neighborBoards = new Board[4];
        int numBoards = 0;
        for (int i = 0; i < 4; i++) {
            neighborBoards[i] = new Board(this.board);
        }
        if (this.zeroPosRow > 0) {
            neighborBoards[numBoards].board[this.zeroPosRow][this.zeroPosCol] =
                    neighborBoards[numBoards].board[this.zeroPosRow - 1][this.zeroPosCol];
            neighborBoards[numBoards].zeroPosRow--;
            neighborBoards[numBoards++].board[this.zeroPosRow - 1][this.zeroPosCol] = 0;
        }
        if (this.zeroPosCol > 0) {
            neighborBoards[numBoards].board[this.zeroPosRow][this.zeroPosCol] =
                    neighborBoards[numBoards].board[this.zeroPosRow][this.zeroPosCol - 1];
            neighborBoards[numBoards].zeroPosCol--;
            neighborBoards[numBoards++].board[this.zeroPosRow][this.zeroPosCol - 1] = 0;
        }
        if (this.zeroPosRow < this.n - 1) {
            neighborBoards[numBoards].board[this.zeroPosRow][this.zeroPosCol] =
                    neighborBoards[numBoards].board[this.zeroPosRow + 1][this.zeroPosCol];
            neighborBoards[numBoards].zeroPosRow++;
            neighborBoards[numBoards++].board[this.zeroPosRow + 1][this.zeroPosCol] = 0;
        }
        if (this.zeroPosCol < this.n - 1) {

            neighborBoards[numBoards].board[this.zeroPosRow][this.zeroPosCol] =
                    neighborBoards[numBoards].board[this.zeroPosRow][this.zeroPosCol + 1];
            neighborBoards[numBoards].zeroPosCol++;
            neighborBoards[numBoards++].board[this.zeroPosRow][this.zeroPosCol + 1] = 0;
        }

        Board[] finalNeighborBoards = Arrays.copyOf(neighborBoards, numBoards);
        return () -> Arrays.stream(finalNeighborBoards).iterator();
    }
}
