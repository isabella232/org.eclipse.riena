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
package org.eclipse.riena.ui.filter.impl;

import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerNavigation;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerRidget;
import org.eclipse.riena.ui.filter.IUIFilterRuleValidatorRidget;
import org.eclipse.riena.ui.filter.extension.IRuleMapperExtension;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerNavigationMapper;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerRidgetMapper;
import org.eclipse.riena.ui.filter.extension.IRuleValidatorRidgetMapper;

/**
 * This class provides the correct rule for a given marker type.
 */
public class RulesProvider {

	private IRuleMapperExtension[] ruleMapperExtensions;

	/**
	 * Creates a new instance of {@code RulesProvider}.
	 */
	public RulesProvider() {
	}

	/**
	 * Returns the rule to set a marker for a ridget.
	 * 
	 * @param type
	 *            type of marker (hidden, disabled, output or mandatory)
	 * @return rule or {@code null} if no rule was found.
	 */
	public IUIFilterRuleMarkerRidget getRuleMarkerRidget(final String type) {

		if (type == null) {
			return null;
		}

		for (final IRuleMapperExtension ruleMapperExtension : ruleMapperExtensions) {

			IRuleMarkerRidgetMapper mapper = null;
			if (type.equals("hidden")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getRidgetHiddenMarker();
			} else if (type.equals("disabled")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getRidgetDisabledMarker();
			} else if (type.equals("output")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getRidgetOutputMarker();
			} else if (type.equals("mandatory")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getRidgetMandatoryMarker();
			}
			if (mapper != null) {
				final IUIFilterRuleMarkerRidget rule = mapper.getRuleClass();
				if (rule != null) {
					return rule;
				}
			}
		}

		return null;

	}

	/**
	 * Returns the rule to set a marker for a menu-/toolItem.
	 * 
	 * @param type
	 *            type of marker (hidden or disabled)
	 * @return rule or {@code null} if no rule was found.
	 */
	public IUIFilterRuleMarkerRidget getRuleMarkerMenuItem(final String type) {

		if (type == null) {
			return null;
		}

		for (final IRuleMapperExtension ruleMapperExtension : ruleMapperExtensions) {

			IRuleMarkerRidgetMapper mapper = null;
			if (type.equals("hidden")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getMenuItemHiddenMarker();
			} else if (type.equals("disabled")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getMenuItemDisabledMarker();
			}
			if (mapper != null) {
				final IUIFilterRuleMarkerRidget rule = mapper.getRuleClass();
				if (rule != null) {
					return rule;
				}
			}
		}

		return null;

	}

	/**
	 * Returns the rule to set a marker for a navigation node.
	 * 
	 * @param type
	 *            type of marker (hidden or disabled)
	 * @return rule or {@code null} if no rule was found.
	 */
	public IUIFilterRuleMarkerNavigation getRuleMarkerNavigation(final String type) {

		if (type == null) {
			return null;
		}

		for (final IRuleMapperExtension ruleMapperExtension : ruleMapperExtensions) {

			IRuleMarkerNavigationMapper mapper = null;
			if (type.equals("hidden")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getNavigationHiddenMarker();
			} else if (type.equals("disabled")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getNavigationDisabledMarker();
			}
			if (mapper != null) {
				final IUIFilterRuleMarkerNavigation rule = mapper.getRuleClass();
				if (rule != null) {
					return rule;
				}
			}
		}

		return null;

	}

	/**
	 * Returns the rule to add a validator to a ridget.
	 * 
	 * @return rule or {@code null} if no rule was found.
	 */
	public IUIFilterRuleValidatorRidget getRuleValidatorRidget() {

		for (final IRuleMapperExtension ruleMapperExtension : ruleMapperExtensions) {
			final IRuleValidatorRidgetMapper mapper = ruleMapperExtension.getRidgetValidator();
			if (mapper != null) {
				final IUIFilterRuleValidatorRidget rule = mapper.getRuleClass();
				if (rule != null) {
					return rule;
				}
			}
		}

		return null;

	}

	/**
	 * This is the callback method for the extension injector.
	 * 
	 * @param ruleMapperExtensions
	 */
	@InjectExtension
	public void update(final IRuleMapperExtension[] ruleMapperExtensions) {
		this.ruleMapperExtensions = ruleMapperExtensions;
	}

}
