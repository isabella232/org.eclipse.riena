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

import java.lang.reflect.Array;

import org.eclipse.core.runtime.Assert;

/**
 * Helper for Array operations.
 */
public final class ArraysUtil {

	private ArraysUtil() {
		// Utility class
	}

	/**
	 * Copy from the given source array from index from to index to into a newly
	 * created array of the same type with size to - from.
	 * 
	 * @param <T>
	 * @param source
	 * @param from
	 * @param to
	 * @return
	 */
	public static <T> T[] copyRange(final T[] source, final int from, final int to) {
		Assert.isLegal(source != null, "source array must no be null."); //$NON-NLS-1$
		final int length = to - from;
		Assert.isLegal(length >= 0, "to must be greater that from"); //$NON-NLS-1$
		Assert.isLegal(to <= source.length, "to must be less or equal length of source array"); //$NON-NLS-1$
		final T[] target = (T[]) Array.newInstance(source.getClass().getComponentType(), length);
		System.arraycopy(source, from, target, 0, length);
		return target;
	}

}
