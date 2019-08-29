package seam_carver;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Topological;

import java.awt.Color;

public class SeamCarver {

	private final Picture picture;
	private int width;
	private int height;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		checkForNull(picture);
		this.picture = new Picture(picture);
		width = picture.width();
		height = picture.height();
	}

	// current picture
	public Picture picture() {
		Picture res = new Picture(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				res.set(i, j, picture.get(i, j));
			}
		}
		return res;
	}

	// width of current picture
	public int width() {
		return width;
	}

	// height of current picture
	public int height() {
		return height;
	}

	private void checkRange(int x, int y) {
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) throw new IllegalArgumentException();
	}

	private void checkForNull(Object... parameters) {
		for (Object parameter : parameters) {
			if (parameter == null) throw new IllegalArgumentException();
		}
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		checkRange(x, y);

		if (x == 0 || x == width - 1 || y == 0 || y == height - 1) return 1000;
		Color cLeft = picture.get(x - 1, y);
		Color cRight = picture.get(x + 1, y);
		Color cTop = picture.get(x, y - 1);
		Color cBottom = picture.get(x, y + 1);

		int dRedX = cLeft.getRed() - cRight.getRed();
		int dGreenX = cLeft.getGreen() - cRight.getGreen();
		int dBlueX = cLeft.getBlue() - cRight.getBlue();

		int dRedY = cTop.getRed() - cBottom.getRed();
		int dGreenY = cTop.getGreen() - cBottom.getGreen();
		int dBlueY = cTop.getBlue() - cBottom.getBlue();

		return Math.sqrt(dRedX * dRedX + dGreenX * dGreenX + dBlueX * dBlueX + dRedY * dRedY + dGreenY * dGreenY + dBlueY * dBlueY);
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		return findSeam(true);
	}

	private int[] findSeam(boolean horizontal) {
		int w = width;
		int h = height;
		if (horizontal) {
			w = height;
			h = width;
		}

		EdgeWeightedDigraph g = new EdgeWeightedDigraph(width * height + 2);
		DirectedEdge[] edgeTo = new DirectedEdge[g.V()];
		double[] distTo = new double[g.V()];

		// start  node is g.V() - 2
		// finish node is g.V() - 1
		for (int i = 0; i < distTo.length; i++) {
			distTo[i] = Double.POSITIVE_INFINITY;
		}
		final int FIRST_NODE = g.V() - 2;
		final int LAST_NODE = g.V() - 1;
		distTo[FIRST_NODE] = 0;

		for (int i = 0; i < w * (h - 1); i++) {
			// link to the bottom left
			// not for two last rows and the column is not the first
			if (i / w < h - 2 && i % w > 0)
				addEdge(g, i, i + w - 1, w, horizontal);
			// link to the bottom
			addEdge(g, i, i + w, w, horizontal);
			// link to the bottom right
			// not for two last rows and the column is not the last
			if (i / w < h - 2 && i % w < w - 1)
				addEdge(g, i, i + w + 1, w, horizontal);
		}
		for (int i = 0; i < w; i++) {
			g.addEdge(new DirectedEdge(FIRST_NODE, i, 0));
			g.addEdge(new DirectedEdge(w * (h - 1) + i, LAST_NODE, 0));
		}

		Topological topological = new Topological(g);
		for (int v : topological.order()) {
			for (DirectedEdge edge : g.adj(v)) {
				double newDist = distTo[v] + edge.weight();
				if (newDist < distTo[edge.to()]) {
					distTo[edge.to()] = newDist;
					edgeTo[edge.to()] = edge;
				}
			}
		}

		int[] res = new int[h];
		DirectedEdge cur = edgeTo[LAST_NODE];
		int i = h;
		while (cur.from() != FIRST_NODE) {
			res[--i] = cur.from() % w;
			cur = edgeTo[cur.from()];
		}
		return res;
	}

	private void addEdge(EdgeWeightedDigraph g, int i, int ind, int w, boolean horizontal) {
		int x = ind % w;
		int y = ind / w;
		if (horizontal) {
			y = ind % w;
			x = ind / w;
		}
		g.addEdge(new DirectedEdge(i, ind, energy(x, y)));
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		return findSeam(false);
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		checkForNull(seam);
		for (int x = 0; x < width; x++) {
			for (int y = seam[x]; y < height - 1; y++) {
				picture.set(x, y, picture.get(x, y + 1));
			}
			picture.set(x, height - 1, new Color(0, 0, 0));
		}
		height--;
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		checkForNull(seam);
		for (int y = 0; y < height; y++) {
			for (int x = seam[y]; x < width - 1; x++) {
				picture.set(x, y, picture.get(x + 1, y));
			}
			picture.set(width - 1, y, new Color(0, 0, 0));
		}
		width--;
	}

	//  unit testing (optional)
//	public static void main(String[] args) {
//		Picture pic = new Picture("C:\\work\\algorithms\\src\\main\\java\\6x5.png");
//		SeamCarver sc = new SeamCarver(pic);
//		int[] seam = sc.findVerticalSeam();
//		seam = sc.findHorizontalSeam();
//		sc.removeHorizontalSeam(seam);
//		sc.picture().save("C:\\work\\algorithms\\src\\main\\java\\6x5_new.png");
//	}

}