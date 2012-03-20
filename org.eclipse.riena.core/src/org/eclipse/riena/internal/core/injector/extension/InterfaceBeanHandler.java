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
package org.eclipse.riena.internal.core.injector.extension;

import static org.eclipse.riena.internal.core.injector.extension.InterfaceBeanHandler.MethodKind.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.injector.extension.CreateLazy;
import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.DoNotReplaceSymbols;
import org.eclipse.riena.core.injector.extension.DoNotWireExecutable;
import org.eclipse.riena.core.injector.extension.ExtensionInjector;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapContent;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.core.Activator;

/**
 * InvocationHandler for proxies that map to configuration elements.
 */
final class InterfaceBeanHandler implements InvocationHandler {

	private final Class<?> interfaceType;
	private final IConfigurationElement configurationElement;
	private final boolean symbolReplace;
	private final Map<Method, Result> resolved;

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), InterfaceBeanHandler.class);

	InterfaceBeanHandler(final Class<?> interfaceType, final boolean symbolReplace,
			final IConfigurationElement configurationElement) {
		this.interfaceType = interfaceType;
		this.configurationElement = configurationElement;
		this.symbolReplace = symbolReplace && !interfaceType.isAnnotationPresent(DoNotReplaceSymbols.class);
		this.resolved = new HashMap<Method, Result>();
		if (!interfaceType.isAnnotationPresent(ExtensionInterface.class)) {
			LOGGER.log(LogService.LOG_WARNING, "The interface '" + interfaceType.getName() //$NON-NLS-1$
					+ "' is NOT annotated with @" + ExtensionInterface.class.getSimpleName() + " but it should!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final MethodKind methodKind = MethodKind.of(method);
		synchronized (resolved) {
			Result result = resolved.get(method);
			if (result == null) {
				result = invoke(method, args, methodKind);
				if (result.cacheIt) {
					resolved.put(method, result);
				}
			}
			return result.object;
		}
	}

	private Result invoke(final Method method, final Object[] args, final MethodKind methodKind) throws Throwable {
		if (method.getParameterTypes().length == 0) {
			if (method.getName().equals("toString")) { //$NON-NLS-1$
				return Result.cache(proxiedToString());
			} else if (method.getName().equals("hashCode")) { //$NON-NLS-1$
				return Result.cache(proxiedHashCode());
			}
		}
		if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Object.class
				&& method.getName().equals("equals")) { //$NON-NLS-1$
			return Result.noCache(proxiedEquals(args[0]));
		}
		final Class<?> returnType = method.getReturnType();
		if (returnType == Bundle.class) {
			return Result.cache(ContributorFactoryOSGi.resolve(configurationElement.getContributor()));
		}
		if (returnType == IConfigurationElement.class) {
			return Result.cache(configurationElement);
		}
		final String attributeName = getAttributeName(method, methodKind);
		if (returnType == String.class) {
			final boolean methodSymbolReplace = !method.isAnnotationPresent(DoNotReplaceSymbols.class);
			return Result.noCache(modify(method.isAnnotationPresent(MapContent.class) ? configurationElement.getValue()
					: getAttribute(attributeName, method), methodSymbolReplace));
		}
		if (returnType.isPrimitive()) {
			final boolean methodSymbolReplace = !method.isAnnotationPresent(DoNotReplaceSymbols.class);
			return Result.noCache(coerce(returnType, modify(getAttribute(attributeName, method), methodSymbolReplace)));
		}
		if (returnType == Class.class) {
			String value = configurationElement.getAttribute(attributeName);
			if (value == null) {
				return Result.CACHED_NULL;
			}
			final Bundle bundle = ContributorFactoryOSGi.resolve(configurationElement.getContributor());
			if (bundle == null) {
				return Result.CACHED_NULL;
			}
			// does it contain initialization data?
			final int colon = value.indexOf(':');
			if (colon != -1) {
				value = value.substring(0, colon);
			}
			return Result.cache(loadClass(bundle, value));
		}
		if (returnType.isEnum()) {
			final String value = configurationElement.getAttribute(attributeName);
			if (StringUtils.isEmpty(value)) {
				return Result.CACHED_NULL;
			}
			for (final Object enumConstant : returnType.getEnumConstants()) {
				if (enumConstant.toString().equalsIgnoreCase(value)) {
					return Result.cache(enumConstant);
				}
			}
			throw new IllegalStateException("Invalid enum value '" + value + "' for enum type '" + returnType.getName() //$NON-NLS-1$ //$NON-NLS-2$
					+ "'" + within()); //$NON-NLS-1$
		}
		if (returnType.isInterface() && returnType.isAnnotationPresent(ExtensionInterface.class)) {
			final IConfigurationElement[] cfgElements = configurationElement.getChildren(attributeName);
			if (cfgElements.length == 0) {
				return Result.CACHED_NULL;
			}
			if (cfgElements.length == 1) {
				return Result.cache(Proxy.newProxyInstance(returnType.getClassLoader(), new Class[] { returnType },
						new InterfaceBeanHandler(returnType, symbolReplace, cfgElements[0])));
			}
			throw new IllegalStateException(
					"Got more than one configuration element but the interface expected exactly one, .i.e no array type has been specified for method '" //$NON-NLS-1$
							+ method + "'" + within()); //$NON-NLS-1$
		}
		if (returnType.isArray() && returnType.getComponentType().isInterface()) {
			final IConfigurationElement[] cfgElements = configurationElement.getChildren(attributeName);
			final Object[] result = (Object[]) Array.newInstance(returnType.getComponentType(), cfgElements.length);
			for (int i = 0; i < cfgElements.length; i++) {
				result[i] = Proxy.newProxyInstance(returnType.getComponentType().getClassLoader(),
						new Class[] { returnType.getComponentType() },
						new InterfaceBeanHandler(returnType.getComponentType(), symbolReplace, cfgElements[i]));
			}
			return Result.cache(result);
		}

		if (returnType == Void.class || (args != null && args.length > 0)) {
			throw new UnsupportedOperationException("Can not handle method '" + method + "'" + within());//$NON-NLS-1$ //$NON-NLS-2$
		}
		// Now try to create a fresh instance,i.e. createExecutableExtension()
		if (configurationElement.getAttribute(attributeName) == null
				&& configurationElement.getChildren(attributeName).length == 0) {
			return Result.CACHED_NULL;
		}
		final boolean wire = !(method.isAnnotationPresent(DoNotWireExecutable.class) || Boolean
				.getBoolean(ExtensionInjector.RIENA_EXTENSIONS_DONOTWIRE_SYSTEM_PROPERTY));
		if (method.isAnnotationPresent(CreateLazy.class)) {
			return Result.noCache(LazyExecutableExtension.newInstance(returnType, configurationElement, attributeName,
					wire));
		}
		final Object result = configurationElement.createExecutableExtension(attributeName);
		if (wire) {
			// Try wiring the created executable extension
			final Bundle bundle = ContributorFactoryOSGi.resolve(configurationElement.getContributor());
			BundleContext context = null;
			if (bundle != null) {
				context = bundle.getBundleContext();
			}
			if (context == null) {
				context = Activator.getDefault().getContext();
			}
			Wire.instance(result).andStart(context);
		}
		return Result.noCache(result);
	}

	private String within() {
		return " with in interface '" + interfaceType.getName() + "' and bundle '" //$NON-NLS-1$ //$NON-NLS-2$
				+ ContributorFactoryOSGi.resolve(configurationElement.getContributor()) + "'"; //$NON-NLS-1$
	}

	private Class<?> loadClass(final Bundle bundle, final String className) throws ClassNotFoundException {
		try {
			return bundle.loadClass(className);
		} catch (final ClassNotFoundException e) {
			// Are we a fragment bundle
			final Bundle[] hosts = Platform.getHosts(bundle);
			if (hosts == null) {
				throw e;
			}
			for (final Bundle host : hosts) {
				try {
					return host.loadClass(className);
				} catch (final ClassNotFoundException e2) {
					Nop.reason("try next host"); //$NON-NLS-1$
				}
			}
			throw e;
		}
	}

	public boolean proxiedEquals(final Object obj) {
		try {
			final InvocationHandler handler = Proxy.getInvocationHandler(obj);
			if (handler instanceof InterfaceBeanHandler) {
				return configurationElement.equals(((InterfaceBeanHandler) handler).configurationElement);
			}
		} catch (final IllegalArgumentException e) {
			return false;
		}
		return false;
	}

	public int proxiedHashCode() {
		return configurationElement.hashCode();
	}

	public String proxiedToString() {
		final StringBuilder bob = new StringBuilder("Dynamic proxy for "); //$NON-NLS-1$
		bob.append(interfaceType.getName()).append(':');
		final String[] names = configurationElement.getAttributeNames();
		for (final String name : names) {
			bob.append(name).append('=').append(configurationElement.getAttribute(name)).append(',');
		}
		bob.setLength(bob.length() - 1);
		return bob.toString();
	}

	private String getAttributeName(final Method method, final MethodKind methodKind) {
		final Annotation annotation = method.getAnnotation(MapName.class);
		if (annotation != null) {
			return ((MapName) annotation).value();
		}

		// No annotations
		if (methodKind == OTHER) {
			return null;
		}
		final String name = method.getName().substring(methodKind.prefix.length());
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	private String getAttribute(final String attributeName, final Method method) {
		final String value = configurationElement.getAttribute(attributeName);
		if (value != null) {
			return value;
		}
		final Annotation annotation = method.getAnnotation(DefaultValue.class);
		if (annotation != null) {
			return ((DefaultValue) annotation).value();
		}
		return null;
	}

	private Object coerce(final Class<?> toType, final String value) {
		if (toType == Long.TYPE) {
			return Long.valueOf(value);
		}
		if (toType == Integer.TYPE) {
			return Integer.valueOf(value);
		}
		if (toType == Boolean.TYPE) {
			return Boolean.valueOf(value);
		}
		if (toType == Float.TYPE) {
			return Float.valueOf(value);
		}
		if (toType == Double.TYPE) {
			return Double.valueOf(value);
		}
		if (toType == Short.TYPE) {
			return Short.valueOf(value);
		}
		if (toType == Character.TYPE) {
			return Character.valueOf(value.charAt(0));
		}
		if (toType == Byte.TYPE) {
			return Byte.valueOf(value);
		}
		return value;
	}

	private String modify(final String value, final boolean methodSymbolReplace) {
		if (!symbolReplace || !methodSymbolReplace || value == null) {
			return value;
		}

		final IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		if (variableManager == null) {
			return value;
		}

		try {
			return variableManager.performStringSubstitution(value);
		} catch (final CoreException e) {
			LOGGER.log(LogService.LOG_ERROR, "Could not perfrom string substitution for '" + value + "' .", e); //$NON-NLS-1$ //$NON-NLS-2$
			return value;
		}
	}

	private final static class Result {

		private final Object object;
		private final boolean cacheIt;

		private static final Result CACHED_NULL = Result.cache(null);

		private static Result noCache(final Object object) {
			return new Result(object, false);
		}

		private static Result cache(final Object object) {
			return new Result(object, true);
		}

		private Result(final Object object, final boolean cacheIt) {
			this.object = object;
			this.cacheIt = cacheIt;
		}
	}

	enum MethodKind {
		GET("get"), IS("is"), CREATE("create"), OTHER; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		private final String prefix;

		private MethodKind(final String kind) {
			this.prefix = kind;
		}

		private MethodKind() {
			this.prefix = null;
		}

		private static MethodKind of(final Method method) {
			final String name = method.getName();
			if (name.startsWith(GET.prefix)) {
				return GET;
			} else if (name.startsWith(IS.prefix)) {
				return IS;
			} else if (name.startsWith(CREATE.prefix)) {
				return CREATE;
			}
			return OTHER;
		}

		@Override
		public String toString() {
			return prefix;
		}

	}

}
