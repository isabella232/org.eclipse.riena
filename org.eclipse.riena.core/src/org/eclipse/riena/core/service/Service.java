/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *               compeople AG    - this is a beefed up (modified for generics, changed names, 
 *                                 added convenience methods, ..) version of p2´s  
 *                                 org.eclipse.equinox.internal.p2.core.helpers.ServiceHelper
 ******************************************************************************/
package org.eclipse.riena.core.service;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.internal.core.Activator;

/**
 * Helper to get immediate access to services.<br>
 * The {@code Service} helper class gives immediate access to a service.
 * <p>
 * {@code Service.get(...)} returns a service instance described by the given
 * arguments. The returned service is already returned {@code unget()} to the
 * service registry. This results in a window where the system thinks the
 * service is not in use but indeed the caller is about to use the returned
 * service object. This service instance should not be kept in fields.
 * <p>
 * <b>Note:</b> This class can be used where it is not (easily) possible to use
 * the service injector.
 * 
 * @since 1.2
 */
public final class Service {

	private Service() {
		// utility
	}

	/**
	 * Returns the service described by the given arguments. Note that this is a
	 * helper class that <b>immediately</b> ungets the service reference. This
	 * results in a window where the system thinks the service is not in use but
	 * indeed the caller is about to use the returned service object.
	 * 
	 * @param <S>
	 * @param context
	 * @param clazz
	 * @return The requested service or {@code null} if not present
	 */
	@SuppressWarnings("unchecked")
	public static <S> S get(final BundleContext context, final Class<S> clazz) {
		Assert.isNotNull(context, "BundleContext must not be null."); //$NON-NLS-1$
		Assert.isNotNull(clazz, "Class must not be null."); //$NON-NLS-1$
		try {
			final ServiceReference reference = context.getServiceReference(clazz.getName());
			if (reference == null) {
				return null;
			}
			final S result = (S) context.getService(reference);
			context.ungetService(reference);
			return result;
		} catch (final RuntimeException e) {
			return null;
		}
	}

	/**
	 * This version uses this bundles {@code BundleContext}. <br>
	 * Otherwise it behaves as {@link get(BundleContext context, Class&lt;S&gt;
	 * clazz)}
	 * 
	 * @param <S>
	 * @param clazz
	 * @return The requested service or {@code null} if not present
	 */
	public static <S> S get(final Class<S> clazz) {
		return get(Activator.getDefault().getContext(), clazz);
	}

	/**
	 * Returns the service described by the given arguments. Note that this is a
	 * helper class that <b>immediately</b> ungets the service reference. This
	 * results in a window where the system thinks the service is not in use but
	 * indeed the caller is about to use the returned service object.
	 * 
	 * @param <S>
	 * @param context
	 * @param clazz
	 * @param filter
	 * @return The requested service or {@code null} if not present
	 */
	@SuppressWarnings("unchecked")
	public static <S> S get(final BundleContext context, final Class<S> clazz, final String filter) {
		Assert.isNotNull(context, "BundleContext must not be null."); //$NON-NLS-1$
		Assert.isNotNull(clazz, "Class must not be null."); //$NON-NLS-1$
		try {
			final ServiceReference[] references = context.getServiceReferences(clazz.getName(), filter);
			if (references == null || references.length == 0) {
				return null;
			}
			final S result = (S) context.getService(references[0]);
			context.ungetService(references[0]);
			return result;
		} catch (final InvalidSyntaxException e) {
			throw new IllegalArgumentException("Given filter expression has a syntax error.", e); //$NON-NLS-1$
		} catch (final RuntimeException e) {
			return null;
		}
	}

	/**
	 * This version uses this bundles {@code BundleContext}. <br>
	 * Otherwise it behaves as {@link get(BundleContext context, Class&lt;S&gt;
	 * clazz, String filter)}
	 * 
	 * @param <S>
	 * @param clazz
	 * @param filter
	 * @return The requested service or {@code null} if not present
	 */
	public static <S> S get(final Class<S> clazz, final String filter) {
		return get(Activator.getDefault().getContext(), clazz, filter);
	}

}
