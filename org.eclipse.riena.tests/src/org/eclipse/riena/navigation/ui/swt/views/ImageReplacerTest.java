package org.eclipse.riena.navigation.ui.swt.views;

import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Point;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ImageReplacer}.
 */
@UITestCase
public class ImageReplacerTest extends TestCase {

	/**
	 * Tests the <i>private</i> method {@code getImageName(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetImageName() throws Exception {

		final ImageReplacer replacer = ImageReplacer.getInstance();

		String imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", ""); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "def.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("def", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "ghi00.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("ghi00", imageName); //$NON-NLS-1$

		final RienaDefaultLnf originalLnf = LnfManager.getLnf();
		LnfManager.setLnf(new MyLnf());

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "ghi00.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("ghi", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "/folder00/jkl00_d_.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("jkl00_d_", imageName); //$NON-NLS-1$

		imageName = ReflectionUtils.invokeHidden(replacer, "getImageName", "/folderXY/MNO00.png"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("MNO", imageName); //$NON-NLS-1$

		LnfManager.setLnf(originalLnf);

	}

	/**
	 * Tests the <i>private</i> method {@code getImageName(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetScaledImage() throws Exception {

		final RienaDefaultLnf originalLnf = LnfManager.getLnf();
		LnfManager.setLnf(new MyLnf());

		final ImageReplacer replacer = ImageReplacer.getInstance();

		ImageDescriptor fileImageDescriptor = ImageDescriptor.createFromFile(null, "/icons/testimagea00.png"); //$NON-NLS-1$
		ImageDescriptor imageDescriptor = ReflectionUtils.invokeHidden(replacer, "getScaledImage", fileImageDescriptor); //$NON-NLS-1$ 
		assertNotNull(imageDescriptor);
		assertEquals(16, imageDescriptor.getImageData().width);
		assertEquals(16, imageDescriptor.getImageData().height);

		final Point dpi = SwtUtilities.getDpi();
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpi", new Point(144, 144)); //$NON-NLS-1$

		fileImageDescriptor = ImageDescriptor.createFromFile(null, "/icons/testimagea00.png"); //$NON-NLS-1$
		imageDescriptor = ReflectionUtils.invokeHidden(replacer, "getScaledImage", fileImageDescriptor); //$NON-NLS-1$ 
		assertNotNull(imageDescriptor);
		assertEquals(24, imageDescriptor.getImageData().width);
		assertEquals(24, imageDescriptor.getImageData().height);

		final URL url = this.getClass().getResource("/icons/testimagea00.png"); //$NON-NLS-1$
		final ImageDescriptor urlimageDescriptor = ImageDescriptor.createFromURL(url);
		imageDescriptor = ReflectionUtils.invokeHidden(replacer, "getScaledImage", urlimageDescriptor); //$NON-NLS-1$ 
		assertNotNull(imageDescriptor);
		assertEquals(24, imageDescriptor.getImageData().width);
		assertEquals(24, imageDescriptor.getImageData().height);

		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpi", dpi); //$NON-NLS-1$
		LnfManager.setLnf(originalLnf);

	}

	/**
	 * Tests the <i>private</i> method {@code isImageDescriptorSupported}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testIsImageDescriptorSupported() throws Exception {

		final ImageReplacer replacer = ImageReplacer.getInstance();

		final ImageDescriptor fileImageDescriptor = ImageDescriptor.createFromFile(null, "/icons/testimagea00.png"); //$NON-NLS-1$
		boolean isSupported = ReflectionUtils.invokeHidden(replacer, "isImageDescriptorSupported", fileImageDescriptor); //$NON-NLS-1$
		assertTrue(isSupported);

		final URL url = this.getClass().getResource("/icons/testimagea00.png"); //$NON-NLS-1$
		final ImageDescriptor urlimageDescriptor = ImageDescriptor.createFromURL(url);
		isSupported = ReflectionUtils.invokeHidden(replacer, "isImageDescriptorSupported", urlimageDescriptor); //$NON-NLS-1$
		assertTrue(isSupported);

	}

	/**
	 * Tests the method {@code getInstance()}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetInstance() throws Exception {

		final ImageReplacer replacer = ImageReplacer.getInstance();
		assertNotNull(replacer);

		assertSame(replacer, ImageReplacer.getInstance());
	}

	private static class MyLnf extends RienaDefaultLnf {
		@Override
		public String getIconScaleSuffix(final Point dpi) {

			if (dpi == null) {
				return "00"; //$NON-NLS-1$
			}
			if (dpi.x <= 96) {
				return "00"; //$NON-NLS-1$
			}
			return "03"; //$NON-NLS-1$

		}
	}

}
