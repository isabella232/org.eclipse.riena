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
package org.eclipse.riena.core.wire;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.injector.IStoppable;
import org.eclipse.riena.core.injector.InjectionFailure;
import org.eclipse.riena.core.injector.extension.ExtensionInjector;
import org.eclipse.riena.core.injector.service.ServiceInjector;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.core.util.WeakRef;
import org.eclipse.riena.internal.core.ignore.IgnoreFindBugs;
import org.eclipse.riena.internal.core.wire.ExtensionInjectorBuilder;
import org.eclipse.riena.internal.core.wire.ServiceInjectorBuilder;

/**
 * The {@code WirePuller} is responsible for the wiring of a bean.
 */
public class WirePuller implements IStoppable {

	private final WeakRef<?> beanRef;
	private BundleContext context;
	private List<IWiring> wirings;
	private List<IStoppable> injections;
	private State state = State.PENDING;
	private BundleListener bundleStoppingListener;

	private static final Comparator<MAOTupel> ORDER_COMPARATOR = new OrderComparator();

	// Only for unit testing of classes using accessors
	private static Map<Class<?>, Class<? extends IWiring>> wiringMocks;

	private static final Logger LOGGER = Log4r.getLogger(WirePuller.class);

	/**
	 * Create a 'wire puller' for the given bean.
	 * 
	 * @param bean
	 */
	WirePuller(final Object bean) {
		Assert.isLegal(bean != null, "bean must exist"); //$NON-NLS-1$
		this.beanRef = new WeakRef<Object>(bean, new Runnable() {
			public void run() {
				stop();
			}
		});
	}

	/**
	 * Start the wiring.<br>
	 * This method tries to retrieve the required {@code BundleContext} from the
	 * {@code bean} that should be wired. This requires that the OSGi runtime is
	 * up - which might not the case in unit tests. However, in such a case no
	 * wiring will be done and this method returns {@code null}.
	 * 
	 * @return the {@code WirePuller} or null if wiring was not possible
	 * @since 3.0
	 */
	public synchronized WirePuller andStart() {
		final Class<?> beanClass = getBeanClass();
		if (beanClass == null) {
			LOGGER.log(LogService.LOG_WARNING, "Could not wire bean because it is aleady garbage collected."); //$NON-NLS-1$
			return null;
		}
		final Bundle bundle = FrameworkUtil.getBundle(beanClass);
		if (bundle != null) {
			return andStart(bundle.getBundleContext());
		}
		LOGGER.log(LogService.LOG_WARNING, "Could not wire bean '" + beanClass //$NON-NLS-1$
				+ "' because there is no bundle context."); //$NON-NLS-1$
		return null;
	}

	/**
	 * Start the wiring.
	 * 
	 * @param context
	 *            the bundle context
	 * @return the {@code WirePuller}
	 */
	public synchronized WirePuller andStart(final BundleContext context) {
		Assert.isLegal(context != null, "context must be given."); //$NON-NLS-1$
		Assert.isLegal(state == State.PENDING, "state must be pending."); //$NON-NLS-1$
		this.context = context;
		if (!wire(getBean(), getBeanClass())) {
			return this;
		}
		state = State.STARTED;
		bundleStoppingListener = new BundleStoppingListener();
		context.addBundleListener(bundleStoppingListener);
		return this;
	}

	/**
	 * Stop the wiring.
	 */
	public synchronized void stop() {
		if (state != State.STARTED) {
			return;
		}
		context.removeBundleListener(bundleStoppingListener);
		final Object bean = getBean();
		if (bean != null && wirings != null) {
			for (final IWiring wiring : wirings) {
				wiring.unwire(bean, context);
			}
		}
		for (final IStoppable stoppable : Iter.ableReverse(injections)) {
			stoppable.stop();
		}
		state = State.STOPPED;
	}

	private boolean wire(final Object bean, final Class<?> beanClass) {
		if (beanClass == null || bean == null || beanClass == Object.class) {
			return false;
		}
		final IWiring wiring = getWiring(getWiringClass(beanClass));
		add(wiring);
		boolean hasWirings = wire(bean, beanClass.getSuperclass());
		if (wiring != null) {
			wiring.wire(bean, context);
			hasWirings = true;
		}
		return injectIntoAnnotatedMethods(bean, beanClass) | hasWirings;
	}

	private boolean injectIntoAnnotatedMethods(final Object bean, final Class<?> beanClass) {
		boolean hasWirings = false;
		for (final MAOTupel maot : sortMethodsByInjectionOrder(beanClass.getDeclaredMethods())) {
			if (maot.annotation == InjectService.class) {
				verifyOrder(beanClass, maot);
				injectServiceInto(bean, maot.method);
				hasWirings = true;
			} else if (maot.annotation == InjectExtension.class) {
				verifyOrder(beanClass, maot);
				injectExtensionInto(bean, maot.method);
				hasWirings = true;
			} else if (maot.annotation == OnWiringDone.class) {
				notifyBean(bean, maot.method);
			}
		}
		return hasWirings;
	}

