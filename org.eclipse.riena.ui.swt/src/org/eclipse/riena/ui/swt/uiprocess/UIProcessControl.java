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
package org.eclipse.riena.ui.swt.uiprocess;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.swt.utils.IPropertyNameProvider;

/**
 * Control for showing a progress or process info window.
 */
public class UIProcessControl implements IProgressControl, IPropertyNameProvider {

	private final static Logger logger = Log4r.getLogger(UIProcessControl.class);

	private static final int UPDATE_DELAY = 200;

	private boolean processing;

	// the jface window
	private UIProcessWindow processWindow;

	private final ListenerList<ICancelListener> cancelListeners = new ListenerList<ICancelListener>(
			ICancelListener.class);

	private ProcessUpdateThread processUpdateThread;

	private String name;
	private Rectangle initBounds;

	public UIProcessControl(final Shell parentShell) {
		Assert.isNotNull(parentShell);
		createProcessWindow(parentShell);
	}

	private void createProcessWindow(final Shell parentShell) {
		// create window
		processWindow = new UIProcessWindow(parentShell, this);
		// observe window
		processWindow.addProcessWindowListener(new IProcessWindowListener() {

			public void windowAboutToClose() {
				stopProcessing();
				fireCanceled(true);
			}

		});
	}

	/**
	 * @return the {@link Window}
	 */
	public Window getWindow() {
		return processWindow;

	}

	/**
	 * open the Jface Window
	 */
	private void showWindow() {
		processWindow.openWindow();
	}

	/**
	 * close the window
	 */
	public void stop() {
		closeWindow();
	}

	private void closeWindow() {
		// stop update thread
		stopProcessing();
		// close AppWindow
		processWindow.closeWindow();

	}

	/**
	 * 
	 * @return true if the window is in processing state
	 */
	protected synchronized boolean isProcessing() {
		return processing;
	}

