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
package org.eclipse.riena.internal.communication.hessian;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.riena.communication.core.attachment.Attachment;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.internal.communication.factory.hessian.serializer.InputStreamSerializerFactory;

/**
 * Test the {@code InputStreamSerializerFactory} class.
 */
public class InputStreamSerializerFactoryTest extends AbstractSerializerFactoryTestCase {

	private final static byte[] BYTES = "That will be pushed through hessian!".getBytes();

	public void testInputStreamFail() throws IOException {
		final Attachment attachment = new Attachment(new ByteArrayInputStream(BYTES));
		try {
			inAndOut(attachment, HessianSerializerVersion.Two, null);
			fail("Must fail!");
		} catch (final IOException e) {
			Nop.reason("Expected");
		}
	}

	public void testInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(BYTES);
		final Attachment attachment = new Attachment(bais);
		final Attachment attechmentOut = (Attachment) inAndOut(attachment, HessianSerializerVersion.Two, null,
				new InputStreamSerializerFactory());
		final byte[] outBytes = new byte[BYTES.length];
		attechmentOut.readAsStream().read(outBytes);
		assertTrue(Arrays.equals(BYTES, outBytes));
	}
}
