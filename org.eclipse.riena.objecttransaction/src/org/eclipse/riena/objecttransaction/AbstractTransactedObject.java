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
package org.eclipse.riena.objecttransaction;

/**
 * Abstract base class for transacted objects. This implementation is a simple
 * base for customer objects.
 * 
 */
public abstract class AbstractTransactedObject implements ITransactedObject {

	private IObjectId objectId;
	private String version;
	private final IObjectTransactionManager objectTransactionManager;

	/**
	 * Create a transacted object without a inital object id and version.
	 */
	protected AbstractTransactedObject() {
		super();
		objectTransactionManager = ObjectTransactionManager.getInstance();
	}

	/**
	 * Create a transacted object with a inital object id and version.
	 * 
	 * @param objectId
	 *            initial object id
	 * @param version
	 *            initial version
	 */
	protected AbstractTransactedObject(final IObjectId objectId, final String version) {
		this();
		setObjectId(objectId);
		setVersion(version);
	}

	/**
	 * @see org.eclipse.riena.objecttransaction.ITransactedObject#getObjectId()
	 */
	public IObjectId getObjectId() {
		return objectId;
	}

	/**
	 * @see org.eclipse.riena.objecttransaction.ITransactedObject#setObjectId(org.eclipse.riena.objecttransaction.IObjectId)
	 */
	public void setObjectId(final IObjectId objectId) {
		this.objectId = objectId;
	}

	/**
	 * @see org.eclipse.riena.objecttransaction.ITransactedObject#getVersion()
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @see org.eclipse.riena.objecttransaction.ITransactedObject#setVersion(java.lang.String)
	 */
	public void setVersion(final String versionString) {
		this.version = versionString;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransactedObject: objectId='" + objectId + "', version='" + version + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (object instanceof ITransactedObject) {
			return objectId.equals(((ITransactedObject) object).getObjectId());
		} else {
			return false;
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getObjectId().hashCode();
	}

	/**
	 * Get the current object transaction.
	 * 
	 * @return current object transaction
	 */
	protected IObjectTransaction getCurrentObjectTransaction() {
		return objectTransactionManager.getCurrent();
	}

}