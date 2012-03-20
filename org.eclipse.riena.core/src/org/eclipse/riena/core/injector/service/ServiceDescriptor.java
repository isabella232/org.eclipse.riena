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
package org.eclipse.riena.core.injector.service;

import org.eclipse.core.runtime.Assert;

/**
 * ServiceDescriptor and ServiceInjector simplify finding of OSGi Services and
 * inject them into a target object.
 * <p>
 * To do so the ServiceInjector contains a service tracker listening to
 * appearing and disappearing services and injects them into the target. A
 * target object defines named and typed bind and unbind methods. The
 * ServiceInjector calls the bind method when the specified service was
 * registered or modified. ServiceInjector calls the unbind method when the
 * specified service becomes unregistered.
 * <p>
 * The service injector tracks the specified OSGi Service with
 * {@link #andStart()} and stops tracking with {@link #stop()}. If a bundle
 * associated with a service injector {@link #andStart()} gets stopped all bound
 * services will be unbound.
 * <p>
 * The ServiceDescriptor and ServiceInjector are implemented as a ´fluent
 * interface´ allowing constructs like:
 * <ul>
 * <li>Inject.service("id1").into(target).andStart(context)</li>
 * <li>
 * Inject.service("id2").useFilter(filter).into(target).bind("register").unbind
 * ("unregister").andStart(context)</li>
 * <li>Inject.service("id3").useRanking().into(target).bind("register").unbind(
 * "unregister").andStart(context)</li>
 * <li>..</li>
 * </ul>
 * <p>
 * This fluent interface makes a few assumptions (defaults) that makes writing
 * service injectors short and expressive , e.g. the first example in the above
 * list means: use no service ranking, no filter, the bind method name is "bind"
 * and the un-bind method name is "unbind".
 * <p>
 * A service or services may be injected into the target by specifying
 * <code>useRanking()</code> and/or by specifying a filter expression with
 * <code>useFilter("filter")</code>.
 * <p>
 * The bind and unbind method that get called by the injector can be
 * polymorphic, i.e. the target can have multiple bind/un-bind methods with the
 * same name but with different signatures. The injector takes responsibility
 * for choosing the appropriate bind/unbind methods.
 */
public class ServiceDescriptor {

	private final String className;
	private final Class<?> clazz;
	private boolean ranking;
	private String filter;

	/**
	 * Create a service descriptor.
	 * 
	 * @param className
	 * @throws some_kind_of_unchecked_exception
	 *             if service descriptor is null.
	 */
	public ServiceDescriptor(final String className) {
		Assert.isNotNull(className, "Service className must not be null."); //$NON-NLS-1$
		this.className = className;
		this.clazz = null;
	}

	/**
	 * Create a service descriptor.
	 * 
	 * @param clazz
	 * @throws some_kind_of_unchecked_exception
	 *             if service descriptor is null.
	 */
	public ServiceDescriptor(final Class<?> clazz) {
		Assert.isNotNull(clazz, "Service clazz must not be null."); //$NON-NLS-1$
		this.clazz = clazz;
		this.className = null;
	}

	/**
	 * Use filtering {@link org.osgi.framework.Filter} for picking the tracked
	 * service(s) to inject.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if a filter has already been set.
	 * @return this service descriptor
	 */
	public ServiceDescriptor useFilter(final String filter) {
		Assert.isTrue(this.filter == null, "Filter has already been set!"); //$NON-NLS-1$
		Assert.isNotNull(filter, "Filter must not be null."); //$NON-NLS-1$
		this.filter = filter;
		return this;
	}

	/**
	 * Use service ranking {@link org.osgi.framework.Constants#SERVICE_RANKING}
	 * picking the tracked service to inject.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if ranking has already been activated
	 * @return this service descriptor
	 */
	public ServiceDescriptor useRanking() {
		Assert.isTrue(!ranking, "Ranking has already been set!"); //$NON-NLS-1$
		ranking = true;
		return this;
	}

	/**
	 * Inject this service descriptor into the specified target.
	 * 
	 * @param target
	 * @throws some_kind_of_unchecked_exception
	 *             on target == null
	 * @return the injector responsible for tracking this service descriptor
	 */
	public ServiceInjector into(final Object target) {
		Assert.isNotNull(target, "Target must not be null."); //$NON-NLS-1$
		return ranking ? new RankingInjector(this, target) : new FilterInjector(this, target);
	}

	String getServiceClassName() {
		return className == null ? clazz.getName() : className;
	}

	Class<?> getServiceClass() {
		return clazz;
	}

	String getFilter() {
		return filter;
	}

}
