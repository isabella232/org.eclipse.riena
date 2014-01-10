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
package org.eclipse.riena.navigation.ui.swt.login;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.ridgets.swt.views.AbstractControlledView;

/**
 * The abstract view for the login splash dialog.
 */
public abstract class AbstractLoginSplashView extends AbstractControlledView<AbstractWindowController> implements
		ILoginSplashView {

	private Control view;

	protected abstract Control buildView(Composite parent);

	protected abstract AbstractWindowController createController();

	public void build(final Composite parent) {

		if (view == null) {
			view = buildView(parent);
		}
		createAndBindController();
	}

	public int getResult() {
		return getController().getReturnCode();
	}

	private void createAndBindController() {
		final AbstractWindowController controller = createController();
		initialize(controller);
		bind(controller);
	}
}
