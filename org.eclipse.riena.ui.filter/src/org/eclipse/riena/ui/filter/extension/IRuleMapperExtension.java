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
package org.eclipse.riena.ui.filter.extension;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * To avoid problems with bundle dependencies (e.g. cycles) the classes of the
 * different rules of the UI Filters are defined with extensions.
 */
@ExtensionInterface(id = "ruleMapper")
public interface IRuleMapperExtension {

	/**
	 * Returns the type that maps the rule to add a {@code HiddenMarker} to a
	 * ridget.
	 * 
	 * @return mapper of {@code HiddenMarker} for ridget
	 */
	IRuleMarkerRidgetMapper getRidgetHiddenMarker();

	/**
	 * Returns the type that maps the rule to add a {@code DisabledMarker} to a
	 * ridget.
	 * 
	 * @return mapper of {@code DisabledMarker} for ridget
	 */
	IRuleMarkerRidgetMapper getRidgetDisabledMarker();

	/**
	 * Returns the type that maps the rule to add a {@code OutputMarker} to a
	 * ridget.
	 * 
	 * @return mapper of {@code OutputMarker} for ridget
	 */
	IRuleMarkerRidgetMapper getRidgetOutputMarker();

	/**
	 * Returns the type that maps the rule to add a {@code MandatoryMarker} to a
	 * ridget.
	 * 
	 * @return mapper of {@code MandatoryMarker} for ridget
	 */
	IRuleMarkerRidgetMapper getRidgetMandatoryMarker();

	/**
	 * Returns the type that maps the rule to add a {@code HiddenMarker} to a
	 * menu item.
	 * 
	 * @return mapper of {@code HiddenMarker} for menu item
	 */
	IRuleMarkerRidgetMapper getMenuItemHiddenMarker();

	/**
	 * Returns the type that maps the rule to add a {@code DisabledMarker} to a
	 * menu item.
	 * 
	 * @return mapper of {@code DisabledMarker} for menu item
	 */
	IRuleMarkerRidgetMapper getMenuItemDisabledMarker();

	/**
	 * Returns the type that maps the rule to add a {@code HiddenMarker} to a
	 * navigation node.
	 * 
	 * @return mapper of {@code HiddenMarker} for navigation node
	 */
	IRuleMarkerNavigationMapper getNavigationHiddenMarker();

	/**
	 * Returns the type that maps the rule to add a {@code DisabledMarker} to a
	 * navigation node.
	 * 
	 * @return mapper of {@code DisabledMarker} for navigation node
	 */
	IRuleMarkerNavigationMapper getNavigationDisabledMarker();

	/**
	 * Returns the type that maps the rule to add a validator to a ridget.
	 * 
	 * @return mapper of validator for ridget
	 */
	IRuleValidatorRidgetMapper getRidgetValidator();

}
