import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

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
			if (size() <= items.length / 2) {
				// move elements to the start of the array
				for (int i = 0; i < size(); i++) {
					items[i] = items[i + first];
					items[i + first] = null;
				}
			} else {
				// create new array
				Item[] itemsOld = items;
				createNewItems(2*items.length);
				for (int i = 0; i < size(); i++) {
					items[i] = itemsOld[i + first];
				}
			}
			first = 0;
			last = size - 1;
		}
	}

	private void shrinkCapacity() {

	}

	// remove and return a random item
	public Item dequeue() {
		checkIsEmpty();

		Item item = items[first];
		items[first] = null;
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

		@Override
		public boolean hasNext() {
			return !isEmpty();
		}

		@Override
		public Item next() {
			return sample();
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
		int i = 0;
		for (int item : queue) {
			if (++i == 20) break;
			System.out.println(item);
		}
	}

}