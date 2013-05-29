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
package org.eclipse.riena.example.client.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 *
 */
public class SystemPropertySourceProvider extends AbstractSourceProvider {

	private boolean isDisposed;

	private static final String JAVA_VERSION = "java.version"; //$NON-NLS-1$
	private static final String OS_VERSION = "os.version"; //$NON-NLS-1$
	private static final String OS_NAME = "os.name"; //$NON-NLS-1$
	private static final String USER_LANGUAGE = "user.language"; //$NON-NLS-1$
	private static final String USER_NAME = "user.name"; //$NON-NLS-1$
	private static final int EVENT_PRIORITY = ISources.ACTIVE_MENU;

	private static final String[] PROVIDED_SOURCE_NAMES = new String[] { JAVA_VERSION, OS_VERSION, OS_NAME, USER_LANGUAGE, USER_NAME };

	public void dispose() {
		isDisposed = true;
	}

	public Map getCurrentState() {
		final Map<String, String> properties = new HashMap<String, String>();
		properties.put(JAVA_VERSION, System.getProperty(JAVA_VERSION));
		properties.put(OS_VERSION, System.getProperty(OS_VERSION));
		properties.put(OS_NAME, System.getProperty(OS_NAME));
		properties.put(USER_LANGUAGE, System.getProperty(USER_LANGUAGE));
		properties.put(USER_NAME, System.getProperty(USER_NAME));
		return properties;
	}

	public boolean isDisposed() {
		return isDisposed;
	}

	public String[] getProvidedSourceNames() {
		return PROVIDED_SOURCE_NAMES;
	}

	void fireSourceChange(final String propName, final String propValue) {
		fireSourceChanged(EVENT_PRIORITY, propName, propValue);
	}

	public void propertyChanged(final String propName) {
		if (Arrays.asList(PROVIDED_SOURCE_NAMES).contains(propName)) {
			fireSourceChange(propName, System.getProperty(propName));
		}
	}

}
