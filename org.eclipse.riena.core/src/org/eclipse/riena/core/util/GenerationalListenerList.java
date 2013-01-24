/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.core.runtime.Assert;

/**
 * The {@code GenerationalListenerList} is a sort of {@code List}
 * implementation. It does not implement all of the required methods of
 * {@code List}.<br>
 * The {@code GenerationalListenerList}'s main purpose is for dealing as a list
 * of listeners.<br>
 * It main characteristic is that it provides stable iterators although it can
 * be modified while iterating (within the same thread), e.g. a notified
 * listener could add or remove listeners.<br>
 * The benefit of this list is that it does not require to create temporary
 * lists just for the sake of iterating over it. Since this list is based on a
 * linked list approach it has similar speed characteristics as the
 * {@code LinkedList}.
 * <p>
 * Implementation note:<br>
 * The GenerationalListenerList is based on the concept of time, i.e. each list
 * entry which keeps the actual object and a reference to the next entry also
 * remembers when it was added and/or removed. This is called the generation.<br>
 * When an iterator is created it is given the actual generation. With this it
 * can traverse the list pick only those entries that are visible to this
 * generation.
 * 
 * @NotThreadSafe
 * @since 3.0
 */
public class GenerationalListenerList<E> implements Iterable<E> {

	private int generation = 0;
	private boolean modified;
	private int size;
	private Entry<E> tail = new Entry<E>(null, generation);
	private final Entry<E> head = tail;

	private int outStandigIterators;
	private final int iteratorReturnedThreshold;
	private final float removedSizeRatioThreshold;
	private final int sizeThreshold;
	private int iteratorsReturned;
	private int virtuallyRemoved;

	private final static int NEVER_GC = -1;

	// Just for printing some stats 
	private static boolean printStats = true;
	private static int statsCounter;
	private int statsListNumber;
	private int statsAdded = 0;
	private int statsRemoved = 0;
	private int statsIterated = 0;
	{
		statsListNumber = ++statsCounter;
	}

	/**
	 * This creates a {@code GenerationalListenerList} that <b>never</b>
	 * <i>garbage collects</i> removed items in the list. Removed items in the
	 * list will not be removed immediately but marked as removed.
	 */
	public GenerationalListenerList() {
		this(NEVER_GC, 1, Integer.MAX_VALUE);
	}

	/**
	 * This creates a {@code GenerationalListenerList} that performs <i>garbage
	 * collection</i> of removed items in the list when certain constraints are
	 * fulfilled. Removed items in the list will not be removed immediately but
	 * marked as removed.<br>
	 * The garbage collection will be done when:
	 * <ul>
	 * <li>there are no more <i>used</i> {@code Iterator}s, i.e. the have been
	 * completely exhausted</li>
	 * <li>a defined amount of {@code Iterator}s have been used</li>
	 * <li>a defined ratio of removed to total items has been reached</li>
	 * <li>a defined total size has been reached</li>
	 * </ul>
	 * These constraints shall avoid that the <i>garbage collection</i> runs to
	 * often and wastes time.
	 */
	public GenerationalListenerList(final int iteratorReturnedThreshold, final float removedSizeRatioThreshold,
			final int sizeThreshold) {
		this.iteratorReturnedThreshold = iteratorReturnedThreshold;
		this.removedSizeRatioThreshold = removedSizeRatioThreshold;
		this.sizeThreshold = sizeThreshold;
	}

	/**
	 * {@link java.util.List#size()}
	 */
	public int size() {
		return size;
	}

	/**
	 * {@link java.util.List#isEmpty()}
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * {@link java.util.List#contains(Object)}
	 */
	public boolean contains(final Object o) {
		if (o == null) {
			return false;
		}
		Entry<E> next = head;
		while (next != null) {
			if (next.isVisible(generation) && o.equals(next.item)) {
				return true;
			}
			next = next.nextEntry;
		}
		return false;
	}

	/**
	 * {@link java.util.List#iterator()}
	 */
	public Iterator<E> iterator() {
		statsIterated++;
		stats();

		outStandigIterators++;
		final Iterator<E> stableIterator = new StableIterator(generation);
		if (modified) {
			modified = false;
			generation++;
		}
		return stableIterator;
	}

	private void iteratorhasFinished() {
		outStandigIterators--;
		if (iteratorReturnedThreshold == -1) {
			return;
		}
		iteratorsReturned++;
		if (outStandigIterators == 0 && iteratorsReturned >= iteratorReturnedThreshold && size >= sizeThreshold
				&& ((float) virtuallyRemoved) / ((float) size) >= removedSizeRatioThreshold) {
			iteratorsReturned = 0;
			gc();
		}
	}

