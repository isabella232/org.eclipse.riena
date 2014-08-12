package org.eclipse.riena.core.util;

import junit.framework.TestCase;

import org.eclipse.core.runtime.AssertionFailedException;

import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests of the class {@link FileUtils}.
 */
@NonUITestCase
public class FileUtilsTest extends TestCase {

	/**
	 * Tests the method {@code getNameWithoutExtension(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetNameWithoutExtension() throws Exception {

		try {
			FileUtils.getNameWithoutExtension(null);
			fail("AssertionFailedException expected"); //$NON-NLS-1$
		} catch (final AssertionFailedException ex) {
			// expected exception
		}

		String name = FileUtils.getNameWithoutExtension(""); //$NON-NLS-1$
		assertEquals("", name); //$NON-NLS-1$

		name = FileUtils.getNameWithoutExtension("a"); //$NON-NLS-1$
		assertEquals("a", name); //$NON-NLS-1$

		name = FileUtils.getNameWithoutExtension("b."); //$NON-NLS-1$
		assertEquals("b", name); //$NON-NLS-1$

		name = FileUtils.getNameWithoutExtension("c.1"); //$NON-NLS-1$
		assertEquals("c", name); //$NON-NLS-1$

		name = FileUtils.getNameWithoutExtension("dd.1.2"); //$NON-NLS-1$
		assertEquals("dd.1", name); //$NON-NLS-1$

		name = FileUtils.getNameWithoutExtension(".3"); //$NON-NLS-1$
		assertEquals("", name); //$NON-NLS-1$

		name = FileUtils.getNameWithoutExtension("/folder/fileName.txt"); //$NON-NLS-1$
		assertEquals("fileName", name); //$NON-NLS-1$

	}

	/**
	 * Tests the method {@code getFileExtension(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetFileExtension() throws Exception {

		try {
			FileUtils.getFileExtension(null);
			fail("AssertionFailedException expected"); //$NON-NLS-1$
		} catch (final AssertionFailedException ex) {
			// expected exception
		}

		String name = FileUtils.getFileExtension(""); //$NON-NLS-1$
		assertEquals("", name); //$NON-NLS-1$

		name = FileUtils.getFileExtension("a"); //$NON-NLS-1$
		assertEquals("", name); //$NON-NLS-1$

		name = FileUtils.getFileExtension("b."); //$NON-NLS-1$
		assertEquals("", name); //$NON-NLS-1$

		name = FileUtils.getFileExtension("c.1"); //$NON-NLS-1$
		assertEquals("1", name); //$NON-NLS-1$

		name = FileUtils.getFileExtension("dd.1.2"); //$NON-NLS-1$
		assertEquals("2", name); //$NON-NLS-1$

		name = FileUtils.getFileExtension(".3"); //$NON-NLS-1$
		assertEquals("3", name); //$NON-NLS-1$

		name = FileUtils.getFileExtension("/folder/fileName.txt"); //$NON-NLS-1$
		assertEquals("txt", name); //$NON-NLS-1$

	}

}
