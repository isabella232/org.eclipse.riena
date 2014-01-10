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

package org.eclipse.riena.ui.swt.facades;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Implements {@link GCFacade} for RCP.
 */
public class GCFacadeRCP extends GCFacade {

	@Override
	public int getAdvanceWidth(final GC gc, final char ch) {
		return gc.getAdvanceWidth(ch);
	}

	@Override
	public void setAdvanced(final GC gc, final boolean isEnabled) {
		gc.setAdvanced(isEnabled);
	}

	@Override
	public void setAntialias(final GC gc, final int option) {
		gc.setAntialias(option);
	}

	@Override
	public void setLineDash(final GC gc, final int[] dashes) {
		gc.setLineDash(dashes);
	}

	@Override
	public GC createGCFromImage(final Image img) {
		return new GC(img);
	}

	@Override
	public Image createImage(final Display display, final int width, final int height) {
		return new Image(display, width, height);
	}

}
