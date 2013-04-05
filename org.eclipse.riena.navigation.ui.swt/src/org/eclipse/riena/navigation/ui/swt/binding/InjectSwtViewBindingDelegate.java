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
package org.eclipse.riena.navigation.ui.swt.binding;

import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.InjectBindingManager;

/**
 *
 */
public class InjectSwtViewBindingDelegate extends DefaultSwtBindingDelegate {

	/**
	 * @param propertyStrategy
	 * @param mapper
	 */
	public InjectSwtViewBindingDelegate() {
		super();
	}

	public InjectSwtViewBindingDelegate(final IControlRidgetMapper<Object> ridgetMapper) {
		super(ridgetMapper);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate#createBindingManager(org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator,
	 *      org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper)
	 */
	@Override
	protected IBindingManager createBindingManager(final IBindingPropertyLocator propertyStrategy,
			final IControlRidgetMapper<Object> mapper) {
		return new InjectBindingManager(propertyStrategy, mapper);
	}

}
