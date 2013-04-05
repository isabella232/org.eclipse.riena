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
package org.eclipse.riena.communication.core.hooks;

import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorList;

/**
 * This interface must be implemented by each service provider. When
 * riena.communication needs to populate the CallContext for the CallHooks it
 * will use an instance of the ICallMessageContext to retrieve the information
 * like request headers, method name called etc. There are also event methods in
 * here that are called in certain stages of the remote service invokation. The
 * can be used to update IRemoteProgressMonitor about what is going on (how much
 * data is transferred and so on).
 */
public interface ICallMessageContext {

	/**
	 * add a request header. (only makes sense in the CallHook beforeCall
	 * method)
	 * 
	 * @param name
	 * @param value
	 */
	void addRequestHeader(String name, String value);

	/**
	 * @return current list of request headers (http headers)
	 */
	Map<String, List<String>> listRequestHeaders();

	/**
	 * @return map of oall response headers in the call
	 */
	Map<String, List<String>> listResponseHeaders();

	/**
	 * @param name
	 *            of the response header
	 * @return list of values for the http header specified in name
	 */
	List<String> getResponseHeaderValues(String name);

	/**
	 * @return return a list of IRemoteProgressMonitorList that are registered
	 *         in this ICallMessageContext
	 */
	IRemoteProgressMonitorList getProgressMonitorList();

	/**
	 * Is called in the CallMessageContext when the first byte is transferred
	 */
	void fireStartCall();

	/**
	 * Is called in the CallMessageContext when the call is finished.
	 */
	void fireEndCall();

	/**
	 * Is called for every n bytes that are read (bytes returned from the
	 * server). Its up to the implementation to decide what 'n' is. Currently
	 * its 512 bytes. The required that it also reports the last block of data.
	 * So usually every call to fireReadEvent will report a fixed amount like
	 * 512 and only the last event in one call then has the rest number of
	 * bytes.
	 * 
	 * @param bytesRead
	 */
	void fireReadEvent(int bytesRead);

	/**
	 * Is called for every n bytes that written (bytes written to the server).
	 * Its up to the implementation to decide what 'n' is. Currently its 512
	 * bytes. The required that it also reports the last block of data. So
	 * usually every call to fireWriteEvent will report a fixed amount like 512
	 * and only the last event in one call then has the rest number of bytes.
	 * 
	 * @param bytesSent
	 */
	void fireWriteEvent(int bytesSent);

	/**
	 * @return methodName of the current remote service call
	 */
	String getMethodName();

	/**
	 * The idea is that each and every remote service call gets a world wide
	 * unique id (that is pretty short) and is appended to the URL of every
	 * remote service call. It allows you to track a remote service call in your
	 * server park through the infrastructure (load balancers etc.). It contains
	 * no information by itself, only it should be unique to make sense.
	 * 
	 * @return a unique request id for this remote service call.
	 */
	String getRequestId();

}
