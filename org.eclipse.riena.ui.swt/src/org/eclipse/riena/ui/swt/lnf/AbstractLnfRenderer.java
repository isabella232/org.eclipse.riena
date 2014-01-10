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
package org.eclipse.riena.ui.swt.lnf;

import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.marker.Markable;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.swt.facades.GCFacade;

/**
 * Renderer of a widget or a part of a widget.
 */
public abstract class AbstractLnfRenderer implements ILnfRenderer {

	private Collection<? extends IMarker> markers;
	private Rectangle bounds;

	/**
	 * {@inheritDoc}
	 */
	public void paint(final GC gc, final Object value) {
		Assert.isNotNull(gc);
		initGC(gc);
	}

	/**
	 * {@inheritDoc}
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBounds(final int x, final int y, final int width, final int height) {
		setBounds(new Rectangle(x, y, width, height));

	}

	/**
	 * {@inheritDoc}
	 */
	public void setBounds(final Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMarkers(final Collection<? extends IMarker> markers) {
		this.markers = markers;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<? extends IMarker> getMarkers() {
		return markers;
	}

	/**
	 * {@inheritDoc}
	 */
	public <T extends IMarker> Collection<T> getMarkersOfType(final Class<T> type) {
		return Markable.getMarkersOfType(getMarkers(), type);
	}

	protected boolean isEnabled() {
		return getMarkersOfType(DisabledMarker.class).isEmpty();
	}

	/**
	 * Inits the given graphics context.
	 * 
	 * @param gc
	 *            graphics context
	 * @since 1.2
	 */
	protected void initGC(final GC gc) {
		final GCFacade gcFacade = GCFacade.getDefault();
		gcFacade.setAdvanced(gc, true);
		gcFacade.setAntialias(gc, SWT.ON);
	}

}
