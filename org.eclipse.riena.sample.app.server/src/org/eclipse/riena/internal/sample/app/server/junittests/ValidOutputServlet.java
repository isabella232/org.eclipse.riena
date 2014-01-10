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
package org.eclipse.riena.internal.sample.app.server.junittests;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@SuppressWarnings("serial")
public class ValidOutputServlet extends HttpServlet {

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
			IOException {
		final PrintWriter printWriter = new PrintWriter(resp.getOutputStream());
		printWriter.write("here is what I got to say...."); //$NON-NLS-1$
		printWriter.close();
	}
}
