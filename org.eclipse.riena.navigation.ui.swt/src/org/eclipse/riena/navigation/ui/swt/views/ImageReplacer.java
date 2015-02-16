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
package org.eclipse.riena.navigation.ui.swt.views;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.internal.provisional.action.IToolBarContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.singleton.SessionSingletonProvider;
import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.core.util.FileUtils;
import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.ImageFileExtension;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 * Class to replace in {@link CommandContributionItem} not scaled images with scaled images.
 * <p>
 * <i>(This class does not support e4 implementations of {@link IContributionItem})</i>
 * 
 * @since 6.0
 */
public class ImageReplacer {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ImageReplacer.class);
	private final static SingletonProvider<ImageReplacer> IR = new SessionSingletonProvider<ImageReplacer>(ImageReplacer.class);

	private final Class<? extends ImageDescriptor> fileImageDescriptorClass;
	private final Class<? extends ImageDescriptor> urlImageDescriptorClass;

	protected ImageReplacer() {

		ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(this.getClass(), ""); //$NON-NLS-1$
		fileImageDescriptorClass = imageDescriptor.getClass();

		Class<? extends ImageDescriptor> imageDescriptorClass = null;
		try {
			final URL url = new URL("file", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			imageDescriptor = ImageDescriptor.createFromURL(url);
			imageDescriptorClass = imageDescriptor.getClass();
		} catch (final MalformedURLException ex) {
			LOGGER.log(LogService.LOG_ERROR, "Can't create ImageDescriptor for URL.", ex); //$NON-NLS-1$
		}
		urlImageDescriptorClass = imageDescriptorClass;

	}

	public static ImageReplacer getInstance() {
		return IR.getInstance();
	}

	/**
	 * This class only can handle a limited number of {@link ImageDescriptor}.
	 * <p>
	 * This methods checks if the given {@link ImageDescriptor} is supported.
	 * 
	 * @param imageDescriptor
	 * @return {@code true} given imageDescriptor is supported; otherwise {@code false}
	 */
	private boolean isImageDescriptorSupported(final ImageDescriptor imageDescriptor) {
		if (imageDescriptor != null) {
			if (imageDescriptor.getClass() == urlImageDescriptorClass) {
				return true;
			}
			if (imageDescriptor.getClass() == fileImageDescriptorClass) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Replaces all not scaled images of the given contribution manager.
	 */
	public void replaceImagesOfManager(final IContributionManager contributionManager) {

		if (contributionManager != null) {
			final IContributionItem[] items = contributionManager.getItems();
			for (final IContributionItem item : items) {
				if (item instanceof IToolBarContributionItem) {
					replaceImagesOfManager(((IToolBarContributionItem) item).getToolBarManager());
				} else if (item instanceof IMenuManager) {
					replaceImagesOfManager((IMenuManager) item);
				} else {
					replaceImages(item);
				}
			}
		}

	}

	/**
	 * Replace all not scaled images (default, disable, hover) of the given item.
	 * 
	 * @param contributionManager
	 * @param item
	 *            command item
	 */
	protected void replaceImages(final IContributionItem item) {

		if (item instanceof CommandContributionItem) {

			final CommandContributionItem commandItem = (CommandContributionItem) item;

			final CommandContributionItemParameter itemParamter = commandItem.getData();

			if (itemParamter.icon != null) {
				final ImageDescriptor scaledImage = getScaledImage(itemParamter.icon);
				if (scaledImage != null && !itemParamter.icon.equals(scaledImage)) {
					ReflectionUtils.setHidden(item, "contributedIcon", scaledImage); //$NON-NLS-1$
					ReflectionUtils.setHidden(item, "icon", scaledImage); //$NON-NLS-1$
				}
			}

			if (itemParamter.disabledIcon != null) {
				final ImageDescriptor scaledImage = getScaledImage(itemParamter.disabledIcon);
				if (scaledImage != null && !itemParamter.disabledIcon.equals(scaledImage)) {
					ReflectionUtils.setHidden(item, "contributedDisabledIcon", scaledImage); //$NON-NLS-1$
					ReflectionUtils.setHidden(item, "disabledIcon", scaledImage); //$NON-NLS-1$
				}
			}

			if (itemParamter.hoverIcon != null) {
				final ImageDescriptor scaledImage = getScaledImage(itemParamter.hoverIcon);
				if (scaledImage != null && !itemParamter.hoverIcon.equals(scaledImage)) {
					ReflectionUtils.setHidden(item, "contributedHoverIcon", scaledImage); //$NON-NLS-1$
					ReflectionUtils.setHidden(item, "hoverIcon", scaledImage); //$NON-NLS-1$
				}
			}
		}
	}

	/**
	 * Returns the scaled image that can be used to replace the (not scaled) given image.
	 * 
	 * @param image
	 *            not scaled image
	 * @return scaled image or {@code null} if no scaled image exists.
	 */
	private ImageDescriptor getScaledImage(final ImageDescriptor image) {

		if (image == null) {
			return null;
		}

		if (isImageDescriptorSupported(image)) {
			try {
				final String filePath = ReflectionUtils.invokeHidden(image, "getFilePath"); //$NON-NLS-1$
				if (StringUtils.isEmpty(filePath)) {
					return null;
				}
				final String imageName = getImageName(filePath);
				ImageFileExtension imageFileExtension = null;
				final String fileNameExtension = FileUtils.getFileExtension(filePath);
				imageFileExtension = ImageFileExtension.getImageFileExtension(fileNameExtension);
				if (imageFileExtension == null) {
					imageFileExtension = ImageFileExtension.PNG;
				}
				return ImageStore.getInstance().getImageDescriptor(imageName, imageFileExtension);
			} catch (final ReflectionFailure failure) {
				return null;
			}
		}

		return null;

	}

	/**
	 * Returns the name of the image (without scaling suffix) from the given file name.
	 * 
	 * @param fullName
	 *            file name of the image
	 * @return image name
	 */
	private String getImageName(final String fullName) {

		if (StringUtils.isEmpty(fullName)) {
			return fullName;
		}

		// remove extension
		String imageName = FileUtils.getNameWithoutExtension(fullName);

		// remove (default) scaling
		if (LnfManager.isLnfCreated()) {
			final String suffix = LnfManager.getLnf().getIconScaleSuffix(null);
			if (!StringUtils.isEmpty(suffix)) {
				if (imageName.endsWith(suffix)) {
					imageName = imageName.substring(0, imageName.length() - suffix.length());
				}
			}
		}

		return imageName;

	}

	public String getImageName(final URI imageUrl) {

		if (imageUrl == null) {
			return null;
		}

		final String fullName = imageUrl.getPath();
		return getImageName(fullName);

	}

}
