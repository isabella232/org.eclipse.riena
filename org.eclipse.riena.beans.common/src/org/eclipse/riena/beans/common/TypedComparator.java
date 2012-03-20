/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.beans.common;

import java.util.Comparator;

/**
 * A comparator that works with any class that implements Comparable.
 * <p>
 * Examples:
 * 
 * <pre>
 * new TypedComparator&lt;String&gt;();
 * new TypedComparator&lt;Integer&gt;();
 * new TypedComparator&lt;Boolean&gt;();
 * </pre>
 */
public class TypedComparator<T extends Comparable<T>> implements Comparator<Object> {

	private static final long serialVersionUID = 1L;

	public int compare(final Object o1, final Object o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null || o2 == null) { // cannot both be null here, see above
			return o1 == null ? -1 : 1;
		}
		// casting is necessary because we want to implement Comparator<Object>,
		// not Comparator<T>
		final T t1 = (T) o1;
		final T t2 = (T) o2;
		return t1.compareTo(t2);
	}
}
