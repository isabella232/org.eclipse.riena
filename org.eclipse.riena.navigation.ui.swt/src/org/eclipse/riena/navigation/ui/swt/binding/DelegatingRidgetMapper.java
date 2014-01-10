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
package org.eclipse.riena.navigation.ui.swt.binding;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IMappingCondition;

/**
 *
 */
public class DelegatingRidgetMapper implements IControlRidgetMapper<Object> {

	private final SwtControlRidgetMapper delegate;

	private final Map<Class<? extends Object>, Class<? extends IRidget>> mappings;

	public DelegatingRidgetMapper(final SwtControlRidgetMapper delegate) {
		this.delegate = delegate;
		this.mappings = new HashMap<Class<? extends Object>, Class<? extends IRidget>>();
	}

	public void addMapping(final Class<? extends Object> controlClazz, final Class<? extends IRidget> ridgetClazz) {
		if (Widget.class.isAssignableFrom(controlClazz)) {
			delegate.addMapping(controlClazz, ridgetClazz);
			return;
		}
		mappings.put(controlClazz, ridgetClazz);

	}

	public void addMapping(final Class<? extends Object> controlClazz, final Class<? extends IRidget> ridgetClazz,
			final IMappingCondition condition) {
		if (Widget.class.isAssignableFrom(controlClazz)) {
			delegate.addMapping(controlClazz, ridgetClazz, condition);
			return;
		}
		mappings.put(controlClazz, ridgetClazz); // do we need condition here?
	}

	public Class<? extends IRidget> getRidgetClass(final Class<? extends Object> controlClazz) {
		if (Widget.class.isAssignableFrom(controlClazz)) {
			return delegate.getRidgetClass(controlClazz);
		}
		return mappings.get(controlClazz);

	}

	public Class<? extends IRidget> getRidgetClass(final Object control) {
		if (Widget.class.isAssignableFrom(control.getClass())) {
			return delegate.getRidgetClass(control);
		}

		return getRidgetClass(control.getClass());
	}

}
