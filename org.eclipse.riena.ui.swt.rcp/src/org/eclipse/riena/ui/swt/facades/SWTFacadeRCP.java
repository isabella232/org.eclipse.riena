/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt.facades;

import java.util.EventListener;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.navigation.ui.swt.component.SubApplicationSwitcherWidget;
import org.eclipse.riena.navigation.ui.swt.views.ModuleNavigationListener;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.InfoFlyout;
import org.eclipse.riena.ui.swt.facades.internal.CompletionComboRCP;
import org.eclipse.riena.ui.swt.facades.internal.CompletionComboWithImageRCP;
import org.eclipse.riena.ui.swt.facades.internal.DisabledPainter;
import org.eclipse.riena.ui.swt.facades.internal.EmbeddedTitleBarToolTip;
import org.eclipse.riena.ui.swt.facades.internal.GrabCornerListenerWithTracker;
import org.eclipse.riena.ui.swt.facades.internal.InfoFlyoutRCP;
import org.eclipse.riena.ui.swt.facades.internal.MultilineButton;
import org.eclipse.riena.ui.swt.facades.internal.SubApplicationToolTip;
import org.eclipse.riena.ui.swt.facades.internal.SubModuleToolTip;
import org.eclipse.riena.ui.swt.facades.internal.TreeItemEraserAndPainter;

/**
 * Implements {@link SWTFacade} for RCP.
 */
public final class SWTFacadeRCP extends SWTFacade {

	@Override
	public void addEraseItemListener(final Table table, final Listener listener) {
		table.addListener(SWT.EraseItem, listener);
	}

	@Override
	public void addEraseItemListener(final Tree tree, final Listener listener) {
		tree.addListener(SWT.EraseItem, listener);
	}

	@Override
	public void addFilterMouseExit(final Display display, final Listener listener) {
		display.addFilter(SWT.MouseExit, listener);
	}

	@Override
	public void addFilterMouseMove(final Display display, final Listener listener) {
		display.addFilter(SWT.MouseMove, listener);
	}

	@Override
	public void addFilterMouseWheel(final Display display, final Listener listener) {
		display.addFilter(SWT.MouseWheel, listener);
	}

	@Override
	public void addMouseMoveListener(final Control control, final MouseMoveListener listener) {
		if (listener != null) {
			control.addMouseMoveListener(listener);
		}
	}

	@Override
	public void addMouseTrackListener(final Control control, final MouseTrackListener listener) {
		control.addMouseTrackListener(listener);
	}

	@Override
	public void addPaintItemListener(final Tree tree, final Listener listener) {
		tree.addListener(SWT.PaintItem, listener);
	}

	@Override
	public void addPaintListener(final Control control, final EventListener listener) {
		if (listener != null) {
			control.addPaintListener((PaintListener) listener);
		}
	}

	@Override
	public void attachModuleNavigationListener(final Tree tree) {
		new ModuleNavigationListener(tree);
	}

	@Override
	public CompletionCombo createCompletionCombo(final Composite parent, final int style) {
		return new CompletionComboRCP(parent, style);
	}

	@Override
	public CompletionCombo createCompletionComboWithImage(final Composite parent, final int style) {
		return new CompletionComboWithImageRCP(parent, style);
	}

	@Override
	public void copyEventKeyLocation(final Event source, final Event target) {
		target.keyLocation = source.keyLocation;
	}

	@Override
	public Cursor createCursor(final Display display, final Image cursorImage, final int alternateStyle) {
		Cursor result;
		if (cursorImage != null) {
			final Rectangle imageBounds = cursorImage.getBounds();
			final int hotspotX = imageBounds.width / 2;
			final int hotspotY = imageBounds.height / 2;
			final ImageData imageData = cursorImage.getImageData();
			result = new Cursor(display, imageData, hotspotX, hotspotY);
		} else {
			result = new Cursor(display, alternateStyle);
		}
		return result;
	}

	@Override
	public EventListener createDisabledPainter() {
		return new DisabledPainter();
	}

	@Override
	public void createGrabCornerListenerWithTracker(final Control control) {
		new GrabCornerListenerWithTracker(control);
	}

	@Override
	public InfoFlyout createInfoFlyout(final Composite parent) {
		return new InfoFlyoutRCP(parent);
	}

	@Override
	public void createEmbeddedTitleBarToolTip(final EmbeddedTitleBar parent) {
		new EmbeddedTitleBarToolTip(parent);
	}

	@Override
	public void createSubModuleToolTip(final Tree parent, final ILabelProvider labelProvider) {
		new SubModuleToolTip(parent, labelProvider);
	}

	@Override
	public void createSubApplicationToolTip(final Control parent) {
		Assert.isTrue(parent instanceof SubApplicationSwitcherWidget);
		new SubApplicationToolTip((SubApplicationSwitcherWidget) parent);
	}

	@Override
	public Listener createTreeItemEraserAndPainter() {
		return new TreeItemEraserAndPainter();
	}

	@Override
	public boolean postEvent(final Display display, final Event event) {
		return display.post(event);
	}

	@Override
	public void removeEraseItemListener(final Table table, final Listener listener) {
		table.removeListener(SWT.EraseItem, listener);
	}

	@Override
	public void removeEraseItemListener(final Tree tree, final Listener listener) {
		tree.removeListener(SWT.EraseItem, listener);
	}

	@Override
	public void removeFilterMouseWheel(final Display display, final Listener listener) {
		display.removeFilter(SWT.MouseWheel, listener);
	}

	@Override
	public void removeMouseMoveListener(final Control control, final Object listener) {
		if (control != null) {
			control.removeMouseMoveListener((MouseMoveListener) listener);
		}
	}

	@Override
	public void removeMouseTrackListener(final Control control, final MouseTrackListener listener) {
		control.removeMouseTrackListener(listener);
	}

	@Override
	public void removePaintItemListener(final Tree tree, final Listener listener) {
		tree.removeListener(SWT.PaintItem, listener);
	}

	@Override
	public void removePaintListener(final Control control, final EventListener listener) {
		if (listener != null) {
			control.removePaintListener((PaintListener) listener);
		}
	}

	@Override
	public boolean traverse(final Control control, final int traversal) {
		return control.traverse(traversal);
	}

	@Override
	public void setIncrement(final ScrollBar scrollBar, final int value) {
		scrollBar.setIncrement(value);
	}

	@Override
	public Button createMultilineButton(final Composite parent, final int style) {
		return new MultilineButton(parent, style | SWT.WRAP);
	}

	// protected methods
	////////////////////

	@Override
	protected void addModifyListeners(final Control control, final Object[] listeners) {
		for (final Object listener : listeners) {
			control.addListener(SWT.Modify, (Listener) listener);
		}
	}

	@Override
	protected void addVerifyListeners(final Control control, final Object[] listeners) {
		for (final Object listener : listeners) {
			control.addListener(SWT.Verify, (Listener) listener);
		}
	}

	@Override
	protected Object[] removeModifyListeners(final Control control) {
		final Listener[] result = control.getListeners(SWT.Modify);
		for (final Listener listener : result) {
			control.removeListener(SWT.Modify, listener);
		}
		return result;
	}

	@Override
	protected Object[] removeVerifyListeners(final Control control) {
		final Listener[] result = control.getListeners(SWT.Verify);
		for (final Listener listener : result) {
			control.removeListener(SWT.Verify, listener);
		}
		return result;
	}
}
