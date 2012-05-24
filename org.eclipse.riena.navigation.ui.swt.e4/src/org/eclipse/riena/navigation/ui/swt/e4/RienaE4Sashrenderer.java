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
package org.eclipse.riena.navigation.ui.swt.e4;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.renderers.swt.SashRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

/**
 *
 */
final class RienaE4Sashrenderer extends SashRenderer {
	@Override
	public Object createWidget(final MUIElement element, final Object parent) {
		final Composite composite = (Composite) super.createWidget(element, parent);
		composite.setLayout(new FormLayout());
		composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		return composite;
	}
}