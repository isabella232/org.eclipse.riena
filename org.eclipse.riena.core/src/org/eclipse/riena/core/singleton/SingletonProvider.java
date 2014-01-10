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
package org.eclipse.riena.core.singleton;

import org.eclipse.riena.internal.core.singleton.RCPSingletonProvider;

/**
 * A singleton provider that creates <i>true</i> singletons.
 * <p>
 * The {@code SingletonProvider} creates singletons based on their type
 * {@code S}. A <i>true</i> singleton is a singleton that exists only once.<br>
 * The {@code SingletonProvider} also wires the singletons.
 * 
 * @param <S>
 *            the type of the singleton
 * 
 * @since 3.0
 */
public class SingletonProvider<S> {

	protected final Class<S> singletonClass;
	/**
	 * @since 4.0
	 */
	protected final ISingletonInitializer<S> initializer;
	private volatile S singleton;

	/**
	 * Create a {@code SingletonProvider} that creates a singleton for the
	 * specified {@code singletonClass}. The singleton will be wired.
	 * 
	 * @param singletonClass
	 *            the class of the requested singleton
	 */
	public SingletonProvider(final Class<S> singletonClass) {
		this(singletonClass, null);
	}

	/**
	 * Create a {@code SingletonProvider} that creates a singleton for the
	 * specified {@code singletonClass}. The singleton can be initialized with
	 * the given 'call back' and will wired.
	 * 
	 * @param singletonClass
	 *            the class of the requested singleton
	 * @param initializer
	 *            a initializer 'call back'
	 * @since 4.0
	 */
	public SingletonProvider(final Class<S> singletonClass, final ISingletonInitializer<S> initializer) {
		this.singletonClass = singletonClass;
		this.initializer = initializer;
	}

	/**
	 * Return the requested probably initialized and wired singleton.
	 * 
	 * @return the singleton
	 */
	public S getInstance() {
		// Double-check idiom for lazy initialization of instance fields (Joshua Bloch, Effective Java, Item 71)
		S result = singleton;
		if (result == null) { // First check (no locking)
			synchronized (this) {
				result = singleton;
				if (result == null) {
					singleton = result = RCPSingletonProvider.getInstance(singletonClass, initializer);
				}
			}
		}
		return result;
	}

}
