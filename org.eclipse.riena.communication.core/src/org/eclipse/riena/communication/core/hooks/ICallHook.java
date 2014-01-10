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
 * {@link ICallHook} is an interface that needs to be implemented by a component
 * that wants to plug into the remote service call process. A component needs to
 * register as OSGi Service to activate itself as such an hook i.e.
 * {@code context.registerService(ICallHook.class.getName(), new YouCallHook(),null);}
 * <p>
 * Then the beforeCall method is called (on the client) before EACH and EVERY
 * remote service call together with a {@link CallContext} instance. The
 * afterCall method is called after the remote service calls return and before
 * control is returned to the application code with the same {@link CallContext}
 * instance.
 * <p>
 * A new {@link CallContext} instance is created for every remote service call -
 * they are never reused.
 */
public interface ICallHook {

	/**
	 * Is called before the remote service call.
	 * <p>
	 * <b>Note:</b>Throwing an exception will terminate the service call!
	 * 
	 * @param context
	 *            CallContext instance that contains meta-information about the
	 *            call and contains a properties hash-map that can be used to
	 *            store information for the afterCall method.
	 */
	void beforeCall(CallContext context);

	/**
	 * Is called after the remote service call.
	 * <p>
	 * <b>Note:</b>Throwing an exception will terminate the service call!
	 * 
	 * @param context
	 *            CallContext instance (same as beforeCall) that contains
	 *            meta-information and a generic properties hash-map.
	 */
	void afterCall(CallContext context);

}
