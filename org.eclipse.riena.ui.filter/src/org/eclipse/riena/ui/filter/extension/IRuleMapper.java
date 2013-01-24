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
package org.eclipse.riena.ui.filter.extension;

import org.eclipse.riena.ui.filter.IUIFilterRule;

/**
 * To avoid problems with bundle dependencies (e.g. cycles) the classes of the
 * different rules of the UI Filters are defined with extensions.
 */
public interface IRuleMapper {

	/**
	 * Creates and returns (a new) rule.
	 * 
	 * @return new rule
	 */
	IUIFilterRule getRuleClass();

}
