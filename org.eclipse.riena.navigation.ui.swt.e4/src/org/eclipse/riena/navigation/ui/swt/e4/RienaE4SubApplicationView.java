/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.e4;

import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.internal.ui.ridgets.swt.uiprocess.UIProcessRidget;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
import org.eclipse.riena.navigation.ui.swt.binding.DelegatingRidgetMapper;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.views.IComponentUpdateListener;
import org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.uiprocess.UIProcessControl;

/**
 *
 */
public class RienaE4SubApplicationView implements INavigationNodeView<SubApplicationNode> {

	private SubApplicationNode node;

	private UIProcessControl uiProcessControl;

	private final Widget widget;

	/**
	 * 
	 */
	public RienaE4SubApplicationView(final Widget widget) {
		this.widget = widget;
	}

	public void bind(final SubApplicationNode node) {
		SubApplicationController controller = (SubApplicationController) node.getNavigationNodeController();
		if (null == controller) {
			controller = new SubApplicationController(node);
			bind(controller);
		}

	}

	@SuppressWarnings("restriction")
	private void bind(final SubApplicationController controller) {
		final DelegatingRidgetMapper ridgetMapper = new DelegatingRidgetMapper(SwtControlRidgetMapper.getInstance());
		ridgetMapper.addMapping(UIProcessControl.class, UIProcessRidget.class);
		final InjectSwtViewBindingDelegate binding = new InjectSwtViewBindingDelegate(ridgetMapper);
		createUIProcessControl(binding);
		binding.injectAndBind(controller);
		controller.afterBind();

	}

	private void createUIProcessControl(final InjectSwtViewBindingDelegate binding) {
		uiProcessControl = new UIProcessControl(widget.getDisplay().getShells()[0]);
		uiProcessControl.setPropertyName("uiProcessRidget"); //$NON-NLS-1$
		binding.addUIControl(uiProcessControl);
	}

	public void unbind() {

	}

	public SubApplicationNode getNavigationNode() {
		return node;
	}

	public void addUpdateListener(final IComponentUpdateListener listener) {
	}

}
