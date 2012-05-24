package org.eclipse.riena.navigation.ui.e4;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.ui.application.IApplicationModelCreator;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.e4.listener.MyApplicationNodeListener;
import org.eclipse.riena.navigation.ui.e4.listener.MyModuleGroupNodeListener;
import org.eclipse.riena.navigation.ui.e4.listener.MyModuleNodeListener;
import org.eclipse.riena.navigation.ui.e4.listener.MySubApplicationNodeListener;
import org.eclipse.riena.navigation.ui.e4.listener.MySubModuleNodeListener;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;

public class ApplicationLifeCycle {
	private static final String MODEL_CREATORS_EXT_POINT_SUFFIX = ".applicationModelCreators";

	@Inject
	private IExtensionRegistry extensionRegistry;

	@Inject
	private EModelService modelService;

	/**
	 * called very early (application model is not created)
	 */
	@PostContextCreate
	public void initApplicationNode(final IEclipseContext context) {
		// Shell activeShell = getShell();
		// if (!MessageDialog.openQuestion(activeShell, "Login",
		// "Do you like to log in?")) {
		// System.exit(0);
		// }

		/** AbstractApplication */
		final SwtApplication legacyHelper = new SwtApplication();
		final IApplicationModelCreator c = getModelCreatorFromExtension();
		final IApplicationNode applicationNode = c != null ? c.createModel() : legacyHelper.createModel();
		legacyHelper.initApplicationNode(applicationNode);

		/** SwtApplication */
		final ApplicationController controller = new ApplicationController(applicationNode);
		// initializeLoginNonActivityTimer()

		/** ApplicationAdvisor */
		// error handling getWorkbenchErrorHandler()
		// installDefaultBinding()
		// preShutdown()

		/** ApplicationViewAdvisor */
		new InjectSwtViewBindingDelegate().bind(controller);
		initializeListener(controller, context);
	}

	/**
	 * @return
	 */
	private IApplicationModelCreator getModelCreatorFromExtension() {
		final String pluginId = Activator.getDefault().getBundleContext().getBundle().getSymbolicName();
		for (final IConfigurationElement e : extensionRegistry.getConfigurationElementsFor(pluginId + MODEL_CREATORS_EXT_POINT_SUFFIX)) {
			try {
				return (IApplicationModelCreator) e.createExecutableExtension("class");
			} catch (final CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * called after the application model has been fully created
	 */
	@ProcessAdditions
	public void initRienaNavigation(final IEclipseContext context) {
		ApplicationNodeManager.getApplicationNode().activate();
	}

	private void initializeListener(final ApplicationController controller, final IEclipseContext context) {
		final NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new MyApplicationNodeListener(controller.getNavigationNode(), context));
		navigationTreeObserver.addListener(new MySubApplicationNodeListener(context));
		navigationTreeObserver.addListener(new MyModuleGroupNodeListener());
		navigationTreeObserver.addListener(new MyModuleNodeListener());
		navigationTreeObserver.addListener(new MySubModuleNodeListener(context));
		navigationTreeObserver.addListenerTo(controller.getNavigationNode());
	}

	private Shell getShell() {
		final Display display = Display.getCurrent();
		Shell activeShell = display.getActiveShell();
		if (activeShell == null) {
			activeShell = new Shell(display);
		}
		return activeShell;
	}
}
