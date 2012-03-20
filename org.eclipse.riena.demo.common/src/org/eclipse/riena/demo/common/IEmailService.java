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
package org.eclipse.riena.demo.common;

import java.util.List;

/**
 *
 */
public interface IEmailService {

	String ID = IEmailService.class.getName();
	String WS_ID = "/EmailServiceWS"; //$NON-NLS-1$

	List<Email> showEmailsList(String directoryName);

	List<Email> findEmailsForCustomer(String emailAddress);

	boolean store(Email email);
}
