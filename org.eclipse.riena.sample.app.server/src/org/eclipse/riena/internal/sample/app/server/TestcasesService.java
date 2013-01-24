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
package org.eclipse.riena.internal.sample.app.server;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.riena.sample.app.common.BigIntegerContainer;
import org.eclipse.riena.sample.app.common.ITestcasesWS;

/**
 *
 */
public class TestcasesService implements ITestcasesWS {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.ITestcasesWS#echoBigInteger(java.
	 * math.BigInteger)
	 */
	public BigInteger echoBigInteger(final BigInteger value) {
		if (value == null) {
			throw new RuntimeException("not expected null value"); //$NON-NLS-1$
		}
		return value;
	}

	/**
	 * @param valueNull
	 *            value must be null
	 * @return
	 */
	public BigInteger echoNullBigInteger(final BigInteger valueNull) {
		if (valueNull != null) {
			throw new RuntimeException("expected null value"); //$NON-NLS-1$
		}
		return valueNull;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.ITestcasesWS#echoXMLGregorianCalendar
	 * (javax.xml.datatype.XMLGregorianCalendar)
	 */
	public XMLGregorianCalendar echoXMLGregorianCalendar(final XMLGregorianCalendar value) {
		if (value == null) {
			throw new RuntimeException("not expected null value"); //$NON-NLS-1$
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.ITestcasesWS#echoBigIntegerContainer
	 * (org.eclipse.riena.sample.app.common.BigIntegerContainer)
	 */
	public BigIntegerContainer echoBigIntegerContainer(final BigIntegerContainer value) {
		if (value == null) {
			throw new RuntimeException("not expected null value"); //$NON-NLS-1$
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.ITestcasesWS#echoBigIntegerHashMap
	 * (java.util.HashMap)
	 */
	public HashMap<String, BigInteger> echoBigIntegerHashMap(final HashMap<String, BigInteger> value) {
		if (value == null) {
			throw new RuntimeException("not expected null value"); //$NON-NLS-1$
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.ITestcasesWS#echoBigIntegerList(java
	 * .util.List)
	 */
	public List<BigInteger> echoBigIntegerList(final List<BigInteger> value) {
		if (value == null) {
			throw new RuntimeException("not expected null value"); //$NON-NLS-1$
		}
		return value;
	}
}