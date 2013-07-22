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
package org.eclipse.riena.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.SynchronousBundleListener;

import org.eclipse.riena.core.exception.Failure;
import org.eclipse.riena.core.wire.Wire;

/**
 * The utility class {@code Companion} tries to mimic the companion object known from Scala.
 * <p>
 * With this it is possible to create a single instance for any (a few restrictions of course) given class. It should be noted that this is different from the
 * <i>singleton pattern</i> where the class which implements the singleton has to have a static {@code getInstance()} method. The {@code Companion} does not
 * need this!
 * <p>
 * However, classes that can be used with the {@code Companion} must implement a default constructor. The default constructor can be {@code private} or
 * {@code protected} if there is no security policy preventing to change the accessibility.
 * <p>
 * The {@code Companion} also <b>wires</b> its single object. The companion object will be hold as long as the bundle containing the companion class is active.<br>
 * This behavior ensures that also the class may be garbage collected and thus the whole bundle could be stopped.
 * 
 * @since 4.0
 */
public final class Companion {

	private static final Map<Class<Object>, Object> CLASS_TO_COMPANION = new HashMap<Class<Object>, Object>();
	private static final Map<Bundle, List<Class<Object>>> BUNDLE_TO_CLASSES = new HashMap<Bundle, List<Class<Object>>>();

	private static final Bundle THIS_BUNDLE = FrameworkUtil.getBundle(Companion.class);
	private static final SynchronousBundleListener FREE_ME = new FreeMe();
	private static final Object LOCK = new Object();
	static {
		if (THIS_BUNDLE != null) {
			final BundleContext bundleContext = THIS_BUNDLE.getBundleContext();
			if (bundleContext != null) {
				bundleContext.addBundleListener(FREE_ME);
			}
		}
	}

	private Companion() {
		// utility
	}

	/**
	 * Get the companion object for the given class.
	 * 
	 * @param companionClass
	 *            the class to get the companion object for
	 * @return the companion object
	 */
	public static synchronized <T> T per(final Class<T> companionClass) {
		synchronized (LOCK) {
			T companion = (T) CLASS_TO_COMPANION.get(companionClass);
			if (companion == null) {
				companion = createCompanion(companionClass);
				Wire.instance(companion).andStart();
				register(companionClass, companion);
			}
			return companion;
		}
	}

	/**
	 * @since 5.0
	 */
	public static synchronized void reset() {
		synchronized (LOCK) {
			CLASS_TO_COMPANION.clear();
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> void register(final Class<T> companionClass, final T companion) {
		CLASS_TO_COMPANION.put((Class<Object>) companionClass, (Object) companion);
		final Bundle bundle = FrameworkUtil.getBundle(companionClass);
		if (bundle == null) {
			return;
		}
		List<Class<Object>> classes = BUNDLE_TO_CLASSES.get(bundle);
		if (classes == null) {
			classes = new ArrayList<Class<Object>>();
			BUNDLE_TO_CLASSES.put(bundle, classes);
		}
		classes.add((Class<Object>) companionClass);
	}

	private final static class FreeMe implements SynchronousBundleListener {

		public void bundleChanged(final BundleEvent event) {
			if (event.getType() != BundleEvent.STOPPING) {
				return;
			}

			synchronized (LOCK) {
				final List<Class<Object>> remove = BUNDLE_TO_CLASSES.remove(event.getBundle());
				for (final Class<Object> clazz : Iter.able(remove)) {
					CLASS_TO_COMPANION.remove(clazz);
				}
			}
			if (event.getBundle().equals(THIS_BUNDLE)) {
				final BundleContext bundleContext = THIS_BUNDLE.getBundleContext();
				if (bundleContext != null) {
					bundleContext.removeBundleListener(FREE_ME);
				}
			}
		}
	}

	private static <T> T createCompanion(final Class<T> companionClass) {
		try {
			final Constructor<T> constructor = companionClass.getDeclaredConstructor();
			final boolean isAccessible = constructor.isAccessible();
			try {
				if (!isAccessible) {
					constructor.setAccessible(true);
				}
				return constructor.newInstance();
			} finally {
				if (!isAccessible) {
					constructor.setAccessible(false);
				}
			}
		} catch (final SecurityException e) {
			throw new CompanionCreationFailure("Could not create an instance for " + companionClass.getName(), e); //$NON-NLS-1$
		} catch (final NoSuchMethodException e) {
			throw new CompanionCreationFailure("Could not create an instance for " + companionClass.getName(), e); //$NON-NLS-1$
		} catch (final IllegalArgumentException e) {
			throw new CompanionCreationFailure("Could not create an instance for " + companionClass.getName(), e); //$NON-NLS-1$
		} catch (final InstantiationException e) {
			throw new CompanionCreationFailure("Could not create an instance for " + companionClass.getName(), e); //$NON-NLS-1$
		} catch (final IllegalAccessException e) {
			throw new CompanionCreationFailure("Could not create an instance for " + companionClass.getName(), e); //$NON-NLS-1$
		} catch (final InvocationTargetException e) {
			throw new CompanionCreationFailure("Could not create an instance for " + companionClass.getName(), e); //$NON-NLS-1$
		}
	}

	private final static class CompanionCreationFailure extends Failure {

		private static final long serialVersionUID = 1L;

		public CompanionCreationFailure(final String msg, final Throwable cause) {
			super(msg, cause);
		}

	}

}
