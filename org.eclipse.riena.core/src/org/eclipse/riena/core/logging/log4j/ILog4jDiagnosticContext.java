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
package org.eclipse.riena.core.logging.log4j;

/**
 * Implementations of {@code ILog4jDiagnosticContext} allow to set diagnostic
 * context information for the log4j logging.
 */
public interface ILog4jDiagnosticContext {

	/**
	 * Called just before the log4j logging is called.
	 */
	void push();

	/**
	 * Called right after the log4j logging is called.
	 */
	void pop();
}
