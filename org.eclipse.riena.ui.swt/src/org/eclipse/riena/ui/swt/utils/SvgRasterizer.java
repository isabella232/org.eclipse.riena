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

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Map;

import org.apache.batik.gvt.renderer.ConcreteImageRendererFactory;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.gvt.renderer.ImageRendererFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;

import org.eclipse.swt.graphics.Rectangle;

class SvgRasterizer {
	/**
	 * The transcoder input.
	 */
	protected TranscoderInput input;

	/**
	 * The transcoder hints.
	 */
	protected TranscodingHints hints = new TranscodingHints();

	/**
	 * The image that represents the SVG document.
	 */
	protected BufferedImage outputImg;

	/**
	 * Constructs a new SVGRasterizer.
	 *
	 * @param url
	 *            the URL of the document to rasterize
	 */
	public SvgRasterizer() {
	}

	public void setUrl(final URL url) {
		this.input = new TranscoderInput(url.toString());
	}

	/**
	 * Returns the image that represents the SVG document.
	 */
	public BufferedImage createBufferedImage(final Rectangle imageBounds) throws TranscoderException {

		final Rasterizer r = new Rasterizer();
		hints.put(ImageTranscoder.KEY_WIDTH, new Float(imageBounds.width));
		hints.put(ImageTranscoder.KEY_HEIGHT, new Float(imageBounds.height));
		hints.put(ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE, false);

		r.setTranscodingHints((Map) hints);
		r.transcode(input, null);
		return outputImg;
	}

	/**
	 * An image transcoder that stores the resulting image.
	 * 
	 * @param <RenderingHints>
	 */
	private class Rasterizer extends ImageTranscoder {

		private final RenderingHints renderingHints = new RenderingHints(null);

		@Override
		protected ImageRenderer createRenderer() {

			renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			final ImageRendererFactory rendFactory = new ConcreteImageRendererFactory();
			final ImageRenderer renderer = rendFactory.createStaticImageRenderer();

			renderer.setRenderingHints(renderingHints);

			return renderer;
		}

		@Override
		public BufferedImage createImage(final int w, final int h) {
			return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}

		@Override
		public void writeImage(final BufferedImage img, final TranscoderOutput output) throws TranscoderException {
			SvgRasterizer.this.outputImg = img;
		}
	}
}