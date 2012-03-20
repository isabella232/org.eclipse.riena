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

/**
 * ChangeEntry Class that maintains a single property or 1:1 reference change
 * 
 */
public class SingleChange extends AbstractBaseChange {

	private Object childObject;

	/**
	 * @param relationName
	 * @param childObject
	 */
	public SingleChange(final String relationName, final Object childObject) {
		super(relationName);
		this.childObject = childObject;
	}

	/**
	 * @return the childObject.
	 */
	public Object getChildObject() {
		return childObject;
	}

	/**
	 * @param childObject
	 *            The childObject to set.
	 */
	public void setChildObject(final Object childObject) {
		this.childObject = childObject;
	}

	@Override
	public String toString() {
		return "SingleChange: new:" + childObject; //$NON-NLS-1$
	}
}