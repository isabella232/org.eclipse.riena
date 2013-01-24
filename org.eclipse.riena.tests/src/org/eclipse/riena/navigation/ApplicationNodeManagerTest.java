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
package org.eclipse.riena.navigation;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.model.ApplicationNode;

/**
 * Tests of the class <code>SwtControlRidgetMapper</code>
 */
@NonUITestCase
public class ApplicationNodeManagerTest extends TestCase {

	private IApplicationNode model = null;

	@Override
	protected void setUp() throws Exception {
		ApplicationNodeManager.clear();
		model = new ApplicationNode();
	}

	@Override
	protected void tearDown() throws Exception {
		ApplicationNodeManager.clear();
		model = null;
	}

	public void testAddDefaultModel() throws Exception {
		ApplicationNodeManager.registerApplicationNode(model);
		final IApplicationNode rModel = ApplicationNodeManager.getApplicationNode();
		assertNotNull(rModel);
	}

	public void testAddNamedModel() throws Exception {
		ApplicationNodeManager.registerApplicationNode(new ApplicationNode(null, "MyModel"));
		final IApplicationNode rModel = ApplicationNodeManager.getApplicationNode("MyModel");
		assertNotNull(rModel);
	}

	public void testApplicationModelFailure() throws Exception {
		boolean exOk = false;
		try {
			ApplicationNodeManager.registerApplicationNode(model);
			ApplicationNodeManager.registerApplicationNode(model);
		} catch (final ApplicationModelFailure f) {
			exOk = true;
		}
		assertTrue("duplicate default model registration didn't fire a ApplicationModelFailure", exOk);
		exOk = false;
		try {
			ApplicationNodeManager.registerApplicationNode(new ApplicationNode(null, "MyModel"));
			ApplicationNodeManager.registerApplicationNode(new ApplicationNode(null, "MyModel"));
		} catch (final ApplicationModelFailure f) {
			exOk = true;
		}
		assertTrue("duplicate named registration didn't fire a ApplicationModelFailure", exOk);
	}

	public void testGetDefaultModel() throws Exception {
		ApplicationNodeManager.registerApplicationNode(model);
		ApplicationNodeManager.registerApplicationNode(new ApplicationNode(null, "MyModel"));
		final IApplicationNode rModel = ApplicationNodeManager.getApplicationNode();
		assertNotNull(rModel);
		assertSame(model, rModel);
	}

	public void testGetDefaultModelWhenNamedAndSingle() throws Exception {
		model = new ApplicationNode(null, "MyModel");
		ApplicationNodeManager.registerApplicationNode(model);
		final IApplicationNode rModel = ApplicationNodeManager.getApplicationNode();
		assertNotNull(rModel);
		assertSame(model, rModel);
	}

	public void testGetNamedModel() throws Exception {
		model = new ApplicationNode(null, "MyModel");
		ApplicationNodeManager.registerApplicationNode(model);
		ApplicationNodeManager.registerApplicationNode(new ApplicationNode(null, "MyModell"));
		ApplicationNodeManager.registerApplicationNode(new ApplicationNode(null, "MyModel2"));
		final IApplicationNode rModel = ApplicationNodeManager.getApplicationNode("MyModel");
		assertNotNull(rModel);
		assertSame(model, rModel);
	}

	/**
	 * Tests the method {@code getDefaultNavigationProcessor()}.
	 */
	public void testGetDefaultNavigationProcessor() {
		final INavigationProcessor navigationProcessor = ApplicationNodeManager.getDefaultNavigationProcessor();
		assertNotNull(navigationProcessor);
		final INavigationProcessor navigationProcessor2 = ApplicationNodeManager.getDefaultNavigationProcessor();
		assertSame(navigationProcessor, navigationProcessor2);
	}

}
