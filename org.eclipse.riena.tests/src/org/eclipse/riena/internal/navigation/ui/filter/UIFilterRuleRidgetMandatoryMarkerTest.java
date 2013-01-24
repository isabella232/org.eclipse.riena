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
package org.eclipse.riena.internal.navigation.ui.filter;

import junit.framework.TestCase;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.filter.impl.AbstractUIFilterRuleMarker;

/**
 * Tests of the class {@link UIFilterRuleRidgetMandatoryMarker}.
 */
@NonUITestCase
public class UIFilterRuleRidgetMandatoryMarkerTest extends TestCase {

	/**
	 * Tests the method {@code getMarker()}.
	 */
	public void testGetMarker() {

		final AbstractUIFilterRuleMarker rule = new UIFilterRuleRidgetMandatoryMarker();
		final IMarker marker1 = rule.getMarker();
		final IMarker marker2 = rule.getMarker();
		assertEquals(marker1, marker2);
		assertNotSame(marker1, marker2);

	}

}
