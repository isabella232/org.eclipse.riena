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
package org.eclipse.riena.ui.ridgets;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.SubModuleUtils;

/**
 * Tests of the class {@link SubModuleUtils}.
 */
@NonUITestCase
public class SubModuleUtilsTest extends TestCase {

	/**
	 * Tests the method {@code isPrepareView()}.
	 */
	public void testIsPrepareView() {

		System.clearProperty(SubModuleUtils.RIENA_PREPARE_VIEW_SYSTEM_PROPERTY);
		assertFalse(SubModuleUtils.isPrepareView());

		System.setProperty(SubModuleUtils.RIENA_PREPARE_VIEW_SYSTEM_PROPERTY, Boolean.TRUE.toString());
		assertTrue(SubModuleUtils.isPrepareView());

		System.setProperty(SubModuleUtils.RIENA_PREPARE_VIEW_SYSTEM_PROPERTY, Boolean.FALSE.toString());
		assertFalse(SubModuleUtils.isPrepareView());

		System.clearProperty(SubModuleUtils.RIENA_PREPARE_VIEW_SYSTEM_PROPERTY);

	}

}
