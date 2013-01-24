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
package org.eclipse.riena.internal.core.injector.extension;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import org.eclipse.riena.core.injector.extension.ExtensionDescriptor;

/**
 * The <code>ExtensionMapper</code> maps interfaces to extensions. Extension
 * properties (attributes, sub-elements and element value) can then accessed by
 * <i>getters</i> in the interface definition. It is only necessary to define
 * the interfaces for the mapping. The Riena extension injector creates dynamic
 * proxies fulfilling these interfaces for retrieving the data from the
 * <code>ExtensionRegistry</code>.<br>
 * 
 * The ExtensionMapper does not evaluate the extension schema, so it can only
 * trust that the extension and the interface match.
 * <p>
 * The basic rules for the mapping are:
 * <ul>
 * <li>one interface maps to one extension element type</li>
 * <li>the interface has to be annotated with <code>@ExtensionInterface</code></li>
 * <li>interface methods can contain <i>getters</i> prefixed with:
 * <ul>
 * <li>get...</li>
 * <li>is...</li>
 * <li>create...</li>
 * </ul>
 * Such prefixed methods enforce a default mapping to attribute or element
 * names, i.e. the remainder of the methods name is interpreted as the attribute
 * or element name. A simple name mangling is performed, e.g for the method
 * <code>getDatabaseURL</code> the mapping looks for the attribute name
 * <code>databaseURL</code>.
 * <li>To enforce another name mapping a method can be annotated with
 * <code>@MapName("name")</code>. The <i>name</i> specifies the name of the
 * element or attribute. The extension element´s value can be retrieved by
 * annotating the method with <code>@MapContent()</code>. The return type must
 * be <code>String</code>. The method names of such annotated methods can be
 * arbitrary</li>
 * </ul>
 * The return type of a method indicates how the value of an attribute will be
 * converted. If the return type is
 * <ul>
 * <li>a <i>primitive</i> type or <code>java.lang.String</code>than the mapping
 * converts the attribute's value to the corresponding type.</li>
 * <li>an interface or an array of interfaces annotated with
 * <code>@ExtensionInterface</code> than the mapping tries to resolve to a
 * nested element or to nested elements.</li>
 * <li><code>org.osgi.framework.Bundle</code> than the methods returns the
 * contributing bundle.</li>
 * <li><code>org.eclipse.core.runtime.IConfigurationElement</code> than the
 * methods returns the underlying configuration element.</li>
 * <li><code>java.lang.Class</code> than the attribute is interpreted as a class
 * name and a class instance will be returned.</li>
 * <li><code>an enum</code> than the attribute is interpreted as the value (case
 * insensitive) of the corresponding enum type. <code>null</code> will be
 * returned if the attribute´s value is not given, i.e. <code>null</code> or the
 * empty string.</li>
 * <li>and finally if none of the above matches the mapping tries to create an
 * new instance of the attribute´s value (interpreted as class name) each time
 * it is called. If the extension attribute is not defined <code>null</code>
 * will be returned.</li>
 * </ul>
 */
public final class ExtensionMapper {

	private ExtensionMapper() {
		throw new IllegalStateException("Should never be invoked!"); //$NON-NLS-1$
	}

	/**
	 * Static method to read extensions
	 * 
	 * @param <T>
	 * @param symbolReplace
	 *            on true symbol replacement occurs (via
	 *            {@code StringVariableManager})
	 * @param extensionDesc
	 * @param componentType
	 * @return
	 * @throws IllegalArgumentException
	 *             if extension point is not existent
	 */
	public static <T> T[] map(final boolean symbolReplace, final ExtensionDescriptor extensionDesc,
			final Class<T> componentType, final boolean nonSpecific) {
		final IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		final List<Object> list = new ArrayList<Object>();
		boolean atLeastOneExtensionPointExists = false;
		for (final String extensionPointId : extensionDesc.getExtensionPointId().compatibleIds()) {
			final int lastDot = extensionPointId.lastIndexOf('.');
			if (lastDot < 1) {
				throw new IllegalStateException("ExtensionPointId has no namespace!"); //$NON-NLS-1$
			}
			final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(extensionPointId);
			if (extensionPoint == null) {
				continue;
			}
			atLeastOneExtensionPointExists = true;

			if (nonSpecific) {
				if (extensionDesc.isHomogeneous()) {
					for (final IConfigurationElement element : extensionPoint.getConfigurationElements()) {
						list.add(InterfaceBeanFactory.newInstance(symbolReplace, componentType, element));
					}
				} else {
					list.add(InterfaceBeanFactory
							.newInstance(symbolReplace, componentType, new Wrapper(extensionPoint)));
				}
			} else {
				for (final IExtension extension : extensionPoint.getExtensions()) {
					list.add(InterfaceBeanFactory.newInstance(symbolReplace, componentType, new Wrapper(extension)));
				}
			}
		}
		if (!atLeastOneExtensionPointExists) {
			throw new IllegalArgumentException("Extension point " + extensionDesc.getExtensionPointId() //$NON-NLS-1$
					+ " does not exist"); //$NON-NLS-1$
		}
		return list.toArray((T[]) Array.newInstance(componentType, list.size()));
	}

