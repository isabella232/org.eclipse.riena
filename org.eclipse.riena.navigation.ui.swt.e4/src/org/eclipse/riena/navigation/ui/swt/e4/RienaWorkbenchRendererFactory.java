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

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.PerspectiveRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.SashRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;

@SuppressWarnings("restriction")
public class RienaWorkbenchRendererFactory extends WorkbenchRendererFactory {

	private RienaWorkbenchRenderer wbRenderer;
	private SashRenderer sashRenderer;
	private ContributedPartRenderer contributedPartRenderer;
	private PerspectiveRenderer perspRenderer;

	@Override
	public AbstractPartRenderer getRenderer(final MUIElement uiElement, final Object parent) {
		if (uiElement instanceof MWindow) {
			if (wbRenderer == null) {
				wbRenderer = new RienaWorkbenchRenderer();
				initRenderer(wbRenderer);
			}
			return wbRenderer;
		} else if (uiElement instanceof MPartSashContainer) {
			if (sashRenderer == null) {
				sashRenderer = new RienaE4Sashrenderer();
				initRenderer(sashRenderer);
			}
			return sashRenderer;
		} else if (uiElement instanceof MPart) {
			if (contributedPartRenderer == null) {
				contributedPartRenderer = new RienaE4PartRenderer();
				initRenderer(contributedPartRenderer);
			}
			return contributedPartRenderer;
		} else if (uiElement instanceof MPerspective) {
			if (perspRenderer == null) {
				perspRenderer = new RienaE4PerspectiveRenderer();
				initRenderer(perspRenderer);
			}
			return perspRenderer;
		}
		return super.getRenderer(uiElement, parent);
	}

	@Override
	protected void initRenderer(final AbstractPartRenderer renderer) {
		super.initRenderer(renderer);
		Wire.instance(renderer).andStart(Activator.getDefault().getContext());
	}
}
