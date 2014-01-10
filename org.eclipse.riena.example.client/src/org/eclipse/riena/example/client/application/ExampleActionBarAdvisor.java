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
package org.eclipse.riena.example.client.application;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * This extension of the {@link ActionBarAdvisor} registers the help actions.
 */
public class ExampleActionBarAdvisor extends ActionBarAdvisor {

	/**
	 * Creates a new action bar advisor to configure a workbench window's action
	 * bars via the given action bar configurer.
	 * 
	 * @param configurer
	 *            the action bar configurer
	 */
	public ExampleActionBarAdvisor(final IActionBarConfigurer configurer) {
		super(configurer);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Registers the help actions.
	 */
	@Override
	protected void makeActions(final IWorkbenchWindow window) {

		super.makeActions(window);

		register(ActionFactory.HELP_CONTENTS.create(window));
		register(ActionFactory.HELP_SEARCH.create(window));
		register(ActionFactory.DYNAMIC_HELP.create(window));

	}

}
