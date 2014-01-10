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
package org.eclipse.riena.objecttransaction;

/**
 * This interface must be implemented to identify a
 * <code>ITransactedObject</code> uniquely, within the transaction context.
 * 
 * Any object that implements this interface must also implement the equals and
 * hashCode method for this object. It cannot rely on the implementation of
 * java.lang.Object for this. - equals must be implemented to return TRUE if two
 * IObjectId are logically equal. This must also be true, if they are two
 * instances of the same object. - hashCode must work as specified in
 * java.lang.Object. However it must create the same hashCode for any two
 * objects that represent logically the same Object or in other words where the
 * equals method returns TRUE.
 * 
 */
public interface IObjectId {

	/**
	 * Must return a string that identifies the type of object that is
	 * identified by this type of ObjectId.
	 * 
	 * @return
	 */
	String getType();
}