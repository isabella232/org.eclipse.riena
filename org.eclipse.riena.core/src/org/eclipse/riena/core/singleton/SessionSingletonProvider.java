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
package org.eclipse.riena.core.singleton;

import org.eclipse.riena.internal.core.singleton.RAPSingletonProvider;

/**
 * A generic (RCP/RAP) singleton provider.
 * <p>
 * The {@code SessionSingletonProvider} creates singletons based on their type
 * {@code S}. It <i>automagically</i> detects whether it runs in a RAP/RWT
 * environment. In this case it delegates singleton creation/management to the
 * RAP {@code SessionSingletonBase} which takes care of session managed
 * singletons.<br>
 * In a <i>standard RCP</i> environment it handles the singleton
 * creation/management itself by delegating this to it's super class.<br>
 * However, in both cases it wires the singletons.
 * 
 * @param <S>
 *            the type of the singleton
 * 
 * @since 3.0
 */
public class SessionSingletonProvider<S> extends SingletonProvider<S> {

	/**
	 * Create a {@code SessionSingletonProvider} that creates (a) singleton(s)
	 * for the specified {@code singletonClass}.
	 * 
	 * @param singletonClass
	 *            the class of the requested singleton
	 */
	public SessionSingletonProvider(final Class<S> singletonClass) {
		this(singletonClass, null);
	}

	/**
	 * Create {@code SessionSingletonProvider} that creates (a) singleton(s) for
	 * the specified {@code singletonClass}.The singleton can be initialized
	 * with the given 'call back' and will wired.
	 * 
	 * @param singletonClass
	 *            the class of the requested singleton
	 * @param initializer
	 *            a initializer 'call back'
	 * @since 4.0
	 */
	public SessionSingletonProvider(final Class<S> singletonClass, final ISingletonInitializer<S> initializer) {
		super(singletonClass, initializer);
	}

	/**
	 * Return the requested singleton.
	 * 
	 * @return the singleton
	 */
	@Override
	public S getInstance() {
		return RAPSingletonProvider.isAvailable() ? RAPSingletonProvider.getInstance(singletonClass, initializer)
				: super.getInstance();
	}

}
