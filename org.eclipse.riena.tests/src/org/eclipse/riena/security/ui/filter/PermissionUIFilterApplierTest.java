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
package org.eclipse.riena.security.ui.filter;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.security.common.authorization.ISentinelService;
import org.eclipse.riena.security.ui.filter.IPermissionFilterMappingExtension;
import org.eclipse.riena.security.ui.filter.PermissionUIFilterApplier;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.IUIFilterProvider;
import org.eclipse.riena.ui.filter.impl.UIFilter;

/**
 * {@link TestCase} for {@link PermissionUIFilterApplier}
 */
@NonUITestCase
public class PermissionUIFilterApplierTest extends TestCase {

	public void testApplyFilters() throws Exception {
		final IApplicationNode app = new ApplicationNode();
		final SubApplicationNode subApp1 = new SubApplicationNode(new NavigationNodeId("subapp1"));
		app.addChild(subApp1);
		final SubApplicationNode subApp2 = new SubApplicationNode(new NavigationNodeId("subapp2"));
		app.addChild(subApp2);
		final ModuleGroupNode mg1 = new ModuleGroupNode(new NavigationNodeId("mg1"));
		subApp1.addChild(mg1);
		final ModuleGroupNode mg2 = new ModuleGroupNode(new NavigationNodeId("mg2"));
		subApp2.addChild(mg2);
		final ModuleNode m1 = new ModuleNode(new NavigationNodeId("m1"));
		mg1.addChild(m1);
		final ModuleNode m2 = new ModuleNode(new NavigationNodeId("m2"));
		mg2.addChild(m2);
		final SubModuleNode sm1 = new SubModuleNode(new NavigationNodeId("sm1"));
		m1.addChild(sm1);
		final SubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("sm2"));
		m2.addChild(sm2);
		app.activate();

		final UIFilter filter = new UIFilter("filter1");
		final TestUIFilterContainer filterContainer = new TestUIFilterContainer(filter,
				"/application/subapp1/mg1/m1/sm1");
		final TestUIFilterProvider filterProvider = new TestUIFilterProvider();
		filterProvider.filterContainers.put("filter_sm1", filterContainer);

		final TestPermissionFilterManager filterManager = new TestPermissionFilterManager();
		filterManager.filterProvider = filterProvider;

		final FilterMappingExtension filterMapping = new FilterMappingExtension(null, "somePName", "somePAction",
				"filter_sm1");
		filterManager.update(new IPermissionFilterMappingExtension[] { filterMapping });
		final TestSentinalService sentinalService = new TestSentinalService();
		sentinalService.valid = false;
		filterManager.bind(sentinalService);

		filterManager.applyFilter(app);
		assertTrue(sm1.getFilters().size() == 1);
		assertEquals(sm1.getFilters().iterator().next(), filter);
		filterManager.applyFilter(app);
		assertTrue(sm1.getFilters().size() == 1);
		sm1.removeAllFilters();
		sentinalService.valid = true;
		filterManager.applyFilter(app);
		assertTrue(sm1.getFilters().isEmpty());

	}

	private class TestSentinalService implements ISentinelService {

		boolean valid = true;

		public boolean checkAccess(final Permission permission) {
			return valid;
		}

	}

	private class TestUIFilterContainer implements IUIFilterContainer {

		IUIFilter filter;
		String targetNodeId;

		public TestUIFilterContainer(final IUIFilter filter, final String targetNodeId) {
			this.filter = filter;
			this.targetNodeId = targetNodeId;
		}

		public IUIFilter getFilter() {
			return filter;
		}

		public Collection<String> getFilterTargetNodeIds() {
			final List<String> ids = new ArrayList<String>();
			ids.add(targetNodeId);
			return ids;
		}

	}

	private class FilterMappingExtension implements IPermissionFilterMappingExtension {

		Class<? extends Permission> permissionClass;
		String permissionName;
		String permissionAction;
		String filterId;

		public FilterMappingExtension(final Class<? extends Permission> permissionClass, final String permissionName,
				final String permissionAction, final String filterId) {
			this.permissionClass = permissionClass;
			this.permissionName = permissionName;
			this.permissionAction = permissionAction;
			this.filterId = filterId;
		}

		public Class<? extends Permission> getPermissionClass() {
			return permissionClass;
		}

		public String getPermissionName() {
			return permissionName;
		}

		public String getPermissionAction() {
			return permissionName;
		}

		public String getFilterID() {
			return filterId;
		}

	}

	private class TestUIFilterProvider implements IUIFilterProvider {

		Map<String, IUIFilterContainer> filterContainers = new HashMap<String, IUIFilterContainer>();

		public IUIFilterContainer provideFilter(final String filterID) {
			return filterContainers.get(filterID);
		}

	}

	private final class TestPermissionFilterManager extends PermissionUIFilterApplier {
		IUIFilterProvider filterProvider;

		@Override
		protected IUIFilterProvider getUIFilterProvider() {
			return filterProvider;
		}

		@Override
		protected void observeNavigationModel() {
		}

	}
}
