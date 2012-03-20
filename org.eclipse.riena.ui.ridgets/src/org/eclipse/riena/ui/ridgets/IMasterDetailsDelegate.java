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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.ui.core.marker.MandatoryMarker;

/**
 * Delegate for {@link IMasterDetailsRidget}, responsible for driving the
 * details area by:
 * <ul>
 * <li>configuring the ridgets for the details area</li>
 * <li>creating object instances for the master table</li>
 * <li>creating object instances for the details area</li>
 * <li>updating the details area from a value from the master table</li>
 * <li>updating a value in the master table from the details area</li>
 * <li>deciding if data in the details area has been changed</li>
 * <li>deciding if data in the details area is valid</li>
 * <li>optionally &ndash; participating at life-cycle events, such as selection,
 * creation, apply and removal</li>
 * </ul>
 * <p>
 * It is recommended to extend AbstractMasterDetailsDelegate instead of
 * implementing the interface yourself.
 * <p>
 * Developers using a {@link IMasterDetailsRidget} must introduce an appropriate
 * implementation of this interface to the ridget, by invoking
 * {@link IMasterDetailsRidget#setDelegate(IMasterDetailsDelegate)}.
 * <p>
 * 
 * @see AbstractMasterDetailsDelegate
 * */
public interface IMasterDetailsDelegate {

	/**
	 * This method is called once, after all ridgets are injected.
	 * <p>
	 * Implementors must use this method to bind the ridgets from the details
	 * area to the appropriate data. The recommended approach is to create an
	 * instance of the working copy object and bind that instance to the
	 * ridgets. The bound instance should remain the same over the lifetime of
	 * the delegate (see {@link #getWorkingCopy()}). It should be updated as
	 * necessary (see {@link #updateDetails(IRidgetContainer)}).
	 * 
	 * @param container
	 *            an IRidgetContainer container that holds the ridgets available
	 *            to the delegate. Invoke {@code container#getRidget(String id)}
	 *            to obtain a reference to a ridget with that id. Never null.
	 */
	void configureRidgets(IRidgetContainer container);

	/**
	 * Create a 'master entry' instance. The object represents the model driving
	 * the master area (i.e. a row entry in the table).
	 * <p>
	 * The 'master entry' is always an instance of the '{@code rowClass'}
	 * specified in the {@code bindToModel(...)} method of the corresponding
	 * {@link IMasterDetailsRidget}. Note that the copyXXX() methods in this
	 * interface must be able to work with the type returned by this method.
	 * 
	 * @return an Object; never null.
	 * @see IMasterDetailsRidget#bindToModel(org.eclipse.core.databinding.observable.list.IObservableList,
	 *      Class, String[], String[])
	 * @see IMasterDetailsRidget#bindToModel(Object, String, Class, String[],
	 *      String[])
	 * @since 3.0
	 */
	Object createMasterEntry();

	/**
	 * Creates a 'working copy' instance. The object represents the model
	 * driving the details area.
	 * <p>
	 * The 'working copy' any type but is typically an instance of the
	 * 'rowClass' specified in the {@code bindToModel(...) } method of the
	 * corresponding {@link IMasterDetailsRidget}. Note that the copyXXX()
	 * methods in this interface must be able to work with the type returned by
	 * this method.
	 * 
	 * @return an Object; never null.
	 * @see IMasterDetailsRidget#bindToModel(org.eclipse.core.databinding.observable.list.IObservableList,
	 *      Class, String[], String[])
	 * @see IMasterDetailsRidget#bindToModel(Object, String, Class, String[],
	 *      String[])
	 */
	Object createWorkingCopy();

	/**
	 * Copies the content of the source object into the target object.
	 * Implementors only need to copy attributes that can be modified in the
	 * details area.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>if the same type is returned by {@link #createMasterEntry()} and
	 * {@link #createWorkingCopy()}: implement {@link #copyBean(Object, Object)}
	 * and ignore the other copy-methods</li>
	 * <li>if different types are returned by {@link #createMasterEntry()} and
	 * {@link #createWorkingCopy()}: implement
	 * {@link #copyMasterEntry(Object, Object)} and
	 * {@link #copyWorkingCopy(Object, Object)} and ignore
	 * {@link #copyBean(Object, Object)}</li>
	 * </ul>
	 * 
	 * @param source
	 *            The source object; never null
	 * @param target
	 *            The target object; never null
	 * @return the target object; never null.
	 */
	Object copyBean(Object source, Object target);

