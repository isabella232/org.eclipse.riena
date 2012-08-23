package org.eclipse.riena.navigation.ui.e4;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;

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
import org.eclipse.riena.navigation.ui.e4.security.LoginHelper;
import org.eclipse.riena.navigation.ui.swt.application.LoginNonActivityTimer.ILoginExecutor;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;

@SuppressWarnings("restriction")
public class ApplicationLifeCycle {
	private static final String MODEL_CREATORS_EXT_POINT_SUFFIX = ".applicationModelCreators"; //$NON-NLS-1$

	@Inject
	private IExtensionRegistry extensionRegistry;

	@Inject
	private Logger logger;

	@Inject
	private IEclipseContext eclipseContext;

	/**
	 * called very early (application model is not created)
	 */
	@PostContextCreate
	public void initApplicationNode() {

		// if there is some login configured, ask for login and remember the extension in the app context
		// later we will need this extension for the (in)activity login timer
		eclipseContext.set(ILoginExecutor.class, ContextInjectionFactory.make(LoginHelper.class, eclipseContext).checkLogin());

		/** AbstractApplication */
		final SwtApplication legacyHelper = new SwtApplication();
		final IApplicationModelCreator applicationModelCreator = getModelCreatorFromExtension();
		final IApplicationNode applicationNode = applicationModelCreator != null ? createModel(applicationModelCreator) : createModel(legacyHelper);

		eclipseContext.set(IApplicationNode.class, applicationNode);
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
		initializeListener(controller);
	}

	/**
	 * Creates the riena application model represented by an instance of {@link IApplicationNode}
	 */
	private IApplicationNode createModel(final IApplicationModelCreator creator) {
		// call configuration hook
		creator.configure();
		return creator.createModel();
	}

	private IApplicationModelCreator getModelCreatorFromExtension() {
		final String pluginId = Activator.getDefault().getBundleContext().getBundle().getSymbolicName();
		for (final IConfigurationElement e : extensionRegistry.getConfigurationElementsFor(pluginId + MODEL_CREATORS_EXT_POINT_SUFFIX)) {
			try {
				return (IApplicationModelCreator) e.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (final CoreException e1) {
				logger.error(e1);
			}
		}
		return null;
	}

	/**
	 * called after the application model has been fully created
	 */
	@ProcessAdditions
	public void initRienaNavigation() {
		ApplicationNodeManager.getApplicationNode().activate();
	}

	private void initializeListener(final ApplicationController controller) {
		final NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(createInstance(MyApplicationNodeListener.class, eclipseContext));
		navigationTreeObserver.addListener(createInstance(MySubApplicationNodeListener.class, eclipseContext));
		navigationTreeObserver.addListener(createInstance(MyModuleGroupNodeListener.class, eclipseContext));
		navigationTreeObserver.addListener(createInstance(MyModuleNodeListener.class, eclipseContext));
		navigationTreeObserver.addListener(createInstance(MySubModuleNodeListener.class, eclipseContext));

		navigationTreeObserver.addListenerTo(controller.getNavigationNode());
	}

	private <T> T createInstance(final Class<T> clazz, final IEclipseContext context) {
		return ContextInjectionFactory.make(clazz, context);
	}

}
