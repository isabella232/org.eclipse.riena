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
package org.eclipse.riena.internal.communication.hessian;

import java.math.BigInteger;

import org.eclipse.riena.internal.communication.factory.hessian.serializer.BigIntegerSerializerFactory;

/**
 * Test the {@code BigIntegerSerializerFactory} class.
 */
public class BigIntegerSerializerFactoryTest extends AbstractSerializerFactoryTestCase {

	public void testBigIntegerFailString42() {
		assertFalse(isBackAndForthOk(new BigInteger("42"), HessianSerializerVersion.Two, BigInteger.class));
	}

	public void testBigIntegerString42() {
		assertTrue(isBackAndForthOk(new BigInteger("42"), HessianSerializerVersion.Two, BigInteger.class,
				new BigIntegerSerializerFactory()));
	}

	public void testBigIntegerFailValueOf5() {
		assertFalse(isBackAndForthOk(BigInteger.valueOf(5), HessianSerializerVersion.Two, BigInteger.class));
	}

	public void testBigIntegerValueOf5() {
		assertTrue(isBackAndForthOk(BigInteger.valueOf(5), HessianSerializerVersion.Two, BigInteger.class,
				new BigIntegerSerializerFactory()));
	}
}
