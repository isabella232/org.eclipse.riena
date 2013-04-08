/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.swt.facades;

import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Returns an Eclipse 3.x or E4 specific instance of a given type.
 * <p>
 * This class is not API. Do not invoke.
 * 
 * @since 5.0
 */
public abstract class WorkbenchFacade {

	/**
	 * Returns an instance of the given type.
	 * <p>
	 * The code will append "Impl" to the given type, try to load the resulting class and invoke the 0-argument constructor. If successful it will return an
	 * instance that implements the argument {@code type}.
	 * 
	 * @return an instance of type; never null
	 * @throws RuntimeException
	 *             if no matching instance could be found
	 */
	public static WorkbenchFacade getInstance() {
		final String suffix = "Impl"; //$NON-NLS-1$
		final Class<WorkbenchFacade> type = WorkbenchFacade.class;
		final String name = type.getName() + suffix;
		try {
			return type.cast(type.getClassLoader().loadClass(name).newInstance());
		} catch (final ClassCastException e) {
			final String msg = NLS.bind("Could not create an instance of {0} because it is not a {1}", name, type.getName()); //$NON-NLS-1$
			throw new RuntimeException(msg, e);
		} catch (final Throwable throwable) {
			final String msg = NLS.bind("Could not load {0}", name); //$NON-NLS-1$
			throw new RuntimeException(msg, throwable);
		}
	}

	/**
	 * Opens a view by calling a specific 3x or e4 implementation.
	 */
	public abstract void showView(IWorkbenchPage page, IViewReference viewRef);

}
