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
package org.eclipse.riena.client.controller.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.core.RienaStatus;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * This controller adds dynamically nodes to the navigation.
 */
public class NavigationSubModuleController extends SubModuleController {

	private IActionRidget addSubModuleToModuleBtn;
	private IActionRidget addSubModuleToSelfBtn;
	private IActionRidget removeSubModuleBtn;
	private IActionRidget addModuleBtn;
	private IActionRidget addModuleGroupBtn;

	private static int nodeCount = 0;

	public NavigationSubModuleController() {
		this(null);
	}

	/**
	 * @param navigationNode
	 */
	public NavigationSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Binds and updates the ridgets.<br>
	 * Sets texts and adds action listeners.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		addSubModuleToModuleBtn = getRidget(IActionRidget.class, "addSubModuleToModuleBtn"); //$NON-NLS-1$
		addSubModuleToModuleBtn.setText("Add Sub-Module to &Root"); //$NON-NLS-1$
		addSubModuleToModuleBtn.addListener(new IActionListener() {
			public void callback() {
				final ISubModuleNode newNode = createSubModuleNode("Node " + String.valueOf(nodeCount++)); //$NON-NLS-1$
				final IModuleNode parent = getParentNodeOfType(getNavigationNode(), IModuleNode.class);
				parent.setNodeId(new NavigationNodeId("org.eclipse.riena.example.navigate.root")); //$NON-NLS-1$
				parent.addChild(newNode);
				showStatusLineMessage("Sub-Module was added!"); //$NON-NLS-1$
			}

		});

		addSubModuleToSelfBtn = getRidget(IActionRidget.class, "addSubModuleToSelfBtn"); //$NON-NLS-1$
		addSubModuleToSelfBtn.setText("Add S&ub-Module this Node"); //$NON-NLS-1$
		addSubModuleToSelfBtn.addListener(new IActionListener() {
			public void callback() {
				final ISubModuleNode navigationNode = getNavigationNode();
				final ISubModuleNode subModule = createSubModuleNode("Node " + String.valueOf(nodeCount++)); //$NON-NLS-1$
				navigationNode.addChild(subModule);
				subModule.activate();
				showStatusLineMessage("Sub-Module was added!"); //$NON-NLS-1$
			}
		});

		removeSubModuleBtn = getRidget(IActionRidget.class, "removeSubModuleBtn"); //$NON-NLS-1$
		removeSubModuleBtn.setText("Remove all children"); //$NON-NLS-1$
		removeSubModuleBtn.addListener(new IActionListener() {
			public void callback() {
				final ISubModuleNode navigationNode = getNavigationNode();
				final List<ISubModuleNode> children = new ArrayList<ISubModuleNode>(navigationNode.getChildren());
				for (final ISubModuleNode child : children) {
					child.dispose();
				}
				if (children.size() > 0) {
					showStatusLineMessage("All children removed!"); //$NON-NLS-1$
				}
			}
		});

		addModuleBtn = getRidget(IActionRidget.class, "addModuleBtn"); //$NON-NLS-1$
		addModuleBtn.setText("Add &Module"); //$NON-NLS-1$
		addModuleBtn.addListener(new IActionListener() {
			public void callback() {
				final IModuleGroupNode parent = getParentNodeOfType(getNavigationNode(), IModuleGroupNode.class);
				parent.addChild(createModuleNode());
				showStatusLineMessage("Module was added!"); //$NON-NLS-1$
			}
		});

		addModuleGroupBtn = getRidget(IActionRidget.class, "addModuleGroupBtn"); //$NON-NLS-1$
		addModuleGroupBtn.setText("Add Module-&Group"); //$NON-NLS-1$
		addModuleGroupBtn.addListener(new IActionListener() {
			public void callback() {
				final ISubApplicationNode parent = getParentNodeOfType(getNavigationNode(), ISubApplicationNode.class);
				parent.addChild(createModuleGroupNode());
				showStatusLineMessage("Module-Group was added!"); //$NON-NLS-1$
			}
		});
	}

	private void showStatusLineMessage(final String text) {
		if (!RienaStatus.isTest()) {
			getApplicationController().getStatusline().setMessage(text);
		}
	}

	private <N extends INavigationNode<?>> N getParentNodeOfType(final INavigationNode<?> node, final Class<N> clazz) {
		return node.getParentOfType(clazz);
	}

	/**
	 * Creates a new module group and adds a new module to the group.
	 * 
	 * @return module group
	 */
	private IModuleGroupNode createModuleGroupNode() {

		final NavigationNodeId nodeId = new NavigationNodeId("moduleGroup", Integer.toString(++nodeCount)); //$NON-NLS-1$
		final IModuleGroupNode newModuleGroupNode = new ModuleGroupNode(nodeId);
		newModuleGroupNode.setLabel("ModuleGroup"); //$NON-NLS-1$
		final IModuleNode newModuleNode = createModuleNode();
		newModuleGroupNode.addChild(newModuleNode);

		return newModuleGroupNode;
	}

	/**
	 * Creates a new module and adds a new sub-module to the module.
	 * 
	 * @return module
	 */
	private IModuleNode createModuleNode() {

		final NavigationNodeId nodeId = new NavigationNodeId("module", Integer.toString(++nodeCount)); //$NON-NLS-1$
		final IModuleNode newModuleNode = new ModuleNode(nodeId, "Module"); //$NON-NLS-1$
		final ISubModuleNode newSubModuleNode = createSubModuleNode("SubModule"); //$NON-NLS-1$
		newModuleNode.addChild(newSubModuleNode);
		return newModuleNode;
	}

	/**
	 * Creates a new sub-module with the given label.
	 * 
	 * @param label
	 *            label of the sub-module
	 * @return sub-module
	 */
	private ISubModuleNode createSubModuleNode(final String label) {

		final ISubModuleNode newSubModuleNode = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.navigation", Integer.toString(nodeCount)), label); //$NON-NLS-1$
		return newSubModuleNode;

	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private ApplicationController getApplicationController() {
		return (ApplicationController) getNavigationNode().getParentOfType(IApplicationNode.class)
				.getNavigationNodeController();
	}

}
