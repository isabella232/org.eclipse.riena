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
package org.eclipse.riena.objecttransaction.noreg.value;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.objecttransaction.IObjectId;

/**
 * TODO Fehlender Klassen-Kommentar
 */
public class GenericOID implements IObjectId {
	private final String type;
	private final String primName;
	private final Object primValue;

	/**
	 * @param type
	 * @param primName
	 * @param primValue
	 */
	public GenericOID(final String type, final String primName, final Object primValue) {
		super();
		this.type = type;
		this.primName = primName;
		this.primValue = primValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Map<String, Object> getProperties() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(primName, primValue);
		return map;
	}

	@Override
	public boolean equals(final Object oid) {
		if (oid instanceof GenericOID) {
			final GenericOID gOID = (GenericOID) oid;
			if (gOID.type.equals(type) && gOID.primName.equals(primName) && gOID.primValue.equals(primValue)) {
				return true;
			}
		}
		return super.equals(oid);
	}

	@Override
	public int hashCode() {
		try {
			if (primValue instanceof Integer) {
				return ((Integer) primValue).intValue();
			}
			return Integer.parseInt((String) primValue);
		} catch (final NumberFormatException e) {
			return super.hashCode();
		}
	}

	@Override
	public String toString() {
		return "type:" + type + " primName:" + primName + " primValue:" + primValue;
	}

}