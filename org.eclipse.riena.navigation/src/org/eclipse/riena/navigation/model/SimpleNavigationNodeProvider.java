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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.extension.INavigationAssembly2Extension;

/**
 * This class provides service methods to get information provided by
 * WorkAreaDefinitions.
 */
public class SimpleNavigationNodeProvider extends AbstractSimpleNavigationNodeProvider {

	/**
	 * Creates a new provider and injects all assemblies (legacy and new (2)).
	 */
	public SimpleNavigationNodeProvider() {

		Inject.extension(getLegacyNavigationAssemblyExtensionPointSafe())
				.useType(getLegacyNavigationAssemblyExtensionIFSafe()).into(this)
				.andStart(Activator.getDefault().getContext());
		Inject.extension(getNavigationAssemblyExtensionPointSafe()).useType(getNavigationAssemblyExtensionIFSafe())
				.into(this).andStart(Activator.getDefault().getContext());

	}

	/**
	 * This is called by extension injection to provide the extension points
	 * found
	 * 
	 * @param data
	 *            The navigation assemblies contributed by all extension points
	 */
	public void update(final INavigationAssemblyExtension[] data) {
		// cleanUp();
		for (final INavigationAssemblyExtension assembly : data) {
			register(assembly);
		}
	}

	public String getNavigationAssemblyExtensionPointSafe() {
		if (StringUtils.isDeepEmpty(getNavigationAssemblyExtensionPoint())) {
			return INavigationAssembly2Extension.EXTENSIONPOINT;
		} else {
			return getNavigationAssemblyExtensionPoint();
		}
	}

	/**
	 * Override this method if you intend to use a different extension point
	 * 
	 * @return The extension point used to contribute navigation assemblies
	 */
	protected String getNavigationAssemblyExtensionPoint() {
		return INavigationAssembly2Extension.EXTENSIONPOINT;
	}

	public Class<? extends INavigationAssembly2Extension> getNavigationAssemblyExtensionIFSafe() {
		if (getNavigationAssemblyExtensionIF() != null && getNavigationAssemblyExtensionIF().isInterface()) {
			return getNavigationAssemblyExtensionIF();
		} else {
			return INavigationAssembly2Extension.class;
		}
	}

	protected Class<? extends INavigationAssembly2Extension> getNavigationAssemblyExtensionIF() {
		return INavigationAssembly2Extension.class;
	}

	/**
	 * This is called by extension injection to provide the extension points
	 * found
	 * 
	 * @param data
	 *            The navigation assemblies contributed by all extension points
	 */
	public void update(final INavigationAssembly2Extension[] data) {
		// cleanUp();
		for (final INavigationAssembly2Extension assembly : data) {
			register(assembly);
		}
	}

	private String getLegacyNavigationAssemblyExtensionPointSafe() {
		if (StringUtils.isDeepEmpty(getLegacyNavigationAssemblyExtensionPoint())) {
			return INavigationAssemblyExtension.EXTENSIONPOINT;
		} else {
			return getLegacyNavigationAssemblyExtensionPoint();
		}
	}

	protected String getLegacyNavigationAssemblyExtensionPoint() {
		return INavigationAssemblyExtension.EXTENSIONPOINT;
	}

	private Class<? extends INavigationAssemblyExtension> getLegacyNavigationAssemblyExtensionIFSafe() {
		if (getLegacyNavigationAssemblyExtensionIF() != null && getLegacyNavigationAssemblyExtensionIF().isInterface()) {
			return getLegacyNavigationAssemblyExtensionIF();
		} else {
			return INavigationAssemblyExtension.class;
		}
	}

	private Class<? extends INavigationAssemblyExtension> getLegacyNavigationAssemblyExtensionIF() {
		return INavigationAssemblyExtension.class;
	}

	private void register(final INavigationAssemblyExtension assembly) {
		final INavigationAssembly2Extension assembly2 = AssembliesConverter.convert(assembly);
		register(assembly2);
	}

}