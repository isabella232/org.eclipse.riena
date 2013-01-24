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
package org.eclipse.riena.internal.ui.ridgets.swt.uiprocess;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests of the class {@link DefaultProcessDetailComparator}
 */
@NonUITestCase
public class DefaultProcessDetailComparatorTest extends TestCase {

	/**
	 * Tests the method {@code compare}.
	 */
	public void testCompare() {

		final DefaultProcessDetailComparator comparator = new DefaultProcessDetailComparator();

		final ProcessDetail detail1 = new ProcessDetail(1, null);
		final ProcessDetail detail2 = new ProcessDetail(2, null);

		assertEquals(1, comparator.compare(detail1, detail2));
		assertEquals(-1, comparator.compare(detail2, detail1));
		assertEquals(0, comparator.compare(detail1, detail1));

	}

}
