package permutations;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private static final int START_CAPACITY = 10;
	private Item[] items;
	private int first = -1;
	private int last = -1;

	// construct an empty randomized queue
	public RandomizedQueue() {
		createNewItems(START_CAPACITY);
	}

	private void createNewItems(int capacity) {
		items = (Item[]) new Object[capacity];
	}

	// is the randomized queue empty?
	public boolean isEmpty() {
		return first == -1;
	}

	// return the number of items on the randomized queue
	public int size() {
		return isEmpty() ? 0 : last - first + 1;
	}

	// add the item
	public void enqueue(Item item) {
		if (item == null) throw new IllegalArgumentException();
		ensureCapacity();

		if (isEmpty()) {
			first = 0;
			last = 0;
		} else {
			last++;
		}
		items[last] = item;
	}

	private void ensureCapacity() {
		if (last == items.length - 1) {
			int size = size();
			if (size <= items.length / 2) {
				// move elements to the start of the array
				for (int i = 0; i < size(); i++) {
					items[i] = items[i + first];
					items[i + first] = null;
				}
			} else {
				// create new array
				Item[] itemsOld = items;
				createNewItems(2 * size);
				for (int i = 0; i < size(); i++) {
					items[i] = itemsOld[i + first];
				}
			}
			initPointers(size());
		}
	}

	private void shrinkCapacity() {
		int size = size();
		if (size <= items.length / 4) {
			Item[] itemsOld = items;
			createNewItems(2 * size);
			for (int i = 0; i < size(); i++) {
				items[i] = itemsOld[i + first];
			}
			initPointers(size());
		}
	}

	private void initPointers(int size) {
		first = 0;
		last = size - 1;
	}

	// remove and return a random item
	public Item dequeue() {
		checkIsEmpty();

		int size = size();
		int random = StdRandom.uniform(size);

		Item item = items[first + random];
		if (random > 0) {
			items[first + random] = items[first];
			items[first] = null;
		}
		first++;

		if (first > last) {
			first = -1;
			last = -1;
		} else {
			shrinkCapacity();
		}
		return item;
	}

	private void checkIsEmpty() {
		if (isEmpty()) throw new NoSuchElementException();
	}

	// return a random item (but do not remove it)
	public Item sample() {
		checkIsEmpty();

		int random = StdRandom.uniform(first, last + 1);
		return items[random];
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return new RandomizeQueueIterator();
	}

	private class RandomizeQueueIterator implements Iterator<Item> {
		private final int[] unusedIndexes;
		private int unusedCount;

		public RandomizeQueueIterator() {
			int size = size();
			unusedIndexes = new int[size];
			unusedCount = size;
		}

		@Override
		public boolean hasNext() {
			return unusedCount > 0;
		}

		@Override
		public Item next() {
			if (unusedCount == 0) throw new NoSuchElementException();

			int random = StdRandom.uniform(unusedCount);
			unusedCount--;
			Item res;
			if (unusedIndexes[random] == 0) {
				res = items[random + first];
			} else {
				res = items[unusedIndexes[random] + first];
			}
			if (random < unusedCount)
				unusedIndexes[random] = unusedIndexes[unusedCount] == 0 ? unusedCount : unusedIndexes[unusedCount];
			return res;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	// unit testing (required)
	public static void main(String[] args) {
		RandomizedQueue<Integer> queue = new RandomizedQueue<>();
		System.out.println(queue.isEmpty());
		System.out.println(queue.size());
		for (int i = 0; i < 10; i++) {
			queue.enqueue(i);
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(queue.dequeue());
		}
		for (int i = 10; i < 20; i++) {
			queue.enqueue(i);
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(queue.dequeue());
		}
		System.out.println(queue.isEmpty());
		System.out.println(queue.size());

		System.out.println("sample");
		for (int i = 0; i < 20; i++) {
			System.out.println(queue.sample());
		}
		System.out.println("iterate over queue");
		for (int item : queue) {
			System.out.println(item);
		}
	}
}