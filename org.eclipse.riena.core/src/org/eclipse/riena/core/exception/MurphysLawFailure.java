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
package org.eclipse.riena.core.exception;

/**
 * A pattern often observed is that there is a {@code try catch} block where the
 * catch clause just contains a {@code printStackTrace()} or a little bit better
 * a log statement. This is the case where it seems impossible that an exception
 * can occur, e.g. when a <a href="http://www.ietf.org/rfc/rfc1960.txt">RFC
 * 1960</a>-based Filter gets generated programmatically and you think that the
 * filter's syntax must be correct and creating a
 * {@link org.osgi.framework.Filter} will never fail.<br>
 * In such cases this failure should be thrown, so that this unlikely event gets
 * trapped and not ignored.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Murphy%27s_law">Murphy's Law</a>
 * 
 * @since 4.0
 */
public class MurphysLawFailure extends Failure {

	private static final long serialVersionUID = 1L;
	private static final String OOPS = "This should not have happened, but it did: "; //$NON-NLS-1$

	/**
	 * Create a Murphy's law failure with a message.
	 * 
	 * @param msg
	 */
	public MurphysLawFailure(final String msg) {
		super(OOPS + msg);
	}

	/**
	 * Create a Murphy's law failure with a message and a throwable.
	 * 
	 * @param msg
	 * @param t
	 */
	public MurphysLawFailure(final String msg, final Throwable t) {
		super(OOPS + msg, t);
	}
}
