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
package org.eclipse.riena.ui.swt.utils;

import org.eclipse.swt.graphics.Image;

import org.eclipse.riena.ui.core.resource.IconSize;

/**
 * @since 6.2
 */
public class ImageStoreStrategyContextHolder {
	private IImageFind imageFindStrategy;

	public ImageStoreStrategyContextHolder(final IImageFind findImageStrategy) {
		this.imageFindStrategy = findImageStrategy;
	}

	public void applayStrategy(final IImageFind findImageStrategy) {
		this.imageFindStrategy = findImageStrategy;
	}

	public Image executeStrategy(final String imageName, final ImageFileExtension fileExtension, final IconSize imageSizeRequested) {
		return imageFindStrategy.find(imageName, fileExtension, imageSizeRequested);

	}
}