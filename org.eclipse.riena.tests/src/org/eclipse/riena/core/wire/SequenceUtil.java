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
package org.eclipse.riena.core.wire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

/**
 *
 */
public final class SequenceUtil {

	private SequenceUtil() {
		// util
	}

	private static List<Object> sequence;

	public static void init() {
		sequence = new ArrayList<Object>();
	}

	/**
	 * @param object
	 */
	public static void add(final Object object) {
		if (sequence == null) {
			return;
		}
		sequence.add(object);
	}

	public static void assertExpected(final Object... objects) {
		assertExpected(Arrays.asList(objects));
	}

	public static void assertExpected(final List<?> objects) {
		Assert.assertEquals(objects, sequence);
	}
}
