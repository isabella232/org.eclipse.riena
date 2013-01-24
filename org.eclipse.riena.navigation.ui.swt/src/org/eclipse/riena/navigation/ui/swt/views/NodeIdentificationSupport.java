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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.swt.utils.WidgetIdentificationSupport;

/**
 * Helper class for setting 'rienaid' on widgets, that are related to riena
 * nodes.
 * 
 * @since 1.2
 */
public final class NodeIdentificationSupport {

	private NodeIdentificationSupport() {
		// utility class
	}

	/**
	 * Sets rienaid for widget, identified by NavigationNodeId. Value for riena
	 * id is built according to following syntax:
	 * <widgetId>.<typeId>[.instanceId]. WidgetId can be any string, and can be
	 * used to distinguish several widgets, that are related to same
	 * NavigationNodeID
	 * 
	 * @param aWidget
	 *            : Widget
	 * @param aWidgetId
	 *            : Widget id
	 * @param aNodeId
	 *            : Node Id
	 */
	public static void setIdentification(final Widget aWidget, final String aWidgetId, final NavigationNodeId aNodeId) {
		if (aNodeId.getInstanceId() != null) {
			WidgetIdentificationSupport.setIdentification(aWidget, aWidgetId,
					String.format("%s.%s", aNodeId.getTypeId(), aNodeId //$NON-NLS-1$
							.getInstanceId()));
		} else {
			WidgetIdentificationSupport.setIdentification(aWidget, aWidgetId, aNodeId.getTypeId());
		}
	}

	/**
	 * Sets rienaid for a widget, related to a navigation node. If nodeId is not
	 * null, it's string representation is used (see setIdentification(Widget,
	 * String, NavigationNodeId )). If it is null textual label is used
	 * (INavigationNode.getLabel())
	 * 
	 * @param aWidget
	 *            : Widget
	 * @param aWidgetId
	 *            : Widget id
	 * @param Node
	 */

	@SuppressWarnings("rawtypes")
	public static void setIdentification(final Widget aWidget, final String aWidgetId, final INavigationNode aNode) {
		if (aNode.getNodeId() != null) {
			setIdentification(aWidget, aWidgetId, aNode.getNodeId());
		} else {
			WidgetIdentificationSupport.setIdentification(aWidget, aWidgetId, aNode.getLabel());
		}
	}
}
