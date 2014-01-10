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
package org.eclipse.riena.core.injector.extension;

import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Test the heterogeneous aspects of the extension injector.
 */
@NonUITestCase
public class HeterogeneousInjectingTest extends RienaTestCase {

	private IPreferencesDesc[] prefs;
	private static final String PREFERENCES = "org.eclipse.core.runtime.preferences";

	// If you want printing enable the comment below:
	//	{
	//		setPrint(true);
	//	}

	public void update(final IPreferencesDesc[] prefs) {
		// TODO warning suppression: Ignore FindBugs warning about internal
		// representation being exposed: seems ok for testing
		this.prefs = prefs;
	}

	public void testPreferencesInjectionHeterogeneousSpecific() {
		printTestName();
		Inject.extension(PREFERENCES).heterogeneous().into(this).specific().andStart(getContext());

		assertNotNull(prefs);
		assertTrue(prefs.length > 1);
		int scops = 0;
		int inits = 0;
		int modis = 0;
		for (final IPreferencesDesc pref : prefs) {
			println(pref.toString());
			final IScopeDesc[] scopes = pref.getScope();
			for (final IScopeDesc scope : scopes) {
				println("\tScope: " + scope.getName() + ", " + scope.getScope() + ", " + scope.getContributingBundle());
			}
			scops += scopes.length;
			final IInitializerDesc[] initializers = pref.getInitializer();
			for (final IInitializerDesc initializer : initializers) {
				println("\tInit: " + initializer.getInitializer() + ", " + initializer.getContributingBundle());
			}
			inits += initializers.length;
			final IModifierDesc[] modifiers = pref.getModifier();
			for (final IModifierDesc modifier : modifiers) {
				println("\tModifier: " + modifier.getModifier() + ", " + modifier.getContributingBundle());
			}
			modis += modifiers.length;
		}

		final IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(PREFERENCES);
		assertEquals(extensionPoint.getExtensions().length, prefs.length);

		assertEquals(extensionPoint.getConfigurationElements().length, scops + inits + modis);
	}

	public void testPreferencesInjectionHeterogeneousUnspecific() {
		printTestName();
		Inject.extension(PREFERENCES).heterogeneous().into(this).andStart(getContext());

		assertNotNull(prefs);
		assertEquals(1, prefs.length);
		final IPreferencesDesc pref = prefs[0];
		final IScopeDesc[] scopes = pref.getScope();
		for (final IScopeDesc scope : scopes) {
			println("\tScope: " + scope.getName() + ", " + scope.getScope() + ", " + scope.getContributingBundle());
		}
		final IInitializerDesc[] initializers = pref.getInitializer();
		for (final IInitializerDesc initializer : initializers) {
			println("\tInit: " + initializer.getInitializer() + ", " + initializer.getContributingBundle());
		}
		final IModifierDesc[] modifiers = pref.getModifier();
		for (final IModifierDesc modifier : modifiers) {
			println("\tModifier: " + modifier.getModifier() + ", " + modifier.getContributingBundle());
		}

		final IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(PREFERENCES);
		assertEquals(extensionPoint.getConfigurationElements().length, scopes.length + initializers.length
				+ modifiers.length);
	}

}
