/* *****************************************************************************
 *  Name: Jeffrey Knox
 *  Date: 09/09/19
 *  Description: Percolation computation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private Percolation perc;
  private double[] thresholdValues;
  private int experiments;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("Illegal argument");
    }

    thresholdValues = new double[trials];
    experiments = trials;

    for (int i = 0; i < trials; i++) {
      perc = new Percolation(n);
      int a, b;
      while (!perc.percolates()) {
        a = StdRandom.uniform(1, n + 1);
        b = StdRandom.uniform(1, n + 1);
        if (!perc.isOpen(a, b))
          perc.open(a, b);
      }
      thresholdValues[i] = perc.numberOfOpenSites() / (double) (n * n);
    }
  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(thresholdValues);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(thresholdValues);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - ((1.96 * stddev()) / Math.sqrt(experiments));
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + ((1.96 * stddev()) / Math.sqrt(experiments));
  }

  // test client (see below)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int t = Integer.parseInt(args[1]);
    PercolationStats ps = new PercolationStats(n, t);

    String confidenceInterval = "[" + ps.confidenceLo() + ", " +
      ps.confidenceHi() + "]";
    StdOut.println("mean                    = " + ps.mean());
    StdOut.println("stddev                  = " + ps.stddev());
    StdOut.println("95% confidence interval = " + confidenceInterval);
  }
}
