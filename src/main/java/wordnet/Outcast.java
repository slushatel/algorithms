package wordnet;

public class Outcast {
	private final WordNet wordNet;

	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		this.wordNet = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		int minDist = 0;
		String outcast = null;
		for (String noun1 : nouns) {
			int sum = 0;
			for (String noun2 : nouns) {
				int curDist = wordNet.distance(noun1, noun2);
				if (curDist != -1) sum += curDist;
			}
			if (sum > minDist) {
				minDist = sum;
				outcast = noun1;
			}
		}
		return outcast;
	}

	// see test client below
//	public static void main(String[] args) {
//
//	}
}