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
package org.eclipse.riena.core.logging;

import java.io.PrintStream;
import java.util.Date;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.ExtendedLogEntry;

import org.eclipse.riena.internal.core.logging.LogLevelMapper;

public class SysoLogListener implements LogListener {

	public void logged(final LogEntry entry) {
		final ExtendedLogEntry eEntry = (ExtendedLogEntry) entry;
		final StringBuilder buffer = new StringBuilder();
		buffer.append(new Date(eEntry.getTime()).toString()).append(' ');
		final String level = LogLevelMapper.getValue(eEntry.getLevel());
		buffer.append(level).append(' ');
		buffer.append("[" + eEntry.getThreadName() + "] "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(eEntry.getLoggerName()).append(' ');
		if (eEntry.getContext() != null) {
			buffer.append(eEntry.getContext()).append(' ');
		}
		buffer.append(entry.getMessage());
		final PrintStream stream = LogService.LOG_ERROR == eEntry.getLevel()
				|| LogService.LOG_WARNING == eEntry.getLevel() ? System.err : System.out;
		stream.println(buffer.toString());
		if (eEntry.getException() != null) {
			eEntry.getException().printStackTrace(stream);
		}
	}
}
