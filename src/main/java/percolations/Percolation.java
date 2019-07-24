package percolations;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private boolean[][] grid;
	private int openSitesCount = 0;
	private final WeightedQuickUnionUF quickUnionTree;
	private final int topNode, bottomNode;
	private final int n;

	// creates n-by-n grid, with all sites initially blocked
	public Percolation(int n) {
		if (n <= 0) throw new IllegalArgumentException();

		grid = new boolean[n][n];
		quickUnionTree = new WeightedQuickUnionUF(n * n + 2);
		topNode = n * n;
		bottomNode = n * n + 1;
		this.n = n;
	}

	// opens the site (row, col) if it is not open already
	public void open(int row, int col) {
		checkRowCol(row, col);

		int currentNumber = map2Dto1DIndices(row, col);
		if (!grid[row - 1][col - 1]) {
			openSitesCount++;
			grid[row - 1][col - 1] = true;
			if (row == 1) quickUnionTree.union(topNode, currentNumber);
			if (row == n) quickUnionTree.union(bottomNode, currentNumber);
			if (col > 1 && isOpen(row, col - 1)) quickUnionTree.union(currentNumber - 1, currentNumber);
			if (col < n && isOpen(row, col + 1)) quickUnionTree.union(currentNumber + 1, currentNumber);
			if (row > 1 && isOpen(row - 1, col)) quickUnionTree.union(currentNumber - n, currentNumber);
			if (row < n && isOpen(row + 1, col)) quickUnionTree.union(currentNumber + n, currentNumber);
		}
	}

	private int map2Dto1DIndices(int row, int col) {
		return (row - 1) * n + col - 1;
	}

	private void checkRowCol(int row, int col) {
		if (row <= 0 || col <= 0 || row > n || col > n) throw new IllegalArgumentException();
	}

	// is the site (row, col) open?
	public boolean isOpen(int row, int col) {
		checkRowCol(row, col);
		return grid[row - 1][col - 1];
	}

	// is the site (row, col) full?
	public boolean isFull(int row, int col) {
		checkRowCol(row, col);
		int currentNumber = map2Dto1DIndices(row, col);
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

	public static void main(String[] args) {
		Percolation p = new Percolation(10);
		p.open(2147483647, 2147483647);
		System.out.println();
	}
}
