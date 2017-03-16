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

import org.eclipse.swt.graphics.Image;

import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 * Wrapper for resource image.
 */
public class ImageLnfResource extends AbstractLnfResource<Image> {

	private final String imagePath;
	private final IconSize iconSize;

	/**
	 * Creates a new {@link ImageLnfResource} with the given image path and {@link IconSize.NONE}.
	 * 
	 * @param image
	 *            image to wrap
	 */
	public ImageLnfResource(final String imagePath) {
		this(imagePath, IconSize.NONE);
	}

	/**
	 * Creates a new {@link ImageLnfResource} instance with the given image path and {@link IconSize}.
	 * 
	 * @since 6.2
	 */
	public ImageLnfResource(final String imagePath, final IconSize iconSize) {
		this.imagePath = imagePath;
		this.iconSize = iconSize;
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfResource#createResource()
	 */
	public Image createResource() {
		return ImageStore.getInstance().getImage(imagePath, iconSize);
	}

	/**
	 * @return ImagePath that was used to create the ImageLnfResource
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Returns the {@link IconSize} of this {@link ImageLnfResource}.
	 * 
	 * @return the {@link IconSize}
	 * @since 6.2
	 */
	public IconSize getIconSize() {
		return iconSize;
	}
}
