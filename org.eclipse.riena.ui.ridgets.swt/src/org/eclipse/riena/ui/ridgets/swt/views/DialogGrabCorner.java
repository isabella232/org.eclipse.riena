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
package org.eclipse.riena.ui.ridgets.swt.views;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 * Area to grab so that the shell can be resized.
 * 
 * @since 3.0
 */
public class DialogGrabCorner extends Composite {

	/**
	 * @param parent
	 * @param style
	 */
	public DialogGrabCorner(final Composite parent, final int style) {

		super(parent, style);
		setBackground(parent.getBackground());
		setData("sizeexecutor", "grabcorner"); //$NON-NLS-1$ //$NON-NLS-2$

		setLayoutData();

		final SWTFacade facade = SWTFacade.getDefault();
		facade.addPaintListener(this, new GrabPaintListener());
		facade.createGrabCornerListenerWithTracker(this);
	}

	/**
	 * Sets the (form) layout data for the grab corner.
	 */
	private void setLayoutData() {
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
	public static Point getGrabCornerSize() {
		Point grabCornerSize = new Point(0, 0);
		final Image grabCorner = getGrabCornerImage();
		if ((grabCorner != null)) {
			grabCornerSize = new Point(grabCorner.getBounds().width, grabCorner.getBounds().height);
		}
		return grabCornerSize;
	}

	/**
	 * Returns the image of the grab corner.
	 * 
	 * @return grab corner image
	 */
	private static Image getGrabCornerImage() {
		Image image = LnfManager.getLnf().getImage(LnfKeyConstants.DIALOG_GRAB_CORNER_IMAGE);
		if (image == null) {
			image = ImageStore.getInstance().getMissingImage();
		}
		return image;
	}

	/**
	 * This Listener paint the grab corner.
	 */
	private static class GrabPaintListener implements PaintListener {

		public void paintControl(final PaintEvent e) {
			final GC gc = e.gc;
			final Image grabCornerImage = getGrabCornerImage();
			if (grabCornerImage != null) {
				gc.drawImage(grabCornerImage, 0, 0);
			}
		}

	}

}
