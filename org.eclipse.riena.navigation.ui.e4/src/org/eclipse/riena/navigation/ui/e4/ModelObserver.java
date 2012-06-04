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
package org.eclipse.riena.navigation.ui.e4;

import javax.inject.Inject;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.ui.e4.listener.MyApplicationNodeListener;
import org.eclipse.riena.navigation.ui.e4.listener.MyModuleGroupNodeListener;
import org.eclipse.riena.navigation.ui.e4.listener.MyModuleNodeListener;
import org.eclipse.riena.navigation.ui.e4.listener.MySubApplicationNodeListener;
import org.eclipse.riena.navigation.ui.e4.listener.MySubModuleNodeListener;

public class ModelObserver {

	@Inject
	private IApplicationNode appNode;

	@Inject
	private MyApplicationNodeListener appListener;
	@Inject
	private MySubApplicationNodeListener subAppListener;
	@Inject
	private MyModuleGroupNodeListener moduleGroupListener;
	@Inject
	private MyModuleNodeListener moduleListener;
	@Inject
	private MySubModuleNodeListener subModuleListener;

	public void install() {
		final NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(appListener);
		navigationTreeObserver.addListener(subAppListener);
		navigationTreeObserver.addListener(moduleGroupListener);
		navigationTreeObserver.addListener(moduleListener);
		navigationTreeObserver.addListener(subModuleListener);
		navigationTreeObserver.addListenerTo(appNode);

	}

}
