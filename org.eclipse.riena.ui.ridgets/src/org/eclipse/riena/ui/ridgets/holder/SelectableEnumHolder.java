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
package org.eclipse.riena.ui.ridgets.holder;

import java.util.List;

/**
 * A special value and selection holder for enum types.
 * 
 * @since 4.0
 */
public class SelectableEnumHolder<E extends Enum<E>> extends SelectableListHolder<E> {

	/**
	 * Create an empty {@code SelectableEnumHolder}.
	 */
	public SelectableEnumHolder() {
		super();
	}

	/**
	 * Create an {@code SelectableEnumHolder} with probably an empty item.
	 * 
	 * @param withEmtpy
	 *            on {@code true} has an empty item
	 */
	public SelectableEnumHolder(final boolean withEmpty) {
		super(withEmpty);
	}

	/**
	 * Create an {@code SelectableListHolder} with the given items.
	 * 
	 * @param items
	 *            items of the list
	 */
	public SelectableEnumHolder(final E... items) {
		super(items);
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
	public SelectableEnumHolder(final boolean withEmpty, final E... items) {
		super(withEmpty, items);
	}

	/**
	 * Create an {@code SelectableListHolder} with the given items.
	 * 
	 * @param items
	 *            items of the list
	 */
	public SelectableEnumHolder(final List<E> items) {
		super(items);
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
	public SelectableEnumHolder(final boolean withEmpty, final List<E> items) {
		super(withEmpty, items);
	}

	/**
	 * Create a {@code EnumDisplayListHolder} with all enum constants of the
	 * given enum in order of their declaration.
	 * 
	 * @param withEmpty
	 *            if {@code true} an empty enum constant will prepended to the
	 *            list
	 * @param enu
	 *            the enum type
	 */
	public SelectableEnumHolder(final Class<E> enu) {
		super(enu.getEnumConstants());
	}

	/**
	 * Create a {@code EnumDisplayListHolder} with all enum constants of the
	 * given enum in order of their declaration and an probably empty item.
	 * 
	 * @param withEmpty
	 *            if {@code true} an empty enum constant will prepended to the
	 *            list
	 * @param enu
	 *            the enum type
	 */
	public SelectableEnumHolder(final boolean withEmpty, final Class<E> enu) {
		super(withEmpty, enu.getEnumConstants());
	}
}
