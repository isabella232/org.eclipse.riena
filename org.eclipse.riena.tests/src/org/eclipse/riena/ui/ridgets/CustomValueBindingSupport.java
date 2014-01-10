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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 * Custom binding support for tests.
 */
public class CustomValueBindingSupport extends ValueBindingSupport {

	/**
	 * @param target
	 */
	public CustomValueBindingSupport(final IObservableValue target) {
		super(target);
	}

	/**
	 * @param target
	 * @param model
	 */
	public CustomValueBindingSupport(final IObservableValue target, final IObservableValue model) {
		super(target, model);
	}
}