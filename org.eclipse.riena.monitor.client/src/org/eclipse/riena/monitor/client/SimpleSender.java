/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.monitor.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.Literal;
import org.eclipse.riena.core.util.Millis;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.internal.monitor.client.Activator;
import org.eclipse.riena.monitor.common.Collectible;
import org.eclipse.riena.monitor.common.IReceiver;

/**
 * This simple sender implements {@code ISender} that uses riena´s (remote)
 * services to communicate with the ´server´.
 * <p>
 * The simple sender expects the following configuration that can be passed with
 * its definition in an extension:
 * <ul>
 * <li>retryTime - defines the time in minutes that will be waited for a retry
 * when a send has failed. (default value is 15 minutes if not defined)</li>
 * </ul>
 * Periods of time can be specified as a string conforming to
 * {@link Millis#valueOf(String)}.<br>
 * Example extension:
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.monitor.client.sender&quot;&gt;
 *       &lt;sender
 *             name=&quot;SimpleSender&quot;
 *             class=&quot;org.eclipse.riena.monitor.client.SimpleSender:retryTime=20 m&quot;&gt;
 *       &lt;/sender&gt;
 * &lt;/extension&gt;
 * </pre>
 */
public class SimpleSender implements ISender, IExecutableExtension {

	private IStore store;
	private IReceiver receiver;
	private boolean started;
	private long retryTime;
	private final Map<String, Sender> senders = new HashMap<String, Sender>();
	private static final String RETRY_TIME = "retryTime"; //$NON-NLS-1$
	private static final String RETRY_TIME_DEFAULT = "15 m"; //$NON-NLS-1$

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SimpleSender.class);

	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		Map<String, String> properties = null;
		try {
			properties = PropertiesUtils.asMap(data, Literal.map(RETRY_TIME, RETRY_TIME_DEFAULT));
			retryTime = Millis.valueOf(properties.get(RETRY_TIME));
			Assert.isLegal(retryTime > 0, "retryTime must be greater than 0."); //$NON-NLS-1$
		} catch (final IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
	}

	private CoreException configurationException(final String message, final Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

	@InjectService(useRanking = true)
	public void bind(final IReceiver receiver) {
		this.receiver = receiver;
	}

	public void unbind(final IReceiver receiver) {
		this.receiver = null;
	}

	public void start(final IStore store, final Collection<Category> categories) {
		if (started) {
			return;
		}
		Assert.isNotNull(store, "store must not be null"); //$NON-NLS-1$
		Assert.isNotNull(categories, "categories must not be null"); //$NON-NLS-1$
		this.store = store;
		started = true;

		for (final Category category : categories) {
			final Sender sender = new Sender(category.getName());
			senders.put(category.getName(), sender);
			// check if there are remaining {@code Collectible}s, if so trigger transfer in the background with a slight delay
			sender.tryIt(Millis.seconds(5));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ISender#stop()
	 */
	public void stop() {
		if (!started) {
			return;
		}
		started = false;
		for (final Sender sender : senders.values()) {
			sender.cancel();
		}
		senders.clear();
	}

	public synchronized void triggerTransfer(final String categoryName) {
		if (!started) {
			return;
		}
		final Sender sender = senders.get(categoryName);
		if (sender == null) {
			return;
		}
		sender.tryIt(0);
	}

	private final class Sender extends Job {

		private final String category;
		private boolean retrying;

		private Sender(final String category) {
			super("SimpleSender"); //$NON-NLS-1$
			this.category = category;
		}

		private void tryIt(final long delay) {
			if (retrying) {
				LOGGER.log(LogService.LOG_DEBUG, "Sender(" + category + ") retry already scheduled."); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			schedule(delay);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
		 * IProgressMonitor)
		 */
		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			LOGGER.log(LogService.LOG_DEBUG, "Sender(" + category + ") started with" + (retrying ? "" : "out") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					+ " retry"); //$NON-NLS-1$
			if (receiver == null) {
				LOGGER.log(LogService.LOG_DEBUG, "Sender(" + category + ") ended (no receiver)"); //$NON-NLS-1$ //$NON-NLS-2$
				return Status.OK_STATUS;
			}
			final List<Collectible<?>> transferables = store.retrieveTransferables(category);
			if (transferables.size() == 0) {
				LOGGER.log(LogService.LOG_DEBUG, "Sender(" + category + ") ended (nothing to send)"); //$NON-NLS-1$ //$NON-NLS-2$
				return Status.OK_STATUS;
			}
			transfer(transferables);
			LOGGER.log(LogService.LOG_DEBUG, "Sender(" + category + ") ended"); //$NON-NLS-1$ //$NON-NLS-2$
			return Status.OK_STATUS;
		}

		/**
		 * 
		 */
		private void transfer(final List<Collectible<?>> transferables) {
			LOGGER.log(LogService.LOG_DEBUG, "sender transfer " + transferables.size() + " transferables:"); //$NON-NLS-1$ //$NON-NLS-2$
			for (final Collectible<?> transferable : transferables) {
				LOGGER.log(LogService.LOG_DEBUG, " - " + transferable.toLogString()); //$NON-NLS-1$
			}
			try {
				if (receiver.take(System.currentTimeMillis(), transferables)) {
					store.commitTransferred(transferables);
					retrying = false;
				} else {
					throw new RuntimeException("Retry sending later because receiver rejected it."); //$NON-NLS-1$
				}
			} catch (final Throwable t) {
				LOGGER.log(LogService.LOG_DEBUG, "sending failed with: " + condense(t)); //$NON-NLS-1$
				LOGGER.log(LogService.LOG_DEBUG, "retrying in " + retryTime + " milli seconds"); //$NON-NLS-1$ //$NON-NLS-2$
				retrying = true;
				schedule(retryTime);
			}
		}

		private final static String CAUSED_BY = " Caused by: "; //$NON-NLS-1$

		private String condense(Throwable throwable) {
			final StringBuilder bob = new StringBuilder();
			do {
				bob.append(throwable.toString()).append(CAUSED_BY);
				throwable = throwable.getCause();
			} while (throwable != null);
			bob.setLength(bob.length() - CAUSED_BY.length());
			return bob.toString();
		}
	}

}
