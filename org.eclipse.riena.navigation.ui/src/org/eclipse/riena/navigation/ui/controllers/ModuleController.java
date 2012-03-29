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
package org.eclipse.riena.navigation.ui.controllers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.ModuleNodeListener;
import org.eclipse.riena.ui.ridgets.IWindowRidget;
import org.eclipse.riena.ui.ridgets.listener.IWindowRidgetListener;

/**
 * Default implementation for a ModuleController.
 */
public class ModuleController extends NavigationNodeController<IModuleNode> {

	private IWindowRidget windowRidget;
	private final IWindowRidgetListener windowListener;
	private boolean closeable;
	private boolean dragEnabled;

	/**
	 * @param navigationNode
	 */
	public ModuleController(final IModuleNode navigationNode) {
		super(navigationNode);

		closeable = true;
		dragEnabled = true;
		getNavigationNode().addListener(new MyModuleNodeListener());
		windowListener = new WindowListener();

	}

	/**
	 * Listener observes the node of the module. If the label or the icon changed, the window ridget will be updated.
	 */
	private final class MyModuleNodeListener extends ModuleNodeListener {

		@Override
		public void labelChanged(final IModuleNode moduleNode) {
			updateWindowTitle();
		}

		@Override
		public void iconChanged(final IModuleNode source) {
			updateIcon();
		}
	}

	/**
	 * @param windowRidget
	 *            the windowRidget to set
	 */
	public void setWindowRidget(final IWindowRidget windowRidget) {
		if (getWindowRidget() != null) {
			getWindowRidget().removeWindowRidgetListener(windowListener);
		}
		this.windowRidget = windowRidget;
		if (getWindowRidget() != null) {
			getWindowRidget().addWindowRidgetListener(windowListener);
		}
	}

	/**
	 * @return the windowRidget
	 */
	public IWindowRidget getWindowRidget() {
		return windowRidget;
	}

	public void configureRidgets() {
		setCloseable(getNavigationNode().isClosable());
	}

	@Override
	public void afterBind() {
		super.afterBind();
		updateWindowTitle();
		updateToolTipText();
		updateIcon();
		updateCloseable();
		updateActive();
	}

	private void updateIcon() {
		updateIcon(getWindowRidget());
	}

	private void updateWindowTitle() {
		if (getWindowRidget() != null) {
			getWindowRidget().setTitle(getNavigationNode().getLabel());
			final ISubModuleNode subModule = ApplicationNodeManager.locateActiveSubModuleNode();
			if ((subModule != null) && (subModule.getNavigationNodeController() instanceof SubModuleController)) {
				((SubModuleController) subModule.getNavigationNodeController()).updateWindowTitle();
			}
		}
	}

	private void updateToolTipText() {
		if (getWindowRidget() != null) {
			getWindowRidget().setToolTipText(getNavigationNode().getToolTipText());
		}
	}

	private void updateCloseable() {
		if (getWindowRidget() != null) {
			getWindowRidget().setCloseable(getNavigationNode().isClosable());
		}
	}

	private void updateActive() {
		if (getWindowRidget() != null) {
			getWindowRidget().setActive(getNavigationNode().isActivated());
		}
	}

	public boolean hasSingleLeafChild() {

		final List<INavigationNode<?>> children = getVisibleChildren(getNavigationNode());
		return children.size() == 1 && children.get(0).isLeaf();
	}

	/**
	 * Returns a list of all visible children of the given node.
	 * 
	 * @param parent
	 *            parent node
	 * @return list of visible child nodes
	 */
	public List<INavigationNode<?>> getVisibleChildren(final INavigationNode<?> parent) {

		final List<INavigationNode<?>> visibleChildren = new ArrayList<INavigationNode<?>>();

		for (final Object child : parent.getChildren()) {
			if (child instanceof INavigationNode<?>) {
				final INavigationNode<?> childNode = (INavigationNode<?>) child;
				if (childNode.isVisible()) {
					visibleChildren.add(childNode);
				}
			}
		}

		return visibleChildren;

	}

	public boolean isFirstChild() {
		return getNavigationNode().getParent().getChild(0) == getNavigationNode();
	}

	/**
	 * @return the closeable
	 */
	public boolean isCloseable() {
		return closeable;
	}

	/**
	 * @param closeable
	 *            the closeable to set
	 */
	public void setCloseable(final boolean closeable) {
		this.closeable = closeable;
	}

	/**
	 * @return the dragEnabled
	 */
	public boolean isDragEnabled() {
		return dragEnabled;
	}

	/**
	 * @param dragEnabled
	 *            the dragEnabled to set
	 */
	public void setDragEnabled(final boolean dragEnabled) {
		this.dragEnabled = dragEnabled;
	}

	private class WindowListener implements IWindowRidgetListener {

		public void activated() {
			getNavigationNode().activate();
		}

		public void closed() {
			getNavigationNode().dispose();
		}

	}

}
