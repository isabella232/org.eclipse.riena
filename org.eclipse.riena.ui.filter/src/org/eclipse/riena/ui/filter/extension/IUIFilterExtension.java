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
package org.eclipse.riena.ui.filter.extension;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Interface for a UIFiltere extension that defines how to create a filter with
 * a list of rules.
 */
@ExtensionInterface(id = "uiFilters")
public interface IUIFilterExtension {

	/**
	 * Returns the ID of the UI filter.
	 * 
	 * @return ID of the filter
	 */
	String getFilterId();

	/**
	 * Returns the an array of navigation-node IDs. The UI filter will be added
	 * to every node with the matching ID.
	 * 
	 * @return navigation-node IDs
	 */
	@MapName("nodeId")
	IFilterNodeIdExtension[] getNodeIds();

	/**
	 * Returns the array of marker rules for Ridgets.
	 * 
	 * @return marker rules for Ridgets
	 */
	@MapName("ruleMarkerRidget")
	IRuleMarkerRidget[] getRuleMarkerRidgets();

	/**
	 * Returns the array of marker rules for menu items.
	 * 
	 * @return rules for menu itmes
	 */
	@MapName("ruleMarkerMenuItem")
	IRuleMarkerMenuItem[] getRuleMarkerMenuItems();

	/**
	 * Returns the array of marker rules for navigation nodes.
	 * 
	 * @return rules for navigation nodes
	 */
	@MapName("ruleMarkerNavigation")
	IRuleMarkerNavigation[] getRuleMarkerNavigations();

	/**
	 * Returns the array of validator rules for Ridgets.
	 * 
	 * @return validator rules for Ridgets
	 */
	@MapName("ruleValidatorRidget")
	IRuleValidatorRidget[] getRuleValidatorRidgets();

}
