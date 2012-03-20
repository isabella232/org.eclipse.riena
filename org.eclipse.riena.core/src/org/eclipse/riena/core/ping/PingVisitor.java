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
package org.eclipse.riena.core.ping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.riena.core.util.Nop;

/**
 * The {@code PingVisitor} is responsible for crawling down the <i>hierarchy</i>
 * of pingable services. The entry point for starting a ping is method
 * {@link #ping(IPingable) ping()}.
 */
public class PingVisitor {

	protected final List<PingFingerprint> cycleDectector = new ArrayList<PingFingerprint>();
	protected final List<PingResult> pingResultList = new ArrayList<PingResult>();
	protected final Stack<PingResult> resultStack = new Stack<PingResult>();
	private final String name;

	/**
	 * Default Constructor
	 */
	public PingVisitor() {
		name = "PingVisitor#" + System.identityHashCode(this); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Return a List of the {@link PingResult PingResult}s.
	 * 
	 * @return a List of the {@link PingResult PingResult}s.
	 */
	public List<PingResult> getPingResults() {
		return pingResultList;
	}

	/**
	 * Calls {@link IPingable#ping(PingVisitor) ping()} on all members that
	 * implement the {@link IPingable IPingable} interface. If you have
	 * additional IPingables that are not members, just provide them in a method
	 * <code>Iterable&lt;IPingable&gt; getAdditionalPingables()</code>. Also all
	 * methods that start with <code>ping</code> (see
	 * {@link #isPingMethod(IPingable, Method) isPingMethod()}) are called,
	 * which is useful to ping non-Java objects such as Databases ( e.g.
	 * <code>pingDatabase()</code>). If a cycle is detected, the method returns
	 * immediately.
	 * 
	 * @param pingable
	 *            the IPingable to visit.
	 * @return this.
	 */
	public PingVisitor visit(final IPingable pingable) {

		PingVisitor visitor = this;
		PingFingerprint fingerprint = null;
		fingerprint = pingable.getPingFingerprint();
		if (cycleDectector.contains(fingerprint)) {
			// cycle detected
			return visitor;
		}

		cycleDectector.add(fingerprint);

		// do not recurse on PingMethodAdapter
		if (pingable instanceof PingMethodAdapter) {
			return visitor;
		}

		try {
			final Collection<IPingable> children = getChildPingablesOf(pingable);
			for (final IPingable child : children) {
				visitor = visitor.ping(child);
			}
		} finally {
			visitor.cycleDectector.remove(fingerprint);
		}
		return visitor;
	}

	/**
	 * This is the entry point to the pingable API. This method creates a
	 * {@link PingResult PingResult}, calls {@link IPingable#ping(PingVisitor)
	 * ping() } on the given pingable, catches any occurring Exception and
	 * reports it in the PingResult.
	 * 
	 * @param pingable
	 *            the {@link IPingable IPingable} to ping.
	 * 
	 * @return the PingVisitor. It might not be 'this' (e.g. in case of remote
	 *         calls where the visitor is serialized), so always work on the one
	 *         returned by this method.
	 */
	public PingVisitor ping(final IPingable pingable) {
		PingVisitor visitor = this;
		PingResult pingResult = new PingResult(getPingableName(pingable));

		if (resultStack.isEmpty()) {
			// root of a new ping, add to list
			pingResultList.add(pingResult);
		} else {
			final PingResult parent = resultStack.peek();
			parent.addNestedResult(pingResult);
		}
		resultStack.push(pingResult);

		Exception caughtException = null;
		try {
			visitor = pingable.ping(visitor);
		} catch (final Exception e) {
			caughtException = e;
		} finally {
			pingResult = visitor.resultStack.pop();
			pingResult.setPingFailure(caughtException);
		}

		return visitor;
	}

	/**
	 * Returns all IPingable belonging to the given one.
	 * 
	 * @param pingable
	 * @return all IPingable belonging to the given one.
	 */
	public Collection<IPingable> getChildPingablesOf(final IPingable pingable) {
		final Set<IPingable> pingableList = new HashSet<IPingable>();
		collectPingableMembers(pingable, pingableList);
		collectPingMethods(pingable, pingableList);
		collectAdditionalPingables(pingable, pingableList);
		return pingableList;
	}

	/**
	 * Collects all <code>ping..()</code> methods.
	 * 
	 * @param pingable
	 * @param pingableList
	 * @see #isPingMethod(IPingable, Method)
	 */
	public void collectPingMethods(final IPingable pingable, final Set<IPingable> pingableList) {
		collectPingMethods(pingable.getClass(), pingable, pingableList);
	}

	private void collectPingMethods(final Class<?> clazz, final IPingable pingable, final Set<IPingable> pingableList) {
		final Method[] methods = clazz.getDeclaredMethods();
		for (final Method method : methods) {
			if (isPingMethod(pingable, method)) {
				setAccessible(method);
				pingableList.add(new PingMethodAdapter(pingable, method));
			}
		}
		final Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			collectPingMethods(superClass, pingable, pingableList);
		}
	}