	public synchronized void setProcessing(final boolean processing) {
		this.processing = processing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.swt.uiprocess.IProgressControl#showProcessing()
	 */
	public void showProcessing() {
		startProcessing();
	}

	/**
	 * technical implementation of progressing in the window
	 */
	private void startProcessing() {
		// only if not already processing
		if (!isProcessing()) {// use the synched way..
			setProcessing(true);

			if (null != getProgressBar() && !getProgressBar().isDisposed()) {
				getProgressBar().setMaximum(100);
			}

			if (null != getPercentControl() && !getPercentControl().isDisposed()) {
				getPercentControl().setText(""); //$NON-NLS-1$
			}

			if (processUpdateThread == null || !processUpdateThread.isAlive()) {
				startUpdateThread();
			}
		}
	}

	/*
	 * starts the update thread needed in conjunction with the progressing state
	 * to visualize some activity
	 */
	private void startUpdateThread() {
		processUpdateThread = new ProcessUpdateThread();
		processUpdateThread.start();
	}

	/**
	 * This Thread is used to visualize "progressing"
	 */
	private final class ProcessUpdateThread extends Thread {

		@Override
		public void run() {
			setPriority(Thread.MAX_PRIORITY);
			setName("ProcessUpdateThread"); //$NON-NLS-1$
			processUpdateLoop();
		}
	}

	/*
	 * called by a worker thread used to visualize progressing activity
	 */
	private void processUpdateLoop() {
		final int[] selection = new int[] { 0 };

		while (isProcessing()) {
			if (selection[0] <= 100) {
				selection[0] += 10;
			} else {
				selection[0] = 0;
			}
			if (null != getProgressBar() && !getProgressBar().isDisposed()) {
				getProgressBar().getDisplay().asyncExec(new Runnable() {

					public void run() {
						if (!getProgressBar().isDisposed()) {
							if (isProcessing()) {
								getProgressBar().setSelection(selection[0]);
							}
						}
					}
				});
			}

			try {
				Thread.sleep(UPDATE_DELAY);
			} catch (final InterruptedException e) {
				logger.log(LogService.LOG_ERROR, e.getMessage(), e);
			}
		}
	}

	/*
	 * returns the progressbar of the window
	 */
	private ProgressBar getProgressBar() {
		return processWindow.getProgressBar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.swt.uiprocess.IProgressControl#showProgress(int,
	 * int)
	 */
	public void showProgress(final int value, final int maxValue) {
		if (maxValue <= 0) {
			return;
		}
		stopProcessing();
		final int percentValue = calcSelection(value, maxValue);
		if (getWindow().getShell() != null && !getWindow().getShell().isDisposed()) {
			getPercentControl().setText(String.valueOf(percentValue) + " %"); //$NON-NLS-1$
			getProgressBar().setSelection(percentValue);
		}
	}

	private Label getPercentControl() {
		return processWindow.getPercent();
	}

	private int calcSelection(final int value, final int maxValue) {
		final double v = (double) value / (double) maxValue * 100;
		return (int) v;
	}

	private void stopProcessing() {
		setProcessing(false);
	}

	public void start() {
		// open the ui
		showWindow();
	}

	public void setDescription(final String text) {
		processWindow.setDescription(text);
	}

	/**
	 * @since 4.0
	 */
	public String getDescription() {
		final String text = processWindow.getDescription().getText();
		if (text == null) {
			return ""; //$NON-NLS-1$
		}
		return text;
	}

	public void setTitle(final String text) {
		final Shell shell = processWindow.getShell();
		if (!StringUtils.equals(text, shell.getText())) {
			shell.setText(text);
			final Rectangle bounds = shell.getBounds();
			shell.redraw(0, 0, bounds.width, bounds.height, true);
		}
	}

	/**
	 * add an {@link ICancelListener} to be notified about cancel events.
	 */
	public void addCancelListener(final ICancelListener listener) {
		cancelListeners.add(listener);
	}

	public void removeCancelListener(final ICancelListener listener) {
		cancelListeners.remove(listener);
	}

	/**
	 * notify listener
	 * 
	 * @param windowClosing
	 */
	protected void fireCanceled(final boolean windowClosing) {
		for (final ICancelListener listener : cancelListeners.getListeners()) {
			listener.canceled(windowClosing);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.swt.utils.IPropertyNameProvider#getPropertyName()
	 */
	public String getPropertyName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.swt.utils.IPropertyNameProvider#setPropertyName(
	 * java.lang.String)
	 */
	public void setPropertyName(final String propertyName) {
		this.name = propertyName;
	}

	/**
	 * @param sets
	 *            the cancelButton of the {@link UIProcessWindow} visible/hidden
	 * @since 3.0
	 */
	public void setCancelVisible(final boolean cancelVisible) {
		processWindow.setCancelVisible(cancelVisible);
	}

	/**
	 * @param sets
	 *            the cancelButton of the {@link UIProcessWindow}
	 *            enabled/disabled
	 * @since 3.0
	 */
	public void setCancelEnabled(final boolean cancelEnabled) {
		processWindow.setCancelEnabled(cancelEnabled);
	}

	/**
	 * @since 4.0
	 */
	public void pack() {
		if (initBounds == null) {
			final Shell shell = getWindow().getShell();
			shell.pack();
			final Rectangle bounds = shell.getBounds();
			final Rectangle constrainedShellBounds = getConstrainedShellBounds(bounds);
			if (!bounds.equals(constrainedShellBounds)) {
				shell.setBounds(constrainedShellBounds);
				shell.layout();
			}
			initBounds = shell.getBounds();
		}
	}

	/**
	 * @since 4.0
	 */
	protected Rectangle getConstrainedShellBounds(final Rectangle preferredSize) {
		final Rectangle result = new Rectangle(preferredSize.x, preferredSize.y, preferredSize.width,
				preferredSize.height);

		final Shell shell = getWindow().getShell();
		final Rectangle bounds = shell.getMonitor().getClientArea();
		bounds.x += 10;
		bounds.y += 10;
		bounds.height -= 20;
		bounds.width -= 20;

		if (result.height > bounds.height) {
			result.height = bounds.height;
		}

		if (result.width > bounds.width) {
			result.width = bounds.width;
		}

		result.x = Math.max(bounds.x, Math.min(result.x, bounds.x + bounds.width - result.width));
		result.y = Math.max(bounds.y, Math.min(result.y, bounds.y + bounds.height - result.height));

		return result;
	}

}
