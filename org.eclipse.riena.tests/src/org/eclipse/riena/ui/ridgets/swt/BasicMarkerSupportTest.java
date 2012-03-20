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
package org.eclipse.riena.ui.ridgets.swt;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.DisabledMarkerVisualizer;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link BasicMarkerSupport}.
 */
@UITestCase
public class BasicMarkerSupportTest extends TestCase {

	private Display display;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		display = Display.getDefault();
		shell = new Shell(display);
	}

	@Override
	protected void tearDown() {
		SwtUtilities.dispose(shell);
		display = null;
	}

	/**
	 * Tests (parts of) the method {@code init(IBasicMarkableRidget,
	 * PropertyChangeSupport)}.
	 */
	public void testInit() {

		final DefaultRealm realm = new DefaultRealm();
		try {
			final Text control = new Text(shell, SWT.NONE);
			final ITextRidget ridget = new TextRidget();
			ridget.setUIControl(control);
			ridget.addMarker(new ErrorMarker());
			ridget.addMarker(new MandatoryMarker());

			final MyBasicMarkerSupport markerSupport = new MyBasicMarkerSupport();
			markerSupport.setClearAllMarkersCalled(false);
			markerSupport.init(ridget, null);
			assertEquals(2, ridget.getMarkers().size());

			control.dispose();
			assertEquals(2, ridget.getMarkers().size());
			assertTrue(markerSupport.isClearAllMarkersCalled());
		} finally {
			realm.dispose();
		}

	}

	public void testBind() throws Exception {
		final Shell shell = new Shell();
		final Text txt = new Text(shell, SWT.NONE);
		final BasicMarkerSupport support = new BasicMarkerSupport() {

			@Override
			protected Control getUIControl() {
				return txt;
			}
		};
		support.bind();
		assertTrue(support.isInitialEnabled());
		txt.setEnabled(false);
		support.bind();
		assertFalse(support.isInitialEnabled());
		shell.setEnabled(false);
		txt.setEnabled(true);
		assertFalse(txt.isEnabled());
		assertTrue(txt.getEnabled());
		support.bind();
		assertTrue(support.isInitialEnabled());
		shell.setEnabled(true);
	}

	public void testUnbind() throws Exception {
		final Boolean[] x = new Boolean[2];
		final DisabledMarkerVisualizer vMock = new DisabledMarkerVisualizer(null) {
			private int i = 0;

			@Override
			public void updateDisabled(final Control control, final boolean enabled) {
				x[i++] = enabled;
			}
		};
		final Shell shell = new Shell();
		final Text txt = new Text(shell, SWT.NONE);
		final BasicMarkerSupport support = new BasicMarkerSupport() {

			@Override
			protected Control getUIControl() {
				return txt;
			}
		};
		ReflectionUtils.setHidden(support, "disabledMarkerVisualizer", vMock); //$NON-NLS-1$
		support.bind();
		support.unbind();
		assertTrue(x[0]);
		txt.setEnabled(false);
		support.bind();
		support.unbind();
		assertFalse(x[1]);

	}

	/**
	 * Tests the <i>private</i> method {@code clearVisible(Control)}.
	 */
	public void testClearVisible() {

		final DefaultRealm realm = new DefaultRealm();
		try {
			final Label control = new Label(shell, SWT.NONE);
			final ILabelRidget ridget = new LabelRidget();
			ridget.setUIControl(control);
			final boolean isVisible = control.getVisible();

			assertTrue(control.getVisible());

			ridget.setVisible(false);
			assertFalse(control.getVisible());

			final AbstractMarkerSupport markerSupport = ReflectionUtils.getHidden(ridget, "markerSupport"); //$NON-NLS-1$
			assertTrue(markerSupport instanceof BasicMarkerSupport);
			ReflectionUtils.invokeHidden(markerSupport, "clearVisible", control); //$NON-NLS-1$
			assertEquals(isVisible, control.getVisible());

		} finally {
			realm.dispose();
		}

	}

	private static class MyBasicMarkerSupport extends BasicMarkerSupport {

		private boolean clearAllMarkersCalled;

		@Override
		protected void clearAllMarkers(final Control control) {
			super.clearAllMarkers(control);
			setClearAllMarkersCalled(true);
		}

		public void setClearAllMarkersCalled(final boolean clearAllMarkersCalled) {
			this.clearAllMarkersCalled = clearAllMarkersCalled;
		}

		public boolean isClearAllMarkersCalled() {
			return clearAllMarkersCalled;
		}

	}

}
