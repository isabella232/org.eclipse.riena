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
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.renderers.swt.PerspectiveRenderer;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.navigation.model.SubApplicationNode;

/**
 *
 */
final class RienaE4PerspectiveRenderer extends PerspectiveRenderer {
	@Override
	public Widget createWidget(final MUIElement element, final Object parent) {
		final Widget widget = super.createWidget(element, parent);
		final SubApplicationNode node = (SubApplicationNode) ((MPerspective) element).getContext().get("node");
		final RienaE4SubApplicationView subApplicationView = new RienaE4SubApplicationView(widget);
		subApplicationView.bind(node);
		return widget;
	}
}