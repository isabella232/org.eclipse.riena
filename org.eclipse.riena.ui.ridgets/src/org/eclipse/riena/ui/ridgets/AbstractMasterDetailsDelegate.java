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
package org.eclipse.riena.ui.ridgets;

/**
 * Default implementation of the {@link IMasterDetailsDelegate}.
 * <p>
 * It is recommended that subclasses extend this class instead of implementing
 * {@link IMasterDetailsDelegate} directly.
 * <p>
 * Subclasses still need to implement the abstract methods listed below. These
 * are model specific, so it is not possible to provide a usable default
 * implementation.
 * <ul>
 * <li>{@link #configureRidgets(IRidgetContainer)}</li>
 * <li>{@link #createWorkingCopy()}</li>
 * <li>{@link #copyBean(Object, Object)}</li>
 * <li>{@link #getWorkingCopy()}</li>
 * </ul>
 * Typically will also want to override {@link #isChanged(Object, Object)}, to
 * fine tune the "dirty" state of the details area.
 * <p>
 * 
 * @since 2.0
 */
public abstract class AbstractMasterDetailsDelegate implements IMasterDetailsDelegate {

	/**
	 * {@inheritDoc}
	 * <p>
	 * The default implementation for this method uses
	 * {@link #copyBean(Object, Object)}. Subclasses should override this method
	 * and {@link #copyWorkingCopy(Object, Object)}, if they need to use
	 * separate types in the master table and the details area.
	 * 
	 * @since 3.0
	 */
	public Object copyMasterEntry(final Object masterEntry, final Object workingCopy) {
		return copyBean(masterEntry, workingCopy);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The default implementation for this method uses
	 * {@link #copyBean(Object, Object)}. Subclasses should override this method
	 * and {@link #copyMasterEntry(Object, Object)}, if they need to use
	 * separate types in the master table and the details area.
	 * 
	 * @since 3.0
	 */
	public Object copyWorkingCopy(final Object workingCopy, final Object masterEntry) {
		return copyBean(workingCopy, masterEntry);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The default implementation uses {@link #createWorkingCopy()}. Subclasses
	 * may override this method, if they need to use separate types in the
	 * master table and the details area.
	 * 
	 * @since 3.0
	 */
	public Object createMasterEntry() {
		return createWorkingCopy();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 * <p>
	 * <b>Note:</b> it is recommended to provide a tailored implementation for
	 * this method. This will fine tune the "dirty" state of the details area.
	 */
	public boolean isChanged(final Object source, final Object target) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public String isRemovable(final Object item) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public String isValid(final IRidgetContainer container) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 * 
	 * @since 3.0
	 */
	public boolean isValidMaster(final IMasterDetailsRidget mdRidget) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void itemApplied(final Object changedItem) {
		// empty
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void itemCreated(final Object newItem) {
		// empty
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void itemRemoved(final Object oldItem) {
		// empty
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void itemSelected(final Object newSelection) {
		// empty
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 * 
	 * @since 3.0
	 */
	public void prepareItemApplied(final Object selection) {
		// empty
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void prepareItemSelected(final Object newSelection) {
		// empty
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override &ndash;
	 * though it should not be necessary in most cases.
	 */
	public void updateDetails(final IRidgetContainer container) {
		for (final IRidget ridget : container.getRidgets()) {
			ridget.updateFromModel();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 * 
	 * @since 3.0
	 */
	public void updateMasterDetailsActionRidgets(final IMasterDetailsActionRidgetFacade actionRidgetFacade,
			final Object selection) {
	}

}
