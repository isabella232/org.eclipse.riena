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
package org.eclipse.riena.navigation.ui.swt.application;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.Workbench;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.exception.ExceptionFailure;
import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.internal.navigation.ui.swt.IAdvisorHelper;
import org.eclipse.riena.internal.ui.swt.utils.RcpUtilities;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.listener.ApplicationNodeListener;
import org.eclipse.riena.navigation.ui.application.AbstractApplication;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.login.ILoginDialogView;
import org.eclipse.riena.navigation.ui.swt.login.ILoginSplashViewExtension;
import org.eclipse.riena.navigation.ui.swt.splashHandlers.AbstractLoginSplashHandler;
import org.eclipse.riena.navigation.ui.swt.views.ApplicationAdvisor;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 * Creates and starts an empty swt application Subclass to create own model or
 * controller.
 */
public class SwtApplication extends AbstractApplication {

	protected ILoginSplashViewExtension loginSplashViewExtension;
	private LoginNonActivityTimer loginNonActivityTimer;

	@Override
	protected void initializeUI() {
		PlatformUI.createDisplay();
	}

	@Override
	public Object createView(final IApplicationContext context, final IApplicationNode pNode) {
		final Display display = Display.getCurrent();
		try {
			final ApplicationAdvisor advisor = new ApplicationAdvisor(createApplicationController(pNode),
					new AdvisorHelper());
			initializeLoginNonActivityTimer(display, pNode, context);
			final int returnCode = PlatformUI.createAndRunWorkbench(display, advisor);
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	/**
	 * Creates an action bar advisor for a window.
	 * <p>
	 * Subclasses may override.
	 * 
	 * @return an AdvisorBarAdvisor; never null
	 * @since 1.2
	 */
	public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer) {
		return new ActionBarAdvisor(configurer);
	}

	/**
	 * Return the default keyboard scheme for this applications.
	 * <p>
	 * Subclasses defining their own scheme using the '...' extension point,
	 * should override this method and return the appropriate id.
	 * <p>
	 * When defining your own scheme, you can specify '...' as the parent
	 * scheme, to inherit the default Riena keybindings. If you wish to start
	 * with a blank scheme, leave the parent attribute empty.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * &lt;extension
	 *          point=&quot;org.eclipse.ui.bindings&quot;&gt;
	 *       &lt;scheme
	 *             id=&quot;org.eclipse.riena.example.client.scheme&quot;
	 *             name=&quot;Riena Client Key Bindings&quot;
	 *             parentId=&quot;org.eclipse.riena.ui.defaultBindings&quot;&gt;
	 *       &lt;/scheme&gt;
	 *       &lt;key
	 *             commandId=&quot;org.eclipse.riena.example.client.exitCommand&quot;
	 *             contextId=&quot;org.eclipse.ui.contexts.window&quot;
	 *             schemeId=&quot;org.eclipse.riena.example.client.scheme&quot;
	 *             sequence=&quot;F10&quot;&gt;
	 *       &lt;/key&gt;
	 * &lt;/extension&gt;
	 * </pre>
	 * 
	 * @return the identifer of a valid scheme as String; never null.
	 */
	protected String getKeyScheme() {
		return "org.eclipse.riena.ui.defaultBindings"; //$NON-NLS-1$
	}

	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return;
		}
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}

	/*
	 * keep public, called via reflection
	 */
	@InjectExtension
	public void update(final ILoginSplashViewExtension[] data) {
		if (data.length > 0) {
			loginSplashViewExtension = data[0];
		}
	}

	protected ApplicationController createApplicationController(final IApplicationNode pModel) {
		return new ApplicationController(pModel);
	}

	protected void prePerformLogin(final IApplicationContext context) {
		if (RcpUtilities.getWorkbenchShell() != null) {
			// To minimize the workbench and show the login dialog later, the workbench has be made first invisible and then minimized.
			RcpUtilities.getWorkbenchShell().setVisible(false);
			RcpUtilities.getWorkbenchShell().setMinimized(true);
			// Make workbench visible to be shown as minimized in the (windows) task bar.
			RcpUtilities.getWorkbenchShell().setVisible(true);
		}
	}

	protected void postPerformLogin(final IApplicationContext context, final Object result) {
		if (!EXIT_OK.equals(result)) {
			PlatformUI.getWorkbench().close();
		} else {
			RcpUtilities.getWorkbenchShell().setMinimized(false);
			loginNonActivityTimer.schedule();
		}
	}

	@Override
	protected Object doPerformLogin(final IApplicationContext context) {
		final Realm realm = SWTObservables.getRealm(getDisplay());
		// TODO Is really necessary that the loginDialogViewExtension is used here. Shouldn�t it be done it the super class??
		final ILoginDialogView loginDialogView = loginDialogViewExtension.createViewClass();
		do {
			Realm.runWithDefault(realm, new Runnable() {
				public void run() {
					loginDialogView.build();
				}
			});
		} while (EXIT_RESTART.equals(loginDialogView.getResult()));

		return loginDialogView.getResult();
	}

	@Override
	protected Object doPerformSplashLogin(final IApplicationContext context) {

		final Shell shell = new Shell(getDisplay(), SWT.NO_TRIM | SWT.APPLICATION_MODAL);
		initilizeShellBackgroundImage(shell, getBackgroundImagePath(context));
		final AbstractLoginSplashHandler loginSplashHandler = getLoginSplashHandler();
		loginSplashHandler.init(shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}

		return loginSplashHandler.getResult();
	}

	@Override
	protected boolean isSplashLogin(final IApplicationContext context) {
		return loginSplashViewExtension != null && getLoginSplashHandler() != null;
	}

	@Override
	protected boolean isDialogLogin(final IApplicationContext context) {
		return super.isDialogLogin(context) && loginSplashViewExtension == null;
	}

	@Override
	protected void initializeLoginViewDefinition() {
		super.initializeLoginViewDefinition();

		initializeLoginSplashViewDefinition();
	}

	// helping methods
	//////////////////

	private String getBackgroundImagePath(final IApplicationContext context) {
		return "splash.bmp"; //$NON-NLS-1$
	}

	private Display getDisplay() {
		if (PlatformUI.isWorkbenchRunning()) {
			return PlatformUI.getWorkbench().getDisplay();
		} else {
			return PlatformUI.createDisplay();
		}
	}

	private AbstractLoginSplashHandler getLoginSplashHandler() {
		AbstractLoginSplashHandler result = null;
		if (PlatformUI.isWorkbenchRunning() && !SWTFacade.isRAP()) { // RAP does not have a splash
			// Use the splash handler of the workbench both for the login and for
			// a later re-login after inactivity timeout. Unfortunately it is not
			// accessible and an enhancement request to change that was rejected:
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=260736.
			try {
				final Object loginSplashHandler = ReflectionUtils.invokeHidden(Workbench.class, "getSplash"); //$NON-NLS-1$
				if (loginSplashHandler instanceof AbstractLoginSplashHandler) {
					result = (AbstractLoginSplashHandler) loginSplashHandler;
				}
			} catch (final ReflectionFailure refFail) {
				Log4r.getLogger(SwtApplication.class).log(LogService.LOG_ERROR, "Workbench.getSplash()", refFail); //$NON-NLS-1$
			}
		}
		return result;
	}

	private void initializeLoginNonActivityTimer(final Display display, final IApplicationNode pNode,
			final IApplicationContext context) {
		pNode.addListener(new ApplicationNodeListener() {
			@Override
			public void afterActivated(final IApplicationNode source) {
				if (isSplashLogin(context) && loginSplashViewExtension.getNonActivityDuration() > 0) {
					loginNonActivityTimer = new LoginNonActivityTimer(display, context, loginSplashViewExtension
							.getNonActivityDuration());
					loginNonActivityTimer.schedule();
				} else if (isDialogLogin(context) && loginDialogViewExtension.getNonActivityDuration() > 0) {
					// TODO See todo in method doPerformLogin(IApplicationContext context)
					loginNonActivityTimer = new LoginNonActivityTimer(display, context, loginDialogViewExtension
							.getNonActivityDuration());
					loginNonActivityTimer.schedule();
				}
			}
		});
	}

	private void initializeLoginSplashViewDefinition() {
		Wire.instance(this).andStart(Activator.getDefault().getContext());
	}

	private void initilizeShellBackgroundImage(final Shell shell, final String imageName) {
		final Image bi = ImageStore.getInstance().getImage(imageName);
		shell.setSize(bi.getBounds().width, bi.getBounds().height);
		shell.setBackgroundImage(bi);
	}

	// helping classes
	//////////////////

	private final class AdvisorHelper implements IAdvisorHelper {
		public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer) {
			return SwtApplication.this.createActionBarAdvisor(configurer);
		}

		public String getKeyScheme() {
			return SwtApplication.this.getKeyScheme();
		}
	}

	private static final class EventListener implements Listener {
		private boolean activity;
		private long activityTime;

		private EventListener() {
			activity = false;
			activityTime = -1;
		}

		public void handleEvent(final Event event) {
			activity = true;
			activityTime = System.currentTimeMillis();
		}
	}

	private final class LoginNonActivityTimer implements Runnable {
		private final Display display;
		private final IApplicationContext context;
		private EventListener eventListener;
		private final int nonActivityDuration;

		private LoginNonActivityTimer(final Display display, final IApplicationContext context,
				final int nonActivityDuration) {
			super();

			this.display = display;
			this.context = context;
			this.nonActivityDuration = nonActivityDuration;
			initializeEventListener();
		}

		public void run() {
			try {
				if (eventListener.activity) {
					schedule();
					return;
				}

				prePerformLogin(context);
				postPerformLogin(context, performLogin(context));
			} catch (final Exception e) {
				throw new ExceptionFailure(e.getLocalizedMessage(), e);
			}
		}

		private void initializeEventListener() {
			eventListener = new EventListener();
			display.addFilter(SWT.KeyDown, eventListener);
			display.addFilter(SWT.KeyUp, eventListener);
			display.addFilter(SWT.MouseDoubleClick, eventListener);
			display.addFilter(SWT.MouseDown, eventListener);
			display.addFilter(SWT.MouseUp, eventListener);
			display.addFilter(SWT.Traverse, eventListener);
			final SWTFacade facade = SWTFacade.getDefault();
			facade.addFilterMouseExit(display, eventListener);
			facade.addFilterMouseMove(display, eventListener);
			facade.addFilterMouseWheel(display, eventListener);
		}

		private void schedule() {
			initializeForSchedule();
			display.timerExec(getTimerDelay(), this);
		}

		private void initializeForSchedule() {
			if (eventListener.activityTime == -1) {// initialize on first schedule
				eventListener.activityTime = System.currentTimeMillis();
			}
			eventListener.activity = false;
		}

		private int getTimerDelay() {
			return nonActivityDuration - (int) (System.currentTimeMillis() - eventListener.activityTime);
		}
	}

}
