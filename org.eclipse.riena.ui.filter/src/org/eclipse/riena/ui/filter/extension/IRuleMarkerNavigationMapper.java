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
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerNavigation;

/**
 * {@inheritDoc}
 * <p>
 * The mapped rule class to mark a navigation node.
 */
@ExtensionInterface
public interface IRuleMarkerNavigationMapper extends IRuleMapper {

	/**
	 * {@inheritDoc}
	 * 
	 * @return rule to add a marker to a navigation node
	 */
	IUIFilterRuleMarkerNavigation getRuleClass();

}
