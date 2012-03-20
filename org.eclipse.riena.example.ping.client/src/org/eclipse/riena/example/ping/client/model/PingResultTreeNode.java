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
package org.eclipse.riena.example.ping.client.model;

import java.util.List;

import org.eclipse.riena.core.ping.PingResult;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;

/**
 * A {@link TreeNode} that represents a {@link PingResult}.
 */
public class PingResultTreeNode extends TreeNode {

	private static final String PING_METHOD_PREFIX = "#"; //$NON-NLS-1$
	private static final String PINGABLE_OK_ICON = "testok.gif"; //$NON-NLS-1$
	private static final String PINGABLE_ERROR_ICON = "testerr.gif"; //$NON-NLS-1$
	private static final String PINGABLE_ICON = "test.gif"; //$NON-NLS-1$

	private PingResult pingResult;

	/**
	 * Creates a PingResultTreeNode.
	 * 
	 * @param parent
	 *            the parent node.
	 * @param name
	 *            the name of the node.
	 */
	public PingResultTreeNode(final ITreeNode parent, final String name) {
		super(parent, stripOffPackage(name));
	}

	/**
	 * Returns <code>true</code> if the ping has failed.
	 * 
	 * @return <code>true</code> if the ping has failed.
	 */
	public boolean hasPingFailed() {
		return getPingResult() != null && getPingResult().hasPingFailed();
	}

	/**
	 * Return <code>true</code> if a {@link #getPingResult() PingResult} is set.
	 * 
	 * @return <code>true</code> if a ping has been performed.
	 */
	public boolean isPingDone() {
		return getPingResult() != null;
	}

	/**
	 * Sets the {@link PingResult} represented by this node.
	 * 
	 * @param pingResult
	 *            the result.
	 */
	public void setPingResult(final PingResult pingResult) {
		this.pingResult = pingResult;
	}

	/**
	 * Returns the {@link PingResult} represented by this node.
	 * 
	 * @return the {@link PingResult} represented by this node.
	 */
	public PingResult getPingResult() {
		return pingResult;
	}

	/**
	 * Return the appropriate icon representing the nodes state.
	 * 
	 * @return the node's icon.
	 */
	public String getIcon() {
		if (!isPingDone()) {
			return PINGABLE_ICON;
		}
		if (showFailureIcon()) {
			return PINGABLE_ERROR_ICON;
		}
		return PINGABLE_OK_ICON;
	}

	/**
	 * Indicates if the failure icon should be shown. This is the case if
	 * <ul>
	 * <li>this nodes result has a failure message</li>
	 * <li>one of its child nodes has a failure</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if the failure icon should be shown.
	 */
	private boolean showFailureIcon() {
		if (hasPingFailed()) {
			return true;
		}
		final List<ITreeNode> children = getChildren();
		if (children == null) {
			return false;
		}
		for (final ITreeNode child : children) {
			if (((PingResultTreeNode) child).showFailureIcon()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the node's label.
	 * 
	 * @return the label.
	 */
	public String getLabel() {
		return (String) getValue();
	}

	/**
	 * Strips off any packages in the name in order to provide a short label.
	 * 
	 * @param name
	 *            the given name to strip.
	 * @return the stripped off name.
	 */
	protected static String stripOffPackage(String name) {
		int dot = name.lastIndexOf(PING_METHOD_PREFIX);
		if (dot >= 0 && (dot + PING_METHOD_PREFIX.length()) <= name.length() + 1) {
			name = name.substring(dot + PING_METHOD_PREFIX.length());
			return name;
		}
		dot = name.lastIndexOf('.');
		if (dot < 0 || dot == name.length()) {
			return name;
		}
		name = name.substring(dot + 1);
		return name;
	}
}
