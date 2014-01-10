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
package org.eclipse.riena.internal.example.client.beans;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.beans.common.AbstractBean;

/**
 *
 */
public class Extra extends AbstractBean {

	/**
	 * Property name of the selected property.
	 */
	public static final String PROPERTY_SELECTED = "selected"; //$NON-NLS-1$

	public enum Category {
		PERFORMANCE("Performance"), CONVENIENCE("Convenience"), ENTERTAINMENT("Entertainment"), SAFETY("Safety"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		private String label;

		private Category(final String label) {
			Assert.isNotNull(label);
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	private boolean selected;
	private final boolean defaultSelected;
	private final String name;
	private final Category category;
	private final double price;

	public Extra(final String name, final Category category, final double price, final boolean selected) {
		super();
		this.name = name;
		this.category = category;
		this.price = price;
		this.selected = selected;
		this.defaultSelected = selected;
	}

	public void setDefault() {
		setSelected(this.defaultSelected);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(final boolean selected) {
		final boolean oldSelected = this.selected;
		this.selected = selected;
		firePropertyChanged(PROPERTY_SELECTED, oldSelected, selected);
	}

	public String getName() {
		return name;
	}

	public Category getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(" "); //$NON-NLS-1$
		sb.append("Category: "); //$NON-NLS-1$
		sb.append(getCategory());
		sb.append(", "); //$NON-NLS-1$
		sb.append("Price (€): "); //$NON-NLS-1$
		sb.append(", "); //$NON-NLS-1$
		sb.append(getPrice());
		sb.append(", "); //$NON-NLS-1$
		sb.append("selected: "); //$NON-NLS-1$
		sb.append(isSelected());
		return sb.toString();
	}

}
