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
package org.eclipse.riena.objecttransaction;

import java.util.List;
import java.util.Set;

/**
 * The ObjectTransaction contains all the information about registered objects,
 * changed properties and relationships to other registered objects.
 * Relationships can be 1:1 relationships and 1:n relationships.
 * 
 * Instances can be RootTransactions or SubObjectTransactions.
 * SubObjectTransactions transfer their changes to the next higher level, if
 * they are committed.
 * 
 * RootTransactions cannot be committed but rather have to be passed on to the
 * Persistent Layer which will read out the recorded changes. It is possible to
 * have more than one RootTransaction. Only the currently activated
 * ObjectTransaction will record changes in the ITransactedObjects.
 * 
 */
public interface IObjectTransaction {

	/**
	 * Registers an ITransactedObject as CLEAN and not modified object
	 * 
	 * @param object
	 *            transactedobject that should be registered
	 * 
	 * @pre !isInvalid() && object!=null && ((object.getObjectId()!=null &&
	 *      !isRegistered(object)) || object.getObjectId()==null)
	 */
	void register(ITransactedObject object);

	/**
	 * Registers an ITransactedObject as a NEW object
	 * 
	 * @param object
	 *            transactedobject that should be registered
	 * 
	 * @pre !isInvalid() && object!=null && object.getObjectId()!=null &&
	 *      !isRegistered(object)
	 */
	void registerNew(ITransactedObject object);

	/**
	 * Register an ITransactedObject in the ObjectTransaction as being deleted,
	 * requires that the object was already registered in the ObjectTransaction.
	 * A call to registerAsDeleted modifies its state to "toBeDeleted".
	 * 
	 * @param object
	 *            transactedobject that was deleted
	 * @pre !isInvalid()&& object!=null && object.getObjectId()!=null
	 */
	void registerAsDeleted(ITransactedObject object);

	/**
	 * Is the transacted Object in the parameter already registered in this
	 * object transaction ? This method also checks out parent transactions. So
	 * if this is a subtransaction and the transactedobject was registered in
	 * the rootTransactions, it returns true.
	 * 
	 * @param object
	 *            transactedobject that should be checked
	 * 
	 * @return true if registered
	 * @pre !isInvalid() && object!=null && object.getObjectId()!=null
	 */
	boolean isRegistered(ITransactedObject object);

	/**
	 * This method enables the application to disable the register
	 * functionality. If register is not allowed, a register, registerNew or
	 * registerDelete call will return with no function performed.
	 * 
	 * @param allowRegister
	 */
	void allowRegister(boolean allowRegister);

	/**
	 * Returns whether this ObjectTransaction is a RootTransaction. A Root has
	 * no parentTransaction. Commit and Rollback is only allowed on
	 * ObjectTransactions which are NOT RootTransactions
	 * 
	 * @return "true" if it is a RootTransaction;
	 */
	boolean isRootTransaction();

	/**
	 * Creates an extract of all the recorded changes in this objectTransaction.
	 * This extract only contains the changes but no original business object.
	 * 
	 * @return objectTransactionextract that contains all the deltas
	 * @pre !isInvalid()
	 */
	IObjectTransactionExtract exportExtract();

	/**
	 * Creates an extract of all changes within the objectTransaction. This
	 * extract contains no reference to clean objects. References to clean
	 * objects are contained in exportExtract(). To import this extract again,
	 * use the regular importExtract()
	 * 
	 * @see exportExtract
	 * @see importExtract
	 * @return
	 */
	IObjectTransactionExtract exportOnlyModifedObjectsToExtract();

	/**
	 * Imports a previously exported extract. It requires that all transacted
	 * object in the extract were previously loaded (somehow) and registered
	 * with this ObjectTransaction. If that was not done an
	 * InvalidTransactionFailure is thrown.
	 * 
	 * @param extract
	 * @pre !isInvalid() && !isCleanModus() && isRootTransaction()
	 */
	void importExtract(IObjectTransactionExtract extract);

	/**
	 * Imports from a full extract only those objects that were modified. Clean
	 * objects are not imported and therefore do not have to exist in the
	 * objecttransaction.
	 * 
	 * @param extract
	 */
	void importOnlyModifedObjectsFromExtract(IObjectTransactionExtract extract);

	/**
	 * Set the ObjectTransaction into cleanmodus. That means no changes are
	 * recorded. However each object, is registered in the transaction as clean.
	 * Clean Modus is activated if the parameter is "true". Initial state of
	 * Clean Modus of a objectTransaction is "false".
	 * 
	 * @param cleanModus
	 *            "true" or "false" whether a objectTransaction is in cleanModus
	 */
	void setCleanModus(boolean cleanModus);

