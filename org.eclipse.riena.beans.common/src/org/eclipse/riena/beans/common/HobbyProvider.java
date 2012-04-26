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
package org.eclipse.riena.beans.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HobbyProvider {

	private List<Hobby> hobbies;

	/**
	 * 
	 */
	public HobbyProvider() {
		hobbies = new ArrayList<Hobby>();
		hobbies.add(new Hobby("Sport", "doing sports"));
		hobbies.add(new Hobby("Chess", "playing chess"));
		hobbies.add(new Hobby("Video Games", "playing video games"));
	}

	/**
	 * @return the hobbies
	 */
	public List<Hobby> getHobbies() {
		return hobbies;
	}

	/**
	 * @param hobbies
	 *            the hobbies to set
	 */
	public void setHobbies(final List<Hobby> hobbies) {
		this.hobbies = hobbies;
	}

}
