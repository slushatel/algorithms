package slider_puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Solver {
	private final boolean isSolvable;
	//	private final int moves;
	private final List<Board> solution;

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		if (initial == null) throw new IllegalArgumentException();
		Function<Board, Integer> hFunction = Board::hamming;

		solution = solve(initial, hFunction);
		isSolvable = solution != null;
	}

	private class SearchNode {
		final Board board;
		final SearchNode parent;
		final int moves;
		final int hScore;


		public SearchNode(Board board, SearchNode parent, int moves) {
			this.board = board;
			this.parent = parent;
			this.moves = moves;
			this.hScore = board.manhattan();
		}
	}

	private class SearchNodeComparator implements Comparator<SearchNode> {
		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			return Integer.compare(o1.moves + o1.hScore, o2.moves + o2.hScore);
		}
	}

	private List<Board> solve(Board initial, Function<Board, Integer> hFunction) {
		Board twinBoard = initial.twin();

		SearchNodeComparator searchNodeComparator = new SearchNodeComparator();

		MinPQ<SearchNode> pq = new MinPQ<>(searchNodeComparator);
		pq.insert(new SearchNode(initial, null, 0));
		MinPQ<SearchNode> pqTwin = new MinPQ<>(searchNodeComparator);
		pqTwin.insert(new SearchNode(twinBoard, null, 0));

		boolean useInitial = true;
		while (!pq.isEmpty() || !pqTwin.isEmpty()) {
			MinPQ<SearchNode> currentPQ = useInitial ? pq : pqTwin;
			SearchNode current = currentPQ.delMin();

			if (current.board.isGoal()) {
				if (useInitial) return buildPath(current);
				else return null;
			}
			for (Board neighbor : current.board.neighbors()) {
				if (current.parent != null && current.parent.board.equals(neighbor)) continue;
				currentPQ.insert(new SearchNode(neighbor, current, current.moves + 1));
			}
			useInitial = !useInitial;
		}
		return null;
	}

	private List<Board> buildPath(SearchNode current) {
		List<Board> path = new LinkedList<>();
		while (current != null) {
			path.add(0, current.board);
			current = current.parent;
		}
		return path;
	}

//	private List<Board> solve(Board initial, Function<Board, Integer> hFunction) {
//		Board twinBoard = initial.twin();
//
//		MinPQ<Board> pq = new MinPQ<>(Comparator.comparingInt(hFunction::apply));
//		pq.insert(initial);
//		MinPQ<Board> pqTwin = new MinPQ<>(Comparator.comparingInt(hFunction::apply));
//		pqTwin.insert(twinBoard);
//
//		Set<Board> used = new HashSet<>();
//
//		Map<Board, Board> predecessors = new HashMap<>();
//
//		Map<Board, Integer> gScore = new HashMap<>();
//		gScore.put(initial, 0);
//		gScore.put(twinBoard, 0);
//
//		Map<Board, Integer> fScore = new HashMap<>();
//		fScore.put(initial, hFunction.apply(initial));
//		fScore.put(twinBoard, hFunction.apply(twinBoard));
//
//		boolean useInitial = true;
//		while (!pq.isEmpty() || !pqTwin.isEmpty()) {
//			MinPQ<Board> currentPQ = useInitial ? pq : pqTwin;
//			Board current = currentPQ.delMin();
//			if (used.contains(current)) continue;
//			else used.add(current);
//
//			if (current.isGoal()) {
//				if (useInitial) return buildPath(predecessors, current);
//				else return null;
//			}
//			for (Board neighbor : current.neighbors()) {
//				if (used.contains(neighbor)) continue;
//				currentPQ.insert(neighbor);
//
//				int neighborGScore = gScore.get(current) + 1;
//				if (neighborGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
//					predecessors.put(neighbor, current);
//					gScore.put(neighbor, neighborGScore);
//					fScore.put(neighbor, neighborGScore + hFunction.apply(neighbor));
//				}
//			}
//			useInitial = !useInitial;
//		}
//		return null;
//	}

//	private List<Board> buildPath(Map<Board, Board> predecessors, Board current) {
//		List<Board> path = new LinkedList<>();
//		path.add(current);
//		while (true) {
//			current = predecessors.get(current);
//			if (current != null) path.add(0, current);
//			else break;
//		}
//		return path;
//	}

	// is the initial board solvable? (see below)
	public boolean isSolvable() {
		return isSolvable;
	}

	// min number of moves to solve initial board
	public int moves() {
		return isSolvable ? solution.size() - 1 : -1;
	}

	// sequence of boards in a shortest solution
	public Iterable<Board> solution() {
		return solution;
	}

	// test client (see below)
	public static void main(String[] args) {
		int[][] tiles = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
		Board initial = new Board(tiles);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}

}