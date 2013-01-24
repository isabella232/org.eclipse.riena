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
package org.eclipse.riena.ui.swt;

import org.osgi.framework.BundleContext;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.riena.core.IRienaActivator;

/**
 * Abstract base class for riena UI plugins.<br>
 * It provides all descendant classes access to the bundle context.
 * <p>
 * <b>Note: </b>Derived plugins must call <code>super.start()</code> and
 * <code>super.stop()</code> within their <code>start()</code> and
 * <code>stop()</stop> methods.
 */
public abstract class AbstractRienaUIPlugin extends AbstractUIPlugin implements IRienaActivator {

	/**
	 * Get the shared context.
	 * 
	 * @return
	 */
	public BundleContext getContext() {
		return getBundle().getBundleContext();
	}

}
