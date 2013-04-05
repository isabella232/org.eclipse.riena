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
package org.eclipse.riena.core.injector.extension;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.core.injector.Inject;

/**
 * This class contains just a few scribbles for the fluent interface of the
 * extension injector.
 */
public class Scribble {

	public void scribble() {
		final BundleContext context = null;
		Inject.extension("").useType(Object.class).expectingMinMax(0, 1).into(this).andStart(context);
		Inject.extension("").useType(Object.class).expectingExactly(1).into(this).update("update").specific()
				.andStart(context);
		Inject.extension("").heterogeneous().into(this).doNotReplaceSymbols().andStart(context).stop();
	}

}
