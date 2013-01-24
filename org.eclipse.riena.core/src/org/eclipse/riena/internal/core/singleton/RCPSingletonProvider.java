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
package org.eclipse.riena.internal.core.singleton;

import java.lang.reflect.Constructor;

import org.eclipse.riena.core.singleton.ISingletonInitializer;
import org.eclipse.riena.core.singleton.SingletonFailure;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.wire.Wire;

/**
 * Utility for creating RCP singletons. Newly created singleton will also be
 * wired.
 * 
 * @since 4.0
 */
public final class RCPSingletonProvider {

	private RCPSingletonProvider() {
		Nop.reason("utility"); //$NON-NLS-1$
	}

	/**
	 * Return the requested wired RCP singleton.
	 * 
	 * @param singletonClass
	 *            the class to create a RCP singleton
	 * 
	 * @return the singleton
	 */
	public static <S> S getInstance(final Class<S> singletonClass) {
		return getInstance(singletonClass, null);
	}

	/**
	 * Return the requested probably initialized and wired RCP singleton.
	 * 
	 * @param singletonClass
	 *            the class to create a RCP singleton
	 * @param initializer
	 *            a optional initializer 'call back' (may be {@code null}
	 * 
	 * @return the singleton
	 */
	public static <S> S getInstance(final Class<S> singletonClass, final ISingletonInitializer<S> initializer) {
		boolean isAccessible = true;
		Constructor<S> constructor = null;
		try {
			constructor = singletonClass.getDeclaredConstructor();
			isAccessible = constructor.isAccessible();
			if (!isAccessible) {
				constructor.setAccessible(true);
			}
			final S result = constructor.newInstance();
			if (initializer != null) {
				initializer.init(result);
			}
			Wire.instance(result).andStart();
			return result;
		} catch (final Exception e) {
			throw new SingletonFailure("Could not instantiate RCP controlled singleton.", e); //$NON-NLS-1$
		} finally {
			if (!isAccessible && constructor != null) {
				constructor.setAccessible(false);
			}
		}
	}
}
