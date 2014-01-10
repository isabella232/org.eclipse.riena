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
package org.eclipse.riena.internal.communication.factory.hessian;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.caucho.hessian.client.HessianRuntimeException;
import com.caucho.hessian.io.SerializerFactory;

/**
 * The {@code RienaHessianWatchDog} is a java.util.logging {@code Handler}. Its
 * purpose is to watch the logging events and bark (throw an exception) if a
 * certain <i>warning</i> message has been logged. Riena treats this
 * <i>warning</i> message as an error!
 */
final class RienaHessianWatchDog extends Handler {

	private static final Handler WATCH_DOG = new RienaHessianWatchDog();

	/**
	 * Install the watch dog.
	 */
	public static void install() {
		getSerializerFactoryLogger().addHandler(WATCH_DOG);
	}

	/**
	 * Uninstall the watch dog.
	 */
	public static void uninstall() {
		getSerializerFactoryLogger().removeHandler(WATCH_DOG);
	}

	private static Logger getSerializerFactoryLogger() {
		return Logger.getLogger(SerializerFactory.class.getName());
	}

	@Override
	public void publish(final LogRecord record) {
		if (record.getLevel() != Level.WARNING) {
			return;
		}
		if (record.getMessage().contains("Hessian/Burlap: '") //$NON-NLS-1$
				&& record.getMessage().contains("' is an unknown class in ")) { //$NON-NLS-1$
			throw new HessianRuntimeException(record.getMessage());
		}
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
	}
}