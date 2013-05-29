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

import java.util.Map;

/**
 * Handles the id-to-ridget mapping in ridget containers.
 * 
 * @since 5.0
 */
public interface IRidgetResolver {

	/**
	 * Find a {@link IRidget} in the given container by its identifier.
	 * 
	 * @param id
	 *            the ridget identifier
	 * @param ridgets
	 *            the ridgets map
	 * @return the resolved ridget or <code>null</code>
	 */
	IRidget getRidget(String id, Map<String, IRidget> ridgets);

	/**
	 * Adds the given {@link IRidget} to the {@link IRidgetContainer} under the specified identifier.
	 * 
	 * @param id
	 *            the ridget identifier to use
	 * @param ridget
	 *            the {@link IRidget} to add
	 * @param toContainer
	 *            the {@link IRidgetContainer}
	 * @param ridgets
	 *            the ridgets map
	 */
	<R extends IRidget> IRidget addRidget(String id, R ridget, IRidgetContainer toContainer, Map<String, IRidget> ridgets);

	/**
	 * Removes the given ridget.
	 * 
	 * @param ridgets
	 *            the ridgets list
	 * @return the removed ridget or <code>null</code>
	 */
	IRidget removeRidget(String id, Map<String, IRidget> ridgets);

}