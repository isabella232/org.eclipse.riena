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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Renderer of the active sub module
 */
public class SubModuleViewRenderer extends AbstractLnfRenderer {

	/**
	 * Creates an new instance of <code>SubModuleViewRenderer</code> and sets
	 * the renderer of the border and the title bar.
	 */
	public SubModuleViewRenderer() {
		super();
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(final GC gc, final Object value) {

		super.paint(gc, value);

		// border
		getBorderRenderer().setActive(true);
		getBorderRenderer().setBounds(getBounds());
		getBorderRenderer().paint(gc, null);

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		getBorderRenderer().dispose();
	}

	/**
	 * Computes the size of the space inside the outer bounds.
	 * 
	 * @param outerBounds
	 *            outer bounds
	 * @return inner bounds
	 */
	public Rectangle computeInnerBounds(final Rectangle outerBounds) {

		return getBorderRenderer().computeInnerBounds(outerBounds);

	}

	/**
	 * Computes the size of the space outside the inner bounds.
	 * 
	 * @param innerBounds
	 *            inner bounds
	 * @return outer bounds
	 */
	public Rectangle computeOuterBounds(final Rectangle innerBounds) {

		return getBorderRenderer().computeOuterBounds(innerBounds);

	}

	public EmbeddedBorderRenderer getBorderRenderer() {
		return getLnfBorderRenderer();
	}

	private EmbeddedBorderRenderer getLnfBorderRenderer() {

		EmbeddedBorderRenderer renderer = (EmbeddedBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.SUB_MODULE_VIEW_BORDER_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedBorderRenderer();
		}
		return renderer;

	}

}
