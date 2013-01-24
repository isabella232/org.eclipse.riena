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

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests {@link ObjectUtils}.
 */
@NonUITestCase
public class ObjectUtilsTest extends RienaTestCase {

	public void testEquals() {
		assertTrue(ObjectUtils.equals(null, null));
		assertFalse(ObjectUtils.equals(null, ""));
		assertFalse(ObjectUtils.equals("", null));
		assertTrue(ObjectUtils.equals("", ""));
		assertFalse(ObjectUtils.equals(Boolean.TRUE, null));
		assertFalse(ObjectUtils.equals(Boolean.TRUE, "true"));
		assertTrue(ObjectUtils.equals(Boolean.TRUE, Boolean.TRUE));
		assertFalse(ObjectUtils.equals(Boolean.TRUE, Boolean.FALSE));
	}
}
