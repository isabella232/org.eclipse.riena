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
import java.util.List;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;

/**
 * This controller always initializes the property "allow" with with "false"
 * before this sub-module is active.
 */
public class BeforeActivateSubModuleController extends SubModuleController {

	private boolean allowNext;
	private ISingleChoiceRidget allowChoice;

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		allowChoice = getRidget(ISingleChoiceRidget.class, "allowChoice"); //$NON-NLS-1$
		allowChoice.addSelectionListener(new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				setAllowContext();
			}
		});
		allowChoice.bindToModel(Arrays.asList(true, false), (List<String>) null, this, "allowNext"); //$NON-NLS-1$

	}

	@Override
	public void setNavigationNode(final ISubModuleNode navigationNode) {
		super.setNavigationNode(navigationNode);

		navigationNode.addListener(new SubModuleNodeListener() {
			@Override
			public void beforeActivated(final ISubModuleNode source) {
				initAllow();
			}

		});

	}

	/**
	 * Sets the value of the property "allow" in the context of other sibling
	 * sub-modules.
	 */
	private void setAllowContext() {
		final IModuleNode module = (IModuleNode) getNavigationNode().getParent();
		final List<ISubModuleNode> children = module.getChildren();
		for (final ISubModuleNode child : children) {
			if (child == this) {
				continue;
			}
			child.setContext("allow", isAllowNext()); //$NON-NLS-1$
		}
	}

	/**
	 * Initializes the property "allow" with with "false".
	 */
	private void initAllow() {
		setAllowNext(false);
		allowChoice.updateFromModel();
		setAllowContext();
	};

	public void setAllowNext(final boolean allowNext) {
		this.allowNext = allowNext;
	}

	public boolean isAllowNext() {
		return allowNext;
	}

}
