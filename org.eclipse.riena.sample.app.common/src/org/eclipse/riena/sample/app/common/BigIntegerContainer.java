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
package org.eclipse.riena.sample.app.common;

import java.math.BigInteger;

/**
 *
 */
public class BigIntegerContainer {

	private BigInteger value1;
	private BigInteger value2;
	private String name;

	public BigIntegerContainer(final BigInteger value1, final BigInteger value2, final String name) {
		this.value1 = value1;
		this.value2 = value2;
		this.name = name;
	}

	public BigInteger getValue1() {
		return value1;
	}

	public void setValue1(final BigInteger value1) {
		this.value1 = value1;
	}

	public BigInteger getValue2() {
		return value2;
	}

	public void setValue2(final BigInteger value2) {
		this.value2 = value2;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "value1=" + value1.toString() + " value2=" + value2.toString() + " name=" + name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		final BigIntegerContainer container = (BigIntegerContainer) obj;
		if (this.getValue1().equals(container.getValue1()) && this.getValue2().equals(container.getValue2())
				&& this.getName().equals(container.getName())) {
			return true;
		} else {
			return false;
		}
	}
}