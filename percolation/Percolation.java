/* *****************************************************************************
 *  Name: Jeffrey Knox
 *  Date: 09/09/19
 *  Description: Modeling a percolation system
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private WeightedQuickUnionUF percolationSystem;
  private int gridSize, topSite, bottomSite, openSites;
  private int[][] siteStatus;
  private int[][] flatArrayPosition;

  private void throwIfIllegal(int n) {
    if (n < 1 || n > gridSize) {
      throw new IllegalArgumentException("Illegal argument " + n);
    }
  }

  private void connect(int i, int j) {
    int arrayPosition = flatArrayPosition[i][j];
    int arrayGridSize = gridSize - 1;

    if (i == 0) {
      percolationSystem.union(arrayPosition, topSite);
    }
    if (i == arrayGridSize) {
      percolationSystem.union(arrayPosition, bottomSite);
    }

    // union above
    if (i > 0 && siteStatus[i - 1][j] == 1) {
      percolationSystem.union(arrayPosition, flatArrayPosition[i - 1][j]);
    }

    // union below
    if (i < arrayGridSize && siteStatus[i + 1][j] == 1) {
      percolationSystem.union(arrayPosition, flatArrayPosition[i + 1][j]);
    }

    // union left
    if (j > 0 && siteStatus[i][j - 1] == 1) {
      percolationSystem.union(arrayPosition, flatArrayPosition[i][j - 1]);
    }

    // union right
    if (j < arrayGridSize && siteStatus[i][j + 1] == 1) {
      percolationSystem.union(arrayPosition, flatArrayPosition[i][j + 1]);
    }
  }

  /////////////////
  // Public methods
  /////////////////

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    gridSize = n;
    openSites = 0;
    throwIfIllegal(n);
    percolationSystem = new WeightedQuickUnionUF(n * n + 2);
    topSite = n * n;
    bottomSite = topSite + 1;
    siteStatus = new int[n][n];
    flatArrayPosition = new int[n][n];
    int counter = 0;
    // all sites set as 0 initially to represent closed
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        siteStatus[i][j] = 0;
        flatArrayPosition[i][j] = counter;
        counter++;
      }
    }
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    throwIfIllegal(row);
    throwIfIllegal(col);
    siteStatus[row - 1][col - 1] = 1;
    openSites++;
    connect(row - 1, col - 1);
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    throwIfIllegal(row);
    throwIfIllegal(col);
    return siteStatus[row - 1][col - 1] == 1;
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    throwIfIllegal(row);
    throwIfIllegal(col);
    int arrayPosition = flatArrayPosition[row - 1][col - 1];
    return percolationSystem.connected(arrayPosition, topSite);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    return percolationSystem.connected(bottomSite, topSite);
  }
}