	/**
	 * Copies the content of a 'master entry' into a 'working copy'.
	 * <p>
	 * The 'master entry' is an instance of the '{@code rowClass'} specified in
	 * {@code bindToModel(...)} method of the corresponding
	 * {@link IMasterDetailsRidget}. The 'working copy' is the type used to
	 * store the content of the details area.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>if the same type is returned by {@link #createMasterEntry()} and
	 * {@link #createWorkingCopy()}: implement {@link #copyBean(Object, Object)}
	 * and ignore the other copy-methods</li>
	 * <li>if different types are returned by {@link #createMasterEntry()} and
	 * {@link #createWorkingCopy()}: implement
	 * {@link #copyMasterEntry(Object, Object)} and
	 * {@link #copyWorkingCopy(Object, Object)} and ignore
	 * {@link #copyBean(Object, Object)}</li>
	 * </ul>
	 * 
	 * @param masterEntry
	 *            a source object of the type created by
	 *            {@link #createMasterEntry()}; never null.
	 * @param workingCopy
	 *            a target object of the type created by
	 *            {@link #createWorkingCopy()}; never null.
	 * @since 3.0
	 */
	Object copyMasterEntry(Object masterEntry, Object workingCopy);

	/**
	 * Copies the content of a 'working copy' into a 'master entry'.
	 * <p>
	 * The 'master entry' is an instance of the '{@code rowClass'} specified in
	 * {@code bindToModel(...)} method of the corresponding
	 * {@link IMasterDetailsRidget}. The 'working copy' is the type used to
	 * store the content of the details area.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>if the same type is returned by {@link #createMasterEntry()} and
	 * {@link #createWorkingCopy()}: implement {@link #copyBean(Object, Object)}
	 * and ignore the other copy-methods</li>
	 * <li>if different types are returned by {@link #createMasterEntry()} and
	 * {@link #createWorkingCopy()}: implement
	 * {@link #copyMasterEntry(Object, Object)} and
	 * {@link #copyWorkingCopy(Object, Object)} and ignore
	 * {@link #copyBean(Object, Object)}</li>
	 * </ul>
	 * 
	 * @param workingCopy
	 *            a source object of the type created by
	 *            {@link #createWorkingCopy()}; never null.
	 * @param masterEntry
	 *            a target object of the type created by
	 *            {@link #createMasterEntry()}; never null.
	 * @since 3.0
	 */
	Object copyWorkingCopy(Object workingCopy, Object masterEntry);

	/**
	 * Returns the 'working copy' object. This object represents the model
	 * driving the details area.
	 * <p>
	 * It is recommended that the instance returned by this method stays the
	 * same over the lifetime of the delegate (i.e. always return the same
	 * instance).
	 * 
	 * @return an Object; never null
	 * @see IMasterDetailsRidget#bindToModel(org.eclipse.core.databinding.observable.list.IObservableList,
	 *      Class, String[], String[])
	 * @see IMasterDetailsRidget#bindToModel(Object, String, Class, String[],
	 *      String[])
	 */
	Object getWorkingCopy();

	/**
	 * Returns true, if there there is a difference between the two objects,
	 * with respect to the data that is editable in the details area. Returns
	 * false is both objects are the equal, with respect to that data.
	 * <p>
	 * The return value determines the enablement state of the 'update' button
	 * in the {@link IMasterDetailsRidget}.
	 * <p>
	 * The minimal recommended implementation of this method is:
	 * 
	 * <pre>
	 * public boolean isChanged(Object source, Object target) {
	 * 	return true;
	 * }
	 * 
	 * </pre>
	 * 
	 * @param source
	 *            the source object; never null. Holds the original values
	 * @param target
	 *            the target object; never null. Holds the latest values from
	 *            the details area.
	 * @return true if there is a difference between {@code source} and
	 *         {@code target}; otherwise false
	 */
	boolean isChanged(Object source, Object target);

	/**
	 * Returns null if the current item can be removed.
	 * <p>
	 * If removal is not possible, a non-null explaining the reason is returned
	 * and shown to the user.
	 * 
	 * @param item
	 *            the item behind the master row. Can never be null.
	 * @return null if the item is removable; otherwise an error message when
	 *         removal is not possible.
	 * 
	 * @since 2.0
	 */
	String isRemovable(Object item);