	/**
	 * is the CleanModus flag set to "true" ? then return "true" here In
	 * CleanModus="true", changes to registered objects are not tracked.
	 * 
	 * @return value of the cleanModus flag
	 */
	boolean isCleanModus();

	/**
	 * For a description of strict modus see isStrictModus()
	 * 
	 * @param strictModus
	 */
	void setStrictModus(boolean strictModus);

	/**
	 * If strict modus is true, every object must be registered even in clean
	 * modus that means that even if you use an object without a
	 * objectTransaction, you have to register an object. If false, only when
	 * cleanmodus is false, must object be registered before use.
	 * 
	 * @return
	 */
	boolean isStrictModus();

	/**
	 * if a objectTransaction is invalid, no further action can be performed on
	 * it and the objectTransaction cannot be set to be the current
	 * 
	 * @return
	 */
	boolean isInvalid();

	/**
	 * Set property value for an ITransactedObject, this is for setting
	 * properties or referenced object that DO NOT implement the
	 * ITransactedObject interface
	 * 
	 * @param object
	 *            transacted object for which to return the referenced object
	 * @param referenceName
	 *            symbolic name of the reference
	 * @param oldRefValue
	 *            old value of the reference
	 * @param refValue
	 *            value that was set for the reference
	 * @pre object!=null && object.getObjectId()!=null && isRegistered(object)
	 *      && !(refValue instanceof ITransactedObject)
	 */
	void setReference(ITransactedObject object, String referenceName, Object refValue);

	/**
	 * Get property value for an ITransactedObject, the defaultValue is the
	 * current value in the bean. If no pending change is fund in any of the
	 * concerned ObjectTransactions, the defaultValue is returned
	 * 
	 * @param object
	 *            transacted object for which to return the referenced object
	 * @param referenceName
	 *            symbolic name of the reference
	 * @param defaultValue
	 *            current value of the reference in this bean
	 * @return objectvalue for that referenceName (if it exists)
	 * @pre object!=null && object.getObjectId()!=null && isRegistered(object);
	 */
	Object getReference(ITransactedObject object, String referenceName, Object defaultValue);

	/**
	 * Set property value for an ITransactedObject, this is for setting
	 * properties or referenced object that DO implement the ITransactedObject
	 * interface
	 * 
	 * @param object
	 *            transacted object for which to return the referenced object
	 * @param referenceName
	 *            symbolic name of the reference
	 * @param oldRefValue
	 *            old value of the reference
	 * @param refObject
	 *            value that was set for the reference
	 * @pre object.getObjectId()!=null && isRegisted(object)
	 * @pre oldRefObject==null || (oldRefObject.getObjectId()!=null &&
	 *      isRegistered(oldRefObject))
	 * @pre refObject==null || (refObject.getObjectId()!=null &&
	 *      isRegistered(refObject))
	 */
	void setReference(ITransactedObject object, String referenceName, ITransactedObject refObject);

	/**
	 * Get property value for an ITransactedObject, the defaultValue is the
	 * current value in the bean. If no pending change is fund in any of the
	 * concerned ObjectTransactions, the defaultValue is returned
	 * 
	 * @param object
	 *            transacted object for which to return the referenced object
	 * @param referenceName
	 *            symbolic name of the reference
	 * @param defaultRefObject
	 *            current value of the reference in this bean
	 * @return
	 * @pre object!=null && object.getObjectId()!=null && isRegistered(object)
	 * @pre defaultRefObject==null || (defaultRefObject.getObjectId()!=null &&
	 *      isRegistered(defaultRefObject))
	 */
	ITransactedObject getReference(ITransactedObject object, String referenceName, ITransactedObject defaultRefObject);

	/**
	 * Adds the reference to a child object simular to setReferenceObject with
	 * the difference that the referenceName refers to an unsorted collection of
	 * objects.
	 * 
	 * @param object
	 *            transacted object to which the child is added
	 * @param referenceName
	 *            symbolic name of the reference
	 * @param refObject
	 *            transacted child object to be added
	 * @pre object!=null && object.getObjectId()!=null && isRegistered(object)
	 * @pre refObject!=null && refObject.getObjectId()!=null &&
	 *      isRegistered(refObject)
	 */
	void addReference(ITransactedObject object, String referenceName, ITransactedObject refObject);

