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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.riena.ui.swt.utils.WidgetIdentificationSupport;

/**
 * Tests of the class {@link AbstractNavigationCompositeDeligation}.
 */
@UITestCase
public class AbstractNavigationCompositeDeligationTest extends RienaTestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		super.tearDown();
	}

	/**
	 * Tests the method {@code createNavigationComposite(Composite} .
	 */
	public void testCreateNavigationComposite() {
		final MyNavigationCompositeDeligation deligation = new MyNavigationCompositeDeligation(null, shell, null);
		final Composite comp = ReflectionUtils.invokeHidden(deligation, "createNavigationComposite", shell);
		assertNotNull(comp);
		assertEquals(FormLayout.class, comp.getLayout().getClass());
		assertEquals("NavigationView", WidgetIdentificationSupport.getIdentification(comp));
	}

	private class MyNavigationCompositeDeligation extends AbstractNavigationCompositeDeligation {

		public MyNavigationCompositeDeligation(final Composite superParent, final Composite parent,
				final IModuleNavigationComponentProvider navigationProvider) {
			super(superParent, parent, navigationProvider);
		}

		@Override
		protected Composite getScrolledComposite() {
			return new Composite(getParent(), SWT.DEFAULT);
		}

	}

}
