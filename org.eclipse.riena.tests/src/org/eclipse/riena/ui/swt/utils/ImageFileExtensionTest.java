package org.eclipse.riena.ui.swt.utils;

import junit.framework.TestCase;

/**
 * Tests of the class {@link ImageFileExtension}.
 */
public class ImageFileExtensionTest extends TestCase {

	/**
	 * Tests the method {@code getImageFileExtension(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetImageFileExtension() throws Exception {

		ImageFileExtension extension = ImageFileExtension.getImageFileExtension(null);
		assertNull(extension);

		extension = ImageFileExtension.getImageFileExtension(""); //$NON-NLS-1$
		assertNull(extension);

		extension = ImageFileExtension.getImageFileExtension(".png"); //$NON-NLS-1$
		assertSame(ImageFileExtension.PNG, extension);

		extension = ImageFileExtension.getImageFileExtension("png"); //$NON-NLS-1$
		assertSame(ImageFileExtension.PNG, extension);

		extension = ImageFileExtension.getImageFileExtension("PNG"); //$NON-NLS-1$
		assertSame(ImageFileExtension.PNG, extension);

		extension = ImageFileExtension.getImageFileExtension("..png"); //$NON-NLS-1$
		assertNull(extension);

		extension = ImageFileExtension.getImageFileExtension(".txt"); //$NON-NLS-1$
		assertNull(extension);

		extension = ImageFileExtension.getImageFileExtension("GIF"); //$NON-NLS-1$
		assertSame(ImageFileExtension.GIF, extension);

		extension = ImageFileExtension.getImageFileExtension(".svg"); //$NON-NLS-1$
		assertSame(ImageFileExtension.SVG, extension);

		extension = ImageFileExtension.getImageFileExtension(".jpg"); //$NON-NLS-1$
		assertSame(ImageFileExtension.JPG, extension);

	}

}
