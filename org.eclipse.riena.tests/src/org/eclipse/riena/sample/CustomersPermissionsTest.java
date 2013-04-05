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
package org.eclipse.riena.sample;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.sample.app.common.model.CustomersPermission;

/**
 * TODO JavaDoc
 */
@NonUITestCase
public class CustomersPermissionsTest extends TestCase {

	public void testCustomersPermission() {
		final CustomersPermission cp1 = new CustomersPermission("german", "load,save");
		final CustomersPermission cp2 = new CustomersPermission("german", "load,save");
		assertTrue(cp1.equals(cp2));
		final CustomersPermission cp3 = new CustomersPermission("german", "save,load");
		assertTrue(cp1.equals(cp3));
		final CustomersPermission cp4 = new CustomersPermission("german", "load");
		assertFalse(cp1.equals(cp4));
		assertTrue(cp1.implies(cp4));
		assertFalse(cp4.implies(cp1));
		final CustomersPermission cp5 = new CustomersPermission("english", "load,save");
		assertFalse(cp1.equals(cp5));
		final CustomersPermission cp6 = new CustomersPermission("english", "load");
		assertFalse(cp1.implies(cp6));
		assertFalse(cp6.implies(cp1));
	}

}
