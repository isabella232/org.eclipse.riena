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
package org.eclipse.riena.internal.communication.publisher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.communication.core.hooks.IServiceHook;
import org.eclipse.riena.communication.core.hooks.ServiceContext;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.core.util.Orderer;
import org.eclipse.riena.core.wire.InjectExtension;

/**
 * A {@code IServiceHook} that executes the ´ordered´ service hooks.
 */
public class OrderedServiceHooksExecuter implements IServiceHook {

	private List<IServiceHook> orderedServiceHooks;
	private List<IServiceHook> reversedServiceHooks;

	/**
	 * {@inheritDoc}
	 */
	public void beforeService(final ServiceContext context) {
		for (final IServiceHook sHook : orderedServiceHooks) {
			sHook.beforeService(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterService(final ServiceContext context) {
		for (final IServiceHook sHook : reversedServiceHooks) {
			sHook.afterService(context);
		}
	}

	@InjectExtension
	public void update(final IServiceHookExtension[] serviceHookExtensions) {
		final Orderer<IServiceHook> orderer = new Orderer<IServiceHook>();
		for (final IServiceHookExtension extension : serviceHookExtensions) {
			orderer.add(extension.getServiceHook(), extension.getName(), extension.getPreHooks(),
					extension.getPostHooks());
		}
		final List<IServiceHook> tempOrdered = orderer.getOrderedObjects();
		final List<IServiceHook> tempReverse = new ArrayList<IServiceHook>(tempOrdered.size());
		for (final IServiceHook hook : Iter.ableReverse(tempOrdered)) {
			tempReverse.add(hook);
		}
		synchronized (this) {
			this.orderedServiceHooks = tempOrdered;
			this.reversedServiceHooks = tempReverse;
		}
	}

}
