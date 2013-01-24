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
package org.eclipse.riena.ui.core.uiprocess;

import java.util.concurrent.Callable;

/**
 * Class implementing this interface are responsible for executing a
 * {@code Runnable} on the UI-Thread of the underlying widget toolkit.
 */
public interface IUISynchronizer {

	/**
	 * The given {@code Runnable} will be executed on the UI-thread. The calling
	 * thread will be suspended until the executing {@code Runnable} completes.
	 * 
	 * @param runnable
	 *            the {@code Runnable} to be executed on the UI-thread
	 */
	void syncExec(Runnable runnable);

	/**
	 * The given {@code Runnable} will be executed on the UI-thread. The calling
	 * thread will return immediately.
	 * 
	 * @param runnable
	 *            the {@code Runnable} to be executed on the UI-thread
	 */
	void asyncExec(Runnable runnable);

	/**
	 * Dispatches the swt-events while the given condition returns
	 * <code>true</code>
	 * 
	 * @param condition
	 *            the run condition
	 * @since 3.0
	 */
	void readAndDispatch(Callable<Boolean> condition);
}
