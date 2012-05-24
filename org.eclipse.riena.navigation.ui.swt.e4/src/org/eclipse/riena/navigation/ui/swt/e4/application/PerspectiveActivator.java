/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.e4.application;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.impl.PerspectiveStackImpl;

import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.listener.SubApplicationNodeListener;

@SuppressWarnings("restriction")
public class PerspectiveActivator extends SubApplicationNodeListener {
	private final ISubApplicationNode subApp;
	private final PerspectiveStackImpl stack;

	PerspectiveActivator(final ISubApplicationNode subApp, final PerspectiveStackImpl stack) {
		this.subApp = subApp;
		this.stack = stack;
	}

	@Override
	public void activated(final ISubApplicationNode source) {
		activateE4Perspective(stack, subApp);
	}

	private void activateE4Perspective(final PerspectiveStackImpl stack, final ISubApplicationNode subApp) {
		final MPerspective perspective = (MPerspective) subApp.getContext(E4ViewHelper.CONTEXT_KEY_PERSPECTIVE);
		stack.setSelectedElement(perspective);
	}

}