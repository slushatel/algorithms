package permutations;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private Node first;
	private Node last;
	private int size;

	private class Node {
		Item val;
		Node next;
		Node prev;

		public Node(Item val, Node next, Node prev) {
			this.val = val;
			this.next = next;
			this.prev = prev;
		}
	}

	// construct an empty deque
	public Deque() {

	}

	// is the deque empty?
	public boolean isEmpty() {
		return size == 0;
	}

	// return the number of items on the deque
	public int size() {
		return size;
	}

	// add the item to the front
	public void addFirst(Item item) {
		checkAdd(item);
		first = new Node(item, first, null);
		if (first.next != null) first.next.prev = first;
		size++;
		if (last == null) last = first;
	}

	// add the item to the back
	public void addLast(Item item) {
		checkAdd(item);
		last = new Node(item, null, last);
		if (last.prev != null) last.prev.next = last;
		size++;
		if (first == null) first = last;
	}

	private void checkAdd(Item item) {
		if (item == null) throw new IllegalArgumentException();
	}

	// remove and return the item from the front
	public Item removeFirst() {
		checkRemove();
		Node node = first;
		first = first.next;
		if (first != null) first.prev = null;
		size--;
		if (first == null) last = null;
		return node.val;
	}

	// remove and return the item from the back
	public Item removeLast() {
		checkRemove();
		Node node = last;
		last = last.prev;
		if (last != null) last.next = null;
		size--;
		if (last == null) first = null;
		return node.val;
	}

	private void checkRemove() {
		if (size == 0) throw new NoSuchElementException();
	}

	// return an iterator over items in order from front to back
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	private class DequeIterator implements Iterator<Item> {
		Node current = first;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
			if (current == null) throw new NoSuchElementException();
			Item val = current.val;
			current = current.next;
			return val;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	// unit testing (required)
	public static void main(String[] args) {
		System.out.println(Integer.MIN_VALUE);
		System.out.println(Math.abs(Integer.MIN_VALUE));
		Deque<String> deque = new Deque<>();
		System.out.println(deque.size);
		deque.addFirst("first");
		System.out.println(deque.size);
		deque.addLast("last");
		System.out.println(deque.removeFirst());
		System.out.println(deque.removeLast());
		System.out.println(deque.size);

		deque.addFirst("first1");
		deque.addFirst("first2");
		deque.addFirst("first3");
		deque.addLast("last");
		for (String item : deque) {
			System.out.println(item);
		}

	}

}