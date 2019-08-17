package slider_puzzle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board {

	private final int[][] board;
	private final int n;

	// create a board from an n-by-n array of tiles,
	// where tiles[row][col] = tile at (row, col)
	public Board(int[][] tiles) {
		n = tiles.length;
		if (tiles[0].length != n) throw new IllegalArgumentException();

		board = getTilesCopy(tiles);
	}

	private int[][] getTilesCopy(int[][] tiles) {
		int[][] copy = new int[n][n];
		for (int i = 0; i < n; i++) {
			copy[i] = Arrays.copyOf(tiles[i], n);
		}
		return copy;
	}

	// string representation of this board
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append(n + "\n");
		for (int[] line : board) {
			for (int n : line) {
				res.append(String.format("%4d", n));
			}
			res.append("\n");
		}
		return res.toString();
	}

	// board dimension n
	public int dimension() {
		return n;
	}

	// number of tiles out of place
	public int hamming() {
		int hamming = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 0) continue;
				if (board[i][j] != n * i + j + 1) hamming++;
			}
		}
		return hamming;
	}

	// sum of Manhattan distances between tiles and goal
	public int manhattan() {
		int manhattan = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 0) continue;
				manhattan += Math.abs((board[i][j] - 1) / n - i) + Math.abs((board[i][j] - 1) % n - j);
			}
		}
		return manhattan;
	}

	// is this board the goal board?
	public boolean isGoal() {
		return hamming() == 0;
	}

	// does this board equal y?
	public boolean equals(Object y) {
		if (this == y) return true;
		if (y == null || getClass() != y.getClass()) return false;
		Board board1 = (Board) y;
		if (n != board1.n) return false;
		return Arrays.deepEquals(board, board1.board);
//		for (int i = 0; i < n; i++) {
//			if (!Arrays.equals(board[i], board1.board[i])) return false;
//		}
//		return true;
	}
//
//	@Override
//	public int hashCode() {
//		int res = 0;
//		for (int[] row : board) {
//			res = res * 31 + Arrays.hashCode(row);
//		}
//		return res;
//	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		List<Board> neighbors = new LinkedList<>();
		int zeroRow = 0;
		int zeroCol = 0;
		outerLoop:
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 0) {
					zeroRow = i;
					zeroCol = j;
					break outerLoop;
				}
			}
		}

		int[][] tiles = getTilesCopy(board);
		if (zeroRow > 0) {
			swapTiles(tiles, zeroRow, zeroCol, zeroRow - 1, zeroCol);
			neighbors.add(new Board(tiles));
			swapTiles(tiles, zeroRow, zeroCol, zeroRow - 1, zeroCol);
		}
		if (zeroRow < n - 1) {
			swapTiles(tiles, zeroRow, zeroCol, zeroRow + 1, zeroCol);
			neighbors.add(new Board(tiles));
			swapTiles(tiles, zeroRow, zeroCol, zeroRow + 1, zeroCol);
		}
		if (zeroCol > 0) {
			swapTiles(tiles, zeroRow, zeroCol, zeroRow, zeroCol - 1);
			neighbors.add(new Board(tiles));
			swapTiles(tiles, zeroRow, zeroCol, zeroRow, zeroCol - 1);
		}
		if (zeroCol < n - 1) {
			swapTiles(tiles, zeroRow, zeroCol, zeroRow, zeroCol + 1);
			neighbors.add(new Board(tiles));
			swapTiles(tiles, zeroRow, zeroCol, zeroRow, zeroCol + 1);
		}
		return neighbors;
	}

	private void swapTiles(int[][] tiles, int row1, int col1, int row2, int col2) {
		int tmp = tiles[row1][col1];
		tiles[row1][col1] = tiles[row2][col2];
		tiles[row2][col2] = tmp;
	}

	// a board that is obtained by exchanging any pair of tiles
	public Board twin() {
		int[][] tiles = getTilesCopy(board);
		if (tiles[0][0] == 0) {
			swapTiles(tiles, 0, 1, 1, 1);
		} else {
			swapTiles(tiles, 0, 0, 1, 0);
		}
		return new Board(tiles);
	}

}