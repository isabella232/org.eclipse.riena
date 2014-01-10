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
package org.eclipse.riena.preferences;

import junit.framework.TestCase;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import org.eclipse.riena.core.test.collect.ManualTestCase;

/**
 * Prints out the complete preference tree.
 */
@ManualTestCase
public class PreferencesTest extends TestCase {

	public void testIt() throws BackingStoreException {
		final IEclipsePreferences prefs = Platform.getPreferencesService().getRootNode();
		diggIn(prefs, 0);
	}

	/**
	 * @param prefs
	 * @param i
	 * @throws BackingStoreException
	 */
	private void diggIn(final Preferences prefs, int i) throws BackingStoreException {
		String indent = "";
		for (int j = 0; j < i; j++) {
			indent = indent + "  ";
		}
		System.out.println(indent + prefs.name() + "(" + prefs.absolutePath() + "," + prefs.getClass() + ")");
		final String[] keys = prefs.keys();
		for (final String key : keys) {
			System.out.println(indent + "-" + key + "=" + prefs.get(key, "?"));
		}
		final String[] kids = prefs.childrenNames();
		i++;
		for (final String kid : kids) {
			final Preferences kidPrefs = prefs.node(kid);
			diggIn(kidPrefs, i);
		}

	}
}
