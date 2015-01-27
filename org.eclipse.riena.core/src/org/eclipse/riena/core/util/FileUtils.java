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
package org.eclipse.riena.core.util;

import java.io.File;

import org.eclipse.core.runtime.Assert;

/**
 * Collection of File/filename utilities.
 * 
 * @since 6.1
 */
public final class FileUtils {

	/**
	 * Private default constructor.
	 */
	private FileUtils() {
		// Utility class
	}

	/**
	 * Returns the file name without its file extension or path.
	 *
	 * @param file
	 *            The name of the file to trim the extension from. This can be either a fully qualified file name (including a path) or just a file name.
	 * @return The file name without its path or extension.
	 */
	public static String getNameWithoutExtension(final String file) {

		Assert.isNotNull(file, "file must be given!"); //$NON-NLS-1$

		final String fileName = new File(file).getName();
		final int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);

	}

	/**
	 * Returns the file extension for the given file name, or the empty string if the file has no extension. The result does not include the '.'.
	 * 
	 * @param fullName
	 * @return file extension or empty string if the file has no extension
	 */
	public static String getFileExtension(final String fullName) {

		Assert.isNotNull(fullName, "fullName must be given!"); //$NON-NLS-1$

		final String fileName = new File(fullName).getName();
		final int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1); //$NON-NLS-1$

	}

}
