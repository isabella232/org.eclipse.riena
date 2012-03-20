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
package org.eclipse.riena.monitor.client;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import org.eclipse.riena.internal.monitor.client.Activator;

/**
 * Collector for the bundle activity.
 */
public class BundleActivityCollector extends AbstractCollector implements BundleListener {

	private int activatedCounter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.AbstractCollector#doStart()
	 */
	@Override
	protected void doStart() {
		activatedCounter = 0;
		Activator.getDefault().getContext().addBundleListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.AbstractCollector#doStop()
	 */
	@Override
	protected void doStop() {
		Activator.getDefault().getContext().removeBundleListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework.
	 * BundleEvent)
	 */
	public void bundleChanged(final BundleEvent event) {
		collect(event);
		// TODO When do we trigger? Is this a good pattern?
		if (activatedCounter++ % 100 == 0) {
			triggerTransfer();
		}
	}
}
