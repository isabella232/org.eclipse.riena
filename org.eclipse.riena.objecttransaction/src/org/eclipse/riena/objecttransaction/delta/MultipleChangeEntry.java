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
package org.eclipse.riena.objecttransaction.delta;

import org.eclipse.riena.objecttransaction.state.State;

/**
 * Class that maintains the change used by 1:N Relations. This object maintains
 * the changes for a single entry for a 1:N relation.
 * 
 */
public class MultipleChangeEntry {

	private final Object childObject;
	private final State state;

	MultipleChangeEntry(final Object childObject, final State state) {
		this.childObject = childObject;
		this.state = state;
	}

	/**
	 * @return the childObject.
	 */
	public Object getChildObject() {
		return childObject;
	}

	/**
	 * @return the state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "obj:" + childObject + " state:" + State.toString(state); //$NON-NLS-1$ //$NON-NLS-2$
	}
}