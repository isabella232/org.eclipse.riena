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
package org.eclipse.riena.internal.sample.app.server;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.sample.app.common.tests.ITestObjectsOverRemoteService;

/**
 *
 */
public class TestObjectsOverRemoteService implements ITestObjectsOverRemoteService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.tests.ITestObjectsOverRemoteService
	 * #returnObject()
	 */
	public Object returnObject(final int type) {
		if (type == 0) {
			return "hello world"; //$NON-NLS-1$
		}
		if (type == 1) {
			return new TestObject();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.tests.ITestObjectsOverRemoteService
	 * #returnMap()
	 */
	public Map returnMap(final int type) {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (type == 0) {
			map.put("Hello", "World"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (type == 1) {
			map.put("Hello", new TestObject()); //$NON-NLS-1$
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.tests.ITestObjectsOverRemoteService
	 * #sendObject(java.lang.Object)
	 */
	public void sendObject(final Object object) {
		if (object == null) {
			throw new RuntimeException("not expecting a null object");
		}
	}

	public int sendMap(final Map map) {
		if (map == null) {
			throw new RuntimeException("not expected a null for the map object");
		}

		return map.size();
	}
}
