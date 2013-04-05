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
package org.eclipse.riena.security.common.authentication.credentials;

/**
 * 
 */
public class ChoiceCredential extends AbstractCredential {

	private final String[] choices;
	private final int defaultChoice;
	private final boolean multipleSelectionsAllowed;
	private int[] selections;

	/**
	 * @param prompt
	 */
	public ChoiceCredential(final String prompt, final String[] choices, final int defaultChoice,
			final boolean multipleSelectionsAllowed) {
		super(prompt);
		this.choices = choices.clone();
		this.defaultChoice = defaultChoice;
		this.multipleSelectionsAllowed = multipleSelectionsAllowed;
	}

	public String[] getChoices() {
		return choices.clone();
	}

	public int getDefaultChoice() {
		return defaultChoice;
	}

	public boolean isMultipleSelectionsAllowed() {
		return multipleSelectionsAllowed;
	}

	public int[] getSelections() {
		return selections.clone();
	}

	public void setSelections(final int[] selections) {
		this.selections = selections.clone();
	}

}
