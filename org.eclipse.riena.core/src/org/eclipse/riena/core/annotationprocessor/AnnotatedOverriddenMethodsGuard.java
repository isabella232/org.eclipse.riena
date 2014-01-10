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
package org.eclipse.riena.core.annotationprocessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@code AnnotatedOverriddenMethodsGuard} helps annotation processors to
 * figure out whether a ('overridden') method with the same properties has
 * already been handled. And thus avoids registering multiple listeners for a
 * single overridden method.
 * 
 * @since 4.0
 */
public class AnnotatedOverriddenMethodsGuard {

	private final Set<ShallowAnnotatedMethod> annotatedMethods = new HashSet<ShallowAnnotatedMethod>();

	/**
	 * Tries to add the specified combination of annotation and method to this
	 * guard. If the combination is not already present it will be added and
	 * {@code true} will be returned. If this guard already contains the
	 * specified combination, the call leaves this guard unchanged and returns
	 * <tt>false</tt>.
	 * 
	 * @param annotation
	 *            the annotation relevant to the given {@code method} parameter
	 * @param method
	 *            the method to be guarded
	 * @return
	 */
	public boolean add(final Annotation annotation, final Method method) {
		return annotatedMethods.add(new ShallowAnnotatedMethod(annotation, method));
	}

	/**
	 * This class is a 'shallow' method, i.e. it contains only a few properties
	 * of the original {@code Method} class and it contains an annotation that
	 * is relevant for this method.<br>
	 * The primary goal of this class is to provide a key for a {@code Set}.
	 */
	private static class ShallowAnnotatedMethod {

		private final Annotation annotation;
		private final String name;
		private final Class<?>[] parameterTypes;
		private final boolean isPrivate;

		public ShallowAnnotatedMethod(final Annotation annotation, final Method method) {
			this.annotation = annotation;
			this.name = method.getName();
			this.parameterTypes = method.getParameterTypes();
			this.isPrivate = Modifier.isPrivate(method.getModifiers());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((annotation == null) ? 0 : annotation.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + Arrays.hashCode(parameterTypes);
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof ShallowAnnotatedMethod)) {
				return false;
			}
			final ShallowAnnotatedMethod other = (ShallowAnnotatedMethod) obj;
			if (this.isPrivate || other.isPrivate) {
				return false;
			}
			if (annotation == null) {
				if (other.annotation != null) {
					return false;
				}
			} else if (!annotation.equals(other.annotation)) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (!Arrays.equals(parameterTypes, other.parameterTypes)) {
				return false;
			}
			return true;
		}

	}
}