	/**
	 * Returns null, if the data in details area (i.e. working copy) is valid.
	 * This will be invoked before apply writes the data back from the details
	 * area into the model.
	 * <p>
	 * The minimal recommended implementation of this method is:
	 * 
	 * <pre>
	 * public String isValid() {
	 * 	return null;
	 * }
	 * </pre>
	 * 
	 * @param container
	 *            an IRidgetContainer container that holds the ridgets available
	 *            to the delegate. Invoke {@code container#getRidget(String id)}
	 *            to obtain a reference to a ridget with that id. Never null.
	 * @return null if the data is valid; an error message if the data is
	 *         invalid
	 */
	String isValid(IRidgetContainer container);

	/**
	 * When this method returns true, an enabled mandatory marker is added to
	 * all markable ridgets in the details area. The marker is removed
	 * automatically when an entry is changed or the 'Apply' action is invoked.
	 * <p>
	 * The method is evaluated before a new entry is made editable. This happens
	 * when the user invoked the 'New' action or when an entry is suggested via
	 * API.
	 * 
	 * @param mdRidget
	 *            the {@link IMasterDetailsRidget} associated with this
	 *            delegate; never null.
	 * @return true to hide the global mandatory marker, false to show it
	 * 
	 * @see MandatoryMarker
	 * @since 3.0
	 */
	boolean isValidMaster(final IMasterDetailsRidget mdRidget);

	/**
	 * Updates all details from the model.
	 * <p>
	 * Typically this is called when the selection in the master/details ridget
	 * has changed. The {@link IMasterDetailsRidget} will first update the
	 * working copy by invoking {@code copyBean(selection, getWorkingCopy())}
	 * and then invoke this method.
	 * <p>
	 * The minimal recommended implementation of this method is:
	 * 
	 * <pre>
	 * public void updateDetails(IRidgetContainer container) {
	 * 	for (IRidget ridget : container.getRidgets()) {
	 * 		ridget.updateFromModel();
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param container
	 *            an IRidgetContainer container that holds the ridgets available
	 *            to the delegate. Invoke {@code container#getRidget(String id)}
	 *            to obtain a reference to a ridget with that id. Never null.
	 */
	void updateDetails(IRidgetContainer container);

	/**
	 * This method the <b>first</b> method called during an apply operation. It
	 * is invoked <b>before</b> any changes are made.
	 * 
	 * @param selection
	 *            the selected item behind the master row. Can never be null.
	 * 
	 * @since 3.0
	 */
	void prepareItemApplied(Object selection);

	/**
	 * This is the <b>first</b> method called when a row in the master table is
	 * selected. It will be called before any other methods of the delegate. The
	 * given <code>newSelection</code> is the selection.
	 * 
	 * @param newSelection
	 *            the selected item behind the master row. <b>May be null</b>,
	 *            if nothing becomes selected
	 */
	void prepareItemSelected(Object newSelection);

	/**
	 * This is the <b>last</b> method called when a row in the master table
	 * becomes selected. It will be called after any other methods of the
	 * delegate. The given <code>newSelection</code> is the selection.
	 * 
	 * @param newSelection
	 *            the selected item behind the master row. <b>May be null</b>,
	 *            if nothing becomes selected
	 * 
	 * @since 2.0
	 */
	void itemSelected(Object newSelection);

	/**
	 * This method is called when a new item is created.
	 * 
	 * @param newItem
	 *            the added item behind the master row. Can never be null.
	 * 
	 * @since 2.0
	 */
	void itemCreated(Object newItem);

	/**
	 * This method is called when an item is removed from the master table.
	 * 
	 * @param oldItem
	 *            the removed item behind the master row. Can never be null.
	 * 
	 * @since 2.0
	 */
	void itemRemoved(Object oldItem);

	/**
	 * This method is called after a new master record changes are applied to an
	 * item.
	 * 
	 * @param changedItem
	 *            the changed item behind the master row. Can never be null.
	 * 
	 * @since 2.0
	 */
	void itemApplied(Object changedItem);

	/**
	 * Hook to provide safe control over the state of the
	 * {@link IMasterDetailsRidget} inner action ridgets (create, remove, apply)
	 * 
	 * @param actionRidgetFacade
	 *            facade for convenient access to {@link IActionRidget}s of the
	 *            {@link IMasterDetailsRidget}
	 * @param selection
	 *            the selected item behind the master row. <b>May be null</b>,
	 *            if nothing is selected
	 * @since 3.0
	 * 
	 */
	void updateMasterDetailsActionRidgets(IMasterDetailsActionRidgetFacade actionRidgetFacade, Object selection);

}
