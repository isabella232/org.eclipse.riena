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
 * Interface that must be implemented by all transaction enabled Business
 * Objects. It has method to retrieve a unique id and a version for any object
 * 
 */
public interface ITransactedObject {

	/**
	 * Returns the object id of the transacted object. This id must be universal
	 * unique as described in IObjectId. It is used as key for hashMaps.
	 * 
	 * @return IObjectId of this object
	 */
	IObjectId getObjectId();

	/**
	 * changes the object id of the transacted object to the new object id. If
	 * you like to set a new objectid to an object, you should not call this
	 * method directly but rather call @see
	 * IObjectTransaction.setObjectIdUpdate(). This will call this method
	 * internally but makes sure that the modification of the object id is
	 * tracked (and exported in an extract if necessary).
	 * 
	 * @param objectId
	 *            new ObjectId of this object
	 */
	void setObjectId(IObjectId objectId);

	/**
	 * Returns the current version of the transacted object.
	 * 
	 * @return String version String
	 */
	String getVersion();

	/**
	 * Sets a new version string for a transacted object. If you like to set a
	 * new (higher) version to an object, you should not call this method
	 * directly but call @see IObjectTransaction.setVersion(). This will then
	 * call this setVersion internally, but make sure that the version change is
	 * tracked as a modification to object (and exported in an extract if
	 * necessary).
	 * 
	 * @param versionString
	 *            version String
	 */
	void setVersion(String versionString);

}