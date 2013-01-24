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

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 *
 */
@UITestCase
public abstract class AbstractRidgetSharedTestCase extends RienaTestCase {

	private Shell shell;
	private Control widget;
	private IRidget ridget1;
	private IRidget ridget2;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final Display display = Display.getDefault();

		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);

		shell.setLayout(new RowLayout(SWT.VERTICAL));

		widget = createWidget(shell);

		shell.setSize(130, 100);
		shell.setLocation(0, 0);
		shell.open();
	}

	@Override
	protected void tearDown() throws Exception {
		widget = null;
		shell.dispose();
		shell = null;
		super.tearDown();
	}

	protected abstract Control createWidget(final Composite parent);

	protected abstract IRidget createRidget();

	protected IRidget getRidget1() {
		if (ridget1 == null) {
			ridget1 = createRidget();
		}
		return ridget1;
	}

	protected IRidget getRidget2() {
		if (ridget2 == null) {
			ridget2 = createRidget();
		}
		return ridget2;
	}

	protected void activateRidget1() {
		activateRidget(getRidget1(), getRidget2());
	}

	protected void activateRidget2() {
		activateRidget(getRidget2(), getRidget1());
	}

	private void activateRidget(final IRidget ridget, final IRidget toDeactivate) {
		assertNull(ridget.getUIControl());
		assertFalse(SwtUtilities.isDisposed(widget));
		toDeactivate.setUIControl(null);
		ridget.setUIControl(widget);
	}

	protected Control getWidget() {
		return widget;
	}

}
