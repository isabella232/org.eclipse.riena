/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.e4;

import javax.inject.Inject;

import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;

public class RienaPerspectiveRegistry extends PerspectiveRegistry {
	@Inject
	private IEclipseContext context;

	public RienaPerspectiveRegistry() {
		final IExtensionTracker tracker = (IExtensionTracker) context.get(IExtensionTracker.class.getName());
		tracker.registerHandler(this, null);
	}
}
