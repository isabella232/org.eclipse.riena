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
package org.eclipse.riena.navigation.ui.swt.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.riena.core.exception.ExceptionFailure;
import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * @since 5.0
 */
public class LoginNonActivityTimer implements Runnable {
	public interface ILoginExecutor<LOGIN_RESULT> {
		void prePerformLogin() throws Exception;

		LOGIN_RESULT performLogin();

		void postPerformLogin(LOGIN_RESULT result) throws Exception;

		int getNonActivityDuration();
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

	private final Display display;
	private final int nonActivityDuration;
	private EventListener eventListener;
	private final ILoginExecutor loginExecutor;

	public LoginNonActivityTimer(final Display display, final ILoginExecutor loginExecutor, final int nonActivityDuration) {
		this.display = display;
		this.loginExecutor = loginExecutor;
		this.nonActivityDuration = nonActivityDuration;
		initializeEventListener();
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

	public void run() {
		try {
			if (!eventListener.activity) {
				loginExecutor.prePerformLogin();
				loginExecutor.postPerformLogin(loginExecutor.performLogin());
			}
			schedule();
		} catch (final Exception e) {
			throw new ExceptionFailure(e.getLocalizedMessage(), e);
		}
	}

	public void schedule() {
		initializeForSchedule();
		display.timerExec(getTimerDelay(), this);
	}

	private int getTimerDelay() {
		return nonActivityDuration - (int) (System.currentTimeMillis() - eventListener.activityTime);
	}

	private void initializeForSchedule() {
		if (eventListener.activityTime == -1) {// initialize on first schedule
			eventListener.activityTime = System.currentTimeMillis();
		}
		eventListener.activity = false;
	}

}
