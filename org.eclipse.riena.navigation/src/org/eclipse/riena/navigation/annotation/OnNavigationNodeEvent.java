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
package org.eclipse.riena.navigation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;

/**
 * This annotation is used to mark a method as target for an automatically
 * generated listener in {@link INavigattionNodeController} controllers like:
 * 
 * <pre>
 * INavigationNode.addSimpleListener({@link ISimpleNavigationNodeListener})
 * </pre>
 * 
 * The {@code event} parameter denotes the corresponding
 * {@link ISimpleNavigationNodeListener} event method.<br>
 * The annotated method may either have all parameters of the corresponding
 * listener event method or none.
 * 
 * @since 3.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnNavigationNodeEvent {

	enum Event {
		BEFORE_ACTIVATED, AFTER_ACTIVATED, LABEL_CHANGED, ICON_CHANGED, SELECTED_CHANGED, CHILD_ADDED, CHILD_REMOVED, PRESENTATION_CHANGED, PARENT_CHANGED, EXPANDED_CHANGED, MARKER_CHANGED, ACTIVATED, DEACTIVATED, BEFORE_DECTIVATED, AFTER_DECTIVATED, DISPOSED, BEFORE_DISPOSED, AFTER_DISPOSED, STATE_CHANGED, BLOCK, FILTER_ADDED, FILTER_REMOVED, PREPARED, NODE_ID_CHANGED;

		private final String methodName;

		/**
		 * Use this constructor when the method name of the corresponding
		 * listener event method can be simply derived from the enum.
		 */
		private Event() {
			this.methodName = toCamelCase(toString());
		}

		/**
		 * Use this for any other case as described in the default constructor.
		 * 
		 * @param methodName
		 *            method name oft the corresponding listener event method
		 */
		private Event(final String methodName) {
			this.methodName = methodName;
		}

		private String toCamelCase(final String string) {
			final StringBuilder bob = new StringBuilder(string.length());
			boolean nextUp = false;
			for (int i = 0; i < string.length(); i++) {
				final char c = string.charAt(i);
				if (nextUp) {
					bob.append(c);
				} else if (c != '_') {
					bob.append(Character.toLowerCase(c));
				}
				nextUp = c == '_';
			}
			return bob.toString();
		}

		public String getMethodName() {
			return methodName;
		}
	};

	/**
	 * The type of event.
	 * 
	 * @return the type of event
	 */
	Event event();
}
