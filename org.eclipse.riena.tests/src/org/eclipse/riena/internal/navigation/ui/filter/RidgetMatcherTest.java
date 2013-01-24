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
package org.eclipse.riena.internal.navigation.ui.filter;

import junit.framework.TestCase;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link RidgetMatcher}.
 */
@UITestCase
public class RidgetMatcherTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {

		final Display display = Display.getDefault();

		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		shell = new Shell();

	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the method {@code matches}.
	 */
	public void testMatches() {

		RidgetMatcher matcher = new RidgetMatcher("4711");
		assertFalse(matcher.matches((IRidget) null));

		assertFalse(matcher.matches(new Object()));

		final Label control = new Label(shell, SWT.NONE);
		final ILabelRidget ridget = new LabelRidget(control);
		assertFalse(matcher.matches(ridget));

		control.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "4711");
		assertTrue(matcher.matches(ridget));

		final INavigationNode<ISubModuleNode> node = new SubModuleNode(new NavigationNodeId("subMod0815"));
		assertFalse(matcher.matches(node, ridget));

		matcher = new RidgetMatcher("*4711");
		assertTrue(matcher.matches(ridget, node));

		SwtUtilities.dispose(control);

	}
}
