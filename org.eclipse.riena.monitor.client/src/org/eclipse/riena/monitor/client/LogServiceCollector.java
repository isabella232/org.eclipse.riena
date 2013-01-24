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
package org.eclipse.riena.monitor.client;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.log.ExtendedLogEntry;
import org.eclipse.equinox.log.ExtendedLogReaderService;

import org.eclipse.riena.core.logging.SynchronousLogListenerAdapter;
import org.eclipse.riena.core.util.Literal;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.internal.monitor.client.Activator;
import org.eclipse.riena.monitor.common.LogEntryTransferObject;

/**
 * Collects logs that are delivered by the
 * {@code org.osgi.service.log.LogListener}.
 * <p>
 * Example extensions:
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.monitor.client.collectors&quot;&gt;
 *    &lt;collector
 *          category=&quot;LogCollector&quot;
 *          class=&quot;org.eclipse.riena.monitor.client.LogServiceCollector: collectRange=1,2; triggerRange=1,2; async=false&quot;
 *          maxItems=&quot;50&quot;&gt;
 *    &lt;/collector&gt;
 *    &lt;collector
 *          category=&quot;CustomCollector&quot;
 *          class=&quot;org.eclipse.riena.monitor.client.LogServiceCollector: collectRange=-2..0; triggerRange=-2&quot;
 *          maxItems=&quot;250&quot;&gt;
 *    &lt;/collector&gt;
 * &lt;/extension&gt;
 * </pre>
 * 
 * In the example above there are two parameters collectRange and triggerRange.
 * Both parameters are of type range. A range defines a set of integers (see
 * org.eclipse.riena.monitor.client.Range), e.g.
 * <ul>
 * <li>1..3 - is a interval containing 1, 2 and 3</li>
 * <li>1,2 - are simply the values 1 and 2</li>
 * <li>1..3, 5,7 - is a interval containing 1, 2, 3, 5 and 7</li>
 * </ul>
 * These integers are the log levels defined by org.osgi.service.log.LogService.
 * So, for the above defined LogServiceCollector this means collect
 * (collectRange) all logs that are INFO (3), WARNING (2) or ERROR (1) and
 * trigger transmission (triggerRange) on WARNING (2) or ERROR (1). <br>
 * The optional parameter async allows to attach the LogServiceCollector either
 * synchronously or asynchronously. The default is asynchronously. The advantage
 * of attaching synchronously is that the LogServiceCollector can access thread
 * information.
 * <p>
 * The collectRange is quiet limited. For a more fine grained control of what
 * should be collected, a filter class may by specified:
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.monitor.client.collectors&quot;&gt;
 *    &lt;collector
 *          category=&quot;LogCollector&quot;
 *          class=&quot;org.eclipse.riena.monitor.client.LogServiceCollector: triggerRange=1,2; async=false; filterClass=org.eclipse.riena.example.client.monitor.LogServiceCollectorFilter&quot;
 *          maxItems=&quot;50&quot;&gt;
 *    &lt;/collector&gt;
 * &lt;/extension&gt;
 * </pre>
 * 
 * The filter class has to implement the interface
 * <code>ILogServiceCollectorFilter</code>. With this the collectRange is no
 * longer necessary but can still be used. If used its filtering is applied
 * before the filter class.
 * <p>
 * See also <a href=
 * "http://wiki.eclipse.org/Riena/Getting_Started_with_Client_Monitoring#LogServiceCollector"
 * >Riena Wiki LogServiceCollector</a>
 */
public class LogServiceCollector extends AbstractCollector implements IExecutableExtension {

	private Range collectRange;
	private Range triggerRange;
	private ILogServiceCollectorFilter filter;
	private boolean async;
	private ExtendedLogReaderService extendedLogReaderService;
	private LogListener logListener;

	private static final String TRIGGER_RANGE = "triggerRange"; //$NON-NLS-1$
	private static final String COLLECT_RANGE = "collectRange"; //$NON-NLS-1$
	private static final String FILTER_CLASS = "filterClass"; //$NON-NLS-1$
	private static final String ASYNC_EXEC = "async"; //$NON-NLS-1$
	private static final String ASYNC_EXEC_DEFAULT = Boolean.TRUE.toString();

	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		Map<String, String> properties = null;
		try {
			properties = PropertiesUtils.asMap(data, Literal.map(ASYNC_EXEC, ASYNC_EXEC_DEFAULT), TRIGGER_RANGE);
			final String collectRangeString = properties.get(COLLECT_RANGE);
			collectRange = new Range(collectRangeString != null ? collectRangeString : Range.ALL);
			triggerRange = new Range(properties.get(TRIGGER_RANGE));
			filter = newFilter(config, properties.get(FILTER_CLASS));
			async = Boolean.parseBoolean(properties.get(ASYNC_EXEC));
			logListener = newLogListener();
		} catch (final IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
	}

	private ILogServiceCollectorFilter newFilter(final IConfigurationElement configurationElement,
			final String filterClassName) {
		if (filterClassName == null) {
			return null;
		}
		final Bundle bundle = ContributorFactoryOSGi.resolve(configurationElement.getContributor());
		Assert.isLegal(bundle != null, "Could not get bundle for filter class " + filterClassName); //$NON-NLS-1$
		try {
			return (ILogServiceCollectorFilter) bundle.loadClass(filterClassName).newInstance();
		} catch (final Throwable t) {
			throw new IllegalArgumentException("Could not create filter instance for class " + filterClassName, t); //$NON-NLS-1$
		}
	}

	@Override
	protected void doStart() {
		if (extendedLogReaderService == null) {
			return;
		}
		extendedLogReaderService.addLogListener(logListener);
	}

	@Override
	protected void doStop() {
		if (extendedLogReaderService == null) {
			return;
		}
		extendedLogReaderService.removeLogListener(logListener);
	}

	@InjectService(useRanking = true)
	public void bind(final ExtendedLogReaderService extendedLogReaderService) {
		this.extendedLogReaderService = extendedLogReaderService;
	}

	public void unbind(final ExtendedLogReaderService extendedLogReaderService) {
		extendedLogReaderService.removeLogListener(logListener);
		this.extendedLogReaderService = null;
	}

	private LogListener newLogListener() {
		final LogListener listener = new LogListener() {
			public void logged(final LogEntry entry) {
				if (!collectRange.matches(entry.getLevel())) {
					return;
				}
				if (filter != null && !filter.isCollectible((ExtendedLogEntry) entry)) {
					return;
				}
				collect(new LogEntryTransferObject((ExtendedLogEntry) entry));
				if (triggerRange.matches(entry.getLevel())) {
					triggerTransfer();
				}
			}
		};
		return async ? listener : new SynchronousLogListenerAdapter(listener);
	}

	private CoreException configurationException(final String message, final Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

}
