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
package org.eclipse.riena.security.common;

import javax.security.auth.Subject;

/**
 * SubjectChangeEvent notify that the subject is changed
 * 
 */
public class SubjectChangeEvent {
	private final Subject oldSubject;
	private final Subject newSubject;

	/**
	 * Creates an SubjectChangeEvent with the new and old Principal
	 * 
	 * @param newSubject
	 * @param oldSubject
	 */
	public SubjectChangeEvent(final Subject newSubject, final Subject oldSubject) {
		super();
		this.newSubject = newSubject;
		this.oldSubject = oldSubject;
	}

	/**
	 * @return the newSubject.
	 */
	public Subject getNewSubject() {
		return newSubject;
	}

	/**
	 * @return the oldSubject.
	 */
	public Subject getOldSubject() {
		return oldSubject;
	}

}
