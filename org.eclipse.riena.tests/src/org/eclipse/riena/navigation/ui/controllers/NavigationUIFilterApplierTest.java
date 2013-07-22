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
package org.eclipse.riena.navigation.ui.controllers;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.navigation.ui.filter.UIFilterRuleNavigationDisabledMarker;
import org.eclipse.riena.internal.navigation.ui.filter.UIFilterRuleNavigationHiddenMarker;
import org.eclipse.riena.internal.navigation.ui.filter.UIFilterRuleRidgetHiddenMarker;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterRule;
import org.eclipse.riena.ui.filter.impl.UIFilter;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Tests of the class {@link NavigationUIFilterApplier}.
 */
@UITestCase
public class NavigationUIFilterApplierTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		final Display display = Display.getDefault();
		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);
	}

	/**
	 * Tests the method {@code collectFilters}.
	 */
	public void testCollectFilters() {

		final NavigationUIFilterApplier<SubModuleNode> applier = new NavigationUIFilterApplier<SubModuleNode>();

		final Collection<IUIFilter> filters = new ArrayList<IUIFilter>();
		final NavigationNodeId id = new NavigationNodeId("4711");
		final SubModuleNode node = new SubModuleNode(id);

		ReflectionUtils.invokeHidden(applier, "collectFilters", node, filters);
		assertTrue(filters.isEmpty());

		final UIFilter filter = new UIFilter();
		node.addFilter(filter);

		ReflectionUtils.invokeHidden(applier, "collectFilters", node, filters);
		assertTrue(filters.size() == 1);
		assertSame(filter, filters.iterator().next());

		filters.clear();
		final NavigationNodeId id2 = new NavigationNodeId("parent");
		final SubModuleNode node2 = new SubModuleNode(id2);
		node2.addChild(node);
		final UIFilter filter2 = new UIFilter();
		node2.addFilter(filter2);
		ReflectionUtils.invokeHidden(applier, "collectFilters", node, filters);
		assertTrue(filters.size() == 2);
		assertSame(filter, filters.iterator().next());
		assertTrue(filters.contains(filter2));

		filters.clear();
		final NavigationNodeId id3 = new NavigationNodeId("child");
		final SubModuleNode node3 = new SubModuleNode(id3);
		node.addChild(node3);
		final UIFilter filter3 = new UIFilter();
		node3.addFilter(filter3);
		ReflectionUtils.invokeHidden(applier, "collectFilters", node, filters);
		assertTrue(filters.size() == 2);
		assertFalse(filters.contains(filter3));

	}

	/**
	 * Tests the method {@code applyFilters}.
	 */
	public void testApplyFilters() {

		final NavigationUIFilterApplier<SubModuleNode> applier = new NavigationUIFilterApplier<SubModuleNode>();

		final NavigationNodeId id = new NavigationNodeId("4711");
		final SubModuleNode node = new SubModuleNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		final Collection<IUIFilterRule> attributes = new ArrayList<IUIFilterRule>(1);
		attributes.add(new UIFilterRuleNavigationDisabledMarker("*" + node.getNodeId().getTypeId()));
		final IUIFilter filter = new UIFilter(attributes);
		node.addFilter(filter);
		final Collection<IUIFilterRule> attributes2 = new ArrayList<IUIFilterRule>(1);
		attributes2.add(new UIFilterRuleNavigationHiddenMarker("*" + node.getNodeId().getTypeId()));
		final IUIFilter filter2 = new UIFilter(attributes2);
		node.addFilter(filter2);
		ReflectionUtils.invokeHidden(applier, "applyFilters", node);
		assertFalse(node.getMarkersOfType(DisabledMarker.class).isEmpty());
		assertFalse(node.getMarkersOfType(HiddenMarker.class).isEmpty());

	}

	/**
	 * Tests the method {@code applyFilter}.
	 */
	public void testApplyFilter() {

		final NavigationUIFilterApplier<SubModuleNode> applier = new NavigationUIFilterApplier<SubModuleNode>();

		final NavigationNodeId id = new NavigationNodeId("4711");
		final SubModuleNode node = new SubModuleNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		final NavigationNodeId id2 = new NavigationNodeId("0815");
		final SubModuleNode node2 = new SubModuleNode(id2);
		node.addChild(node2);

		final Collection<IUIFilterRule> attributes = new ArrayList<IUIFilterRule>(2);
		attributes.add(new UIFilterRuleNavigationDisabledMarker("*" + node.getNodeId().getTypeId()));
		attributes.add(new UIFilterRuleNavigationHiddenMarker("*" + node.getNodeId().getTypeId()));
		final IUIFilter filter = new UIFilter(attributes);
		IUIFilterRuleClosure closure = ReflectionUtils.getHidden(applier, "APPLY_CLOSURE");
		ReflectionUtils.invokeHidden(applier, "applyFilter", node, filter, closure);
		assertFalse(node.getMarkersOfType(DisabledMarker.class).isEmpty());
		assertFalse(node.getMarkersOfType(HiddenMarker.class).isEmpty());
		assertFalse(node2.getMarkersOfType(DisabledMarker.class).isEmpty());
		assertFalse(node2.getMarkersOfType(HiddenMarker.class).isEmpty());

		closure = ReflectionUtils.getHidden(applier, "REMOVE_CLOSURE");
		ReflectionUtils.invokeHidden(applier, "applyFilter", node, filter, closure);
		assertTrue(node.getMarkersOfType(DisabledMarker.class).isEmpty());
		assertTrue(node.getMarkersOfType(HiddenMarker.class).isEmpty());
		assertTrue(node2.getMarkersOfType(DisabledMarker.class).isEmpty());
		assertTrue(node2.getMarkersOfType(HiddenMarker.class).isEmpty());

	}

	/**
	 * Tests the method {@code applyFilterRule}.
	 */
	public void testApplyFilterRule() {

		final NavigationUIFilterApplier<SubModuleNode> applier = new NavigationUIFilterApplier<SubModuleNode>();

		final NavigationNodeId id = new NavigationNodeId("4711");
		final SubModuleNode node = new SubModuleNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		IUIFilterRule attribute = new UIFilterRuleNavigationDisabledMarker("*" + node.getNodeId().getTypeId());
		final IUIFilterRuleClosure closure = ReflectionUtils.getHidden(applier, "APPLY_CLOSURE");
		ReflectionUtils.invokeHidden(applier, "applyFilterRule", node, attribute, closure);
		assertFalse(node.getMarkersOfType(DisabledMarker.class).isEmpty());

		final SubModuleController controller = new SubModuleController();
		node.setNavigationNodeController(controller);
		controller.setNavigationNode(node);
		final Shell shell = new Shell();
		final Label label = new Label(shell, SWT.NONE);
		label.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "0815");
		final LabelRidget ridget = new LabelRidget(label);
		controller.addRidget("0815", ridget);

		attribute = new UIFilterRuleRidgetHiddenMarker("*0815");
		ReflectionUtils.invokeHidden(applier, "applyFilterRule", node, attribute, closure);
		assertFalse(ridget.getMarkersOfType(HiddenMarker.class).isEmpty());

		shell.dispose();

	}
}
