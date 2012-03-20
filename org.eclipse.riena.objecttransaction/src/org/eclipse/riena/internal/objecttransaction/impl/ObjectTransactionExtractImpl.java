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
package org.eclipse.riena.internal.objecttransaction.impl;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.objecttransaction.IObjectId;
import org.eclipse.riena.objecttransaction.IObjectTransactionExtract;
import org.eclipse.riena.objecttransaction.ITransactedObject;
import org.eclipse.riena.objecttransaction.delta.TransactionDelta;
import org.eclipse.riena.objecttransaction.state.State;

/**
 * Implements the ObjectTransactionExtract which contains all modified
 * properties from one ObjectTransaction
 * 
 */
public class ObjectTransactionExtractImpl implements IObjectTransactionExtract {

	private final ArrayList<TransactionDelta> deltas = new ArrayList<TransactionDelta>();

	/**
	 * @param deltas
	 */
	protected ObjectTransactionExtractImpl() {
	}

	/**
	 * @see org.eclipse.riena.objecttransaction.IObjectTransactionExtract#getDeltas()
	 */
	public TransactionDelta[] getDeltas() {
		return deltas.toArray(new TransactionDelta[deltas.size()]);
	}

	/*
	 * @seeorg.eclipse.riena.objecttransaction.IObjectTransactionExtract#
	 * addTransactedObject
	 * (org.eclipse.riena.objecttransaction.ITransactedObject)
	 */
	public void addCleanTransactedObject(final ITransactedObject transObject) {
		Assert.isTrue((!contains(transObject.getObjectId())), "object must not exist in extract"); //$NON-NLS-1$
		deltas.add(new TransactionDelta(transObject.getObjectId(), State.CLEAN, transObject.getVersion()));
	}

	/*
	 * @see
	 * org.eclipse.riena.objecttransaction.IObjectTransactionExtract#contains
	 * (org.eclipse.riena.objecttransaction.IObjectId)
	 */
	public boolean contains(final IObjectId objectid) {
		for (int i = 0; i < deltas.size(); i++) {
			if (deltas.get(i).getObjectId().equals(objectid)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Internal method to add deltas to the extract
	 * 
	 * @param delta
	 */
	protected void addDelta(final TransactionDelta delta) {
		deltas.add(delta);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("----------extract--------------------\n"); //$NON-NLS-1$
		for (int i = 0; i < deltas.size(); i++) {
			sb.append(deltas.get(i));
		}
		sb.append("----------extract--------------------\n"); //$NON-NLS-1$
		return sb.toString();
	}

}