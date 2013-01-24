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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Tests of the class <code>EmbeddedBorderRenderer</code>.
 */
@NonUITestCase
public class EmbeddedBorderRendererTest extends TestCase {

	/**
	 * Test of the method <code>computeInnerBounds(Rectangle)</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testComputeInnerBounds() throws Exception {

		final Rectangle outerBounds = new Rectangle(12, 24, 36, 48);
		final EmbeddedBorderRenderer renderer = new EmbeddedBorderRenderer();
		final Rectangle innerBounds = renderer.computeInnerBounds(outerBounds);
		final int width = renderer.getBorderWidth();
		assertEquals(outerBounds.x + width, innerBounds.x);
		assertEquals(outerBounds.y + width, innerBounds.y);
		assertEquals(outerBounds.width - width * 2, innerBounds.width);
		assertEquals(outerBounds.height - width * 2, innerBounds.height);

	}

	/**
	 * Tests the <i>protected</i> method {@code getBorderColor()}.
	 */
	public void testGetBorderColor() {

		final EmbeddedBorderRenderer renderer = new EmbeddedBorderRenderer();

		renderer.setActive(true);
		Color borderColor = ReflectionUtils.invokeHidden(renderer, "getBorderColor");
		Color expectedColor = LnfManager.getLnf().getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BORDER_COLOR);
		assertSame(expectedColor, borderColor);

		renderer.setActive(false);
		borderColor = ReflectionUtils.invokeHidden(renderer, "getBorderColor");
		expectedColor = LnfManager.getLnf().getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BORDER_COLOR);
		assertSame(expectedColor, borderColor);

		renderer.setActive(true);
		final Set<IMarker> markers = new HashSet<IMarker>();
		final IMarker newMarker = new DisabledMarker();
		markers.add(newMarker);
		renderer.setMarkers(markers);
		borderColor = ReflectionUtils.invokeHidden(renderer, "getBorderColor");
		expectedColor = LnfManager.getLnf().getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_DISABLED_BORDER_COLOR);
		assertSame(expectedColor, borderColor);

	}

}
