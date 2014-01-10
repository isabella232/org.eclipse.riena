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
package org.eclipse.riena.ui.swt;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;

/**
 * Title bar of a module (view).
 */
public class ModuleTitleBar extends EmbeddedTitleBar {

	/**
	 * @param parent
	 * @param style
	 */
	public ModuleTitleBar(final Composite parent, final int style) {

		super(parent, style);

	}

	@Override
	protected EmbeddedTitlebarRenderer getLnfTitlebarRenderer() {
		final EmbeddedTitlebarRenderer r = (EmbeddedTitlebarRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.MODULE_VIEW_TITLEBAR_RENDERER);
		return r != null ? r : super.getLnfTitlebarRenderer();
	}
}
