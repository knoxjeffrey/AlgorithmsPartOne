/* *****************************************************************************
 *  Name: Jeffrey Knox
 *  Date: 09/09/19
 *  Description: Modeling a percolation system
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final WeightedQuickUnionUF percolationSystem;
  private final WeightedQuickUnionUF percolationSystemNoBottom;
  private final int gridSize;
  private final int topSite;
  private final int bottomSite;
  private int openSites;
  private boolean[][] siteStatus;
  private final int[][] flatArrayPosition;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(final int n) {
    gridSize = n;
    openSites = 0;
    throwIfIllegal(n);
    percolationSystem = new WeightedQuickUnionUF(n * n + 2);
    percolationSystemNoBottom = new WeightedQuickUnionUF(n * n + 1);
    topSite = n * n;
    bottomSite = topSite + 1;
    siteStatus = new boolean[n][n];
    flatArrayPosition = new int[n][n];
    int counter = 0;
    // all sites set as 0 initially to represent closed
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        siteStatus[i][j] = false;
        flatArrayPosition[i][j] = counter;
        counter++;
      }
    }
  }

  // opens the site (row, col) if it is not open already
  public void open(final int row, final int col) {
    throwIfIllegal(row);
    throwIfIllegal(col);
    if (!isOpen(row, col)) {
      siteStatus[row - 1][col - 1] = true;
      openSites++;
      connect(row - 1, col - 1);
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(final int row, final int col) {
    throwIfIllegal(row);
    throwIfIllegal(col);
    return siteStatus[row - 1][col - 1];
  }

  // is the site (row, col) full?
  public boolean isFull(final int row, final int col) {
    throwIfIllegal(row);
    throwIfIllegal(col);
    final int arrayPosition = flatArrayPosition[row - 1][col - 1];
    return percolationSystemNoBottom.connected(arrayPosition, topSite);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    return percolationSystem.connected(bottomSite, topSite);
  }

  private void throwIfIllegal(final int n) {
    if (n <= 0 || n > gridSize) {
      throw new IllegalArgumentException("Illegal argument " + n);
    }
  }

  private void connect(final int i, final int j) {
    final int arrayPosition = flatArrayPosition[i][j];
    final int arrayGridSize = gridSize - 1;

    connectTopBottom(i, arrayPosition, arrayGridSize);

    // union above
    if (i > 0 && siteStatus[i - 1][j]) {
      final int flatPos = flatArrayPosition[i - 1][j];
      percolationSystemNoBottom.union(arrayPosition, flatPos);
      percolationSystem.union(arrayPosition, flatPos);
    }

    // union below
    if (i < arrayGridSize && siteStatus[i + 1][j]) {
      final int flatPos = flatArrayPosition[i + 1][j];
      percolationSystemNoBottom.union(arrayPosition, flatPos);
      percolationSystem.union(arrayPosition, flatPos);
    }

    // union left
    if (j > 0 && siteStatus[i][j - 1]) {
      final int flatPos = flatArrayPosition[i][j - 1];
      percolationSystemNoBottom.union(arrayPosition, flatPos);
      percolationSystem.union(arrayPosition, flatPos);
    }

    // union right
    if (j < arrayGridSize && siteStatus[i][j + 1]) {
      final int flatPos = flatArrayPosition[i][j + 1];
      percolationSystemNoBottom.union(arrayPosition, flatPos);
      percolationSystem.union(arrayPosition, flatPos);
    }
  }

  private void connectTopBottom(int i, int arrayPosition, int arrayGridSize) {
    if (i == 0) {
      percolationSystemNoBottom.union(arrayPosition, topSite);
      percolationSystem.union(arrayPosition, topSite);
    }
    if (i == arrayGridSize) {
      percolationSystem.union(arrayPosition, bottomSite);
    }
  }
}
