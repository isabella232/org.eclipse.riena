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

import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 * Custom binding support provider for tests.
 */
public class CustomValueBindingSupportProvider implements IValueBindingSupportProvider {

	public ValueBindingSupport createInstance(final Class<? extends IRidget> ridgetClass, final IObservableValue ridgetObservable) {
		return new CustomValueBindingSupport(ridgetObservable);
	}

}