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

	protected TranscoderInput inputUrl;
	protected TranscodingHints transcodingHints = new TranscodingHints();
	protected BufferedImage outputImg;

	public SvgRasterizer() {
	}

	public void setUrl(final URL url) {
		this.inputUrl = new TranscoderInput(url.toString());
	}

	/**
	 * Creates and returns the image that represents the SVG Image.
	 */
	public BufferedImage createBufferedImage(final Rectangle imageBounds) throws TranscoderException {

		final Rasterizer r = new Rasterizer();
		transcodingHints.put(ImageTranscoder.KEY_WIDTH, new Float(imageBounds.width));
		transcodingHints.put(ImageTranscoder.KEY_HEIGHT, new Float(imageBounds.height));
		transcodingHints.put(ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE, false);

		r.setTranscodingHints((Map) transcodingHints);
		r.transcode(inputUrl, null);
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
		public void writeImage(final BufferedImage img, final TranscoderOutput output) {
			SvgRasterizer.this.outputImg = img;
		}
	}
}