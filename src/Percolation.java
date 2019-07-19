import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int openSitesCount = 0;
    private WeightedQuickUnionUF quickUnionTree;
    private int topNode, bottomNode;
    private int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        grid = new boolean[n][n];
        quickUnionTree = new WeightedQuickUnionUF(n * n + 2);
        topNode = n * n;
        bottomNode = n * n + 1;
        this.n = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int currentNumber = (row - 1) * n + col - 1;
        if (!grid[row - 1][col - 1]) {
            openSitesCount++;
            grid[row - 1][col - 1] = true;
            if (row == 1) quickUnionTree.union(topNode, currentNumber);
            if (row == n) quickUnionTree.union(bottomNode, currentNumber);
            if (col > 1 && isOpen(row, col - 1)) quickUnionTree.union(currentNumber - 1, currentNumber);
            if (col < n && isOpen(row, col + 1)) quickUnionTree.union(currentNumber + 1, currentNumber);
            if (row > 1 && isOpen(row-1, col)) quickUnionTree.union(currentNumber - n, currentNumber);
            if (row < n && isOpen(row+1, col)) quickUnionTree.union(currentNumber + n, currentNumber);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int currentNumber = (row - 1) * n + col - 1;
        return quickUnionTree.connected(currentNumber, topNode);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return quickUnionTree.connected(bottomNode, topNode);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
