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
package org.eclipse.riena.communication.core.attachment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.easymock.EasyMock;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the ByteArrayDataSource class.
 * 
 */
@NonUITestCase
public class ByteArrayDataSourceTest extends RienaTestCase {

	private IDataSource dataSourceMock;
	private final static String NAME = "Test"; //$NON-NLS-1$

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	private void setUpDataSourceMock(final int length) throws Exception {
		dataSourceMock = EasyMock.createMock(IDataSource.class);

		EasyMock.expect(dataSourceMock.getName()).andReturn(NAME);
		EasyMock.expect(dataSourceMock.getInputStream()).andReturn(getInputStream(length));

		EasyMock.replay(dataSourceMock);
	}

	private InputStream getInputStream(final int length) {
		final byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = (byte) i;
		}
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		if (dataSourceMock != null) {
			EasyMock.verify(dataSourceMock);
		}
		super.tearDown();
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testCreationEmpty() throws Exception {
		checkDataSource(0);
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testCreationLong() throws Exception {
		checkDataSource(10 * 1024);
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testCreationVeryLong() throws Exception {
		checkDataSource(1024 * 1024);
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testCreationVeryVeryLong() throws Exception {
		checkDataSource(10 * 1024 * 1024);
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testNullDataSource() throws Exception {
		// This creation should not fail!
		final ByteArrayDataSource dataSource = new ByteArrayDataSource(null);

		assertNull(dataSource.getName());
		assertNull(dataSource.getInputStream());
	}

	/**
	 * Check the ByteArrayDataSource with various lengths.
	 * 
	 * @param length
	 * @throws Exception
	 */
	private void checkDataSource(final int length) throws Exception {
		setUpDataSourceMock(length);
		final long before = System.currentTimeMillis();
		final ByteArrayDataSource dataSource = new ByteArrayDataSource(dataSourceMock);
		println("Run with " + length + " bytes took " + (System.currentTimeMillis() - before) + " ms."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(getInputStream(length), dataSource.getInputStream());
		assertEquals(NAME, dataSource.getName());
	}

	private void assertEquals(final InputStream inputStream1, final InputStream inputStream2) {
		int byte1 = 0;
		int byte2 = 0;
		while (byte1 != -1 && byte2 != -1) {
			try {
				byte1 = inputStream1.read();
				byte2 = inputStream2.read();
				assertEquals(byte1, byte2);
			} catch (final IOException ioe) {
				fail(ioe.getMessage());
			}
		}
	}
}