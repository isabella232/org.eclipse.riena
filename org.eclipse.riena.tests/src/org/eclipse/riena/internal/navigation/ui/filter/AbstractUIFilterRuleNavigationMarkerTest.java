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
package org.eclipse.riena.internal.navigation.ui.filter;

import junit.framework.TestCase;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.filter.IUIFilterRule;

/**
 * Tests of the class {@link AbstractUIFilterRuleNavigationMarker}.
 */
@NonUITestCase
public class AbstractUIFilterRuleNavigationMarkerTest extends TestCase {

	/**
	 * Tests the method {@code matches}.
	 */
	public void testMatches() {

		final INavigationNode<?> node = new SubModuleNode();
		node.setNodeId(new NavigationNodeId("id"));
		final IUIFilterRule attribute = new MyUIFilterRuleNavigationMarker("*/" + node.getNodeId().getTypeId(), null);
		assertFalse(attribute.matches((Object[]) null));
		assertFalse(attribute.matches(new Object()));
		final SubModuleNode sm = new SubModuleNode();
		sm.setNodeId(new NavigationNodeId("id2"));
		assertFalse(attribute.matches(sm));
		assertTrue(attribute.matches(node));

	}

	/**
	 * Tests the method {@code apply}.
	 */
	public void testApply() {

		final INavigationNode<?> node = new SubModuleNode();
		node.setNodeId(new NavigationNodeId("id"));
		node.setNavigationProcessor(new NavigationProcessor());
		assertTrue(node.isVisible());
		final IUIFilterRule attribute = new MyUIFilterRuleNavigationMarker(node.getNodeId().getTypeId(),
				new HiddenMarker());

		attribute.apply(node);
		assertFalse(node.isVisible());

	}

	/**
	 * Tests the method {@code remove}.
	 */
	public void testRemove() {

		final INavigationNode<?> node = new SubModuleNode();
		node.setNodeId(new NavigationNodeId("id"));
		node.setNavigationProcessor(new NavigationProcessor());
		assertTrue(node.isEnabled());
		final IUIFilterRule attribute = new MyUIFilterRuleNavigationMarker(node.getNodeId().getTypeId(),
				new DisabledMarker());
		attribute.apply(node);
		assertFalse(node.isEnabled());

		attribute.remove(node);
		assertTrue(node.isEnabled());

	}

	private static class MyUIFilterRuleNavigationMarker extends AbstractUIFilterRuleNavigationMarker {

		public MyUIFilterRuleNavigationMarker(final String nodeId, final IMarker marker) {
			super(nodeId, marker);
		}

	}

}
