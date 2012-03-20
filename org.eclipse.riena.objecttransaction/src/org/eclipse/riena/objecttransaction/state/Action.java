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
package org.eclipse.riena.objecttransaction.state;

/**
 * This enumerator class creates a static set of actions that can be performed
 * on a state. This is done by the StateMachine.
 * 
 * @see org.eclipse.riena.objecttransaction.state.StateMachine
 * 
 */
public final class Action {

	private static transient final int NEW_CODE = 1;
	private static transient final int SET_CODE = 2;
	private static transient final int ADD_CODE = 3;
	private static transient final int REMOVE_CODE = 4;
	private static transient final int DELETE_CODE = 5;

	/** <code>NEW</code> */
	public transient final static Action NEW = new Action(NEW_CODE);
	/** <code>SET</code> */
	public transient final static Action SET = new Action(SET_CODE);
	/** <code>ADD</code> */
	public transient final static Action ADD = new Action(ADD_CODE);
	/** <code>REMOVE</code> */
	public transient final static Action REMOVE = new Action(REMOVE_CODE);
	/** <code>DELETE</code> */
	public transient final static Action DELETE = new Action(DELETE_CODE);

	private final int actionCode;

	private Action(final int actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (actionCode == NEW_CODE) {
			return "new"; //$NON-NLS-1$
		} else if (actionCode == SET_CODE) {
			return "set"; //$NON-NLS-1$
		} else if (actionCode == ADD_CODE) {
			return "add"; //$NON-NLS-1$
		} else if (actionCode == REMOVE_CODE) {
			return "remove"; //$NON-NLS-1$
		} else if (actionCode == DELETE_CODE) {
			return "delete"; //$NON-NLS-1$
		}
		return "unknown state ?????"; //$NON-NLS-1$
	}

	/**
	 * @param action
	 * @return
	 */
	public static String toString(final Action action) {
		if (action == null) {
			return "action is NULL ??"; //$NON-NLS-1$
		} else {
			return action.toString();
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (object instanceof Action) {
			return ((Action) object).actionCode == actionCode;
		}

		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return actionCode;
	}
}