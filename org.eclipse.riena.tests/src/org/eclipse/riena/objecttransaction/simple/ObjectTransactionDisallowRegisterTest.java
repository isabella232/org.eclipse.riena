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
package org.eclipse.riena.objecttransaction.simple;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.objecttransaction.IObjectTransaction;
import org.eclipse.riena.objecttransaction.ObjectTransactionFactory;
import org.eclipse.riena.objecttransaction.simple.value.Addresse;

/**
 * TODO Fehlender Klassen-Kommentar
 */
@NonUITestCase
public class ObjectTransactionDisallowRegisterTest extends RienaTestCase {

	/**
	 * 
	 */
	public void testSimpleAllowRegister() {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.allowRegister(false);

		Addresse addresse = new Addresse(true);
		assertTrue("addresse must not be registered", !objectTransaction.isRegistered(addresse));

		objectTransaction.allowRegister(true);
		addresse = new Addresse(true);
		assertTrue("addresse must be registered", objectTransaction.isRegistered(addresse));

		showStatus("testSimpleAllowRegister", objectTransaction);
	}

	private void showStatus(final String testName, final IObjectTransaction objectTransaction) {
		System.out.println("testname >>>>>" + testName + "<<<<<");
		System.out.println(objectTransaction);
	}
}