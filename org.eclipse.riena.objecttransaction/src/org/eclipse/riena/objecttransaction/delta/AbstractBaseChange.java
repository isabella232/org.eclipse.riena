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
package org.eclipse.riena.objecttransaction.delta;

/**
 * Base Object for all recorded object changes of the various types
 * 
 */
public abstract class AbstractBaseChange {

	private String relationName;

	/** <code>SINGLE_CHANGE_ENTRY</code> */
	public static transient final int SINGLE_CHANGE_ENTRY = 1;
	/** <code>SET_CHANGE_ENTRY</code> */
	public static transient final int SET_CHANGE_ENTRY = 2;

	/**
	 * @param relationName
	 */
	public AbstractBaseChange(final String relationName) {
		this.relationName = relationName;
	}

	/**
	 * @return the relationName.
	 */
	public String getRelationName() {
		return relationName;
	}

	/**
	 * @param relationName
	 *            The relationName to set.
	 */
	public void setRelationName(final String relationName) {
		this.relationName = relationName;
	}
}