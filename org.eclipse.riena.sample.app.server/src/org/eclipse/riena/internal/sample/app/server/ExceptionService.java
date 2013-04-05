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
package org.eclipse.riena.internal.sample.app.server;

import java.lang.reflect.Constructor;
import java.util.Calendar;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.sample.app.common.exception.IExceptionService;

/**
 * This class implements the IExceptionService Interface and throws dedicated
 * failures / exceptions on client demand.
 * 
 * Some Examples: java.lang.ClassNotFoundException
 * java.lang.reflect.InvocationTargetException java.io.IOException
 * java.lang.RuntimeException java.lang.ClassCastException
 * java.lang.IndexOutOfBoundsException java.util.NoSuchElementException
 * java.lang.NullPointerException
 * 
 * 
 */
public class ExceptionService implements IExceptionService {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ExceptionService.class);

	public String getDate() {
		final Calendar cld = Calendar.getInstance();

		final int day = cld.get(Calendar.DAY_OF_MONTH);
		final int month = cld.get(Calendar.MONTH);
		final int year = cld.get(Calendar.YEAR);

		return day + "." + month + "." + year; //$NON-NLS-1$ //$NON-NLS-2$
	}

	// public String throwFailure(String name) throws Failure {
	// Failure failure = null;
	//
	// LOGGER.log(LogService.LOG_INFO, "The client requested a Failure '" + name
	// + "'");
	//
	// // let's try to assemble the requested Failure
	// try {
	// // load the class
	// Class failureClass = Class.forName(name);
	// Class[] params = { Class.forName("java.lang.String") };
	//
	// LOGGER.log(LogService.LOG_INFO, "throwFailure: Created a class: " +
	// failureClass);
	//
	// // create a new instance of the Failure
	// String cause = "ExceptionService: Here is your requested failure: " +
	// failureClass.getName() + "...";
	// Object[] args = { cause };
	// failure = (Failure)
	// failureClass.getConstructor(params).newInstance(args);
	// LOGGER.log(LogService.LOG_INFO,
	// "throwException: Created the requested exception " + failure
	// + " of type throwable");
	//
	// } catch (Exception e) {
	// // the requested class is not available
	// throw new RuntimeException("ExceptionService: requested Exception '" +
	// name + "' could not be found....");
	// }
	//
	// // thats the final result - throw it
	// if (failure != null) {
	// throw failure;
	// }
	//
	// return null;
	// }

	public String throwException(final String name) throws Throwable {
		Throwable exception = null;

		LOGGER.log(LogService.LOG_INFO, "The client requested an Exception '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$

		if (name == null) {
			LOGGER.log(LogService.LOG_ERROR, "The name of the requested Exception/Failure is null!"); //$NON-NLS-1$
			return null;
		}

		if (name.equals("default")) { //$NON-NLS-1$
			throw new Exception("ExceptionService: Here is your requested general default Exception..."); //$NON-NLS-1$
		}

		// let's try to assemble the requested Exception
		try {
			// load the class
			final Class<?> exceptionClass = Class.forName(name);
			Class<?>[] params = { Class.forName("java.lang.String") }; //$NON-NLS-1$

			LOGGER.log(LogService.LOG_INFO, "throwException: Created a class: " + exceptionClass); //$NON-NLS-1$

			// create a new instance of the Exception
			final String cause = "ExceptionService: Here is your requested " + exceptionClass.getName() + "..."; //$NON-NLS-1$ //$NON-NLS-2$
			final Object[] args = { cause };

			// Some classes do not have an constructor with the parameter
			// string.
			Constructor<?> c = null;
			try {
				c = exceptionClass.getConstructor(params);
				exception = (Throwable) c.newInstance(args);
			} catch (final NoSuchMethodException ex) {
				params = new Class[0];
				c = exceptionClass.getConstructor(params);
				exception = (Throwable) c.newInstance(new Object[0]);
				LOGGER.log(LogService.LOG_INFO, "Take default constructor."); //$NON-NLS-1$
			}

			LOGGER.log(LogService.LOG_INFO, "throwException: Created the requested exception " + exception //$NON-NLS-1$
					+ " of type throwable"); //$NON-NLS-1$

		} catch (final Exception e) {
			// the requested class is not available
			throw new Exception("ExceptionService: requested Exception '" + name + "' could not be found....", e); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// thats the final result - throw it
		if (exception != null) {
			throw exception;
		}

		return null;
	}

	public String throwNestedException() throws Throwable {
		throw new Exception(new NullPointerException("ExceptionService: Here is your requested nested Exception.")); //$NON-NLS-1$
	}

	public String throwRuntimeException(final String name) {
		try {
			this.throwException(name);
		} catch (final Throwable e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

}