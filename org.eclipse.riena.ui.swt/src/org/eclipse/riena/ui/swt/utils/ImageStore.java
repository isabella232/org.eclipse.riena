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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.net.URL;

import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

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
//import com.kitfox.svg.SVGCache;
//import com.kitfox.svg.SVGDiagram;
//import com.kitfox.svg.SVGException;
//import com.kitfox.svg.SVGRoot;
//import com.kitfox.svg.animation.AnimationElement;
import org.osgi.service.log.LogService;

/**
 * The ImageStore returns the images for given names. The images are loaded form and cached. The ImageStore extends the images name, if a state (@see
 * {@link ImageState}) like pressed of hover is given. If the image name has no file extension, the extension ".png" will be added.
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
	 * @return image or {@code null} if no image exists for the given name.
	 */
	public Image getImage(final String imageName, final ImageFileExtension fileExtension, final IconSize imageSize) {

		String fullName = getFullScaledName(imageName, fileExtension);
		Image image = loadImage(fullName);
		if (image != null) {
			return image;
		}

		fullName = getFullSvgName(imageName, imageSize);
		image = loadSvgImage(fullName, imageSize);
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

	public ImageDescriptor getImageDescriptor(final String imageName, final ImageFileExtension fileExtension) {

		final Image image = getImage(imageName, fileExtension);
		if (image == null) {
			return null;
		}

		return ImageDescriptor.createFromImage(image);

		//		String fullName = getFullScaledName(imageName, fileExtension);
		//		ImageDescriptor descriptor = getImageDescriptor(fullName);
		//		if (descriptor != null) {
		//			return descriptor;
		//		}
		//
		//		// fullName = getFullSvgName(imageName, imageSize);
		//
		//		final String defaultIconName = getDefaultIconMangerImageName(imageName);
		//		if (!StringUtils.equals(defaultIconName, imageName)) {
		//			fullName = getFullName(defaultIconName, fileExtension);
		//			descriptor = getImageDescriptor(fullName);
		//		}
		//
		//		return descriptor;

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
	 */
	public Image getImage(final String imageName) {
		return getImage(imageName, ImageFileExtension.PNG, IconSize.NONE);
	}

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

	private String getFullScaledName(final String imageName, final ImageFileExtension fileExtension) {

		if (StringUtils.isEmpty(imageName)) {
			return null;
		}
		if (imageName.indexOf('.') >= 0) {
			return null;
		}
		if (fileExtension == null) {
			return null;
		}

		String fullName = addImageScaleSuffix(imageName, fileExtension);
		if (fullName != null) {
			return fullName += "." + fileExtension.getFileNameExtension(); //$NON-NLS-1$
		}
		return null;

	}

	private String getFullSvgName(final String imageName, final IconSize imageSize) {

		if (StringUtils.isEmpty(imageName)) {
			return null;
		}
		final String svgExtension = '.' + ImageFileExtension.SVG.getFileNameExtension();
		if ((imageName.indexOf('.') >= 0) && (imageName.endsWith(svgExtension))) {
			return imageName;
		}
		if (imageName.indexOf('.') >= 0) {
			return null;
		}
		String fullName = imageName;
		if ((imageSize != null) && (imageSize != IconSize.NONE)) {
			if (fullName.endsWith(imageSize.getDefaultMapping())) {
				final int endIndex = fullName.length() - imageSize.getDefaultMapping().length();
				fullName = fullName.substring(0, endIndex);
			}
		}
		return fullName += svgExtension;

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
		Image image = imageRegistry.get(fullName);
		if (image == null || image.isDisposed()) {
			image = createSvgImageSalamander(fullName, imageSize);
			if (image != null) {
				imageRegistry.remove(fullName);
				imageRegistry.put(fullName, image);
				image = imageRegistry.get(fullName);
			}
		}
		return image;
	}

	public static ImageData convertAWTImageToSWT(final java.awt.Image image) {
		if (image == null) {
			throw new IllegalArgumentException("Null 'image' argument."); //$NON-NLS-1$
		}
		final int w = image.getWidth(null);
		final int h = image.getHeight(null);
		if (w == -1 || h == -1) {
			return null;
		}
		final BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		final Graphics g = bi.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return convertToSWT(bi);
	}

	private Image createSvgImageSalamander(final String fullName, final IconSize imageSize) {

		return null;

		//		SVGDiagram diagram = null;
		//		for (final IImagePathExtension iconPath : iconPaths) {
		//			final String fullPath = iconPath.getPath() + '/' + fullName;
		//			final URL url = iconPath.getContributingBundle().getEntry(fullPath);
		//			if (url != null) {
		//				final URI svgUri = SVGCache.getSVGUniverse().loadSVG(url);
		//				diagram = SVGCache.getSVGUniverse().getDiagram(svgUri);
		//			}
		//		}
		//		if (diagram == null) {
		//			return null;
		//		}
		//
		//		final Display display = SwtUtilities.getDisplay();
		//		if (display == null) {
		//			return null;
		//		}
		//
		//		final Rectangle bounds = getImageBounds(diagram, imageSize);
		//		final BufferedImage bi = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		//		final Graphics2D ig2 = bi.createGraphics();
		//		ig2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		//		ig2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		//		ig2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//		try {
		//			final SVGRoot root = diagram.getRoot();
		//			root.setAttribute("width", AnimationElement.AT_XML, Integer.toString(bounds.width)); //$NON-NLS-1$
		//			root.setAttribute("height", AnimationElement.AT_XML, Integer.toString(bounds.height)); //$NON-NLS-1$
		//			root.build();
		//			diagram.setIgnoringClipHeuristic(true);
		//			root.render(ig2);
		//		} catch (final SVGException e) {
		//			e.printStackTrace();
		//			return null;
		//		}
		//
		//		return AWTSWTImageUtils.convertToSWTImage(bi);

		//		final ImageData imageData = convertToSWT(bi);
		//
		//		return new Image(display, imageData);

	}

	/**
	 * Converts a buffered image to SWT <code>ImageData</code>.
	 * 
	 * @param bufferedImage
	 *            the buffered image (<code>null</code> not permitted).
	 * 
	 * @return The image data.
	 */
	public static ImageData convertToSWT(final BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			final DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			final PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			final ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			final WritableRaster raster = bufferedImage.getRaster();
			final int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					final int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			final IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
			final int size = colorModel.getMapSize();
			final byte[] reds = new byte[size];
			final byte[] greens = new byte[size];
			final byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			final RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			final PaletteData palette = new PaletteData(rgbs);
			final ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			final WritableRaster raster = bufferedImage.getRaster();
			final int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

	//	private Rectangle getImageBounds(final SVGDiagram svgDiagram, final IconSize imageSize) {
	//		int x = 0;
	//		int y = 0;
	//		int width = 0;
	//		int height = 0;
	//		if ((imageSize == null) || (imageSize == IconSize.NONE)) {
	//			width = Math.round(svgDiagram.getWidth());
	//			height = Math.round(svgDiagram.getHeight());
	//		} else {
	//			width = imageSize.getWidth();
	//			height = imageSize.getHeight();
	//		}
	//		x = SwtUtilities.convertXToDpi(x);
	//		y = SwtUtilities.convertYToDpi(y);
	//		width = SwtUtilities.convertXToDpi(width);
	//		height = SwtUtilities.convertYToDpi(height);
	//		return new Rectangle(x, y, width, height);
	//	}

	/**
	 * Returns a descriptor of the image for the given name. The file of the image is searched in every given bundle + icon path. The icon paths are define via
	 * extension points.
	 * 
	 * @param fullName
	 *            full name of the image (file name)
	 * @return image descriptor or {@code null} if file does not exists.
	 */
	private ImageDescriptor getImageDescriptor(final String fullName) {

		for (final IImagePathExtension iconPath : iconPaths) {
			final String fullPath = iconPath.getPath() + '/' + fullName;
			final URL url = iconPath.getContributingBundle().getEntry(fullPath);
			if (url != null) {
				return ImageDescriptor.createFromURL(url);
			}
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
	 * @since 6.0
	 */
	public String addImageScaleSuffix(final String imageName, final ImageFileExtension fileExtension) {

		if (LnfManager.isLnfCreated()) {
			final Point dpi = SwtUtilities.getDpi();
			String suffix = LnfManager.getLnf().getIconScaleSuffix(dpi);
			if (!StringUtils.isEmpty(suffix)) {
				final String scaledName = imageName + suffix;
				if (imageExists(scaledName, fileExtension)) {
					return scaledName;
				}
			}

			suffix = LnfManager.getLnf().getIconScaleSuffix(new Point(0, 0));
			if (!StringUtils.isEmpty(suffix)) {
				final String scaledName = imageName + suffix;
				if (imageExists(scaledName, fileExtension)) {
					return scaledName;
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
