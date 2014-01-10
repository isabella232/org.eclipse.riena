/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.util;

/**
 * Some helper methods on objects handling {@code null} input gracefully.
 * 
 * @since 3.0
 */
public final class ObjectUtils {

	private ObjectUtils() {
		Nop.reason("utility"); //$NON-NLS-1$
	}

	/**
	 * Compare two {@code Object}s for equality ({@code Object.equals()}).
	 * 
	 * @param object1
	 *            may be {@code null}
	 * @param object2
	 *            may be {@code null}
	 * @return {@code true} if both {@code object1} and {@code object2} are
	 *         {@code null} or the non-null object is equal to the other object
	 *         via the {@code Object.equals()} method; otherwise {@code false}
	 * 
	 * @since 3.0
	 */
	public static boolean equals(final Object object1, final Object object2) {
		if (object1 == null && object2 == null) {
			return true;
		}
		if (object1 == null || object2 == null) {
			return false;
		}
		return object1.equals(object2);
	}
}
