/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
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

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Tests of the class {@link ModuleGroupBorderRenderer}.
 */
@NonUITestCase
public class ModuleGroupBorderRendererTest extends TestCase {

	/**
	 * Tests the <i>protected</i> method {@code getBorderColor()}.
	 */
	public void testGetBorderColor() {

		final ModuleGroupBorderRenderer renderer = new ModuleGroupBorderRenderer();

		renderer.setActive(true);
		Color borderColor = ReflectionUtils.invokeHidden(renderer, "getBorderColor");
		Color expectedColor = LnfManager.getLnf().getColor(LnfKeyConstants.MODULE_GROUP_ACTIVE_BORDER_COLOR);
		assertSame(expectedColor, borderColor);

		renderer.setActive(false);
		borderColor = ReflectionUtils.invokeHidden(renderer, "getBorderColor");
		expectedColor = LnfManager.getLnf().getColor(LnfKeyConstants.MODULE_GROUP_PASSIVE_BORDER_COLOR);
		assertSame(expectedColor, borderColor);

		renderer.setActive(true);
		final Set<IMarker> markers = new HashSet<IMarker>();
		final IMarker newMarker = new DisabledMarker();
		markers.add(newMarker);
		renderer.setMarkers(markers);
		borderColor = ReflectionUtils.invokeHidden(renderer, "getBorderColor");
		expectedColor = LnfManager.getLnf().getColor(LnfKeyConstants.MODULE_GROUP_DISABLED_BORDER_COLOR);
		assertSame(expectedColor, borderColor);

	}

}
