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
 * Facade for the {@link GC} class.
 * 
 * @since 2.0
 */
public abstract class GCFacade {

	private static final GCFacade INSTANCE = FacadeFactory.newFacade(GCFacade.class);

	/**
	 * The applicable implementation of this class.
	 */
	public static final GCFacade getDefault() {
		return INSTANCE;
	}

	/**
	 * Creates an graphics context from a given image.
	 * <p>
	 * 
	 * The RAP implementation creates a graphics context by leveraging
	 * Display.getCurrent().
	 * 
	 * @param img
	 * @return
	 * @since 3.0
	 */
	public abstract GC createGCFromImage(Image img);

	/**
	 * Creates an image width a given width and heigt.
	 * <p>
	 * 
	 * The RAP implementation doesn't work properly and is only used to get rid
	 * of the compile errors.
	 * 
	 * @param display
	 * @param width
	 * @param height
	 * @return
	 * @since 3.0
	 */
	public abstract Image createImage(Display display, int width, int height);

	/**
	 * Compute the horizontal distance, in pixels, the cursor should move after
	 * printing the character in the current font.
	 * <p>
	 * Implementation note: on the RAP-Platform the returned value may be an
	 * approximation.
	 * 
	 * @param gc
	 *            a GC instance; never null
	 * @param ch
	 *            a character
	 * @return an amount in pixels (greater or equal to zero)
	 */
	public abstract int getAdvanceWidth(GC gc, char ch);

	/**
	 * Enable or disable use of the OS's advanced graphics subsystem for
	 * drawing. If advanced graphics are not available, this operation does
	 * nothing.
	 * 
	 * @param gc
	 *            the GC instance, never null
	 * @param isEnabled
	 *            true of false
	 */
	public abstract void setAdvanced(GC gc, boolean isEnabled);

	/**
	 * Enable or disable the user of anti-aliasing for all non-text drawing
	 * operations. This requires advanced graphics support in the OS. If
	 * advanced graphics are not available, this operations does nothing.
	 * 
	 * @param gc
	 *            the GC instance, never null
	 * @param option
	 *            one of the following values: SWT.DEFAULT, SWT.OFF, SWT.ON
	 */
	public abstract void setAntialias(GC gc, int option);

	/**
	 * Sets the line dash style to the given argument. A non-null argument
	 * implies that the line style is set to SWT.LINE_CUSTOM. A null value
	 * implies that the line style is set to SWT.LINE_SOLID.
	 * <p>
	 * The default value for {@code dashes} is null.
	 * 
	 * @param GC
	 *            the GC instance, never null
	 * @param dashes
	 *            the dash style to use when drawing a line. Example:
	 *            <tt>new int[] { 5, 5 }</tt>
	 * @since 3.0
	 */
	public abstract void setLineDash(GC gc, int[] dashes);
}
