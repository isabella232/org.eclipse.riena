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
package org.eclipse.riena.core;

import java.io.File;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.log.Logger;
import org.eclipse.osgi.service.datalocation.Location;

import org.eclipse.riena.core.util.StringUtils;

/**
 * Riena locations.
 */
public final class RienaLocations {

	private final static Logger LOGGER = Log4r.getLogger(RienaLocations.class);

	private RienaLocations() {
		// utility class
	}

	/**
	 * Returns the area on the file system where data files shall be written to.
	 * <p>
	 * Uses/creates a directory within the RCP instance area, as specified by
	 * the osgi.instance.area system property, if it is set. Otherwise, the RCP
	 * configuration area (see system properties osgi.*configuration.*) are used
	 * for data.
	 * 
	 * @return the area on the file system where data files shall be written to.
	 */
	public static File getDataArea() {
		final Location instanceAreaLocation = Platform.getInstanceLocation();
		if (instanceAreaLocation != null && instanceAreaLocation.getURL() != null) {
			LOGGER.log(LogService.LOG_DEBUG, "Platform Instance Location: " + instanceAreaLocation.getURL().toString()); //$NON-NLS-1$
			return existingOrNewDirectory(new File(instanceAreaLocation.getURL().getFile()));
		} else {
			throw new RuntimeException("No instance area could be found! Only set " //$NON-NLS-1$
					+ "osgi.instance.area to @noDefault if you specify one explicitly!"); //$NON-NLS-1$
		}
	}

	/**
	 * Return the data area for the specified bundle.<br>
	 * <b>Note:</b> This version creates a special folder within the Eclipse
	 * install location.
	 * 
	 * @param bundle
	 * @return
	 */
	public static File getDataArea(final Bundle bundle) {
		Assert.isLegal(bundle != null, "bundle must not be null."); //$NON-NLS-1$
		final String symbolicName = bundle.getSymbolicName();
		Assert.isLegal(StringUtils.isGiven(symbolicName), "no symbolic name for bundle."); //$NON-NLS-1$
		return existingOrNewDirectory(new File(getDataArea(), symbolicName));
	}

	/**
	 * Returns the user area on the file system. This is equivalent to
	 * {@code System.getProperty("user.home"}.
	 * 
	 * @return the user area on the file system.
	 * 
	 * @since 3.0
	 */
	public static File getUserArea() {
		return new File(System.getProperty("user.home")); //$NON-NLS-1$
	}

	private static File existingOrNewDirectory(File location) {
		if (location.isDirectory()) {
			LOGGER.log(LogService.LOG_DEBUG, "Found directory at " + location.getAbsolutePath()); //$NON-NLS-1$
			return location;
		} else {
			LOGGER.log(LogService.LOG_DEBUG, location.getAbsolutePath() + " is not a directory."); //$NON-NLS-1$
			final boolean canWrite = true;
			File tempFile = location;
			while (tempFile.getParentFile() != null && canWrite) {
				tempFile = tempFile.getParentFile();
				if (!tempFile.canWrite()) {
					break;
				}
			}

			final String fallbackLocation = System.getProperty("user.home") + "/." //$NON-NLS-1$ //$NON-NLS-2$
					+ System.getProperty("eclipse.product"); //$NON-NLS-1$

			if (!canWrite) {
				LOGGER.log(LogService.LOG_DEBUG,
						"No write permissions; using ${user.home}/.${eclipse.product} as fallback"); //$NON-NLS-1$
				location = new File(fallbackLocation);
			}

			boolean created = location.mkdirs();

			if (!created && canWrite) {
				LOGGER.log(LogService.LOG_DEBUG, "Could not create; using ${user.home}/.${eclipse.product} as fallback"); //$NON-NLS-1$
				location = new File(fallbackLocation);
				created = location.mkdirs();
				if (!created) {
					throw new RuntimeException(
							"Could not create fallback instance area at '" + location.getAbsolutePath() //$NON-NLS-1$
									+ "'. Giving up."); //$NON-NLS-1$
				}
			}
			Assert.isLegal(created, "Missing instance area could not be created."); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_DEBUG, "Created instance area directory at " + location.getAbsolutePath()); //$NON-NLS-1$
			return location;
		}
	}

}
