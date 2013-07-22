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
package org.eclipse.riena.ui.swt.utils;

import org.osgi.framework.Bundle;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.tests.Activator;

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
		final String fullName = ReflectionUtils.invokeHidden(store, "getFullName", "abc", ImageFileExtension.JPG);
		assertEquals("abc.jpg", fullName);

	}

	/**
	 * Tests the method {@code getImage(String)}.
	 */
	public void testGetImage() {

		final Shell shell = new Shell();

		final ImageStore store = ImageStore.getInstance();
		store.update(new IImagePathExtension[] {});
		Image image = store.getImage("spirit");
		assertNull(image);

		final IImagePathExtension extension = new IImagePathExtension() {

			public Bundle getContributingBundle() {
				return Activator.getDefault().getBundle();
			}

			public String getPath() {
				return "icons";
			}

		};
		store.update(new IImagePathExtension[] { extension });
		image = store.getImage("spirit");
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
				return "icons";
			}

		};
		store.update(new IImagePathExtension[] { extension });

		Image image = store.getImage("spirit", ImageFileExtension.PNG);
		assertNotNull(image);
		image = store.getImage("spirit", ImageFileExtension.GIF);
		assertNull(image);
		image = store.getImage("spirit", ImageFileExtension.JPG);
		assertNull(image);

		image = store.getImage("eclipse", ImageFileExtension.PNG);
		assertNull(image);
		image = store.getImage("eclipse", ImageFileExtension.GIF);
		assertNotNull(image);
		image = store.getImage("eclipse", ImageFileExtension.JPG);
		assertNull(image);

		SwtUtilities.dispose(shell);

	}

}
