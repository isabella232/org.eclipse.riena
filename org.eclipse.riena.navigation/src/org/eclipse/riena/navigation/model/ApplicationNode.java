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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationHistoryListener;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.IApplicationNodeListener;
import org.eclipse.riena.navigation.listener.INavigationNodeListener;

/**
 * Default implementation for the ApplicationNode
 */
public class ApplicationNode extends NavigationNode<IApplicationNode, ISubApplicationNode, IApplicationNodeListener> implements IApplicationNode {
	public static final String DEFAULT_APPLICATION_TYPEID = "application"; //$NON-NLS-1$
	private String logoPath;

	/**
	 * Creates an ApplicationNode node which is the root of an application model tree.
	 * 
	 */
	public ApplicationNode() {
		super(new NavigationNodeId(ApplicationNode.DEFAULT_APPLICATION_TYPEID));
		initializeNavigationProcessor();
	}

	public Class<ISubApplicationNode> getValidChildType() {
		return ISubApplicationNode.class;
	}

	/**
	 * Creates an ApplicationNode node which is the root of an application model tree.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 */
	public ApplicationNode(final NavigationNodeId nodeId) {
		super(nodeId);
		initializeNavigationProcessor();
	}

	/**
	 * Creates an ApplicationNode node which is the root of an application model tree.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 * @param label
	 *            Label of the application displayed in the title bar.
	 */
	public ApplicationNode(final NavigationNodeId nodeId, final String label) {
		this(nodeId);
		setLabel(label);
	}

	/**
	 * Creates an ApplicationNode node which is the root of an application model tree.
	 * 
	 * @param label
	 *            Label of the application displayed in the title bar.
	 */
	public ApplicationNode(final String label) {
		this(new NavigationNodeId(ApplicationNode.DEFAULT_APPLICATION_TYPEID), label);
	}

	/**
	 * 
	 */
	protected void initializeNavigationProcessor() {
		setNavigationProcessor(ApplicationNodeManager.getDefaultNavigationProcessor());
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryListenerable# addNavigationHistoryListener (org.eclipse.riena.navigation.INavigationHistoryListener)
	 */
	public void addNavigationHistoryListener(final INavigationHistoryListener listener) {
		getNavigationProcessor().addNavigationHistoryListener(listener);
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryListenerable# removeNavigationHistoryListener
	 *      (org.eclipse.riena.navigation.INavigationHistoryListener)
	 */
	public void removeNavigationHistoryListener(final INavigationHistoryListener listener) {
		getNavigationProcessor().removeNavigationHistoryListener(listener);
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryListenerable#getHistoryBackSize()
	 */
	public int getHistoryBackSize() {
		return getNavigationProcessor().getHistoryBackSize();
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryListenerable#getHistoryForwardSize()
	 */
	public int getHistoryForwardSize() {
		return getNavigationProcessor().getHistoryForwardSize();
	}

	/**
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode#setLogo(java.lang.String)
	 * @since 4.0
	 */
	public void setLogo(final String logoPath) {
		final String old = this.logoPath;
		this.logoPath = logoPath;
		propertyChangeSupport.firePropertyChange(IApplicationNode.PROPERTY_LOGO, old, logoPath);
		notifyLogoChanged(logoPath);
	}

	/**
	 * @return the configured logo image or <code>null</code>
	 * @since 4.0
	 */
	public String getLogo() {
		return logoPath;
	}

	@SuppressWarnings("rawtypes")
	private void notifyLogoChanged(final String logoPath) {
		for (final INavigationNodeListener next : getListeners()) {
			// avoid ClassCastException when listener is NavigationUIFilterApplier
			if (next instanceof IApplicationNodeListener) {
				((IApplicationNodeListener) next).logoChanged(ApplicationNode.this, logoPath);
			}
		}
	}
}
