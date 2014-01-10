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
package org.eclipse.riena.security.ui.filter;

import java.lang.reflect.Constructor;
import java.security.Permission;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.riena.core.injector.InjectionFailure;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.internal.navigation.ui.filter.IUIFilterApplier;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;
import org.eclipse.riena.navigation.NavigationNodeUtility;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.security.common.authorization.ISentinelService;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.IUIFilterProvider;
import org.eclipse.riena.ui.filter.impl.UIFilter;

/**
 * Applies {@link UIFilter}s to the application model and ridgets depending on
 * the {@link Permission}s available for the current {@link Principal}.
 * 
 */
public class PermissionUIFilterApplier implements IUIFilterApplier {

	private IApplicationNode applicationNode;
	private final List<PermissionFilterMapping> permissionFilterMappings;
	private ISentinelService sentinel;

	/**
	 * Creates a new {@link PermissionFilterMapping} instance
	 */
	public PermissionUIFilterApplier() {
		permissionFilterMappings = new ArrayList<PermissionFilterMapping>();
	}

	public void applyFilter(final INavigationNode<?> navigationNode) {
		if (!(navigationNode instanceof IApplicationNode)) {
			return;
		}
		this.applicationNode = (IApplicationNode) navigationNode;
		updateFilters();
		observeNavigationModel();
	}

	protected void observeNavigationModel() {
		new NodeStructureObserver(applicationNode, createStructureEventDelegation()).start();
	}

	/*
	 * updates the user interface filters for the current principal
	 */
	private void updateFilters() {
		if (permissionFilterMappings.size() == 0) {
			return;
		}
		for (final PermissionFilterMapping mapper : permissionFilterMappings) {
			if (!sentinel.checkAccess(mapper.getPermission())) {
				final IUIFilterContainer container = mapper.getFilterContainer();
				final IUIFilter filter = container.getFilter();
				final Collection<String> targetNodeIds = container.getFilterTargetNodeIds();
				for (final String targetNodeId : targetNodeIds) {
					final List<INavigationNode<?>> nodes = new ArrayList<INavigationNode<?>>();
					NavigationNodeUtility.findNodesByLongId(targetNodeId, applicationNode, nodes);
					for (final INavigationNode<?> node : nodes) {
						if (!node.getFilters().contains(filter)) {
							node.addFilter(filter);
						}
					}
				}
			}
		}
	}

	/*
	 * class for delegation of application model events to the filter logic
	 */
	private ISimpleNavigationNodeListener createStructureEventDelegation() {
		return new SimpleNavigationNodeAdapter() {

			@Override
			public void childAdded(final INavigationNode<?> source, final INavigationNode<?> childAdded) {
				updateFilters();
			}

			@Override
			public void childRemoved(final INavigationNode<?> source, final INavigationNode<?> childRemoved) {
				updateFilters();
			}

		};
	}

	/**
	 * Binds the given {@link ISentinelService}
	 */
	@InjectService
	public void bind(final ISentinelService sentinel) {
		this.sentinel = sentinel;
	}

	/**
	 * Unbinds the given {@link ISentinelService}
	 */
	public void unbind(final ISentinelService sentinel) {
		if (this.sentinel == sentinel) {
			this.sentinel = null;
		}
	}

	/**
	 * Binds the {@link IPermissionFilterMappingExtension} extensions
	 */
	@InjectExtension
	public void update(final IPermissionFilterMappingExtension[] principalFilterMappers) {
		permissionFilterMappings.clear();
		final IUIFilterProvider uiFilterProvider = getUIFilterProvider();
		for (final IPermissionFilterMappingExtension mapping : principalFilterMappers) {
			permissionFilterMappings.add(new PermissionFilterMapping(createPermission(mapping), uiFilterProvider
					.provideFilter(mapping.getFilterID())));
		}

	}

	protected IUIFilterProvider getUIFilterProvider() {
		return Service.get(IUIFilterProvider.class);
	}

	private Permission createPermission(final IPermissionFilterMappingExtension mapping) {
		Class<? extends Permission> permissionClass = mapping.getPermissionClass();
		if (permissionClass == null) {
			permissionClass = UserInterfacePermission.class;
		}
		try {
			if (StringUtils.isEmpty(mapping.getPermissionAction())) {
				final Constructor<? extends Permission> constructor = permissionClass.getConstructor(String.class);
				return constructor.newInstance(mapping.getPermissionName());
			} else {
				final Constructor<? extends Permission> constructor = permissionClass.getConstructor(String.class,
						String.class);
				return constructor.newInstance(mapping.getPermissionName(), mapping.getPermissionAction());
			}
		} catch (final Exception e) {
			throw new InjectionFailure("Could not create permission from '" + mapping + "'s.", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * class caching a relation between a permission and a user interface filter
	 */
	private class PermissionFilterMapping {

		private final Permission permission;

		private final IUIFilterContainer filterContainer;

		public PermissionFilterMapping(final Permission permission, final IUIFilterContainer filterContainer) {
			this.permission = permission;
			this.filterContainer = filterContainer;
		}

		public Permission getPermission() {
			return permission;
		}

		public IUIFilterContainer getFilterContainer() {
			return filterContainer;
		}

	}

}
