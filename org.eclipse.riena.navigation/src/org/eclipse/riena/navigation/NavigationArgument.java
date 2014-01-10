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
package org.eclipse.riena.navigation;

/**
 * Contains additional navigation information that is passed on to the opened
 * node during its creation.
 * 
 * @see INavigationNode#navigate(INavigationNodeId, NavigationArgument)
 */
public class NavigationArgument {

	/**
	 * this key is used in INavigationNode.getContext to address THIS (the
	 * NavigationArgument)
	 */
	public static final String CONTEXTKEY_ARGUMENT = "riena.navigation.argument"; //$NON-NLS-1$

	public static final String CONTEXTKEY_PARAMETER = "riena.navigation.parameter"; //$NON-NLS-1$

	private IUpdateListener updateListener = null;
	private Object parameter;
	private NavigationNodeId parentNodeId;
	private String ridgetId;
	private boolean prepareAll;
	private NodePositioner nodePositioner;
	/**
	 * internal flag that indicated creation of nodes on a worker-thread
	 * 
	 */
	private boolean createNodesAsync;

	/**
	 */
	public NavigationArgument() {
		super();
		nodePositioner = NodePositioner.ADD_END;
	}

	/**
	 * @param parameter
	 *            parameter object that is passed to the opened node.
	 */
	public NavigationArgument(final Object parameter) {
		this();
		this.parameter = parameter;
	}

	/**
	 * @param parameter
	 *            parameter object that is passed to the opened node.
	 * @param ridgetId
	 *            ID of the ridget that will get the initial focus in the view
	 *            associated with the opened node
	 * @since 1.2
	 */
	public NavigationArgument(final Object parameter, final String ridgetId) {
		this(parameter);
		this.ridgetId = ridgetId;
	}

	/**
	 * @param parameter
	 *            parameter object that is passed to the opened node.
	 * @param parentNodeId
	 *            overrides the parentTypeId specified for the containing
	 *            assembly extension. The type of the specified parent node has
	 *            to be identical to the type of the original node.
	 */
	public NavigationArgument(final Object parameter, final NavigationNodeId parentNodeId) {
		this(parameter);
		this.parentNodeId = parentNodeId;
	}

	/**
	 * @param parameter
	 *            parameter object that is passed to the opened node.
	 * @param parentNodeId
	 *            overrides the parentTypeId specified for the containing
	 *            assembly extension. The type of the specified parent node has
	 *            to be identical to the type of the original node.
	 * @param ridgetId
	 *            ID of the ridget that will get the initial focus in the view
	 *            associated with the opened node
	 * @since 1.2
	 */
	public NavigationArgument(final Object parameter, final NavigationNodeId parentNodeId, final String ridgetId) {
		this(parameter, parentNodeId);
		this.ridgetId = ridgetId;
	}

	/**
	 * @param parameter
	 *            parameter object that is passed to the opened node.
	 * @param updateListener
	 *            the specified updateListener is informed about update changes
	 *            using this NavigationArgument. The opened node can use
	 *            fireValueChanged() to inform the caller about changes in the
	 *            opened node on which the caller can react.
	 * @param parentNodeId
	 *            overrides the parentTypeId specified for the containing
	 *            assembly extension. The type of the specified parent node has
	 *            to be identical to the type of the original node.
	 */
	public NavigationArgument(final Object parameter, final IUpdateListener updateListener,
			final NavigationNodeId parentNodeId) {
		this(parameter, parentNodeId);
		this.updateListener = updateListener;
	}

	/**
	 * @return the createNodesAsync
	 * @since 3.0
	 */
	public boolean isCreateNodesAsync() {
		return createNodesAsync;
	}

	/**
	 * @param createNodesAsync
	 *            the createNodesAsync to set
	 * @since 3.0
	 */
	public void setCreateNodesAsync(final boolean createNodesAsync) {
		this.createNodesAsync = createNodesAsync;
	}

	/**
	 * @return ID of the ridget that will get the initial focus in the view
	 *         associated with the opened node
	 * @since 1.2
	 */
	public String getRidgetId() {
		return ridgetId;
	}

	/**
	 * @return the parameter object that is passed to the opened node using this
	 *         NavigationArgument.
	 */
	public Object getParameter() {
		return parameter;
	}

	/**
	 * @return the parentNodeId.
	 */
	public NavigationNodeId getParentNodeId() {
		return parentNodeId;
	}

	/**
	 * @return the update listener that will be informed about changes in the
	 *         opened node when fireValueChanged is called.
	 */
	public IUpdateListener getUpdateListener() {
		return updateListener;
	}

	/**
	 * Sets whether all nodes should be prepared.
	 * 
	 * @param prepareAll
	 *            {@code true} prepare all nodes; otherwise {@code false}
	 * @since 2.0
	 */
	public void setPrepareAll(final boolean prepareAll) {
		this.prepareAll = prepareAll;
	}

	/**
	 * Returns whether all nodes should be prepared.
	 * 
	 * @return {@code true} prepare all nodes; otherwise {@code false}
	 * @since 2.0
	 */
	public boolean isPrepareAll() {
		return prepareAll;
	}

	/**
	 * @return the nodePositioner
	 * @since 2.0
	 */
	public NodePositioner getNodePositioner() {
		return nodePositioner;
	}

	/**
	 * @param nodePositioner
	 *            the nodePositioner to set
	 * @since 2.0
	 */
	public void setNodePositioner(final NodePositioner nodePositioner) {
		this.nodePositioner = nodePositioner;
	}

	/**
	 * Notify the update listener of this NavigationArgument about changes in
	 * the opened node.
	 */
	public void fireValueChanged(final Object parameter) {
		if (updateListener == null) {
			return;
		}
		updateListener.handleUpdate(parameter);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * All properties of this class are used.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodePositioner == null) ? 0 : nodePositioner.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + ((parentNodeId == null) ? 0 : parentNodeId.hashCode());
		result = prime * result + (prepareAll ? 1231 : 1237);
		result = prime * result + ((ridgetId == null) ? 0 : ridgetId.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * All properties of this class are used.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final NavigationArgument other = (NavigationArgument) obj;
		if (nodePositioner == null) {
			if (other.nodePositioner != null) {
				return false;
			}
		} else if (!nodePositioner.equals(other.nodePositioner)) {
			return false;
		}
		if (parameter == null) {
			if (other.parameter != null) {
				return false;
			}
		} else if (!parameter.equals(other.parameter)) {
			return false;
		}
		if (parentNodeId == null) {
			if (other.parentNodeId != null) {
				return false;
			}
		} else if (!parentNodeId.equals(other.parentNodeId)) {
			return false;
		}
		if (prepareAll != other.prepareAll) {
			return false;
		}
		if (ridgetId == null) {
			if (other.ridgetId != null) {
				return false;
			}
		} else if (!ridgetId.equals(other.ridgetId)) {
			return false;
		}
		if (updateListener == null) {
			if (other.updateListener != null) {
				return false;
			}
		} else if (!updateListener.equals(other.updateListener)) {
			return false;
		}

		return true;

	}

}
