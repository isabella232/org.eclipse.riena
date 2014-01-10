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
package org.eclipse.riena.beans.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.runtime.Assert;

/**
 * Utility class for getting and setting properties according to the
 * PropertyDescriptor
 * 
 * @since 1.2
 */
public final class BeanPropertyUtils {

	private BeanPropertyUtils() {
		super();
	}

	/**
	 * Gets a propertyValue as described in the PropertyDescriptor for the
	 * passed bean parameter
	 * 
	 * @param bean
	 * @param descriptor
	 * @return
	 */
	public static Object getPropertyValue(final Object bean, final PropertyDescriptor descriptor) {
		Assert.isNotNull(descriptor, "descriptor cannot be null"); //$NON-NLS-1$
		final Method readMethod = descriptor.getReadMethod();
		if (readMethod == null) {
			throw new UnsupportedOperationException("Property '" + descriptor.getName() + "' has no getter method"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (!readMethod.isAccessible()) {
			readMethod.setAccessible(true);
		}

		// call getter and return the value
		try {
			return readMethod.invoke(bean, new Object[0]);
		} catch (final IllegalArgumentException e) {
			throw new PropertyAccessFailure("unexpected error getting the property", e); //$NON-NLS-1$
		} catch (final IllegalAccessException e) {
			throw new PropertyAccessFailure("unexpected error getting the property", e); //$NON-NLS-1$
		} catch (final InvocationTargetException e) {
			throw new PropertyAccessFailure("unexpected error getting the property", e); //$NON-NLS-1$
		}
	}

	public static void setPropertyValue(final Object bean, final PropertyDescriptor descriptor, final Object value) {
		final Method writeMethod = descriptor.getWriteMethod();
		if (writeMethod == null) {
			throw new UnsupportedOperationException("Property '" + descriptor.getName() + "' has no setter method"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (!writeMethod.isAccessible()) {
			writeMethod.setAccessible(true);
		}

		// call setter
		final Object[] values = new Object[1];
		values[0] = value;
		try {
			writeMethod.invoke(bean, values);
		} catch (final IllegalArgumentException e) {
			throw new PropertyAccessFailure("unexpected error setting the property", e); //$NON-NLS-1$
		} catch (final IllegalAccessException e) {
			throw new PropertyAccessFailure("unexpected error setting the property", e); //$NON-NLS-1$
		} catch (final InvocationTargetException e) {
			throw new PropertyAccessFailure("unexpected error setting the property", e); //$NON-NLS-1$
		}
	}

}