	/**
	 * Wrap an IExtension or an IExtensionPoint so that it behaves almost like a
	 * IConfigurationElement.
	 */
	private final static class Wrapper implements IConfigurationElement {

		private final IExtensionPoint wrappedExtensionPoint;
		private final IExtension wrappedExtension;

		private Wrapper(final IExtensionPoint extensionPoint) {
			Assert.isNotNull(extensionPoint, "wrappedExtensionPoint must not be null."); //$NON-NLS-1$
			this.wrappedExtensionPoint = extensionPoint;
			this.wrappedExtension = null;
		}

		private Wrapper(final IExtension extension) {
			Assert.isNotNull(extension, "extension must not be null."); //$NON-NLS-1$
			this.wrappedExtension = extension;
			this.wrappedExtensionPoint = null;
		}

		/*
		 * {@inheritDoc}
		 */
		public Object createExecutableExtension(final String propertyName) throws CoreException {
			throw new UnsupportedOperationException("IExtension/Point does not support createExecutableExtension()"); //$NON-NLS-1$
		}

		/*
		 * {@inheritDoc}
		 */
		public String getAttribute(final String name) {
			return null;
		}

		/*
		 * {@inheritDoc}
		 */
		public String getAttribute(final String attrName, final String locale) {
			return null;
		}

		/*
		 * {@inheritDoc}
		 */
		public String getAttributeAsIs(final String name) {
			return null;
		}

		/*
		 * {@inheritDoc}
		 */
		public String[] getAttributeNames() {
			return new String[0];
		}

		/*
		 * {@inheritDoc}
		 */
		public IConfigurationElement[] getChildren() {
			if (wrappedExtension != null) {
				return wrappedExtension.getConfigurationElements();
			}

			final List<IConfigurationElement> elements = new ArrayList<IConfigurationElement>();
			for (final IExtension extension : wrappedExtensionPoint.getExtensions()) {
				elements.addAll(Arrays.asList(extension.getConfigurationElements()));
			}

			return elements.toArray(new IConfigurationElement[elements.size()]);
		}

		/*
		 * {@inheritDoc}
		 */
		public IConfigurationElement[] getChildren(final String name) {
			final IConfigurationElement[] configurationElements = wrappedExtension != null ? wrappedExtension
					.getConfigurationElements() : wrappedExtensionPoint.getConfigurationElements();

			final List<IConfigurationElement> elements = new ArrayList<IConfigurationElement>();
			for (final IConfigurationElement configurationElement : configurationElements) {
				if (configurationElement.getName().equals(name)) {
					elements.add(configurationElement);
				}
			}
			return elements.toArray(new IConfigurationElement[elements.size()]);
		}

		/*
		 * {@inheritDoc}
		 */
		public IContributor getContributor() {
			return wrappedExtension != null ? wrappedExtension.getContributor() : wrappedExtensionPoint
					.getContributor();
		}

		/*
		 * {@inheritDoc}
		 */
		public IExtension getDeclaringExtension() {
			throw new UnsupportedOperationException("IExtensionPoint does not support getDeclaringExtension()"); //$NON-NLS-1$
		}

		/*
		 * {@inheritDoc}
		 */
		public String getName() {
			return wrappedExtension != null ? wrappedExtension.getLabel() : wrappedExtensionPoint.getLabel();
		}

		/*
		 * {@inheritDoc}
		 */
		@SuppressWarnings("deprecation")
		public String getNamespace() {
			// This method is also @deprecated so it must use the @deprecated methods 
			return wrappedExtension != null ? wrappedExtension.getNamespace() : wrappedExtensionPoint.getNamespace();
		}

		/*
		 * {@inheritDoc}
		 */
		public String getNamespaceIdentifier() {
			return wrappedExtension != null ? wrappedExtension.getNamespaceIdentifier() : wrappedExtensionPoint
					.getNamespaceIdentifier();
		}

		/*
		 * {@inheritDoc}
		 */
		public Object getParent() {
			throw new UnsupportedOperationException("IExtensionPoint does not support getParent()"); //$NON-NLS-1$
		}

		/*
		 * {@inheritDoc}
		 */
		public String getValue() {
			throw new UnsupportedOperationException("IExtensionPoint does not support getValue()"); //$NON-NLS-1$
		}

		/*
		 * {@inheritDoc}
		 */
		public String getValue(final String locale) {
			throw new UnsupportedOperationException("IExtensionPoint does not support getValue(String locale)"); //$NON-NLS-1$
		}

		/*
		 * {@inheritDoc}
		 */
		public String getValueAsIs() {
			throw new UnsupportedOperationException("IExtensionPoint does not support getValueAsIs()"); //$NON-NLS-1$
		}

		/*
		 * {@inheritDoc}
		 */
		public boolean isValid() {
			return wrappedExtension != null ? wrappedExtension.isValid() : wrappedExtensionPoint.isValid();
		}

	}

}
