package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class SAP {

	private final Digraph G;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		checkForNull(G);
		this.G = new Digraph(G);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		SAPResult res = getSAP(v, w);
		return res == null ? -1 : res.length;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		SAPResult res = getSAP(v, w);
		return res == null ? -1 : res.ancestor;
	}

	private int[] getAllDist(int v) {
		// assume that values can be mapped to indexes as 1:1
		boolean[] used = new boolean[G.V()];
		int[] dist = new int[G.V()];
		for (int i = 0; i < G.V(); i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		Queue<Integer> queue = new Queue<>();
		used[v] = true;
		queue.enqueue(v);
		dist[v] = 0;
		while (!queue.isEmpty()) {
			v = queue.dequeue();
			for (Integer vertex : G.adj(v)) {
				if (!used[vertex]) {
					used[vertex] = true;
					queue.enqueue(vertex);
					dist[vertex] = dist[v] + 1;
				}
			}
		}
		return dist;
	}

	private static class SAPResult {
		final int length;
		final int ancestor;

		public SAPResult(int length, int ancestor) {
			this.length = length;
			this.ancestor = ancestor;
		}
	}

	private SAPResult getSAP(int v, int w) {
		checkForRange(v, w);
		int[] dist1 = getAllDist(v);
		int[] dist2 = getAllDist(w);
		int min = Integer.MAX_VALUE;
		int minVertex = -1;
		for (int i = 0; i < G.V(); i++) {
			if (dist1[i] != Integer.MAX_VALUE && dist2[i] != Integer.MAX_VALUE && dist1[i] + dist2[i] < min) {
				min = dist1[i] + dist2[i];
				minVertex = i;
			}
		}
		return min == Integer.MAX_VALUE ? null : new SAPResult(min, minVertex);
	}

	private SAPResult getSAP(Iterable<Integer> v, Iterable<Integer> w) {
		checkForNull(v, w);
		int min = Integer.MAX_VALUE;
		int ancestor = -1;
		for (Integer vertex1 : v) {
			for (Integer vertex2 : w) {
				checkForNull(vertex1, vertex2);
				SAPResult res = getSAP(vertex1, vertex2);
				if (res != null && res.length < min) {
					min = res.length;
					ancestor = res.ancestor;
				}
			}
		}
		return min == Integer.MAX_VALUE ? null : new SAPResult(min, ancestor);
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		SAPResult res = getSAP(v, w);
		return res == null ? -1 : res.length;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		SAPResult res = getSAP(v, w);
		return res == null ? -1 : res.ancestor;
	}

	private void checkForNull(Object... parameters) {
		for (Object parameter : parameters) {
			if (parameter == null) throw new IllegalArgumentException();
		}
	}

	private void checkForRange(int... parameters) {
		for (int parameter : parameters) {
			if (parameter >= G.V() || parameter < 0) throw new IllegalArgumentException();
		}
	}

	// do unit testing of this class
//	public static void main(String[] args) {
//
//	}
}
