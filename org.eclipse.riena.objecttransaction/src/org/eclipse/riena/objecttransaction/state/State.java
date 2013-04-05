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
package org.eclipse.riena.objecttransaction.state;

/**
 * Enumerator class that represented the current state of a transactedobject or
 * or a relation.
 * 
 * @see org.eclipse.riena.objecttransaction.state.StateMachine
 * 
 */
public final class State {

	private static transient final int CREATED_CODE = 1;
	private static transient final int DELETED_CODE = 2;
	private static transient final int MODIFIED_CODE = 3;
	private static transient final int CLEAN_CODE = 4;
	private static transient final int VANISHED_CODE = 5;
	private static transient final int ADDED_CODE = 6;
	private static transient final int REMOVED_CODE = 7;

	/** <code>CLEAN</code> */
	public transient final static State CLEAN = new State(State.CLEAN_CODE);
	/** <code>CREATED</code> */
	public transient final static State CREATED = new State(State.CREATED_CODE);
	/** <code>MODIFIED</code> */
	public transient final static State MODIFIED = new State(State.MODIFIED_CODE);
	/** <code>DELETED</code> */
	public transient final static State DELETED = new State(State.DELETED_CODE);
	/** <code>VANISHED</code> */
	public transient final static State VANISHED = new State(State.VANISHED_CODE);
	/** <code>ADDED</code> */
	public transient final static State ADDED = new State(State.ADDED_CODE);
	/** <code>REMOVED</code> */
	public transient final static State REMOVED = new State(State.REMOVED_CODE);

	private final int state;

	private State(final int state) {
		super();
		this.state = state;
	}

	@Override
	public String toString() {
		if (state == CREATED_CODE) {
			return "created"; //$NON-NLS-1$
		} else if (state == DELETED_CODE) {
			return "deleted"; //$NON-NLS-1$
		} else if (state == MODIFIED_CODE) {
			return "modified"; //$NON-NLS-1$
		} else if (state == CLEAN_CODE) {
			return "clean (unmodified)"; //$NON-NLS-1$
		} else if (state == VANISHED_CODE) {
			return "vanished (was created and then deleted)"; //$NON-NLS-1$
		} else if (state == ADDED_CODE) {
			return "add"; //$NON-NLS-1$
		} else if (state == REMOVED_CODE) {
			return "remove"; //$NON-NLS-1$
		}
		return "unknown state ?????"; //$NON-NLS-1$
	}

	/**
	 * @param state
	 * @return
	 */
	public static String toString(final State state) {
		if (state == null) {
			return "state is NULL ??"; //$NON-NLS-1$
		} else {
			return state.toString();
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (object instanceof State) {
			return ((State) object).state == state;
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return state;
	}
}