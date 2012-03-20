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
package org.eclipse.riena.objecttransaction.delta;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.objecttransaction.state.State;

/**
 * Object for maintaining the changes for 1:N relations
 * 
 */
public class MultipleChange extends AbstractBaseChange {

	private final List<MultipleChangeEntry> changeEntries;

	/**
	 * @param relationName
	 */
	public MultipleChange(final String relationName) {
		super(relationName);
		changeEntries = new ArrayList<MultipleChangeEntry>();
	}

	/**
	 * Adds a new changed relation to the Multichange set
	 * 
	 * @param childObject
	 */
	public void addEntry(final Object childObject) {
		changeEntries.add(new MultipleChangeEntry(childObject, State.ADDED));
	}

	/**
	 * Removes a previously recorded change from the Multichange set
	 * 
	 * @param childObject
	 */
	public void removeEntry(final Object childObject) {
		if (changeEntries.size() > 0) {
			for (int i = changeEntries.size() - 1; i >= 0; i--) {
				final MultipleChangeEntry entry = changeEntries.get(i);
				if (entry.getChildObject().equals(childObject) && entry.getState().equals(State.ADDED)) {
					changeEntries.remove(i);
					return;
				}
			}
		}
		changeEntries.add(new MultipleChangeEntry(childObject, State.REMOVED));
	}

	/**
	 * Returns a changes as List
	 * 
	 * @return
	 */
	public List<MultipleChangeEntry> getEntries() {
		return changeEntries;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (changeEntries.size() == 0) {
			return "SetChange: <no changes>"; //$NON-NLS-1$
		}
		final Object[] array = changeEntries.toArray();
		final StringBuilder sb = new StringBuilder("SetChange: refName:" + this.getRelationName()); //$NON-NLS-1$
		if (array.length > 1) {
			sb.append("\n"); //$NON-NLS-1$
		}
		for (int i = 0; i < array.length; i++) {
			if (array.length > 1) {
				sb.append("---------------->"); //$NON-NLS-1$
			}
			sb.append(" entry[" + i + "]:" + array[i]); //$NON-NLS-1$ //$NON-NLS-2$
			if (array.length > 1 && (i + 1) < array.length) {
				sb.append("\n"); //$NON-NLS-1$
			}
		}
		return sb.toString();
	}
}