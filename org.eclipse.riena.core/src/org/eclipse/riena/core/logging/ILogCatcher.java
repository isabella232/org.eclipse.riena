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
package org.eclipse.riena.core.logging;

/**
 * A <code>ILogCatcher</code> can attach itself (e.g. via listeners) to systems
 * that are able to emit some sort of log events.<br>
 * A catcher can than route the events into the Riena's logging.
 */
public interface ILogCatcher {

	/**
	 * Attach to the log source.
	 */
	void attach();

	/**
	 * Detach from log source.
	 */
	void detach();

}
