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
package org.eclipse.riena.ui.ridgets.uibinding;

/**
 * Helper class to get the ID (binding property) of a UI control used for binding.
 */
public interface IBindingPropertyLocator {

	/**
	 * Returns the ID (binding property) of the given UI control.
	 * 
	 * @param uiControl
	 *            UI control; may be null
	 * @return the ID String if one is available, the empty string if no ID is available, null if uiControl is null or disposed
	 */
	String locateBindingProperty(Object uiControl);

	/**
	 * Returns the complex ID of the given UI control.
	 * <p>
	 * The complex ID is a concatenation of the ID of the UI control and the ID of a parent composite. If the given UI control has no complex parent (
	 * {@code IComplexComponent}) the <i>simple</i> binding ID of the control will be returned.
	 * 
	 * @param uiControl
	 *            UI control; may be null
	 * @return complex or simple ID, the empty string if no ID is available, null if uiControl is null or disposed
	 * @since 5.0
	 */
	String getComplexBindingId(Object uiControl);

}
