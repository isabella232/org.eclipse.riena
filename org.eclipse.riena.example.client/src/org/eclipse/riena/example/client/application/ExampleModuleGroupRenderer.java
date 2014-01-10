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
package org.eclipse.riena.example.client.application;

import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;

/**
 *
 */
public class ExampleModuleGroupRenderer extends ModuleGroupRenderer {

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer#getModuleGroupPadding()
	 */
	@Override
	public int getModuleGroupPadding() {
		return 0;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer#getModuleModuleGap()
	 */
	@Override
	public int getModuleModuleGap() {
		return 0;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer#getItemWidth()
	 */
	@Override
	public int getItemWidth() {
		return super.getItemWidth() + 25;
	}

}
