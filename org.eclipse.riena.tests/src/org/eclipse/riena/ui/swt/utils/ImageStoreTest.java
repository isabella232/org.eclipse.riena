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
package org.eclipse.riena.ui.swt.utils;

import org.osgi.framework.Bundle;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Tests the class {@link ImageStore}.
 */
@UITestCase
public class ImageStoreTest extends RienaTestCase {

	/**
	 * Tests the <i>private</i> method {@code getFullName}.
	 */
	public void testGetFullName() {

		final ImageStore store = ImageStore.getInstance();
		final String fullName = ReflectionUtils.invokeHidden(store, "getFullName", "abc", ImageFileExtension.JPG); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc.jpg", fullName); //$NON-NLS-1$

	}

	/**
	 * Tests the method {@code getImage(String)}.
	 */
	public void testGetImage() {

		final Shell shell = new Shell();

		final ImageStore store = ImageStore.getInstance();
		store.update(new IImagePathExtension[] {});
		Image image = store.getImage("spirit"); //$NON-NLS-1$
		assertNull(image);

		final IImagePathExtension extension = new IImagePathExtension() {

			public Bundle getContributingBundle() {
				return Activator.getDefault().getBundle();
			}

			public String getPath() {
				return "icons"; //$NON-NLS-1$
			}

		};
		store.update(new IImagePathExtension[] { extension });
		image = store.getImage("spirit"); //$NON-NLS-1$
		assertNotNull(image);

		SwtUtilities.dispose(shell);

	}

	/**
	 * Tests the method {@code getImage(String,ImageFileExtension)}.
	 */
	public void testGetImageImageFileExtension() {

		final Shell shell = new Shell();
		final ImageStore store = ImageStore.getInstance();

		final IImagePathExtension extension = new IImagePathExtension() {

			public Bundle getContributingBundle() {
				return Activator.getDefault().getBundle();
			}

			public String getPath() {
				return "icons"; //$NON-NLS-1$
			}

		};
		store.update(new IImagePathExtension[] { extension });

		Image image = store.getImage("spirit", ImageFileExtension.PNG); //$NON-NLS-1$
		assertNotNull(image);
		image = store.getImage("spirit", ImageFileExtension.GIF); //$NON-NLS-1$
		assertNull(image);
		image = store.getImage("spirit", ImageFileExtension.JPG); //$NON-NLS-1$
		assertNull(image);

		image = store.getImage("eclipse", ImageFileExtension.PNG); //$NON-NLS-1$
		assertNull(image);
		image = store.getImage("eclipse", ImageFileExtension.GIF); //$NON-NLS-1$
		assertNotNull(image);
		image = store.getImage("eclipse", ImageFileExtension.JPG); //$NON-NLS-1$
		assertNull(image);

		SwtUtilities.dispose(shell);

	}

	/**
	 * Tests the method {@code addImageScaleSuffix(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testAddImageScaleSuffix() throws Exception {

		final ImageStore store = ImageStore.getInstance();

		final IImagePathExtension extension = new IImagePathExtension() {

			public Bundle getContributingBundle() {
				return Activator.getDefault().getBundle();
			}

			public String getPath() {
				return "icons"; //$NON-NLS-1$
			}

		};
		store.update(new IImagePathExtension[] { extension });

		final RienaDefaultLnf originalLnf = LnfManager.getLnf();
		try {
			LnfManager.setLnf(new MyLnf());

			String name = store.addImageScaleSuffix("imagebutton", ImageFileExtension.PNG); //$NON-NLS-1$
			assertEquals("imagebutton_p_", name); //$NON-NLS-1$

			name = store.addImageScaleSuffix("dontexits", ImageFileExtension.PNG); //$NON-NLS-1$
			assertEquals("dontexits", name); //$NON-NLS-1$

		} finally {
			LnfManager.setLnf(originalLnf);
		}

	}

	/**
	 * Tests the method {@code getFullScaledName}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetFullScaledName() throws Exception {

		final ImageStore store = ImageStore.getInstance();

		final IImagePathExtension extension = new IImagePathExtension() {

			public Bundle getContributingBundle() {
				return Activator.getDefault().getBundle();
			}

			public String getPath() {
				return "icons"; //$NON-NLS-1$
			}

		};
		store.update(new IImagePathExtension[] { extension });

		final RienaDefaultLnf originalLnf = LnfManager.getLnf();
		try {
			LnfManager.setLnf(new MyLnf());

			String name = ReflectionUtils.invokeHidden(store, "getFullScaledName", "abc.jpg", ImageFileExtension.JPG); //$NON-NLS-1$ //$NON-NLS-2$
			assertNull(name);

			name = ReflectionUtils.invokeHidden(store, "getFullScaledName", "", ImageFileExtension.PNG); //$NON-NLS-1$ //$NON-NLS-2$
			assertNull(name);

			name = ReflectionUtils.invokeHidden(store, "getFullScaledName", "imagebutton", null); //$NON-NLS-1$ //$NON-NLS-2$
			assertNull(name);

			name = ReflectionUtils.invokeHidden(store, "getFullScaledName", "imagebutton", ImageFileExtension.PNG); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("imagebutton_p_.png", name); //$NON-NLS-1$

			name = ReflectionUtils.invokeHidden(store, "getFullScaledName", "dontexits", ImageFileExtension.PNG); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("dontexits.png", name); //$NON-NLS-1$

			name = ReflectionUtils.invokeHidden(store, "getFullScaledName", "imagebutton", ImageFileExtension.JPG); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("imagebutton.jpg", name); //$NON-NLS-1$

			name = ReflectionUtils.invokeHidden(store, "getFullScaledName", "dontexits", ImageFileExtension.JPG); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("dontexits.jpg", name); //$NON-NLS-1$

		} finally {
			LnfManager.setLnf(originalLnf);
		}

	}

	private static class MyLnf extends RienaDefaultLnf {
		@Override
		public String getIconScaleSuffix(final Point dpi) {

			if (dpi == null) {
				return "_h_"; //$NON-NLS-1$
			}
			if (dpi.x < 96) {
				return "_h_"; //$NON-NLS-1$
			}
			return "_p_"; //$NON-NLS-1$

		}
	}

}
