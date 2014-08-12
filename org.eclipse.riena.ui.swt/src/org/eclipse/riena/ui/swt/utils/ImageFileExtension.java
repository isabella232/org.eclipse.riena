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

import java.util.EnumSet;

import org.eclipse.riena.core.util.StringUtils;

/**
 * File extensions of image (file) names.
 */
public enum ImageFileExtension {

	PNG("png"), //$NON-NLS-1$
	GIF("gif"), //$NON-NLS-1$
	JPG("jpg"), //$NON-NLS-1$
	SVG("svg"); //$NON-NLS-1$

	private String fileNameExtension;

	private ImageFileExtension(final String fileNameExtension) {
		this.fileNameExtension = fileNameExtension;
	}

	/**
	 * Returns file name extension.
	 * 
	 * @return extension of file name.
	 */
	public String getFileNameExtension() {
		return fileNameExtension;
	}

	/**
	 * Returns to the given extension the matching {@code ImageFileExtension}.
	 * 
	 * @param fileNameExtension
	 *            extension of the file name
	 * @return {@code ImageFileExtension} or {@code null} if no matching exists
	 */
	public static ImageFileExtension getImageFileExtension(final String fileNameExtension) {

		if (StringUtils.isEmpty(fileNameExtension)) {
			return null;
		}

		String extension = fileNameExtension.toLowerCase();
		if (extension.startsWith(".")) { //$NON-NLS-1$
			if (extension.length() == 1) {
				return null;
			}
			extension = extension.substring(1);
		}

		for (final ImageFileExtension imageFileExtension : EnumSet.allOf(ImageFileExtension.class)) {
			if (imageFileExtension.getFileNameExtension().equals(extension)) {
				return imageFileExtension;
			}
		}
		return null;

	}

}
