package org.eclipse.riena.navigation.ui.e4;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

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
import org.eclipse.riena.navigation.ui.login.ILoginDialogView;
import org.eclipse.riena.navigation.ui.swt.application.LoginNonActivityTimer.ILoginExecutor;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.login.ILoginSplashView;
import org.eclipse.riena.navigation.ui.swt.splashHandlers.AbstractLoginSplashHandler;

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
		eclipseContext.set(ILoginExecutor.class, checkLogin());

		/** AbstractApplication */
		final SwtApplication legacyHelper = new SwtApplication();
		final IApplicationModelCreator c = getModelCreatorFromExtension();
		final IApplicationNode applicationNode = c != null ? c.createModel() : legacyHelper.createModel();
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
	 * @return <code>null</code> if there is no configured login extension
	 */
	private ILoginExecutor<Integer> checkLogin() {
		ILoginExecutor<Integer> loginExecutor = null;
		try {
			loginExecutor = createLoginExecutor();
		} catch (final CoreException e) {
			// we will not start the application if something with the login executor went wrong
			e.printStackTrace();
			System.exit(0);
		}
		if (loginExecutor != null && !IApplication.EXIT_OK.equals(loginExecutor.performLogin())) {
			// login was not successful
			System.exit(0);
		}
		return loginExecutor;
	}

	private ILoginExecutor<Integer> createLoginExecutor() throws CoreException {
		final IConfigurationElement[] loginSplashExtensions = extensionRegistry
				.getConfigurationElementsFor("org.eclipse.riena.navigation.ui.swt.loginSplashViewDefinition");
		if (loginSplashExtensions.length > 0) {
			return loginExecutorForSplash(loginSplashExtensions[0]);
		}

		final IConfigurationElement[] loginExtensions = extensionRegistry
				.getConfigurationElementsFor("org.eclipse.riena.navigation.ui.loginDialogViewDefinition");
		if (loginExtensions.length > 0) {
			return loginExecutorForDialog(loginExtensions[0]);
		}

		return null;
	}

	/**
	 * @param iConfigurationElement
	 * @return
	 * @throws CoreException
	 */
	private ILoginExecutor<Integer> loginExecutorForDialog(final IConfigurationElement iConfigurationElement) throws CoreException {
		final String attribute = iConfigurationElement.getAttribute("nonActivityDuration");
		final ILoginDialogView loginDialogView = (ILoginDialogView) iConfigurationElement.createExecutableExtension("viewClass");

		return new LoginExecutor(eclipseContext, Integer.parseInt(attribute)) {
			public Integer performLogin() {
				final Realm realm = SWTObservables.getRealm(Display.getCurrent());
				do {
					Realm.runWithDefault(realm, new Runnable() {
						public void run() {
							loginDialogView.build();
						}
					});
				} while (IApplication.EXIT_RESTART.equals(loginDialogView.getResult()));
				return loginDialogView.getResult();
			}
		};
	}

	/**
	 * @param e
	 * @return
	 * @throws CoreException
	 */
	private ILoginExecutor<Integer> loginExecutorForSplash(final IConfigurationElement iConfigurationElement) throws CoreException {
		final String attribute = iConfigurationElement.getAttribute("nonActivityDuration");
		final ILoginSplashView loginSplashView = (ILoginSplashView) iConfigurationElement.createExecutableExtension("viewClass");

		return new LoginExecutor(eclipseContext, Integer.parseInt(attribute)) {
			public Integer performLogin() {
				final AbstractLoginSplashHandler loginSplashHandler = getLoginSplashHandler();
				final Realm realm = SWTObservables.getRealm(Display.getCurrent());
				realm.runWithDefault(realm, new Runnable() {
					public void run() {
						final Shell shell = new Shell(getDisplay(), SWT.NO_TRIM | SWT.APPLICATION_MODAL);
						initializeShellBackgroundImage(shell, getBackgroundImagePath());
						loginSplashHandler.init(shell);
						shell.setLocation((shell.getDisplay().getBounds().width - shell.getSize().x) / 2,
								(shell.getDisplay().getBounds().height - shell.getSize().y) / 2);
						shell.open();
						while (!shell.isDisposed()) {
							if (!getDisplay().readAndDispatch()) {
								getDisplay().sleep();
							}
						}
					}
				});

				return loginSplashHandler.getResult();
			}

			private void initializeShellBackgroundImage(final Shell shell, final String imageName) {
				//				final Image bi = ImageStore.getInstance().getImage(imageName);
				final ImageDescriptor bid = AbstractUIPlugin.imageDescriptorFromPlugin(
						((RegistryContributor) iConfigurationElement.getContributor()).getActualName(), getBackgroundImagePath());
				final Image bi = bid.createImage();
				shell.setSize(bi.getBounds().width, bi.getBounds().height);
				shell.setBackgroundImage(bi);
			}

			private AbstractLoginSplashHandler getLoginSplashHandler() {
				return new AbstractLoginSplashHandler() {
				};
			}

			private String getBackgroundImagePath() {
				return "splash.bmp";
			}

			private Display getDisplay() {
				return Display.getCurrent();
			}
		};
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
