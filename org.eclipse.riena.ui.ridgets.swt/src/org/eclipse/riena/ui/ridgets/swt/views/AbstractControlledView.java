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
package org.eclipse.riena.ui.ridgets.swt.views;

import java.beans.Beans;

import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;

/**
 * View bound to a controller.
 */
public abstract class AbstractControlledView<C extends IController> {
	private final AbstractViewBindingDelegate binding;
	private C controller;

	public C getController() {
		return controller;
	}

	protected void setController(final C controller) {

		this.controller = controller;
	}

	public AbstractControlledView() {
		super();
		binding = createBinding();
	}

	public void initialize(final C controller) {
		if (!Beans.isDesignTime()) {
			binding.injectRidgets(controller);
		}
	}

	/**
	 * Creates a delegate for the binding of view and controller.
	 * 
	 * @return delegate for binding
	 */
	protected AbstractViewBindingDelegate createBinding() {
		return new DefaultSwtBindingDelegate();
	}

	public void bind(final C controller) {

		if (getController() != null) {
			unbind(getController());
		}
		if (controller != null) {
			setController(controller);

			if (!Beans.isDesignTime()) {
				binding.bind(controller);
				controller.afterBind();
			}
		}
	}

	public void unbind(final C controller) {
		if (!Beans.isDesignTime()) {
			binding.unbind(controller);
		}
		//		setController(null);
	}

	/**
	 * Adds the given control to the list of the controls that will be bound.
	 * 
	 * @param uiControl
	 *            the control to bind.
	 * @param propertyName
	 *            the name for binding the control.
	 */
	protected void addUIControl(final Object uiControl, final String propertyName) {
		binding.addUIControl(uiControl, propertyName);
	}
}
