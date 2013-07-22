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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests for resource sharing helper methods.
 * 
 * @see Activator#getSharedColor(Display, String)
 */
@NonUITestCase
public class SharedResourcesTest extends RienaTestCase {

	public void testSharedColors() {
		final Display display = Display.getDefault();

		final Color colorFlash1 = Activator.getSharedColor(display, SharedColors.COLOR_MANDATORY);
		final Color colorFlash2 = Activator.getSharedColor(display, SharedColors.COLOR_MANDATORY);

		assertNotNull(colorFlash1);
		assertNotNull(colorFlash2);
		assertSame(colorFlash1, colorFlash2);

		final Color colorOutput = Activator.getSharedColor(display, SharedColors.COLOR_OUTPUT);

		assertNotNull(colorOutput);
		assertNotSame(colorFlash2, colorOutput);

		try {
			Activator.getSharedColor(null, SharedColors.COLOR_MANDATORY);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			Activator.getSharedColor(display, "does_not_exist");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			Activator.getSharedColor(display, null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}
}
