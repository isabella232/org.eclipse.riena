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
package org.eclipse.riena.ui.core.marker;

import org.eclipse.core.runtime.Assert;

/**
 * Error marker for a single model (i.e. row) element. May have an optional
 * error message.
 * <p>
 * Two instances will be considered equal, if they mark the same row value (by
 * reference).
 * 
 * @since 2.0
 */
public class RowErrorMessageMarker extends ErrorMessageMarker implements IMessageMarker {

	private final Object rowValue;

	/**
	 * Construct a new row error marker for the given row value
	 * 
	 * @param pMessage
	 *            an optional message. If null it will be converted to the empty
	 *            String.
	 * @param rowValue
	 *            the row value to mark; never null
	 */
	public RowErrorMessageMarker(final String pMessage, final Object rowValue) {
		super(pMessage);
		Assert.isNotNull(rowValue);
		this.rowValue = rowValue;
	}

	/**
	 * Return the row value; never null.
	 */
	public Object getRowValue() {
		return rowValue;
	}

	@Override
	public int hashCode() {
		return rowValue.hashCode();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Two instances are considered equal if they wrap the same rowValue. The
	 * error message is not considered.
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof RowErrorMessageMarker)) {
			return false;
		}
		return rowValue == ((RowErrorMessageMarker) other).rowValue;
	}

}
