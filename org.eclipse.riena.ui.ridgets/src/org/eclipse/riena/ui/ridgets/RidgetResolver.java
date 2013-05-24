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

import org.eclipse.riena.core.util.StringUtils;

/**
 * Identifies a ridget by a given identifier (id). If the ridget is inside a {@link IRidgetContainer}, then its id has the structure "
 * <tt>containerId.childId</tt>"
 * 
 * @since 5.0
 */
public class RidgetResolver implements IRidgetResolver {

	public IRidget getRidget(final String id, final Map<String, IRidget> ridgets) {
		IRidget result = ridgets.get(id);
		if (result == null && id.indexOf('.') != -1) {
			final IRidgetContainer parent = getContainer(id, ridgets);
			if (parent != null) {
				final String childId = getChildId(id);
				result = parent.getRidget(childId);
			}
		}
		return result;
	}

	public <R extends IRidget> IRidget addRidget(final String id, final R ridget, final IRidgetContainer toContainer, final Map<String, IRidget> ridgets) {
		final IRidget oldRidget = getRidget(id, ridgets);

		final String childId = getChildId(id);
		if (childId.equals(id)) {
			toContainer.addRidget(id, ridget);
		} else {
			final IRidgetContainer container = getContainer(id, ridgets);
			if (container != null) {
				container.addRidget(childId, ridget);
			} else {
				// TODO
				toContainer.addRidget(id, ridget);
			}
		}

		return oldRidget;
	}

	private static String getChildId(final String id) {
		if (StringUtils.isEmpty(id)) {
			return id;
		}
		if (id.indexOf('.') != -1) {
			return id.substring(id.lastIndexOf('.') + 1);
		}
		return id;
	}

	private static IRidgetContainer getContainer(final String id, final Map<String, IRidget> ridgets) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		if (id.indexOf('.') != -1) {
			final String parentId = id.substring(0, id.lastIndexOf('.'));
			final IRidget parent = ridgets.get(parentId);
			if (parent instanceof IRidgetContainer) {
				return ((IRidgetContainer) parent);
			}
		}

		return null;
	}

}
