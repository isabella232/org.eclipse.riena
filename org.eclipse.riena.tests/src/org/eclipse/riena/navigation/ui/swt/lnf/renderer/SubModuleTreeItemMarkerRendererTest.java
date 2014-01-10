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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.IIconizableMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link SubModuleTreeItemMarkerRenderer}.
 */
@UITestCase
public class SubModuleTreeItemMarkerRendererTest extends RienaTestCase {

	private Shell shell;
	private GC gc;
	private TreeItem item;
	private RienaDefaultLnf originalLnf;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		final Tree tree = new Tree(shell, SWT.NONE);
		item = new TreeItem(tree, SWT.NONE);
		gc = new GC(tree);
		originalLnf = LnfManager.getLnf();
		LnfManager.setLnf(new MyLnf());
		LnfManager.getLnf().initialize();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		gc.dispose();
		gc = null;
		SwtUtilities.dispose(shell);
		LnfManager.setLnf(originalLnf);
	}

	/**
	 * Tests the method {@code paint}.
	 */
	public void testPaint() {

		final MockRenderer renderer = new MockRenderer();
		renderer.setBounds(0, 0, 100, 100);

		try {
			renderer.paint(gc, null);
			fail("AssertionFailedException expected");
		} catch (final AssertionFailedException e) {
			ok("AssertionFailedException expected");
		}

		try {
			renderer.paint(gc, shell);
			fail("AssertionFailedException expected");
		} catch (final AssertionFailedException e) {
			ok("AssertionFailedException expected");
		}

		try {
			renderer.paint(null, item);
			fail("AssertionFailedException expected");
		} catch (final AssertionFailedException e) {
			ok("AssertionFailedException expected");
		}

		renderer.resetPaintMarkersCalled();
		renderer.paint(gc, item);
		assertFalse(renderer.isPaintMarkersCalled());

		final SubModuleNode node = new SubModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		renderer.setMarkers(node.getMarkers());
		renderer.resetPaintMarkersCalled();
		renderer.paint(gc, item);
		assertFalse(renderer.isPaintMarkersCalled());

		node.addMarker(new ErrorMarker());
		renderer.setMarkers(node.getMarkers());
		renderer.resetPaintMarkersCalled();
		renderer.paint(gc, item);
		assertTrue(renderer.isPaintMarkersCalled());

		node.removeAllMarkers();
		node.addMarker(new NegativeMarker());
		renderer.setMarkers(node.getMarkers());
		renderer.resetPaintMarkersCalled();
		renderer.paint(gc, item);
		assertFalse(renderer.isPaintMarkersCalled());

		renderer.dispose();

	}

	/**
	 * Tests the method {@code paintMarkers}.
	 * 
	 * @throws Exception
	 */
	public void testPaintMarkers() throws Exception {

		item.setImage(createItemImage());

		final SubModuleTreeItemMarkerRenderer renderer = new SubModuleTreeItemMarkerRenderer();
		renderer.setBounds(0, 0, 100, 25);

		// create image without markers
		final Collection<IIconizableMarker> markers = new ArrayList<IIconizableMarker>();
		final byte[] noMarkersBytes = paintMarker(renderer, markers, item);

		// no marker -> no marker image is drawn
		byte[] paintBytes = paintMarker(renderer, markers, item);
		assertTrue(Arrays.equals(noMarkersBytes, paintBytes));

		// one error marker -> error marker image is drawn
		markers.add(new ErrorMarker());
		paintBytes = paintMarker(renderer, markers, item);
		assertFalse(Arrays.equals(noMarkersBytes, paintBytes));

		// one error marker, but item has no image -> no marker image is drawn
		item.setImage((Image) null);
		paintBytes = paintMarker(renderer, markers, item);
		assertTrue(Arrays.equals(noMarkersBytes, paintBytes));

		renderer.dispose();

	}

	public void testPaintMarkersHierarchically() throws Exception {

		LnfManager.setLnf(new MyLnf() {

			@Override
			protected void initializeTheme() {
				super.initializeTheme();
				putLnfSetting(LnfKeyConstants.SUB_MODULE_TREE_MARKER_HIERARCHIC_ORDER_POSITION,
						IIconizableMarker.MarkerPosition.BOTTOM_RIGHT);
			}
		});

		item.setImage(createItemImage());

		final SubModuleTreeItemMarkerRenderer renderer = new SubModuleTreeItemMarkerRenderer();
		renderer.setBounds(0, 0, 100, 25);

		// create image without markers
		final Collection<IIconizableMarker> markers = new ArrayList<IIconizableMarker>();
		final byte[] noMarkersBytes = paintMarker(renderer, markers, item);

		// one ErrorMarker -> ErrorMarker image is drawn
		markers.add(new ErrorMarker());
		final byte[] errorMarkersBytes = paintMarker(renderer, markers, item);
		assertFalse(Arrays.equals(noMarkersBytes, errorMarkersBytes));

		// one ErrorMarker and one MandatoryMarker -> only ErrorMarker image is drawn
		markers.add(new MandatoryMarker());
		byte[] paintBytes = paintMarker(renderer, markers, item);
		assertTrue(Arrays.equals(errorMarkersBytes, paintBytes));

		// remove ErrorMarker -> MandatoryMarker is drawn
		markers.clear();
		markers.add(new MandatoryMarker());
		paintBytes = paintMarker(renderer, markers, item);
		assertFalse(Arrays.equals(noMarkersBytes, paintBytes));
		assertFalse(Arrays.equals(errorMarkersBytes, paintBytes));

		// one MandatoryMarker, but item has no image -> no marker image is drawn
		item.setImage((Image) null);
		paintBytes = paintMarker(renderer, markers, item);
		assertTrue(Arrays.equals(noMarkersBytes, paintBytes));

		renderer.dispose();
	}

	/**
	 * Paints a Collection of markers and returns the bytes written.
	 */
	private byte[] paintMarker(final SubModuleTreeItemMarkerRenderer renderer,
			final Collection<IIconizableMarker> markers, final TreeItem item) {
		final Image paintImage = new Image(shell.getDisplay(), new Rectangle(0, 0, 10, 10));
		final GC paintGC = new GC(paintImage);
		ReflectionUtils.invokeHidden(renderer, "paintMarkers", paintGC, markers, item);
		final byte[] paintBytes = paintImage.getImageData().data;
		paintGC.dispose();
		SwtUtilities.dispose(paintImage);

		return paintBytes;
	}

	/**
	 * Tests the method {@code calcMarkerCoordinates}.
	 */
	public void testCalcMarkerCoordinates() {

		final SubModuleTreeItemMarkerRenderer renderer = new SubModuleTreeItemMarkerRenderer();
		renderer.setBounds(2, 3, 100, 25);

		final Image itemImage = createItemImage();
		final Image markerImage = createMarkerImage();

		Point pos = ReflectionUtils.invokeHidden(renderer, "calcMarkerCoordinates", itemImage, markerImage,
				IIconizableMarker.MarkerPosition.TOP_LEFT);
		assertEquals(2, pos.x);
		assertEquals(3, pos.y);

		pos = ReflectionUtils.invokeHidden(renderer, "calcMarkerCoordinates", itemImage, markerImage,
				IIconizableMarker.MarkerPosition.TOP_RIGHT);
		assertEquals(2 + 5, pos.x);
		assertEquals(3, pos.y);

		pos = ReflectionUtils.invokeHidden(renderer, "calcMarkerCoordinates", itemImage, markerImage,
				IIconizableMarker.MarkerPosition.BOTTOM_LEFT);
		assertEquals(2, pos.x);
		assertEquals(3 + 5, pos.y);

		pos = ReflectionUtils.invokeHidden(renderer, "calcMarkerCoordinates", itemImage, markerImage,
				IIconizableMarker.MarkerPosition.BOTTOM_RIGHT);
		assertEquals(2 + 5, pos.x);
		assertEquals(3 + 5, pos.y);

		SwtUtilities.dispose(itemImage);
		SwtUtilities.dispose(markerImage);

	}

	/**
	 * Creates a image with a small green rectangle.
	 * 
	 * @return image
	 */
	private Image createItemImage() {
		final Image image = new Image(shell.getDisplay(), new Rectangle(0, 0, 10, 10));
		final GC imageGC = new GC(image);
		imageGC.setForeground(LnfManager.getLnf().getColor("green"));
		imageGC.setBackground(LnfManager.getLnf().getColor("green"));
		imageGC.fillRectangle(0, 0, 5, 5);
		imageGC.dispose();
		return image;
	}

	/**
	 * Creates a image with a very small red rectangle.
	 * 
	 * @return image
	 */
	private Image createMarkerImage() {
		final Image image = new Image(shell.getDisplay(), new Rectangle(0, 0, 5, 5));
		final GC imageGC = new GC(image);
		imageGC.setForeground(LnfManager.getLnf().getColor("red"));
		imageGC.setBackground(LnfManager.getLnf().getColor("red"));
		imageGC.fillRectangle(0, 0, 3, 3);
		imageGC.dispose();
		return image;
	}

	/**
	 * Creates a image with a very small green rectangle.
	 * 
	 * @return image
	 */
	private Image createMandatoraMarkerImage() {
		final Image image = new Image(shell.getDisplay(), new Rectangle(0, 0, 5, 5));
		final GC imageGC = new GC(image);
		imageGC.setForeground(LnfManager.getLnf().getColor("green"));
		imageGC.setBackground(LnfManager.getLnf().getColor("green"));
		imageGC.fillRectangle(0, 0, 2, 2);
		imageGC.dispose();
		return image;
	}

	/**
	 * This Look and Feel returns always the same image.
	 */
	private class MyLnf extends RienaDefaultLnf {

		private final Image errorImage;
		private final Image mandatoryImage;

		public MyLnf() {
			super();
			errorImage = createMarkerImage();
			mandatoryImage = createMandatoraMarkerImage();
		}

		@Override
		public Image getImage(final String key) {
			if (key.equals("ErrorMarker")) {
				return errorImage;
			} else {
				return mandatoryImage;
			}
		}

	}

	private static class MockRenderer extends SubModuleTreeItemMarkerRenderer {

		private boolean paintMarkersCalled;

		public MockRenderer() {
			resetPaintMarkersCalled();
		}

		public boolean isPaintMarkersCalled() {
			return paintMarkersCalled;
		}

		public void resetPaintMarkersCalled() {
			this.paintMarkersCalled = false;
		}

		/**
		 * @see org.eclipse.riena.navigation.ui.swt.lnf.renderer.SubModuleTreeItemMarkerRenderer#paintMarkers(org.eclipse.swt.graphics.GC,
		 *      java.util.Collection, org.eclipse.swt.widgets.TreeItem)
		 */
		@Override
		protected void paintMarkers(final GC gc, final Collection<IIconizableMarker> markers, final TreeItem item) {
			super.paintMarkers(gc, markers, item);
			this.paintMarkersCalled = true;
		}

	}

}
