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
package org.eclipse.riena.core.marker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;

/**
 * An abstract implementation of the {@link IMarker IMarker} interface which
 * implements managing the attributes.
 * <p>
 * An abstract marker can be 'unique'. In that case only one instance of the
 * implementing type will be allowed per ridget instance. For example if
 * FooMarker extends AbstractMarker and is 'unique', only one instance of
 * FooMarker will be allowed for per ridget.
 */
public abstract class AbstractMarker implements IMarker {

	private static final String ATTRIBUTE_UNIQUE = "unique"; //$NON-NLS-1$

	private final Map<String, Object> attributeMap = new HashMap<String, Object>(0, 1.0f);
	private final Set<IMarkerAttributeChangeListener> attributeChangeListeners = new HashSet<IMarkerAttributeChangeListener>(
			1, 1.0f);

	private final boolean unique;

	/**
	 * Create a 'unique' marker.
	 */
	public AbstractMarker() {
		this(true);
		setAttribute(ATTRIBUTE_UNIQUE, Boolean.TRUE);
	}

	/**
	 * Create a marker instance.
	 * 
	 * @param unique
	 *            true to create a 'unique' marker; false otherwise.
	 */
	public AbstractMarker(final boolean unique) {
		this.unique = unique;

		if (unique) {
			setAttribute(ATTRIBUTE_UNIQUE, Boolean.TRUE);
		} else {
			setAttribute(ATTRIBUTE_UNIQUE, null);
		}

	}

	/**
	 * Return true if this marker is 'unique'; false otherwise.
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * @see IMarker#getAttribute(String)
	 */
	public Object getAttribute(final String name) {
		Assert.isNotNull(name, "name must not be null"); //$NON-NLS-1$
		return attributeMap.get(name);
	}

	/**
	 * @see IMarker#getAttribute(String, Object)
	 */
	public Object getAttribute(final String name, final Object defaultValue) {
		final Object value = getAttribute(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * @see IMarker#getAttributes()
	 */
	public Map<String, Object> getAttributes() {
		return new HashMap<String, Object>(attributeMap);
	}

	/**
	 * @see IMarker#setAttribute(String, Object)
	 */
	public void setAttribute(final String name, final Object value) {
		Assert.isNotNull(name, "name must not be null"); //$NON-NLS-1$
		final Object oldValue = attributeMap.get(name);
		if ((value != null && !value.equals(oldValue)) || (value == null && oldValue != null)) {
			attributeMap.put(name, value);
			fireAttributesChanged();
		}
	}

	private void fireAttributesChanged() {
		final Iterator<IMarkerAttributeChangeListener> listenerIterator = attributeChangeListeners.iterator();
		while (listenerIterator.hasNext()) {
			final IMarkerAttributeChangeListener listener = listenerIterator.next();
			listener.attributesChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.marker.IMarker#addAttributeChangeListener(org.
	 * eclipse.riena.core.marker.IMarkerAttributeChangeListener)
	 */
	public void addAttributeChangeListener(final IMarkerAttributeChangeListener listener) {
		attributeChangeListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.marker.IMarker#removeAttributeChangeListener(org
	 * .eclipse.riena.core.marker.IMarkerAttributeChangeListener)
	 */
	public void removeAttributeChangeListener(final IMarkerAttributeChangeListener listener) {
		attributeChangeListeners.remove(listener);
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof AbstractMarker)) {
			return false;
		}
		if (!getClass().equals(other.getClass())) {
			return false;
		}

		final AbstractMarker otherMarker = (AbstractMarker) other;
		return getAttributes().equals(otherMarker.getAttributes());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder(this.getClass().getSimpleName());
		buffer.append("[attributes="); //$NON-NLS-1$
		buffer.append(getAttributes());
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	/**
	 * {@inheritDoc} The default priority is {@code IMarker.Priority.NORMAL}.
	 * 
	 * @return {@code IMarker.Priority.NORMAL}
	 * @since 1.2
	 */
	public Priority getPriority() {
		return IMarker.Priority.NORMAL;
	}

}
