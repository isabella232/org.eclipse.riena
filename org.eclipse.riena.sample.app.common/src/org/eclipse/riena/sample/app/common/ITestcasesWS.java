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
package org.eclipse.riena.sample.app.common;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 */
public interface ITestcasesWS {

	BigInteger echoBigInteger(BigInteger value);

	BigInteger echoNullBigInteger(BigInteger valueNull);

	HashMap<String, BigInteger> echoBigIntegerHashMap(HashMap<String, BigInteger> value);

	List<BigInteger> echoBigIntegerList(List<BigInteger> value);

	BigIntegerContainer echoBigIntegerContainer(BigIntegerContainer value);

	XMLGregorianCalendar echoXMLGregorianCalendar(XMLGregorianCalendar value);

}