	private void verifyOrder(final Class<?> beanClass, final MAOTupel maot) {
		if (maot.getOrder() == Integer.MAX_VALUE) {
			throw new InjectionFailure("Annotation '" + maot.annotation + "' on '" + beanClass.getName() + "." //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ maot.method.getName() + "' has forbidden order Integer.MAX_VALUE"); //$NON-NLS-1$
		}
	}

	private List<MAOTupel> sortMethodsByInjectionOrder(final Method[] methods) {
		final List<MAOTupel> maots = new ArrayList<MAOTupel>();
		for (final Method method : methods) {
			final InjectService injectServiceAnnotation = method.getAnnotation(InjectService.class);
			if (injectServiceAnnotation != null) {
				maots.add(new MAOTupel(method, InjectService.class, injectServiceAnnotation.order()));
			}
			final InjectExtension injectExtensionAnnotation = method.getAnnotation(InjectExtension.class);
			if (injectExtensionAnnotation != null) {
				maots.add(new MAOTupel(method, InjectExtension.class, injectExtensionAnnotation.order()));
			}
			final OnWiringDone wiringDoneAnnotation = method.getAnnotation(OnWiringDone.class);
			if (wiringDoneAnnotation != null) {
				maots.add(new MAOTupel(method, OnWiringDone.class, Integer.MAX_VALUE));
			}
		}
		Collections.sort(maots, ORDER_COMPARATOR);
		return maots;
	}

	private void injectServiceInto(final Object bean, final Method method) {
		final ServiceInjectorBuilder bob = new ServiceInjectorBuilder(bean, method);
		final ServiceInjector injector = bob.build();
		add(injector.andStart(context));
	}

	private void injectExtensionInto(final Object bean, final Method method) {
		final ExtensionInjectorBuilder bob = new ExtensionInjectorBuilder(bean, method);
		final ExtensionInjector injector = bob.build();
		add(injector.andStart(context));
	}

	private void notifyBean(final Object bean, final Method method) {
		try {
			method.invoke(bean);
		} catch (final Exception e) {
			throw new InjectionFailure("Invoking the @WiringDone method '" + method + "' on bean class '" //$NON-NLS-1$ //$NON-NLS-2$
					+ bean.getClass().getName() + "'.", e); //$NON-NLS-1$
		}
	}

	/**
	 * Collect the wirings.
	 * 
	 * @param wiring
	 */
	private void add(final IWiring wiring) {
		if (wiring == null) {
			return;
		}
		if (wirings == null) {
			wirings = new ArrayList<IWiring>(2);
		}
		wirings.add(wiring);
	}

	/**
	 * Collect the stoppable injections.
	 * 
	 * @param stoppable
	 */
	private void add(final IStoppable stoppable) {
		if (injections == null) {
			injections = new ArrayList<IStoppable>();
		}
		injections.add(stoppable);
	}

	private IWiring getWiring(final Class<? extends IWiring> wiringClass) {
		if (wiringClass == null) {
			return null;
		}
		try {
			return wiringClass.newInstance();
		} catch (final InstantiationException e) {
			throw new IllegalStateException("Could not create instance of wiring " + wiringClass, e); //$NON-NLS-1$
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException("Could not create instance of wiring " + wiringClass, e); //$NON-NLS-1$
		}
	}

	private Class<? extends IWiring> getWiringClass(final Class<?> beanClass) {
		// If mocks are defined, we use them instead of the regular mechanism.
		if (wiringMocks != null) {
			return wiringMocks.get(beanClass);
		}
		final WireWith wiringAnnotation = beanClass.getAnnotation(WireWith.class);
		// Does the bean have a wiring annotation? Yes, use this class for wiring.
		if (wiringAnnotation != null) {
			return wiringAnnotation.value();
		}
		return null;
	}

	/**
	 * Stops this wire-puller if the owning bundle is stopping.
	 */
	private class BundleStoppingListener implements SynchronousBundleListener {
		@IgnoreFindBugs(value = "IS2_INCONSISTENT_SYNC", justification = "Field context of WirePuller is written only before this listener is added, field bundle of BundleContext is immutable.")
		public void bundleChanged(final BundleEvent event) {
			if (event.getBundle() != context.getBundle()) {
				return;
			}
			if (event.getType() == BundleEvent.STOPPING) {
				stop();
			}
		}
	}

	// Deref bean
	private Object getBean() {
		return beanRef.get();
	}

	// Deref bean class
	private Class<?> getBeanClass() {
		final Object bean = getBean();
		return bean == null ? null : bean.getClass();
	}

	/**
	 * For testing purposes it is sometimes necessary to change the default
	 * behavior for retrieving the wiring classes. With this method it is
	 * possible to define a map that tells the {@codeWirePuller} how to find the
	 * wiring classes.
	 * 
	 * @param wiringMocks
	 */
	public static void injectWiringMocks(final Map<Class<?>, Class<? extends IWiring>> wiringMocks) {
		WirePuller.wiringMocks = wiringMocks;
	}

	private enum State {
		STARTED, STOPPED, PENDING
	}

	private static class MAOTupel {

		private final Method method;
		private final Class<?> annotation;
		private final int order;

		public MAOTupel(final Method method, final Class<?> annotation, final int order) {
			this.method = method;
			this.annotation = annotation;
			this.order = order;
		}

		public int getOrder() {
			return order;
		}
	}

	/**
	 * Compares MAOTuples by order.
	 */
	private static final class OrderComparator implements Comparator<MAOTupel> {
		public int compare(final MAOTupel tupel1, final MAOTupel tupel2) {
			return tupel1.getOrder() == tupel2.getOrder() ? 0 : tupel1.getOrder() < tupel2.getOrder() ? -1 : 1;
		}
	}
}
