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
package org.eclipse.riena.ui.workarea.spi;

import org.eclipse.riena.navigation.INavigationAssembler;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;

/**
 * The IWorkareaDefinitionRegistry registers all {@link IWorkareaDefinition}s.
 * <p>
 * 
 * @see INavigationAssembler
 */
public interface IWorkareaDefinitionRegistry {

	/**
	 * Gets the {@link IWorkareaDefinition} registered with this typeId.
	 * 
	 * @param id
	 *            the typeId or a navigation node
	 * @return The registered workarea definition or null, if the associated
	 *         workarea definition was not found.
	 */
	IWorkareaDefinition getDefinition(Object id);

	/**
	 * Registers the specified workarea definition with the specified typeId.
	 * 
	 * @param id
	 *            The typeId.
	 * @param definition
	 *            A workarea definition.
	 * @return The registered workarea definition or null, if the workarea
	 *         definition could not be registered by this registry.
	 */
	IWorkareaDefinition register(Object id, IWorkareaDefinition definition);
}
