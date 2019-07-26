package permutations;

import edu.princeton.cs.algs4.StdIn;

import java.util.NoSuchElementException;

public class Permutation {
	public static void main(String[] args) {
		if (args.length < 1) return;
		Integer k = Integer.valueOf(args[0]);
		RandomizedQueue<String> queue = new RandomizedQueue<>();
		String s;
		try {
			while ((s = StdIn.readString()) != null) {
				queue.enqueue(s);
			}
		} catch (NoSuchElementException e){

		}
		for (int i = 0; i < k; i++) {
			System.out.println(queue.sample());
		}
	}
}