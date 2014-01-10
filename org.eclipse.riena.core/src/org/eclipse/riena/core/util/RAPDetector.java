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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;

import org.eclipse.core.runtime.Platform;

import org.eclipse.riena.core.service.Service;

/**
 * The {@code RAPDetector} checks whether RAP is available or not. It does this
 * by trying to locate the RAP RWT bundle.
 * 
 * @since 3.0
 */
public final class RAPDetector {

	private static final Bundle RWT_BUNDLE;

	private static final String RWT_PACKAGE_NAME = "org.eclipse.rwt"; //$NON-NLS-1$
	private static final String RWT_BUNDLE_NAME = "org.eclipse.rap.rwt"; //$NON-NLS-1$

	static {
		RWT_BUNDLE = findRWTBundle();
	}

	private RAPDetector() {
		// utility
	}

	/**
	 * Is RAP available?
	 * 
	 * @return {@code true} if so; otherwise {@code false}
	 */
	public static boolean isRAPavailable() {
		return RWT_BUNDLE != null;
	}

	/**
	 * If RAP is available return the RWT bundle otherwise {@code null}.
	 * 
	 * @return the RWT bundle or null
	 */
	public static Bundle getRWTBundle() {
		return RWT_BUNDLE;
	}

	/**
	 * Find the RWT (RAP) bundle.
	 * 
	 * @return the rwt bundle or {@code null} if RAP is not available
	 */
	private static Bundle findRWTBundle() {
		if (FrameworkUtil.getBundle(RAPDetector.class) == null) {
			return null;
		}
		final PackageAdmin packageAdmin = Service.get(PackageAdmin.class);
		if (packageAdmin != null) {
			final ExportedPackage rwtPackage = packageAdmin.getExportedPackage(RWT_PACKAGE_NAME);
			if (rwtPackage != null) {
				final Bundle bundle = rwtPackage.getExportingBundle();
				if (bundle != null) {
					return bundle;
				}
			}
		}
		return Platform.getBundle(RWT_BUNDLE_NAME);
	}

}
