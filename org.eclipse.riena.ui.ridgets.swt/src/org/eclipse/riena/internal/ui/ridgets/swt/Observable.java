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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;

/**
 * A factory for creating observables. This factory tries as much as possible to
 * retrieve the value types from value holders and thus allowing to use generic
 * value holders.
 */
public final class Observable {

	private Observable() {
		// utility class
	}

	private static final Logger LOGGER = Log4r.getLogger(Observable.class);

	/**
	 * Get a value observable for the given bean or pojo value holder with the
	 * given property name.
	 * <p>
	 * If the bean or pojo has a generic type the type will be retrieved (better
	 * guessed) from the bean/pojo by inspecting the value.<br>
	 * If there is no value it will look for a property
	 * {@code valuePropertyName + "Type"} and if it exists and if it return type
	 * is {@code Class} it will use its value as the type of the property.
	 * 
	 * @param valueHolder
	 *            the value holder
	 * @param valuePropertyName
	 *            the property name of the value
	 * @return the observable
	 */
	public static IObservableValue forValue(final Object valueHolder, final String valuePropertyName) {
		final Class<?> valueType = getValueType(valueHolder, valuePropertyName);
		if (AbstractSWTRidget.isBean(valueHolder.getClass())) {
			return BeanProperties.value(valueHolder.getClass(), valuePropertyName, valueType).observe(valueHolder);
		} else {
			return PojoProperties.value(valueHolder.getClass(), valuePropertyName, valueType).observe(valueHolder);
		}
	}

	private static Class<?> getValueType(final Object valueHolder, final String valuePropertyName) {
		if (valueHolder.getClass().getTypeParameters().length != 1) {
			return null;
		}
		try {
			final BeanInfo beanInfo = Introspector.getBeanInfo(valueHolder.getClass());
			final PropertyDescriptor valueTypeDescriptor = getPropertyDescriptor(beanInfo, valuePropertyName + "Type"); //$NON-NLS-1$
			if (valueTypeDescriptor != null && valueTypeDescriptor.getPropertyType() == Class.class) {
				return (Class<?>) valueTypeDescriptor.getReadMethod().invoke(valueHolder);
			}
			final PropertyDescriptor valueDescriptor = getPropertyDescriptor(beanInfo, valuePropertyName);
			if (valueDescriptor == null) {
				return null;
			}
			final Object value = valueDescriptor.getReadMethod().invoke(valueHolder);
			if (value != null) {
				return value.getClass();
			}
		} catch (final Exception e) {
			LOGGER.log(LogService.LOG_WARNING, "retrieving type information for value holder" + valueHolder.getClass() //$NON-NLS-1$
					+ " failed", e); //$NON-NLS-1$
		}

		return null;
	}

	private static PropertyDescriptor getPropertyDescriptor(final BeanInfo beanInfo, final String propertyName) {
		final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.getName().equals(propertyName)) {
				return propertyDescriptor;
			}
		}
		return null;
	}

}