	/**
	 * Removes the reference to a child object from a collection of child
	 * objects
	 * 
	 * @param object
	 *            transacted object from which the child is removed
	 * @param referenceName
	 *            symbolic name of the reference
	 * @param refObject
	 *            child object to be removed
	 * @pre object!=null && object.getObjectId()!=null && isRegistered(object)
	 * @pre refObject!=null && refObject.getObjectId()!=null &&
	 *      isRegistered(refObject)
	 */
	void removeReference(ITransactedObject object, String referenceName, ITransactedObject refObject);

	/**
	 * Lists the entries of a child object collection in the state of this
	 * objectTransaction. The BusinessObject has to pass its own state and then
	 * all changes in this transaction are reapplied. The result is a collection
	 * that contains all current entries.
	 * 
	 * @param object
	 *            transacted object in which the collection is maintained
	 * @param referenceName
	 *            symbolic name of the reference
	 * @param initialSet
	 *            initialSet of collection entries from the business object
	 * @return a new collection with the current state of the entries within
	 *         this transaction
	 * @pre object!=null && object.getObjectId()!=null && isRegistered(object)
	 */
	<T> Set<T> listReference(ITransactedObject object, String referenceName, Set<T> initialSet);

	/**
	 * Same as the other listReference but supplying a List instead of a Set
	 * (with a defined sequence)
	 * 
	 * @param object
	 * @param referenceName
	 * @param initialSet
	 * @return
	 */
	<T> List<T> listReference(ITransactedObject object, String referenceName, List<T> initialSet);

	/**
	 * 
	 * @param object
	 * @param version
	 * @pre object!=null && object.getObjectId()!=null
	 * @pre !isInvalid() && !isCleanModus()
	 */
	void setVersionUpdate(ITransactedObject object, String version);

	/**
	 * @param object
	 * @param defaultVersion
	 * @return
	 */
	// String getVersionUpdate(ITransactedObject object, String defaultVersion);
	/**
	 * Updates the ObjectId for an registered Object in the ObjectTransaction.
	 * The application must immediatelly afterwards also update the ObjectId in
	 * the ITransactedObject. All recorded changes are transferred from the old
	 * ObjectId to the new ObjectId.
	 * 
	 * @param oldObjectId
	 * @param newObjectId
	 * @pre oldObjectId!=null && newObjectId!=null
	 * @pre oldObjectId!=newObjectId
	 */
	void setObjectIdUpdate(IObjectId oldObjectId, IObjectId newObjectId);

	/**
	 * Commits the ObjectTransaction. Cannot be done if it is not a
	 * SubTransaction. A so-called RootTransaction cannot be committed but has
	 * to be passed to the Persistence Layer which will read out all the
	 * changes. A SubObjectTransaction commits its value to the next higer level
	 * of ObjectTransaction. A ObjectTransaction becomes invalid and useless
	 * once committed. The next higher ObjectTransaction becomes the active
	 * ObjectTransaction. Only the active ObjectTransaction records changes.
	 * 
	 * @pre (!isRootTransaction())
	 */
	void commit();

	/**
	 * This method works in similar to commit with some differences. This method
	 * can only be called on the rootTransaction. It commits the changes into
	 * the objects where they become "permanent" (cannot be rolled back). After
	 * the method is processed, all objects have the state CLEAN.
	 * 
	 * @pre isRootTransaction()
	 */
	void commitToObjects();

	/**
	 * Rollback the changes in the ObjectTransaction. A ObjectTransaction
	 * rollback basically erases all recorded changes in it. If this is NOT a
	 * RootObjectTransaction, the next higher ObjectTransaction is automatically
	 * activated, and this ObjectTransaction is invalidated and becomes useless.
	 * If it IS the RootObjectTransaction, it remains active and valid.
	 */
	void rollback();

	/**
	 * creates a new objectTransaction where the current transaction becomes the
	 * "parent" of the new created transaction. The new created transaction
	 * becomes the active transaction.
	 * 
	 * @return newly created transaction
	 */
	IObjectTransaction createSubObjectTransaction();

	/**
	 * Find an ITransactedObject by its objectId
	 * 
	 * @param objectId
	 * @return
	 */
	ITransactedObject lookupObjectById(IObjectId objectId);

	/**
	 * Replaces an registered object in the objectTransaction.
	 * 
	 * @param objectId
	 * @param transactedObject
	 * @pre objectId.equals(transactedObject.getObjectId())
	 * @pre transactedObject!=lookupObjectById(oid)
	 */
	void replaceRegisteredObject(IObjectId objectId, ITransactedObject transactedObject);
}