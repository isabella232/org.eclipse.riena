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
package org.eclipse.riena.ui.core.uiprocess;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 *
 */
@UITestCase
public class UIExecutorTest extends TestCase {

	private Shell shell;
	private Button button;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		button = new Button(shell, SWT.None);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.dispose(shell);
	}

	public void testExecuteLively() throws Exception {
		// FIXME Why is testExecuteLively so slow?
		//		@SuppressWarnings("unchecked")
		//		final Callable<Boolean> callableMock = EasyMock.createNiceMock(Callable.class);
		//		EasyMock.expect(callableMock.call()).andReturn(Boolean.TRUE);
		//		EasyMock.replay(callableMock);
		//
		//		final SelectionListenerMock selectionListenerMock = new SelectionListenerMock();
		//		assertFalse(selectionListenerMock.widgetSelectedCalled);
		//		button.addSelectionListener(selectionListenerMock);
		//
		//		UITestHelper.fireSelectionEvent(button);
		//		final Boolean result = UIExecutor.executeLively(callableMock);
		//		assertNotNull(result);
		//		assertTrue(result);
		//
		//		EasyMock.verify(callableMock);
		//		assertTrue(selectionListenerMock.widgetSelectedCalled);
	}

	private class SelectionListenerMock implements SelectionListener {
		private boolean widgetSelectedCalled = false;

		public void widgetSelected(final SelectionEvent e) {
			widgetSelectedCalled = true;
		}

		public void widgetDefaultSelected(final SelectionEvent e) {
		}
	}
}
