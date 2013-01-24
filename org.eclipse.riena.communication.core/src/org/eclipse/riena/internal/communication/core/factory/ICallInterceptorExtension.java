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
package org.eclipse.riena.internal.communication.core.factory;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Extension interface for call interceptors.
 */
@ExtensionInterface(id = "callInterceptors")
public interface ICallInterceptorExtension {

	/**
	 * The unique name (among the same service interfaces) of the call
	 * interceptors.
	 * 
	 * @return the unique name
	 */
	String getName();

	/**
	 * The comma-separated list of call-interceptor names that shall be executed
	 * before this hook.
	 * 
	 * @return the call-interceptor names list
	 */
	String getPreInterceptors();

	/**
	 * The comma-separated list of call-interceptor names that shall be executed
	 * after this hook.
	 * 
	 * @return the call-interceptor names list
	 */
	String getPostInterceptors();

	/**
	 * The 'remote service' interface class.
	 * 
	 * @return remote service interface
	 */
	@MapName("serviceInterfaceClass")
	Class<?> getServiceInterface();

	/**
	 * The 'call-interceptor' class.
	 * 
	 * @return 'call-interceptor' class
	 */
	@MapName("callInterceptorClass")
	Class<?> getCallInterceptorClass();

}