	/**
	 * Collects all IPingable members.
	 * 
	 * @param pingable
	 * @param pingableList
	 */
	public void collectPingableMembers(final IPingable pingable, final Set<IPingable> pingableList) {
		final Field[] fields = pingable.getClass().getDeclaredFields();
		for (final Field field : fields) {
			if (!IPingable.class.isAssignableFrom(field.getType())) {
				continue;
			}
			try {
				setAccessible(field);
				if (field.get(pingable) == null) {
					// skip null members
					continue;
				}
				pingableList.add((IPingable) field.get(pingable));
			} catch (final Exception e) {
				pingableList.add(new UnavailablePingable(field.getName(),
						"Pingable member " + field.getName() + " not accessible: " + e.getMessage())); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	/**
	 * Collects all IPingable provided by (optional) method
	 * <code>getAdditionalPingables()</code>..
	 * 
	 * @param pingable
	 * @param pingableList
	 */
	public void collectAdditionalPingables(final IPingable pingable, final Set<IPingable> pingableList) {
		collectAdditionalPingables(pingable.getClass(), pingable, pingableList);
	}

	private void collectAdditionalPingables(final Class<?> clazz, final IPingable pingable,
			final Set<IPingable> pingableList) {
		try {
			final Method method = clazz.getDeclaredMethod("getAdditionalPingables", new Class[0]); //$NON-NLS-1$
			setAccessible(method);
			final Type returnType = method.getGenericReturnType();
			if (isIterableOfPingables(returnType)) {
				final Iterable<IPingable> pingables = (Iterable<IPingable>) method.invoke(pingable, new Object[0]);
				for (final IPingable additionalPingable : pingables) {
					pingableList.add(additionalPingable);
				}
			}
		} catch (final NoSuchMethodException nsme) {
			Nop.reason("no getAdditionalPingables() method"); //$NON-NLS-1$
		} catch (final Exception e) {
			pingableList.add(new UnavailablePingable("getAdditionalPingables", //$NON-NLS-1$
					"Method getAdditionalPingables() not accessible: " + e.getMessage())); //$NON-NLS-1$
		}
		final Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			collectAdditionalPingables(superClass, pingable, pingableList);
		}
	}

	private void setAccessible(final Method method) {
		try {
			method.setAccessible(true);
		} catch (final SecurityException e) {
			Nop.reason("security restriction, hopefully it's public :-|"); //$NON-NLS-1$
		}
	}

	private void setAccessible(final Field field) {
		try {
			field.setAccessible(true);
		} catch (final SecurityException e) {
			Nop.reason("security restriction, hopefully it's public :-|"); //$NON-NLS-1$
		}
	}

	private boolean isIterableOfPingables(final Type returnType) {
		if (!(returnType instanceof ParameterizedType)) {
			return false;
		}
		final ParameterizedType parameterizedType = (ParameterizedType) returnType;
		if (!(parameterizedType.getRawType() == Iterable.class)) {
			return false;
		}
		Type[] types = parameterizedType.getActualTypeArguments();
		if (types.length != 1) {
			return false;
		}

		Type type = types[0];
		if (type instanceof WildcardType) {
			types = ((WildcardType) type).getUpperBounds();
			if (types.length != 1) {
				return false;
			}
			type = types[0];
		}

		return (type instanceof Class<?>) && ((Class<?>) type).getName().equals(IPingable.class.getName());

	}

	/**
	 * Returns <code>true</code> if it is a <code>ping...()</code> method. A
	 * ping() method must start with <code>ping</code> followed by an upper case
	 * letter. It must be non-arg and returning void.
	 * 
	 * @param pingable
	 *            the Pingable object.
	 * @param method
	 *            the method to check.
	 * @return <code>true</code> if it is a <code>ping...()</code> method.
	 */
	protected static boolean isPingMethod(final IPingable pingable, final Method method) {
		if (!method.getName().startsWith("ping")) { //$NON-NLS-1$
			return false;
		}
		if (method.getName().length() <= 4) {
			return false;
		}
		if (!Character.isUpperCase(method.getName().charAt(4))) {
			return false;
		}
		if (method.getParameterTypes().length != 0) {
			return false;
		}
		if (method.getReturnType() != Void.TYPE) {
			return false;
		}

		return true;
	}

	private static String getPingableName(final IPingable pingable) {
		try {
			return pingable.getPingFingerprint().getName();
		} catch (final Exception e) {
			return pingable.toString();
		}
	}

}
