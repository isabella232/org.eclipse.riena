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
package org.eclipse.riena.core;

import java.util.Hashtable;

import org.osgi.framework.Constants;

/**
 * Various global constants
 */
public final class RienaConstants {

	/**
	 * Default service ranking for riena services.
	 */
	public final static Integer DEFAULT_RANKING = -100;

	private RienaConstants() {
		// utility class
	}

	/**
	 * Get service properties set with the default ranking for riena services,
	 * i.e. the riena default ranking is lower than the (OSGi) default ranking
	 * so that service created with (OSGi) default ranking will override
	 * services with riena default ranking.
	 * 
	 * @return default service properties
	 */
	public static Hashtable<String, Object> newDefaultServiceProperties() {
		final Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.SERVICE_RANKING, DEFAULT_RANKING);
		return props;
	}

}
