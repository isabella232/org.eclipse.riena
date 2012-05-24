/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.e4.application;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;

/**
 *
 */
@SuppressWarnings("restriction")
final class SubModuleActivationListener extends SubModuleNodeListener {

	private final MApplication wbApp;
	private final Map<ISubModuleNode, MPart> node2Part = new HashMap<ISubModuleNode, MPart>();
	private final E4ViewHelper subModuleViewHelper;

	/**
	 * @param wbApp
	 */
	SubModuleActivationListener(final MApplication wbApp) {
		this.wbApp = wbApp;
		this.subModuleViewHelper = new E4ViewHelper();
	}

	@Override
	public void deactivated(final ISubModuleNode source) {
		final MPart part = node2Part.get(source);
		if (null != part) {
			part.setVisible(false);
		}
	}

	@Override
	public void activated(final ISubModuleNode source) {
		MPart part = node2Part.get(source);
		if (null == part) {
			part = subModuleViewHelper.createSubModulePart(wbApp, source);
			node2Part.put(source, part);
		}
		part.setVisible(true);
	}

}