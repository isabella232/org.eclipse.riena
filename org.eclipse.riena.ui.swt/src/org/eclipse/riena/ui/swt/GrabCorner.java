/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.DialogBorderRenderer;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 * Area to grab so that the shell can be resized.
 * 
 * @since 3.0
 */
public class GrabCorner extends Composite {
	private final Composite parent;

	/**
	 * @param parent
	 * @param style
	 */
	public GrabCorner(final Composite parent, final int style) {
		super(parent, style);
		this.parent = parent;

		setBackground(parent.getBackground());
		//		setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.GRAB_CORNER_BACKGROUND));
		setData("sizeexecutor", "grabcorner"); //$NON-NLS-1$ //$NON-NLS-2$
		if (isMainShell()) {
			setLayoutData();
		} else {
			setDialogLayoutData();
		}

		final SWTFacade facade = SWTFacade.getDefault();
		facade.addPaintListener(this, new GrabPaintListener());
		facade.createGrabCornerListenerWithTracker(this);
	}

	private boolean isMainShell() {
		return parent instanceof Shell;
	}

	/**
	 * Sets the (form) layout data for the grab corner.
	 */
	private void setLayoutData() {
		final Point grabCornerSize = getGrabCornerSize();
		final int borderWidth = getShellBorderWidth();

		final FormData grabFormData = new FormData();
		grabFormData.width = grabCornerSize.y;
		grabFormData.height = grabCornerSize.x;
		grabFormData.bottom = new FormAttachment(100, -borderWidth);
		grabFormData.right = new FormAttachment(100, -borderWidth);
		setLayoutData(grabFormData);
	}

	private void setDialogLayoutData() {
		final Point grabCornerSize = getGrabCornerSize();
		final FormData grabFormData = new FormData();
		grabFormData.width = grabCornerSize.x;
		grabFormData.height = grabCornerSize.y;
		grabFormData.bottom = new FormAttachment(100, 0);
		grabFormData.right = new FormAttachment(100, 0);

		setLayoutData(grabFormData);
	}

	/**
	 * Returns the size of the grab corner (including the border (right,bottom)
	 * of the shell)
	 * 
	 * @return size of grab corner
	 */
	public Point getGrabCornerSize() {
		Point grabCornerSize = new Point(getShellBorderWidth(), getShellBorderWidth());
		final Image grabCorner = getGrabCornerImage();
		if (grabCorner != null) {
			if (!isMainShell()) {
				grabCornerSize = new Point(grabCorner.getBounds().width, grabCorner.getBounds().height);
			} else {
				final Rectangle imageBounds = grabCorner.getBounds();
				final int width = grabCornerSize.x + imageBounds.width;
				final int height = grabCornerSize.y + imageBounds.height;
				grabCornerSize = new Point(width, height);
			}
		}
		return grabCornerSize;
	}

	/**
	 * Returns if the shell is resizable or not.
	 * 
	 * @return true if shell is resizable; otherwise false
	 */
	public static boolean isResizeable() {
		return LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.TITLELESS_SHELL_RESIZEABLE, true);
	}

	/**
	 * Returns the image of the grab corner.
	 * 
	 * @return grab corner image
	 */
	private Image getGrabCornerImage() {
		Image image = LnfManager.getLnf().getImage(
				isMainShell() ? LnfKeyConstants.TITLELESS_SHELL_GRAB_CORNER_IMAGE
						: LnfKeyConstants.DIALOG_GRAB_CORNER_IMAGE);
		if (image == null) {
			image = ImageStore.getInstance().getMissingImage();
		}
		return image;
	}

	/**
	 * Returns the width of the shell border.
	 * 
	 * @return border width
	 */
	private static int getShellBorderWidth() {
		final DialogBorderRenderer borderRenderer = (DialogBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		return borderRenderer != null ? borderRenderer.getBorderWidth() : 0;
	}

	/**
	 * This Listener paint the grab corner.
	 */
	private class GrabPaintListener implements PaintListener {
		public void paintControl(final PaintEvent e) {
			final GC gc = e.gc;
			final Image grabCornerImage = getGrabCornerImage();
			if (grabCornerImage != null) {
				gc.drawImage(grabCornerImage, 0, 0);
			}
		}
	}
}
