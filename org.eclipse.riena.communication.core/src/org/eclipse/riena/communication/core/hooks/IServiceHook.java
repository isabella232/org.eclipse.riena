/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.communication.core.hooks;

/**
 * {@link IServiceHook} is an interface that needs to be implemented by a
 * component that wants to plug into the remote service call process. A
 * component needs to register as OSGi Service to activate itself as such an
 * hook i.e. {@code context.registerService(IServiceHook.class.getName(), new
 * YouServiceHook(),null);}
 * <p>
 * Then the beforeService method is called (on the server) before EACH and EVERY
 * remote service is forwarded to the OSGi Service together with a
 * {@link ServiceContext} instance. The afterService method is called after the
 * remote service invocation is finished and before the call is returned to the
 * server with the same {@link ServiceContext} instance.
 * <p>
 * A new {@link ServiceContext} instance is created for every remote service
 * call - they are never reused.
 */
public interface IServiceHook {

	/**
	 * Is called before the invocation on the server of the remote service
	 * component.
	 * <p>
	 * <b>Note:</b>Throwing an exception will terminate the service call!
	 * 
	 * @param context
	 *            ServiceContext instance that contains meta-information about
	 *            the remote service call and a properties hash-map that can be
	 *            used to store information for the afterService method.
	 */
	void beforeService(ServiceContext context);

	/**
	 * Is called after the invocation on the server of the remote service
	 * component.
	 * 
	 * @param context
	 *            ServiceContext instance that contains meta-information about
	 *            the remote service call and a properties hash-map that can be
	 *            used to pass information from the beforeService method to the
	 *            afterService method.
	 */
	void afterService(ServiceContext context);

}
