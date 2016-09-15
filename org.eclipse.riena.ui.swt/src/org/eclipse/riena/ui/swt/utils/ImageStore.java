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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGRoot;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.ui.swt.Activator;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerProvider;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.core.resource.IconState;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * The ImageStore returns the images for given names. The images are loaded form and cached. The ImageStore extends the images name, if a state (@see
 * {@link ImageState}) like pressed of hover is given. If the image name has no file extension, the extension ".png" will be added.
 * 
 * @since 6.0
 */
public final class ImageStore {

	private Image missingImage;
	private IImagePathExtension[] iconPaths;

	private final static SingletonProvider<ImageStore> IS = new SingletonProvider<ImageStore>(ImageStore.class);

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ImageStore.class);

	private ImageStore() {
		// utility class
	}

	/**
	 * Returns an instance (always the same) of this class.
	 * 
	 * @return instance of {@code ImageStore}
	 */
	public static ImageStore getInstance() {
		return IS.getInstance();
	}

	/**
	 * Returns the image for the given image name and with the given file extension.
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param fileExtension
	 *            extension of the image file (@see ImageFileExtension)
	 * @param imageSize
	 *            Image size is necessary for SVG files.
	 * @return image or {@code null} if no image exists for the given name.
	 * @since 6.1
	 */
	public Image getImage(final String imageName, final ImageFileExtension fileExtension, final IconSize imageSize) {

		Point dpi = SwtUtilities.getDpi();
		String fullName = getFullScaledName(imageName, fileExtension, dpi, imageSize);
		Image image = loadImage(fullName);
		if (image != null) {
			return image;
		}

		fullName = getFullSvgName(imageName);
		if (fullName != null) {
			final String fullNameWithGroupIdentifier = addIconGroupIdentifier(fullName, imageSize);
			image = loadSvgImage(fullNameWithGroupIdentifier, imageSize);
			if (image != null) {
				return image;
			} else {
				image = loadSvgImage(fullName, imageSize);
				if (image != null) {
					return image;
				}
			}
		}

		dpi = SwtUtilities.getDefaultDpi();
		fullName = getFullScaledName(imageName, fileExtension, dpi);
		image = loadImage(fullName);
		if (image != null) {
			return image;
		}

		fullName = getFullName(imageName, fileExtension);
		image = loadImage(fullName);
		if (image != null) {
			return image;
		}

		final String defaultIconName = getDefaultIconMangerImageName(imageName);
		if (!StringUtils.equals(defaultIconName, imageName)) {
			fullName = getFullName(defaultIconName, fileExtension);
			image = loadImage(fullName);
		}

		return image;

	}

	/**
	 * Returns the ImageDescriptor of the image for the given image name
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param fileExtension
	 *            extension of the image file (@see ImageFileExtension)
	 * @return ImageDescriptor of the image or {@code null} if no image exists for the given name.
	 * @since 6.1
	 */
	public ImageDescriptor getImageDescriptor(final String imageName, final ImageFileExtension fileExtension) {

		final Image image = getImage(imageName, fileExtension);
		if (image == null) {
			return null;
		}

		return ImageDescriptor.createFromImage(image);

	}

	/**
	 * Returns the ImageDescriptor of the image for the given image name and size
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param imageSize
	 *            the IconSize of the image
	 * @return ImageDescriptor of the image or {@code null} if no image exists for the given name.
	 * @since 6.2
	 */
	public ImageDescriptor getImageDescriptor(final String imageName, final IconSize imageSize) {

		final Image image = getImage(imageName, imageSize);
		if (image == null) {
			return null;
		}

		return ImageDescriptor.createFromImage(image);
	}

	/**
	 * Returns the URI of the image for the given image name
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param fileExtension
	 *            extension of the image file (@see ImageFileExtension)
	 * @return URI of the image or {@code null} if no image exists for the given name.
	 * @since 6.1
	 */
	public URI getImageUri(final String imageName, final ImageFileExtension fileExtension) {

		URI uri = null;

		Point dpi = SwtUtilities.getDpi();
		String fullName = getFullScaledName(imageName, fileExtension, dpi);
		uri = getUri(fullName);
		if (uri != null) {
			return uri;
		}

		fullName = getFullName(imageName, fileExtension);
		uri = getUri(fullName);
		if (uri != null) {
			return uri;
		}

		dpi = SwtUtilities.getDefaultDpi();
		fullName = getFullScaledName(imageName, fileExtension, dpi);
		uri = getUri(fullName);
		if (uri != null) {
			return uri;
		}

		final String defaultIconName = getDefaultIconMangerImageName(imageName);
		if (!StringUtils.equals(defaultIconName, imageName)) {
			fullName = getFullName(defaultIconName, fileExtension);
			uri = getUri(fullName);
		}

		return uri;

	}

	/**
	 * Tries to find the given image and returns the URI of the image file. The file of the image is searched in every given bundle + icon path. The icon paths
	 * are define via extension points.
	 * 
	 * @param fullName
	 *            complete name of the image (with scaling, with extension etc.)
	 * @return URI of the image or {@code null} if the image file wasn't found
	 * @see #getImageUrl(String)
	 */
	private URI getUri(final String fullName) {

		final URL imageUrl = getImageUrl(fullName);
		if (imageUrl != null) {
			try {
				return imageUrl.toURI();
			} catch (final URISyntaxException ex) {
				LOGGER.log(LogService.LOG_DEBUG, "Can't create URI.", ex); //$NON-NLS-1$
			}
		}

		return null;

	}

	/**
	 * Uses the default icon manager to generate the icon name/ID.
	 * 
	 * @param imageName
	 *            name of the image (icon ID)
	 * @return default icon name/ID
	 */
	private String getDefaultIconMangerImageName(final String imageName) {

		final IIconManager iconManager = IconManagerProvider.getInstance().getIconManager();
		final String name = iconManager.getName(imageName);
		IconSize size = iconManager.getSize(imageName);
		if ((size == null) || (size.getClass() != IconSize.class)) {
			size = IconSize.NONE;
		}
		IconState state = iconManager.getState(imageName);
		if ((state == null) || (state.getClass() != IconState.class)) {
			state = IconState.NORMAL;
		}

		final IIconManager defaultIconManager = IconManagerProvider.getInstance().getDefaultIconManager();
		final String defaultIconName = defaultIconManager.getIconID(name, size, state);

		return defaultIconName;

	}

	/**
	 * Returns the image for the given image name and given state.
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @return image or {@code null} if no image exists for the given name.
	 * @since 6.0
	 */
	public Image getImage(final String imageName) {
		return getImage(imageName, ImageFileExtension.PNG, IconSize.NONE);
	}

	/**
	 * Returns the image for the given image name and given size.
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @return image or {@code null} if no image exists for the given name.
	 * @since 6.1
	 */
	public Image getImage(final String imageName, final IconSize imageSize) {
		return getImage(imageName, ImageFileExtension.PNG, imageSize);
	}

	public Image getImage(final String imageName, final ImageFileExtension fileExtension) {
		return getImage(imageName, fileExtension, IconSize.NONE);
	}

	/**
	 * Returns the full name of the image.
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param state
	 *            state of the image (@see ImageState)
	 * @param fileExtension
	 *            extension of the image file (@see ImageFileExtension)
	 * @return full name of the image (file name).
	 */
	private String getFullName(final String imageName, final ImageFileExtension fileExtension) {

		if (StringUtils.isEmpty(imageName)) {
			return null;
		}

		String fullName = imageName;

		if (imageName.indexOf('.') < 0) {
			if (fileExtension != null) {
				fullName += "." + fileExtension.getFileNameExtension(); //$NON-NLS-1$
			}
		}

		return fullName;

	}

	private String getFullScaledName(final String imageName, final ImageFileExtension fileExtension, final Point dpi) {
		return getFullScaledName(imageName, fileExtension, dpi, null);
	}

	private String getFullScaledName(final String imageName, final ImageFileExtension fileExtension, final Point dpi, final IconSize imageSize) {

		if (StringUtils.isEmpty(imageName)) {
			return null;
		}
		if (imageName.indexOf('.') >= 0) {
			return null;
		}
		if (fileExtension == null) {
			return null;
		}

		String fullName = addImageScaleSuffix(imageName, fileExtension, dpi, imageSize);
		if (fullName != null) {
			return fullName += "." + fileExtension.getFileNameExtension(); //$NON-NLS-1$
		}
		return null;

	}

	/**
	 * Returns the complete name of the SVG file.
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param imageSize
	 * @return the name of the SVG file or {@code null} if this isn't a SVG file (e.g. imageName has other file extension)
	 */
	private String getFullSvgName(final String imageName) {

		if (StringUtils.isEmpty(imageName)) {
			return null;
		}
		final String svgExtension = '.' + ImageFileExtension.SVG.getFileNameExtension();
		if (imageName.endsWith(svgExtension)) {
			return imageName;
		}
		if (imageName.indexOf('.') >= 0) {
			return null;
		}
		return imageName + svgExtension;
	}

	/**
	 * Returns the image for the given name. If the image isn't cached, the image is loaded form the resources and stores in the cache of the {@code ImageStore}
	 * .
	 * 
	 * @param fullName
	 *            full name of the image (file name)
	 * @return image or {@code null} if no image exists for the given name.
	 */
	private synchronized Image loadImage(final String fullName) {
		if (StringUtils.isEmpty(fullName)) {
			return null;
		}

		if (Activator.getDefault() == null) {
			return null;
		}

		final ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
		Image image = imageRegistry.get(fullName);
		if (image == null || image.isDisposed()) {
			final ImageDescriptor descriptor = getImageDescriptor(fullName);
			if (descriptor == null) {
				return null;
			}
			imageRegistry.remove(fullName);
			imageRegistry.put(fullName, descriptor);
			image = imageRegistry.get(fullName);
		}
		return image;
	}

	private synchronized Image loadSvgImage(final String fullName, final IconSize imageSize) {

		// https://svgsalamander.java.net/docs/use.html

		if (StringUtils.isEmpty(fullName)) {
			return null;
		}

		if (Activator.getDefault() == null) {
			return null;
		}

		final ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
		final String registryKey = fullName + "?" + imageSize; //$NON-NLS-1$
		Image image = imageRegistry.get(registryKey);
		if (image == null || image.isDisposed()) {
			image = createSvgImage(fullName, imageSize);
			if (image != null) {
				imageRegistry.remove(registryKey);
				imageRegistry.put(registryKey, image);
				image = imageRegistry.get(registryKey);
			}
		}
		return image;
	}

	/**
	 * Loads the given SVG file and creates a SWT image with the given size.
	 * 
	 * @param fullName
	 *            file name of the SVG
	 * @param imageSize
	 *            expected size of the SWT image (if {@code null} the size of the SVG is used)
	 * @return SWT image or {@code null} if creation failed
	 */
	private Image createSvgImage(final String fullName, final IconSize imageSize) {

		final Display display = SwtUtilities.getDisplay();
		if (display == null) {
			return null;
		}

		final URL url = getImageUrl(fullName);
		if (url == null) {
			return null;
		}

		final URI svgUri = SVGCache.getSVGUniverse().loadSVG(url);
		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(svgUri);
		if (diagram == null) {
			return null;
		}

		final Rectangle bounds = getImageBounds(diagram, imageSize);
		final BufferedImage bi = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D ig2 = bi.createGraphics();
		final AffineTransform at = new AffineTransform();
		at.setToScale(bounds.width / diagram.getWidth(), bounds.height / diagram.getHeight());
		ig2.transform(at);
		ig2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		ig2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		ig2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try {
			final SVGRoot root = diagram.getRoot();
			//root.setAttribute("width", AnimationElement.AT_XML, Integer.toString(bounds.width)); //$NON-NLS-1$
			//root.setAttribute("height", AnimationElement.AT_XML, Integer.toString(bounds.height)); //$NON-NLS-1$
			root.build();
			diagram.setIgnoringClipHeuristic(true);
			root.render(ig2);
		} catch (final SVGException e) {
			e.printStackTrace();
			return null;
		} finally {
			SVGCache.getSVGUniverse().clear();
		}

		final ImageData imageData = SwtUtilities.convertAwtImageToImageData(bi);
		if (imageData == null) {
			return null;
		}

		return new Image(display, imageData);

	}

	/**
	 * Adds the mapped IconSizeGroupIdentifier to the image name.
	 * 
	 * 
	 * @param fullName
	 *            The name of the image with file extension. Precondition: fullName must have the fileextension .svg
	 * @param imageSize
	 *            The size of the Image.
	 * @return The name of the image with the mapped IconSizeGroupIdentifier. Returns null if fullName or imageSize is null.
	 * @since 6.2
	 */
	public String addIconGroupIdentifier(String fullName, final IconSize imageSize) {
		Assert.isNotNull(fullName);
		Assert.isNotNull(imageSize);
		Assert.isTrue(fullName.endsWith(".svg"), "imagename must end with '.svg'"); //$NON-NLS-1$ //$NON-NLS-2$

		final String fileExtension = fullName.substring(fullName.length() - ".svg".length()); //$NON-NLS-1$
		final String fileName = fullName.substring(0, fullName.length() - ".svg".length()); //$NON-NLS-1$

		return fullName = fileName + getIconSizeGroupIdentifier(imageSize) + fileExtension;
	}

	/**
	 * Returns the String value of the iconsize group for the given IconSize.
	 * 
	 * @param imageSize
	 *            the desired iconSize
	 * @return the Group suffix of the mapped iconsize, returns the given iconsize if the map contains no mapping for this value.
	 */
	private String getIconSizeGroupIdentifier(final IconSize iconSize) {
		return LnfManager.getLnf().getIconSizeGroupIdentifier(iconSize);
	}

	/**
	 * Returns the expected (scalded) size/bounds of the image
	 * 
	 * @param svgDiagram
	 *            diagram of the SVG
	 * @param imageSize
	 *            expected size of the SWT image (if {@code null} the size of the SVG is used)
	 * @return bounds of the image
	 */
	private Rectangle getImageBounds(final SVGDiagram svgDiagram, final IconSize imageSize) {
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		if ((imageSize == null) || (imageSize == IconSize.NONE)) {
			if (svgDiagram != null) {
				width = Math.round(svgDiagram.getWidth());
				height = Math.round(svgDiagram.getHeight());
			}
		} else {
			width = imageSize.getWidth();
			height = imageSize.getHeight();
		}
		x = SwtUtilities.convertXToDpi(x);
		y = SwtUtilities.convertYToDpi(y);
		width = SwtUtilities.convertXToDpi(width);
		height = SwtUtilities.convertYToDpi(height);
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Returns a descriptor of the image for the given name. The file of the image is searched in every given bundle + icon path. The icon paths are define via
	 * extension points.
	 * 
	 * @param fullName
	 *            full name of the image (file name)
	 * @return image descriptor or {@code null} if file does not exists.
	 */
	private ImageDescriptor getImageDescriptor(final String fullName) {

		final URL url = getImageUrl(fullName);
		if (url != null) {
			return ImageDescriptor.createFromURL(url);
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("Image resource \""); //$NON-NLS-1$
		sb.append(fullName);
		sb.append("\" not found in:"); //$NON-NLS-1$

		for (final IImagePathExtension iconPath : iconPaths) {
			sb.append("\n  "); //$NON-NLS-1$
			sb.append(iconPath.getContributingBundle().getLocation());
			sb.append(iconPath.getPath());
		}

		LOGGER.log(LogService.LOG_DEBUG, sb.toString());
		return null;

	}

	/**
	 * Tries to find the given image and returns the URL of the image file. The file of the image is searched in every given bundle + icon path. The icon paths
	 * are define via extension points.
	 * 
	 * @param fullName
	 *            complete name of the image (with scaling, with extension etc.)
	 * @return URL of the image or {@code null} if the image file wasn't found
	 */
	private URL getImageUrl(final String fullName) {

		if (iconPaths != null) {
			for (final IImagePathExtension iconPath : iconPaths) {
				final String fullPath = iconPath.getPath() + '/' + fullName;
				final URL url = iconPath.getContributingBundle().getEntry(fullPath);
				if (url != null) {
					return url;
				}
			}
		}

		return null;

	}

	/**
	 * Returns the missing image.
	 * 
	 * @return missing image
	 */
	public synchronized Image getMissingImage() {
		if (missingImage == null) {
			missingImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return missingImage;
	}

	/**
	 * Adds the suffix of scaling to the given name of the image.
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param fileExtension
	 *            extension of the image file
	 * 
	 * @return image name with suffix of scaling or image name without suffix if no matching image file exists
	 * 
	 * @since 6.0
	 */
	public String addImageScaleSuffix(final String imageName, final ImageFileExtension fileExtension) {
		return addImageScaleSuffix(imageName, fileExtension, SwtUtilities.getDpi());
	}

	/**
	 * Adds the suffix of scaling to the given name of the image.
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param fileExtension
	 *            extension of the image file
	 * @param dpi
	 *            this display dpi
	 * 
	 * @return image name with suffix of scaling and icon size identifier or image name without suffix if no matching image file exists
	 * 
	 * @since 6.1
	 */
	public String addImageScaleSuffix(final String imageName, final ImageFileExtension fileExtension, final Point dpi) {
		return addImageScaleSuffix(imageName, fileExtension, dpi, null);
	}

	/**
	 * Adds the suffix of scaling to the given name of the image.
	 * 
	 * @param imageName
	 *            name (ID) of the image
	 * @param fileExtension
	 *            extension of the image file
	 * @param dpi
	 *            this display dpi
	 * @param imageSize
	 *            the requested image size
	 * 
	 * 
	 * @return image name with suffix of scaling and icon size identifier or image name without suffix if no matching image file exists
	 * 
	 * @since 6.2
	 */
	public String addImageScaleSuffix(final String imageName, final ImageFileExtension fileExtension, final Point dpi, final IconSize imageSize) {

		if (LnfManager.isLnfCreated()) {
			final String suffix = LnfManager.getLnf().getIconScaleSuffix(dpi);
			if (!StringUtils.isEmpty(suffix)) {
				final List<String> candidates = new ArrayList<String>((Arrays.asList(imageName + suffix)));
				if (imageSize != null) {
					// honor icon size
					candidates.add(imageName + imageSize.getDefaultMapping() + suffix);
				}
				// concrete names first
				Collections.reverse(candidates);
				for (final String candidate : candidates) {
					if (imageExists(candidate, fileExtension)) {
						return candidate;
					}
				}
			}
		}

		return imageName;

	}

	private synchronized boolean imageExists(final String imageName, final ImageFileExtension fileExtension) {
		final String fullName = getFullName(imageName, fileExtension);
		final ImageDescriptor descriptor = getImageDescriptor(fullName);
		return (descriptor != null);
	}

	@InjectExtension
	public void update(final IImagePathExtension[] iconPaths) {
		this.iconPaths = iconPaths;
	}

}
