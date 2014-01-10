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
package org.eclipse.riena.security.common;

/**
 * The ISubjectChangeListener is informed when the Subject is changed. This
 * interface is used by the @see ISubjectHolder so that interested components
 * can register when any change to the subject happens. This maybe because a
 * principal is added or removed from the subject or when the user logs on or
 * logs off.
 * 
 */
public interface ISubjectChangeListener {

	/**
	 * Signals the Subject has changed
	 * 
	 * @param event
	 *            event containing the old and the new subject
	 */
	void changed(SubjectChangeEvent event);

}
