import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

public class WordNet {

	private final Map<String, Integer> synSetMap;
	private final Digraph dg;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		checkForNull(synsets, hypernyms);

		synSetMap = new HashMap<>();
		In inSynSets = new In(synsets);
		while (inSynSets.hasNextLine()) {
			String[] data = inSynSets.readLine().split(",");
			String[] words = data[1].split(" ");
			for (String word : words) {
				synSetMap.put(word, Integer.valueOf(data[0]));
			}
		}

		dg = new Digraph(synSetMap.size());
		In inHypernyms = new In(hypernyms);
		while (inHypernyms.hasNextLine()) {
			String[] data = inHypernyms.readLine().split(",");
			for (int i = 1; i < data.length; i++) {
				dg.addEdge(Integer.parseInt(data[0]), Integer.parseInt(data[i]));
			}
		}

		DirectedCycle finder = new DirectedCycle(dg);
		if (finder.hasCycle()) {
			throw new IllegalArgumentException();
		}
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return synSetMap.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		checkForNull(word);
		return synSetMap.get(word) != null;
	}



	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		checkForNull(nounA, nounB);
		checkNouns(nounA, nounB);


	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		checkForNull(nounA, nounB);
		checkNouns(nounA, nounB);

	}

	private void checkForNull(Object... parameters) {
		for (Object parameter : parameters) {
			if (parameter == null) throw new IllegalArgumentException();
		}
	}

	private void checkNouns(String... nouns) {
		for (String noun : nouns) {
			if (!isNoun(noun)) throw new IllegalArgumentException();
		}
	}

	// do unit testing of this class
	public static void main(String[] args) {

	}
}