	private void gc() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> GC <<<<<<<<<<<<<<<<<<<<<"); //$NON-NLS-1$
		Entry<E> next = head;
		Entry<E> before = next;
		while (next != null) {
			if (next.isRemoved()) {
				System.out.println("Removing: " + next.item); //$NON-NLS-1$
				before.nextEntry = next.nextEntry;
			}
			before = next;
			next = next.nextEntry;
		}
		virtuallyRemoved = 0;
	}

	/**
	 * {@link java.util.List#add(Object)}
	 */
	public boolean add(final E o) {
		Assert.isLegal(o != null, "o nust not be null"); //$NON-NLS-1$
		size++;
		final Entry<E> entry = new Entry<E>(o, generation);
		tail.nextEntry = entry;
		tail = entry;
		modified = true;

		statsAdded++;
		stats();

		return true;
	}

	/**
	 * 
	 */
	private void stats() {
		if (!printStats) {
			return;
		}
		System.out.println(statsListNumber + " - add: " + statsAdded + ", remove: " + statsRemoved + ", iterator: " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ statsIterated);

	}

	/**
	 * {@link java.util.List#remove(Object)}
	 */
	public boolean remove(final Object o) {
		if (o == null) {
			return false;
		}
		statsRemoved++;
		stats();

		Entry<E> next = head;
		while (next != null) {
			if (next.isVisible(generation) && o.equals(next.item)) {
				next.removedGeneration = generation;
				size--;
				modified = true;
				virtuallyRemoved++;
				return true;
			}
			next = next.nextEntry;
		}
		return false;
	}

	/**
	 * {@link java.util.List#clear()}
	 */
	public void clear() {
		Entry<E> next = head;
		while (next != null) {
			if (next.isVisible(generation)) {
				next.removedGeneration = generation;
			}
			next = next.nextEntry;
		}
		size = 0;
	}

	private static final class Entry<E> {
		private final E item;
		private final int addedGeneration;
		private int removedGeneration = -1;
		private Entry<E> nextEntry;

		private Entry(final E item, final int generation) {
			this.item = item;
			this.addedGeneration = generation;
		}

		private boolean isRemoved() {
			return removedGeneration != -1;
		}

		private boolean isVisible(final int generation) {
			//			final boolean addedBefore = addedGeneration <= generation;
			//			final boolean removedBefore = removedGeneration != -1 && removedGeneration <= generation;
			return addedBefore(generation) && !removedBefore(generation);
		}

		private boolean addedBefore(final int generation) {
			return addedGeneration <= generation;
		}

		private boolean removedBefore(final int generation) {
			return removedGeneration != -1 && removedGeneration <= generation;
		}

		@Override
		public String toString() {
			return "Entry [item=" + item + ", addedGeneration=" + addedGeneration + ", removedGeneration=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ removedGeneration + "]"; //$NON-NLS-1$
		}

	}

	private class StableIterator implements Iterator<E> {

		private final int generation;
		private Entry<E> next = head.nextEntry;

		/**
		 * @param generation
		 */
		public StableIterator(final int generation) {
			this.generation = generation;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean hasNext() {
			while (next != null) {
				if (next.isVisible(generation)) {
					return true;
				}
				next = next.nextEntry;
			}
			iteratorhasFinished();
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public E next() {
			if (next == null) {
				throw new NoSuchElementException();
			}
			final E item = next.item;
			next = next.nextEntry;
			return item;
		}

		/**
		 * {@inheritDoc}
		 */
		public void remove() {
			throw new UnsupportedOperationException("StableIterator.remove"); //$NON-NLS-1$
		}

	}

	// helpers for testing/debugging, not API but needs to be public to be accessible by Junit Plug-in test
	public void printDebugList(final String header) {
		Entry<E> next = head;
		System.out.println(header + "(debug)"); //$NON-NLS-1$
		while (next != null) {
			if (next != head) {
				System.out.println(" - " + next); //$NON-NLS-1$
			}
			next = next.nextEntry;
		}
	}

	// helpers for testing/debugging, not API but needs to be public to be accessible by Junit Plug-in test
	public void printList(final String header) {
		Entry<E> next = head;
		System.out.println(header);
		while (next != null) {
			if (next != head && next.isVisible(generation)) {
				System.out.println(" - " + next); //$NON-NLS-1$
			}
			next = next.nextEntry;
		}
	}

}
