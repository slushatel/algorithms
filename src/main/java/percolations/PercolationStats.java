package percolations;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private static final double CONFIDENCE_COEF = 1.96;
	private final int trials;
	private final double mean;
	private final double stddev;

	// perform independent trials on an n-by-n grid
	public PercolationStats(int n, int trials) {
		if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

		double[] results = new double[trials];
		this.trials = trials;

		for (int i = 0; i < trials; i++) {
			Percolation p = new Percolation(n);
			while (!p.percolates()) {
				int row = StdRandom.uniform(n) + 1;
				int col = StdRandom.uniform(n) + 1;
				p.open(row, col);
			}
			double percThreshold = (double) p.numberOfOpenSites() / (n * n);
			results[i] = percThreshold;

		}
		mean = StdStats.mean(results);
		stddev = StdStats.stddev(results);
	}

	// sample mean of percolation threshold
	public double mean() {
		return mean;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return stddev;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mean() - CONFIDENCE_COEF * stddev() / Math.sqrt(trials);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mean() + CONFIDENCE_COEF * stddev() / Math.sqrt(trials);
	}

	// test client (see below)
	public static void main(String[] args) {
		PercolationStats ps = new PercolationStats(20, 10);
		System.out.println(ps.mean());
	}

}