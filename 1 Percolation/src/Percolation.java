/**
 * @author : joeykhuang
 * @created : 2021-12-07
 **/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF gridTree;
    private final boolean[][] gridOpen;
    private final int n;
    private int numOpenSites = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive\n");
        }

        this.n = n;
        this.gridTree = new WeightedQuickUnionUF(n * n + 2);
        this.gridOpen = new boolean[n][n];
    }

    private boolean checkBoundary(int row, int col) {
        if (row <= 0 || row > this.n || col <= 0 || col > this.n) {
            throw new IllegalArgumentException("Index must be within borders\n");
        }
        return true;
    }

    private int calculatePosition(int row, int col) {
        return (row - 1) * n + col;
    }

    public void open(int row, int col) {
        assert checkBoundary(row, col);
        if (isOpen(row, col)) return;
        this.numOpenSites++;
        this.gridOpen[row - 1][col - 1] = true;

        if (row == 1) {
            this.gridTree.union(0, this.calculatePosition(row, col));
        }
        if (row == n) {
            this.gridTree.union(n * n + 1, this.calculatePosition(row, col));
        }
        if (row > 1 && this.gridOpen[row - 2][col - 1]) {
            this.gridTree.union(this.calculatePosition(row, col), this.calculatePosition(row - 1, col));
        }
        if (row < this.n && this.gridOpen[row][col - 1]) {
            this.gridTree.union(this.calculatePosition(row, col), this.calculatePosition(row + 1, col));
        }
        if (col > 1 && this.gridOpen[row - 1][col - 2]) {
            this.gridTree.union(this.calculatePosition(row, col), this.calculatePosition(row, col - 1));
        }
        if (col < this.n && this.gridOpen[row - 1][col]) {
            this.gridTree.union(this.calculatePosition(row, col), this.calculatePosition(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) {
        assert this.checkBoundary(row, col);
        return this.gridOpen[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        assert this.checkBoundary(row, col);
        if (row == 1 && this.isOpen(row, col)) return true;
        return this.gridTree.find(this.calculatePosition(row, col)) == this.gridTree.find(0);
    }

    public int numberOfOpenSites() {
        return this.numOpenSites;
    }

    public boolean percolates() {
        return this.gridTree.find(n * n + 1) == this.gridTree.find(0);
    }
}
