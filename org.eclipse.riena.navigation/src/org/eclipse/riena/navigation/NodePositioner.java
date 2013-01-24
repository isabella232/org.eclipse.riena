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
package org.eclipse.riena.navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.navigation.model.NavigationModelFailure;

/**
 * A NodePositioner object is responsible to add a NavigationNode as a child to another NavigationNode at a specified index. NodePositioner is for example used
 * by the SimpleNavigationNodeProvider to add a subtree to a parent node. You can add a NodePositioner to a NavigationArgument. Then, whenever new nodes are
 * created the NodePositioner will handle the addition of the new subtree to the root node. Usually it should not be necessary to subclass NodePositioner.
 */
public abstract class NodePositioner {

	public final static String POSITIONING_ORDINALITY_KEY = NodePositioner.class.getName() + "positioning-ordinality-key"; //$NON-NLS-1$

	protected Map<String, Object> context = new HashMap<String, Object>();

	enum Mode {
		FIXED, ORDINAL
	}

	/**
	 * Adds a node to another at index 0.
	 */
	public final static NodePositioner ADD_BEGINNING = new NodePositioner() {
		private final NodePositioner delegate = indexed(0);

		@Override
		public void addChildToParent(final INavigationNode parent, final INavigationNode child) {
			delegate.addChildToParent(parent, child);
		}
	};

	/**
	 * Adds a node to another as the last child.
	 */
	public final static NodePositioner ADD_END = new NodePositioner() {

		@Override
		public void addChildToParent(final INavigationNode parent, final INavigationNode child) {
			assertMode(parent, Mode.FIXED);
			parent.addChild(child);
		}

	};

	/**
	 * Adds a node to another as a child at the specified index.
	 */
	public final static NodePositioner indexed(final int index) {

		return new NodePositioner() {

			@Override
			public void addChildToParent(final INavigationNode parent, final INavigationNode child) {
				assertMode(parent, Mode.FIXED);
				if (index < 0) {
					throw new NavigationModelFailure("Cannot add child " + child + " to parent " + parent //$NON-NLS-1$//$NON-NLS-2$
							+ " at index " + index + ". Index must be >= 0"); //$NON-NLS-1$//$NON-NLS-2$
				}

				// Fallback for out of bound index
				if (index >= parent.getChildren().size()) {
					ADD_END.addChildToParent(parent, child);
					return;
				}
				parent.addChild(index, child);
			}

		};
	}

	/**
	 * Adds a node to another as a child with the specified ordinality(relative index). Note that you may not add different children with fixed index and
	 * ordinality to the same parent node.
	 */
	public final static NodePositioner ordinal(final int ordinality) {
		return new NodePositioner() {

			@Override
			public void addChildToParent(final INavigationNode parent, final INavigationNode child) {
				assertMode(parent, Mode.ORDINAL);
				child.setContext(POSITIONING_ORDINALITY_KEY, ordinality);

				if (parent.getChildren().size() == 0) {
					parent.addChild(child);
					return;
				}

				final List<INavigationNode<?>> oldChildren = parent.getChildren();
				int ix = 0;
				for (final INavigationNode<?> n : oldChildren) {
					final Integer curOrdinality = (Integer) n.getContext(POSITIONING_ORDINALITY_KEY);
					if (curOrdinality == null) {
						throw new IllegalArgumentException("The node '" + n.getNodeId() + "' has no ordinality index. " //$NON-NLS-1$ //$NON-NLS-2$
								+ "You can not combine ordinal with fixed children for the same parent."); //$NON-NLS-1$
					}

					if (ordinality < curOrdinality) {
						break;
					}
					ix++;
				}

				if (ix == oldChildren.size()) {
					parent.addChild(child);
				} else {
					parent.addChild(ix, child);
				}

			}

		};
	}

	private static void assertMode(final INavigationNode node, final Mode expected) {
		if (node.getChildren().size() == 0) {
			return;
		}
		final INavigationNode firstChild = (INavigationNode) node.getChildren().get(0);
		final Mode currentMode = firstChild.getContext(POSITIONING_ORDINALITY_KEY) != null ? Mode.ORDINAL : Mode.FIXED;
		if (!currentMode.equals(expected)) {
			throw new NavigationModelFailure("Node " + node + " cannot be added with NodePositioningMode " + expected //$NON-NLS-1$ //$NON-NLS-2$
					+ " as the current mode is " + currentMode); //$NON-NLS-1$
		}
	}

	/**
	 * Adds the child node to the parent node.
	 * 
	 * @param parent
	 *            the parent node
	 * @param child
	 *            the child node
	 */
	public abstract void addChildToParent(INavigationNode parent, INavigationNode child);

}
