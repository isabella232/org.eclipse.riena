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
package org.eclipse.riena.ui.swt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.Millis;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Represents a label of the status line that displays the current time.
 */
public class StatuslineTime extends AbstractStatuslineComposite {

	protected SimpleDateFormat format;
	private CLabel timeLabel;

	private final StatuslineUpdateJob updateJob = new StatuslineUpdateJob();

	private final static long A_SECOND = Millis.seconds(1);
	private final static Logger LOGGER = Log4r.getLogger(StatuslineTime.class);

	/**
	 * Creates a new instance of <code>StatuslineTime</code>.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public StatuslineTime(final Composite parent, final int style) {
		super(parent, style | SWT.NO_FOCUS);
		updateTime();
	}

	@Override
	protected void createContents() {
		timeLabel = new CLabel(this, SWT.LEFT);
		timeLabel.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.STATUSLINE_BACKGROUND));
	}

	@Override
	public void dispose() {

		super.dispose();
		updateJob.cancel();
		SwtUtilities.dispose(timeLabel);

	}

	/**
	 * Returns the format of the date and/or time.
	 * 
	 * @return format
	 */
	protected SimpleDateFormat getFormat() {

		if (format == null) {
			format = new SimpleDateFormat("HH:mm"); //$NON-NLS-1$
		}
		return format;

	}

	/**
	 * Sets the current time.
	 */
	private void updateTime() {

		final String timeStrg = getFormat().format(new Date());
		if ((timeLabel != null) && (!timeLabel.isDisposed())) {
			timeLabel.setText(timeStrg);
		}
		updateJob.schedule(A_SECOND);

	}

	/**
	 * This job updates the time.
	 */
	private class StatuslineUpdateJob extends Job {

		public StatuslineUpdateJob() {
			super("StatuslineUpdater"); //$NON-NLS-1$
			setSystem(true);
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			if (!isDisposed() && !getDisplay().isDisposed()) {
				try {
					getDisplay().asyncExec(new Runnable() {
						public void run() {
							if (!monitor.isCanceled()) {
								updateTime();
							}
						}
					});
				} catch (final SWTException e) {
					LOGGER.log(LogService.LOG_DEBUG, "StatuslineUpdateJob failed because of a disposed display.", e); //$NON-NLS-1$
				}
			}
			return Status.OK_STATUS;
		}
	}

}
