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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 */
@SuppressWarnings("serial")
public class BasicAuthenticationTestServlet extends HttpServlet {

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
			IOException {
		final String auth = req.getHeader("Authorization"); //$NON-NLS-1$
		final String correctAuth = "Basic c2NwOnNjcHRlc3RwYXNzd29yZA==";// encoded //$NON-NLS-1$
		// version
		// of
		// userid=scp,
		// password=scptestpassword
		if (auth != null && auth.equals(correctAuth)) {
			resp.getOutputStream().write("OK".getBytes()); //$NON-NLS-1$
		} else {
			resp.sendError(401);
		}

	}

}
