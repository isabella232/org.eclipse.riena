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
package org.eclipse.riena.internal.core.logging.log4j;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.core.logging.log4j.ILog4jDiagnosticContext;

/**
 * The {@code ExtensionInterface} for defining a log4j diagnostic context.
 */
@ExtensionInterface(id = "log4jDiagnosticContext")
public interface ILog4jDiagnosticContextExtension {

	/**
	 * Create the diagnostic context.
	 * 
	 * @return the diagnostic context
	 */
	@MapName("class")
	ILog4jDiagnosticContext createDiagnosticContext();

}
