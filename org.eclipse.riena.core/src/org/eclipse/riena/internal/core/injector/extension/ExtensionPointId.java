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
package org.eclipse.riena.internal.core.injector.extension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.StringUtils;

/**
 * A extension point is in general a simple string. However, the extension
 * injector supports multiple ´compatible´ extension point id's for a single
 * ´update´ method. These id's will be treated as if they were one!.
 * <p>
 * Extension points are called ´compatible´ if the same
 * {@code ExtensionInterface} can be used for both of them.
 * <p>
 * The objective here is to support a smooth and simple transition from
 * &#64;deprecated extension point id's to the new one.
 * 
 * @since 1.2
 */
public class ExtensionPointId {

	private final String rawId;
	private List<String> normalizedIds;

	private static final String DOT = "."; //$NON-NLS-1$
	private static final String ID = "ID"; //$NON-NLS-1$

	/**
	 * Create an undefined extension point id.
	 */
	public ExtensionPointId() {
		this.rawId = null;
	}

	/**
	 * Create a ´raw´ extension point id.
	 * 
	 * @param id
	 */
	public ExtensionPointId(final String id) {
		this.rawId = id;
	}

	/**
	 * Normalize the extension point id.
	 * <p>
	 * This means:
	 * <ul>
	 * <li>splitting the ´raw´ extension point id separated by commas into
	 * separate id's</li>
	 * <li>retrieve id's if not specified from the {@code ExtensionInterface},
	 * i.e. from the annotation or by convention from a {@code String} field
	 * named {@code ID}.
	 * <li>transform simple id's to fully qualified id's</li>
	 * </ul>
	 * 
	 * @param extensionInterface
	 */
	public void normalize(final Class<?> extensionInterface) {
		if (normalizedIds != null) {
			return;
		}
		normalizedIds = getNormalizedAndFullyQualifiedExtensionPointIds(extensionInterface);
	}

	/**
	 * Allows to iterate thru the compatible and normalized id's.
	 * <p>
	 * This requires that {@code normalize()} has been called before.
	 * 
	 * @return
	 */
	public Iterable<String> compatibleIds() {
		if (normalizedIds == null) {
			throw new IllegalStateException("ExceptionPointId has not been normalized"); //$NON-NLS-1$
		}
		return normalizedIds;
	}

	/**
	 * Get the normalized and fully qualified extension point ids.
	 * 
	 * @param extensionInterface
	 * @return
	 */
	private List<String> getNormalizedAndFullyQualifiedExtensionPointIds(final Class<?> extensionInterface) {
		final List<String> ids = split(getRawExtensionPointId(rawId, extensionInterface));
		final List<String> result = new ArrayList<String>(ids.size());
		for (final String id : ids) {
			if (id.contains(DOT)) {
				// already a FQ id
				result.add(id);
				continue;
			}
			final Bundle bundle = FrameworkUtil.getBundle(extensionInterface);
			if (bundle != null) {
				result.add(bundle.getSymbolicName() + DOT + id);
				continue;
			}
			// That might fail later!?
			result.add(id);
		}
		return result;
	}

	/**
	 * Split the comma ´,´ separated string.
	 * <p>
	 * That could also be made wit {@code String.split()} but this
	 * implementation avoids creating a regExp and because of that it is a
	 * little bit faster.
	 * 
	 * @param rawId
	 * @return
	 */
	private List<String> split(final String rawId) {
		final List<String> result = new ArrayList<String>();
		int fromIndex = 0;
		int i;
		while ((i = rawId.indexOf(',', fromIndex)) > 0) {
			result.add(rawId.substring(fromIndex, i));
			fromIndex = i + 1;
		}
		result.add(rawId.substring(fromIndex));

		return result;
	}

	/**
	 * Since the extension point can be given via different ways, will retrieve
	 * it here.
	 * 
	 * @param extensionInterface
	 * @param id
	 */
	private String getRawExtensionPointId(final String id, final Class<?> extensionInterface) {
		if (StringUtils.isGiven(id)) {
			return id;
		}
		final ExtensionInterface extensionInterfaceAnnotation = extensionInterface
				.getAnnotation(ExtensionInterface.class);
		if (extensionInterfaceAnnotation != null && StringUtils.isGiven(extensionInterfaceAnnotation.id())) {
			return extensionInterfaceAnnotation.id();
		}
		try {
			final Field idField = extensionInterface.getField(ID);
			final Object value = idField.get(null);
			if (value instanceof String) {
				return (String) value;
			}
		} catch (final Exception e) {
			Nop.reason("Fall throuh!"); //$NON-NLS-1$
		}
		throw new IllegalStateException(
				"It was not possible to retrieve an extension point id from extension interface " + extensionInterface); //$NON-NLS-1$
	}

	@Override
	public String toString() {
		return "ExtensionPointId [normalizedIds=" + normalizedIds + ", rawId=" + rawId + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
