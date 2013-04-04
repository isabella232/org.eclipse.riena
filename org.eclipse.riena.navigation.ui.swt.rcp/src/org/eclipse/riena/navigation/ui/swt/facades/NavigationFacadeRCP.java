package org.eclipse.riena.navigation.ui.swt.facades;

import org.eclipse.riena.navigation.ui.swt.views.ModuleNavigationListener;
import org.eclipse.swt.widgets.Tree;

public class NavigationFacadeRCP extends NavigationFacade {

	@Override
	public void attachModuleNavigationListener(Tree tree) {
		new ModuleNavigationListener(tree);
	}


}
