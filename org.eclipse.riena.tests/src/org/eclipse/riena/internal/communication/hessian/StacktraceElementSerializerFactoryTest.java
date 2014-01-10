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

import org.eclipse.riena.internal.communication.factory.hessian.serializer.StacktraceElementSerializerFactory;

/**
 * Test the {@code StacktraceElementSerializerFactory} class.
 */
public class StacktraceElementSerializerFactoryTest extends AbstractSerializerFactoryTestCase {

	public void testStacktraceElementFail() {
		assertFalse(isBackAndForthOk(new StackTraceElement(StackTraceElement.class.getName(), "getFileName",
				StackTraceElement.class.getSimpleName() + ".java", 42), HessianSerializerVersion.Two, null));
	}

	public void testStacktraceElement() {
		assertTrue(isBackAndForthOk(new StackTraceElement(StackTraceElement.class.getName(), "getFileName",
				StackTraceElement.class.getSimpleName() + ".java", 42), HessianSerializerVersion.Two, null,
				new StacktraceElementSerializerFactory()));
	}
}
