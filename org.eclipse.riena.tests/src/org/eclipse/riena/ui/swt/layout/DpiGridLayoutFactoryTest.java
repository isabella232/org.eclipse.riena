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
package org.eclipse.riena.ui.swt.layout;

import org.junit.Test;

import org.eclipse.swt.widgets.Shell;

import junit.framework.TestCase;

/**
 *
 */
public class DpiGridLayoutFactoryTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		shell.close();
		super.tearDown();
	}

	@Test
	public void testFillDefaults() {
		final DpiGridLayoutFactory fillDefaults = DpiGridLayoutFactory.fillDefaults();
		final DpiGridLayout layout = fillDefaults.create();
		System.out.println(layout.verticalSpacing);
		System.out.println(layout.horizontalSpacing);
	}

}
