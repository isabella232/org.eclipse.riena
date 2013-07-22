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
package org.eclipse.riena.ui.ridgets.validation;

import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests for the class {@link ValidationRuleStatus}
 */
@NonUITestCase
public class ValidationRuleStatusTest extends RienaTestCase {

	public void testStatusOk() {
		final IStatus result = ValidationRuleStatus.ok();

		assertTrue(result.isOK());
	}

	public void testStatusErrorBlocker() {
		final IStatus result = ValidationRuleStatus.error(true, "msg");

		assertFalse(result.isOK());
		assertEquals(ValidationRuleStatus.ERROR_BLOCK_WITH_FLASH, result.getCode());
		assertEquals("msg", result.getMessage());
	}

	public void testStatusError() {
		final IStatus result = ValidationRuleStatus.error(false, "msg");

		assertFalse(result.isOK());
		assertEquals(ValidationRuleStatus.ERROR_ALLOW_WITH_MESSAGE, result.getCode());
		assertEquals("msg", result.getMessage());
	}

	public void testStatusErrorNullMessage() {
		final IStatus result = ValidationRuleStatus.error(false, null);

		assertFalse(result.isOK());
		assertEquals(ValidationRuleStatus.ERROR_ALLOW_WITH_MESSAGE, result.getCode());
		assertEquals("", result.getMessage());
	}

}
