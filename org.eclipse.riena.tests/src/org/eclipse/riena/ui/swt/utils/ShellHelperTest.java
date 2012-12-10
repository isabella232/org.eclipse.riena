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
package org.eclipse.riena.ui.swt.utils;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.swt.utils.ShellHelper;

/**
 * Tests of the class {@link ShellHelper}.
 */
@NonUITestCase
public class ShellHelperTest extends TestCase {

	/**
	 * Tests the <i>private</i> method {@code isTitleless(Shell)}.
	 */
	public void testIsTitleless() {

		final ShellHelper helper = new ShellHelper();

		Shell shell = new Shell(SWT.NO_TRIM);
		boolean ret = ReflectionUtils.invokeHidden(helper, "isTitleless", shell); //$NON-NLS-1$
		assertTrue(ret);
		shell.dispose();

		ret = ReflectionUtils.invokeHidden(helper, "isTitleless", shell); //$NON-NLS-1$
		assertFalse(ret);

		shell = new Shell(SWT.SHELL_TRIM);
		ret = ReflectionUtils.invokeHidden(helper, "isTitleless", shell); //$NON-NLS-1$
		assertFalse(ret);
		shell.dispose();

		shell = new Shell(SWT.NO_TRIM | SWT.TITLE | SWT.CLOSE);
		ret = ReflectionUtils.invokeHidden(helper, "isTitleless", shell); //$NON-NLS-1$
		assertTrue(ret);

	}

}
