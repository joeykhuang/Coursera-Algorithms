/**
 * @author : joeykhuang
 * @created : 2021-12-07
 **/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private final double mean;
    private final double std;
    private static final double CONFIDENCE_95 = 1.96;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and Trials cannot be negative");
        }
        this.trials = trials;
        double[] percolationThresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int randomRow = StdRandom.uniform(1, n + 1);
                int randomColumn = StdRandom.uniform(1, n + 1);
                perc.open(randomRow, randomColumn);
            }
            percolationThresholds[i] = (1.0 * perc.numberOfOpenSites()) / (n * n);
        }
        this.mean = StdStats.mean(percolationThresholds);
        this.std = StdStats.stddev(percolationThresholds);
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.std;
    }

    public double confidenceLo() {
        return this.mean - CONFIDENCE_95 * this.std / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        return this.mean + CONFIDENCE_95 * this.std / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            int n = Integer.parseInt(args[0]);
            int trials = Integer.parseInt(args[1]);
            PercolationStats percStats = new PercolationStats(n, trials);
            StdOut.println("mean                    = " + percStats.mean());
            StdOut.println("stddev                  = " + percStats.stddev());
            StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]");
        } else {
            throw new IllegalArgumentException("Wrong number of input arguments");
        }
    }
}
