/*******************************************************************************
 * Copyright (c) 2007, 2013 Florian Pirchner.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import org.eclipse.riena.internal.ui.ridgets.Activator;

/**
 * Is used to return deviating instances of {@link ValueBindingSupport}.
 * 
 * @since 5.0
 */
public interface IValueBindingSupportProvider {

	/**
	 * Creates a new instance for the given type of ridget. If <code>null</code> is returned, a default ValueBindingSupport will be used.
	 * 
	 * @param ridgetClass
	 *            The class of the ridget that gains the binding support.
	 * @param ridgetObservable
	 *            The observable value that should be used by the {@link ValueBindingSupport}
	 * @return valueBindingSupport
	 */
	ValueBindingSupport createInstance(Class<? extends IRidget> ridgetClass, IObservableValue ridgetObservable);

	/**
	 * An internal class that offers access to the extension registry and returns a ValueBindingSupport instance for the given information or <code>null</code>
	 * if no instance could be created.
	 */
	public static class ExtensionAccess {

		/**
		 * Attribute to access bindingSupportProvider extension.
		 */
		private static final String ATTR_CLASS = "class"; //$NON-NLS-1$

		/**
		 * Creates a new instance of value binding support using the org.eclipse.riena.ui.ridgets.bindingSupportProvider-extension. If no extension could be
		 * found or an exception is thrown, <code>null</code> will be returned.
		 * 
		 * * @param ridgetClass The class of the ridget that gains the binding support.
		 * 
		 * @param ridgetObservable
		 *            The observable value that should be used by the {@link ValueBindingSupport}
		 * @return valueBindingSupport
		 */
		public static ValueBindingSupport createInstance(final Class<? extends IRidget> ridgetClass, final IObservableValue ridgetObservable) {
			ValueBindingSupport result = null;
			final IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(
					"org.eclipse.riena.ui.ridgets.bindingSupportProvider"); //$NON-NLS-1$
			if (elements.length > 0) {
				try {
					final IValueBindingSupportProvider bindingSupport = (IValueBindingSupportProvider) elements[0].createExecutableExtension(ATTR_CLASS);
					result = bindingSupport.createInstance(ridgetClass, ridgetObservable);
				} catch (final Exception e) {
					Activator.getDefault().getLog()
							.log(new Status(Status.ERROR, Activator.PLUGIN_ID, "exception creating valueBindingSupport. Default will be used.", e)); //$NON-NLS-1$
				}
			}
			return result;
		}

	}
}