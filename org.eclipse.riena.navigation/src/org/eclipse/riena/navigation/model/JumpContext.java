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

import org.eclipse.riena.navigation.INavigationNode;

/**
 * Container for holding the source and the targe of a navigation jump.
 * 
 * @see {@link INavigationNode#jump(org.eclipse.riena.navigation.NavigationNodeId)}
 * @since 3.0
 * 
 */
public class JumpContext implements Comparable<JumpContext> {

	private final INavigationNode<?> source;
	private final INavigationNode<?> target;
	private final Long timeStamp;

	public JumpContext(final INavigationNode<?> source, final INavigationNode<?> target) {
		this.source = source;
		this.target = target;
		this.timeStamp = System.currentTimeMillis();
	}

	public INavigationNode<?> getSource() {
		return source;
	}

	public INavigationNode<?> getTarget() {
		return target;
	}

	/**
	 * 
	 * @return the time in milliseconds when the {@link JumpContext} has been
	 *         created
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	public int compareTo(final JumpContext other) {
		return timeStamp.compareTo(other.timeStamp);
	}

}