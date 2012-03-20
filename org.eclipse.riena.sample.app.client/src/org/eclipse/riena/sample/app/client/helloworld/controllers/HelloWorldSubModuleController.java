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
package org.eclipse.riena.sample.app.client.helloworld.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

public class HelloWorldSubModuleController extends SubModuleController {

	private ILabelRidget labelRidget;
	private final TxtBean bean;

	public HelloWorldSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
		bean = new TxtBean();
		bean.setTxt("Hello World"); //$NON-NLS-1$
		bean.setName(""); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		initLabelRidget();
	}

	/**
	 * Binds and updates the label.
	 */
	private void initLabelRidget() {
		if (labelRidget != null) {
			labelRidget.bindToModel(bean, "txt"); //$NON-NLS-1$
			labelRidget.updateFromModel();
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		labelRidget = getRidget("labelRidget"); //$NON-NLS-1$
	}

	/**
	 * The model of this sub module controller.
	 */
	private static class TxtBean {

		private String txt;
		private String name;

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		@SuppressWarnings("unused")
		public String getTxt() {
			return txt;
		}

		public void setTxt(final String txt) {
			this.txt = txt;
		}

	}

}
