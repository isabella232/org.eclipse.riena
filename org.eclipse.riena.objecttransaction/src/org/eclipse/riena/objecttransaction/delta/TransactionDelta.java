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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.objecttransaction.IObjectId;
import org.eclipse.riena.objecttransaction.ObjectTransactionFailure;
import org.eclipse.riena.objecttransaction.state.State;

/**
 * this instance holds all changes for one transacted objects.
 * 
 */
public class TransactionDelta implements Cloneable {

	private IObjectId objectId;
	private State state; // (Clean, Created, Modified, Deleted)
	private String version; // version of TransactionDelta
	private Map<String, AbstractBaseChange> referenceChanges; // (refName à

	// newValue)

	/**
	 * @param objectId
	 * @param state
	 * @param version
	 */
	public TransactionDelta(final IObjectId objectId, final State state, final String version) {
		super();
		setObjectId(objectId);
		setState(state);
		setVersion(version);
		this.referenceChanges = new HashMap<String, AbstractBaseChange>();
	}

	/**
	 * @return the objectId.
	 */
	public IObjectId getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId
	 *            The objectId to set.
	 */
	public void setObjectId(final IObjectId objectId) {
		this.objectId = objectId;
	}

	/**
	 * Sets the change for a 1:1 relation overriding previous changes
	 * 
	 * @param refName
	 * @param childObject
	 */
	public void setSingleRefObject(final String refName, final Object childObject) {
		referenceChanges.put(refName, new SingleChange(refName, childObject));
	}

	/**
	 * @param refName
	 * @return
	 */
	public boolean hasSingleRefObject(final String refName) {
		final SingleChange sEntry = ((SingleChange) referenceChanges.get(refName));
		if (sEntry != null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the change for a 1:1 relation, if there is no it returns null
	 * 
	 * @param refName
	 * @return
	 */
	public Object getSingleRefObject(final String refName) {
		final SingleChange sEntry = ((SingleChange) referenceChanges.get(refName));
		if (sEntry == null) {
			return null;
		}
		return sEntry.getChildObject();
	}

	/**
	 * Adds a change to a 1:N relation, creates a new 1:N relation if it did not
	 * exist
	 * 
	 * @param refName
	 * @param childObject
	 */
	public void addMultiRefObject(final String refName, final Object childObject) {
		final AbstractBaseChange baseEntry = referenceChanges.get(refName);
		if (!(baseEntry instanceof MultipleChange) && baseEntry != null) {
			throw new ObjectTransactionFailure("impossible to add a reference to refName" + refName //$NON-NLS-1$
					+ "as it was previously used with a set object, which creates a 1:1 reference."); //$NON-NLS-1$
		}
		MultipleChange entry = (MultipleChange) baseEntry;
		if (entry == null) {
			entry = new MultipleChange(refName);
			referenceChanges.put(refName, entry);
		}
		entry.addEntry(childObject);
	}

	/**
	 * Removes a previously recorded change from a 1:N relation
	 * 
	 * @param refName
	 * @param childObject
	 */
	public void removeMultiRefObject(final String refName, final Object childObject) {
		final AbstractBaseChange baseEntry = referenceChanges.get(refName);
		if (!(baseEntry instanceof MultipleChange) && baseEntry != null) {
			throw new ObjectTransactionFailure("impossible to add a reference to refName" + refName //$NON-NLS-1$
					+ "as it was previously used with a set object, which creates a 1:1 reference."); //$NON-NLS-1$
		}
		MultipleChange entry = (MultipleChange) baseEntry;
		if (entry == null) {
			entry = new MultipleChange(refName);
			referenceChanges.put(refName, entry);
		}
		entry.removeEntry(childObject);

	}

	protected Map<String, AbstractBaseChange> getAllChanges() {
		return referenceChanges;
	}

	/**
	 * @return the state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(final State state) {
		this.state = state;
	}

	/**
	 * @return true if delta is clean
	 */
	public boolean isClean() {
		return state.equals(State.CLEAN);
	}

	/**
	 * @returntrue if delta is modified
	 */
	public boolean isModified() {
		return state.equals(State.MODIFIED);
	}

	/**
	 * @return true if delta is created
	 */
	public boolean isCreated() {
		return state.equals(State.CREATED);
	}

	/**
	 * @return true if delta is deleted
	 */
	public boolean isDeleted() {
		return state.equals(State.DELETED);
	}

	/**
	 * @return the version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * @return
	 */
	public Map<String, AbstractBaseChange> getChanges() {
		return referenceChanges;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("##### objectId:" + objectId + " #####\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("state:" + State.toString(state) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("reference Changes ---START---\n"); //$NON-NLS-1$
		for (final Map.Entry<String, AbstractBaseChange> entry : referenceChanges.entrySet()) {
			sb.append(entry.toString() + "\n"); //$NON-NLS-1$
		}
		sb.append("reference Changes ---END---\n"); //$NON-NLS-1$
		return sb.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final TransactionDelta delta = (TransactionDelta) super.clone();
		delta.referenceChanges = (Map<String, AbstractBaseChange>) ((HashMap<String, AbstractBaseChange>) referenceChanges)
				.clone();
		return delta;
	}

}