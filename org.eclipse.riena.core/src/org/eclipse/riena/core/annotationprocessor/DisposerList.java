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
package org.eclipse.riena.core.annotationprocessor;

import java.util.ArrayList;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;

/**
 * Maintains a list of {@code IDisposer}s.
 * 
 * @since 4.0
 */
public class DisposerList extends ArrayList<IDisposer> implements IDisposer {

	private static final long serialVersionUID = 2180514251800343367L;
	private static final Logger LOGGER = Log4r.getLogger(DisposerList.class);

	public void dispose() {
		for (final IDisposer disposer : this) {
			try {
				disposer.dispose();
			} catch (final Throwable t) {
				LOGGER.log(LogService.LOG_ERROR, "Exception occured while disposing.", t); //$NON-NLS-1$
			}

		}
		clear();
	}

}
