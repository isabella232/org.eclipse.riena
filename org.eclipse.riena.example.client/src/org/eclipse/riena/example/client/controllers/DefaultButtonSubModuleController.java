/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.example.client.views.DateTimeSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ICompositeRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link DateTimeSubModuleView} example.
 */
public class DefaultButtonSubModuleController extends SubModuleController {

	public DefaultButtonSubModuleController() {
		this(null);
	}

	public DefaultButtonSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void configureRidgets() {
		// one default button per group
		final ICompositeRidget group1 = getRidget(ICompositeRidget.class, "group1"); //$NON-NLS-1$
		final IActionRidget action1 = getActionRidget("1"); //$NON-NLS-1$
		addDefaultAction(group1, action1);

		// two default buttons per group (one per text field)
		final ITextRidget input2a = getRidget(ITextRidget.class, "input2a"); //$NON-NLS-1$
		final IActionRidget action2a = getActionRidget("2a"); //$NON-NLS-1$
		addDefaultAction(input2a, action2a);

		final ITextRidget input2b = getRidget(ITextRidget.class, "input2b"); //$NON-NLS-1$
		final IActionRidget action2b = getActionRidget("2b"); //$NON-NLS-1$
		addDefaultAction(input2b, action2b);

		// nested default buttons;
		final ICompositeRidget group3a = getRidget(ICompositeRidget.class, "group3a"); //$NON-NLS-1$
		final IActionRidget action3a = getActionRidget("3a"); //$NON-NLS-1$
		addDefaultAction(group3a, action3a);

		final ICompositeRidget group3b = getRidget(ICompositeRidget.class, "group3b"); //$NON-NLS-1$
		final IActionRidget action3b = getActionRidget("3b"); //$NON-NLS-1$
		addDefaultAction(group3b, action3b);
	}

	// helping methods
	//////////////////

	private ITextRidget getInputRidget(final String id) {
		final ITextRidget result = getRidget(ITextRidget.class, id);
		result.setText(id);
		return result;
	}

	private ITextRidget getOutputRidget(final String id) {
		final ITextRidget result = getRidget(ITextRidget.class, id);
		result.setOutputOnly(true);
		return result;
	}

	private IActionRidget getActionRidget(final String id) {
		final ITextRidget input = getInputRidget("input" + id); //$NON-NLS-1$
		final ITextRidget output = getOutputRidget("output" + id); //$NON-NLS-1$
		final IActionRidget result = getRidget(IActionRidget.class, "button" + id); //$NON-NLS-1$

		result.addListener(new IActionListener() {
			public void callback() {
				System.out.println(String.format("Apply ('%s') pressed", id)); //$NON-NLS-1$
				output.setText(input.getText());
			}
		});

		return result;
	}
}
