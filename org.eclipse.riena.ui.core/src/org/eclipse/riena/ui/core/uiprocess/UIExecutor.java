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
package org.eclipse.riena.ui.core.uiprocess;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.riena.core.util.Nop;

/**
 * Executes a {@code Callable}.
 * 
 * @since 3.0
 */
public final class UIExecutor {

	private UIExecutor() {
		Nop.reason("Utility"); //$NON-NLS-1$
	}

	/**
	 * Same as {@link executeLively(final Callable<T> callable, final boolean
	 * user)} but {@code user} is false.
	 * 
	 * @param <T>
	 *            the result type of the {@code Callable}
	 * @param callable
	 *            the {@code Callable} to run in background
	 * @return the result of the {@code Callable}
	 * @throws RuntimeException
	 *             the possible exception of the {@code Callable}
	 * 
	 * @see executeLively(final Callable<T> callable, final boolean user)
	 * @since 3.0
	 */
	public static <T> T executeLively(final Callable<T> callable) {
		return executeLively(callable, false);
	}

	/**
	 * Execute the given {@code Callable} in the background and return when
	 * finished but keep the UI lively.<br>
	 * If executing the {@code Callable} caused an exception this will be
	 * re-thrown.
	 * 
	 * @param <T>
	 *            the result type of the {@code Callable}
	 * @param callable
	 *            the {@code Callable} to run in background
	 * @param user
	 *            Sets whether or not this {@code Callable} has been directly
	 *            initiated by a UI end user. These jobs may be presented
	 *            differently in the UI.
	 * @return the result of the {@code Callable}
	 * @throws RuntimeException
	 *             the possible exception of the {@code Callable}
	 * 
	 * @since 3.0
	 */
	public static <T> T executeLively(final Callable<T> callable, final boolean user) {
		final BackgroundProcess<T> process = new BackgroundProcess<T>(callable, user);
		process.start();
		UISynchronizer.createSynchronizer().readAndDispatch(new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return process.isFinished();
			}
		});
		if (process.getError() != null) {
			throw new RuntimeException("Exception while executing callable.", process.getError()); //$NON-NLS-1$
		}
		return process.getResult();
	}

	private static class BackgroundProcess<T> extends UIProcess {

		private final AtomicBoolean finished = new AtomicBoolean(false);
		private final Callable<T> callable;
		private T result;
		private Throwable error;

		public BackgroundProcess(final Callable<T> callable, final boolean user) {
			super("UIExecutor", user); //$NON-NLS-1$
			this.callable = callable;
		}

		@Override
		public boolean runJob(final IProgressMonitor monitor) {
			try {
				result = callable.call();
			} catch (final Throwable e) {
				this.error = e;
			} finally {
				finished.set(true);
			}
			return true;
		}

		private boolean isFinished() {
			return finished.get();
		}

		private T getResult() {
			return result;
		}

		private Throwable getError() {
			return error;
		}

	}

}
