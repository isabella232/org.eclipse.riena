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
package org.eclipse.riena.ui.ridgets.holder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.BindingException;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.ridgets.databinding.ConverterFactory;

/**
 * A generic value and selection holder for combo boxes. It supports an optional
 * empty item. An empty item will be displayed as an empty string and selected
 * as {@code null}.<br>
 * Its intended usage is:
 * 
 * <pre>
 * IComboRidget.bindToModel(SelectableListHolder<?> listHolder, String renderMethodName)
 * </pre>
 * 
 * @param <T>
 *            the generic type of the holder
 * 
 * @since 4.0
 */
public class SelectableListHolder<T> {

	/** Constant for accessing the lists items */
	public static final String PROP_LIST = "items"; //$NON-NLS-1$

	/** Constant for accessing the selection */
	public static final String SELECTION_PROPERTY = "selection"; //$NON-NLS-1$

	private final List<T> items;
	private Class<T> guessedType;
	private T selection;

	protected final static Object EMPTY = new Object() {
		@Override
		public String toString() {
			return ""; //$NON-NLS-1$
		};
	};

	/**
	 * Create an empty {@code SelectableListHolder}.
	 */
	public SelectableListHolder() {
		this(false);
	}

	/**
	 * Create an {@code SelectableListHolder} with probably an empty item.
	 * 
	 * @param withEmtpy
	 *            on {@code true} has an empty item
	 */
	public SelectableListHolder(final boolean withEmtpy) {
		this.items = init(withEmtpy, null);
	}

	/**
	 * Create an {@code SelectableListHolder} with the given items.
	 * 
	 * @param items
	 *            items of the list
	 */
	public SelectableListHolder(final T... items) {
		this(false, items);
	}

	/**
	 * Create an {@code SelectableListHolder} with probably an empty item and
	 * the given items.
	 * 
	 * @param items
	 *            items of the list
	 * @param withEmtpy
	 *            on {@code true} has an empty item
	 */
	public SelectableListHolder(final boolean withEmtpy, final T... items) {
		this.items = init(withEmtpy, Arrays.asList(items));
	}

	/**
	 * Create an {@code SelectableListHolder} with the given items.
	 * 
	 * @param items
	 *            items of the list
	 */
	public SelectableListHolder(final List<T> items) {
		this(false, items);
	}

	/**
	 * Create an {@code SelectableListHolder} with probably an empty item and
	 * the given items.
	 * 
	 * @param items
	 *            items of the list
	 * @param withEmtpy
	 *            on {@code true} has an empty item
	 */
	public SelectableListHolder(final boolean withEmtpy, final List<T> items) {
		this.items = init(withEmtpy, items);
	}

	@SuppressWarnings("unchecked")
	private List<T> init(final boolean withEmpty, final List<T> items) {
		guessedType = (Class<T>) (items != null && items.size() > 0 ? items.get(0).getClass() : EMPTY.getClass());
		final List<T> list = new ArrayList<T>();
		if (withEmpty) {
			list.add((T) EMPTY);
		}
		if (items != null) {
			list.addAll(items);
		}
		return list;
	}

	/**
	 * Get items.
	 * 
	 * @return the items
	 */
	public List<T> getItems() {
		return items;
	}

	/**
	 * Get the selected item.
	 * 
	 * @return selected item
	 */
	public T getSelection() {
		return selection == EMPTY ? null : selection;
	}

	/**
	 * Set the selected item.
	 * 
	 * @param selection
	 *            the selected item
	 * 
	 */
	public void setSelection(final T selection) {
		this.selection = selection;
	}

	/**
	 * Sort the items with the given comparator.
	 * 
	 * @param comparator
	 *            the comparator
	 */
	public void sortItems(final Comparator<? super T> comparator) {
		Collections.sort(items, comparator);
	}

	/**
	 * Get the 'guessed' type of the list holder. The type is guessed by
	 * inspecting the lists items.
	 * <p>
	 * <b>Note: </b>This method is called by the ridgets bind method.
	 * 
	 * @return the guessed type.
	 */
	public Class<?> getGuessedType() {
		return guessedType;
	}

	/**
	 * Get a {@code ConverterFactory}.
	 * <p>
	 * <b>Note: </b>This method is called by the ridgets bind method.
	 * 
	 * @param renderMethodName
	 *            a string that identifies a parameterless method returning a
	 *            {@code String}.
	 * @return the {@code ConverterFactory}
	 */
	public ConverterFactory<T, String> getConverterFactory(final String renderMethodName) {
		final ConverterFactory<T, String> factory = new ConverterFactory<T, String>(guessedType, String.class);
		final Method renderer = getRenderer(renderMethodName);
		for (final T item : getItems()) {
			try {
				factory.add(item, item == EMPTY ? EMPTY.toString() : renderer.invoke(item).toString());
			} catch (final Exception e) {
				throw new BindingException("Invoking render method for type " + guessedType.getName() + " failed.", e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return factory;
	}

	private Method getRenderer(final String renderMethodName) {
		try {
			final Method renderMethod = guessedType.getMethod((StringUtils.isGiven(renderMethodName) ? renderMethodName
					: "toString")); //$NON-NLS-1$
			if (renderMethod.getReturnType() != String.class) {
				throw new BindingException("Render method for type " + guessedType.getName() //$NON-NLS-1$
						+ " has not String as return type."); //$NON-NLS-1$
			}
			return renderMethod;
		} catch (final Exception e) {
			throw new BindingException("Could not find render method for type " + guessedType.getName(), e); //$NON-NLS-1$
		}
	}

}
