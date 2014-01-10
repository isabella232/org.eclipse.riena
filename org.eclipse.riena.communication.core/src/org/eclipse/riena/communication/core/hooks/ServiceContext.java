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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import org.eclipse.riena.communication.core.RemoteServiceDescription;

/**
 * A ServiceContext is used to give ServiceHooks meta-information about the
 * remote service call that is currently in progress. A new ServiceContext
 * object is instantiated for each and every remote service call.
 * 
 * The same ServiceContext instance is used for all ServiceHooks that are
 * registered in the beforeService and afterService method. ServiceHooks can
 * store their own information with setProperty and read it with getProperty
 * between beforeService and afterService if required.
 * 
 */
public class ServiceContext {

	private final RemoteServiceDescription rsd;
	private final Method method;
	private final Object service;
	private final IServiceMessageContext messageContext;
	private Throwable targetException;
	private HashMap<String, Object> properties;

	public ServiceContext(final RemoteServiceDescription rsd, final Method method, final Object service,
			final IServiceMessageContext messageContext) {
		this.rsd = rsd;
		this.service = service;
		this.method = method;
		this.messageContext = messageContext;
	}

	/**
	 * The name as string of the remote service interface
	 * 
	 * @return
	 */
	public String getInterfaceName() {
		return rsd.getServiceInterfaceClassName();
	}

	/**
	 * The name as class object of the remote service interface
	 * 
	 * @return
	 */
	public Class<?> getInterfaceClass() {
		return rsd.getServiceInterfaceClass();
	}

	/**
	 * The method that is called as string.
	 * 
	 * @return
	 */
	public String getMethodName() {
		return method.getName();
	}

	/**
	 * The method instance corresponding to the interface method invoked on the
	 * {@code ServiceHooksProxy} instance.
	 * 
	 * @return the method instance
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Return the service instance on which the method is called.
	 * 
	 * @return the service instance
	 */
	public Object getService() {
		return service;
	}

	/**
	 * Arbitrary property that can be associated with this call. Accessible in
	 * beforeService and afterService
	 * 
	 * @param name
	 *            key
	 * @param value
	 *            value for that key
	 */
	public void setProperty(final String name, final Object value) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		properties.put(name, value);
		return;
	}

	/**
	 * Getter access to key/value property store in setProperty
	 * 
	 * @param name
	 * @return
	 */
	public Object getProperty(final String name) {
		if (properties == null) {
			return null;
		}
		return properties.get(name);
	}

	/**
	 * Access to the ServiceMessageContext that has more lowlevel access to
	 * properties
	 * 
	 * @return
	 */
	public IServiceMessageContext getMessageContext() {
		return messageContext;
	}

	/**
	 * Accessor to read cookies that where transferred as part of the remote
	 * service call. Currently used for session information.
	 * 
	 * @return
	 */
	public Cookie[] getCookies() {
		final List<String> cookieValues = messageContext.getRequestHeaderValue("Cookie"); //$NON-NLS-1$
		if (cookieValues == null || cookieValues.size() == 0) {
			return null;
		}
		final List<Cookie> cookies = new ArrayList<Cookie>();
		for (final String temp : cookieValues) {
			cookies.add(new Cookie(temp.substring(0, temp.indexOf("=")), temp.substring(temp.indexOf("=") + 1))); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return cookies.toArray(new Cookie[cookies.size()]);
	}

	/**
	 * Adds a new Set-Cookie on the service side, which is sent back to the
	 * client when the remote service call returns
	 * 
	 * @param cookie
	 */
	public void addCookie(final Cookie cookie) {
		messageContext.addResponseHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * This property will be set by the communication layer (server side) if an
	 * exception occurred while executing the remote service.<br>
	 * It can be queried by the afterService() method of the
	 * {@code IServiceHook}. Additionally a {@code IServiceHook} can exchange
	 * the exception with a different one. In this case the changed exception
	 * will be thrown, i.e. transmitted to the client.
	 * 
	 * @since 4.0
	 */
	public Throwable getTargetException() {
		return targetException;
	}

	/**
	 * Set a <i>new</i> target exception. This exception will be thrown and
	 * transmitted to the client.<br>
	 * However, setting a {@code null} exception has no effect. The original
	 * exception will be thrown.
	 * 
	 * @since 4.0
	 */
	public void setTargetException(final Throwable targetException) {
		this.targetException = targetException;
	}
}
