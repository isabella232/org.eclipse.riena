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
package org.eclipse.riena.objecttransaction.interf.value;

/**
 * TODO Fehlender Klassen-Kommentar
 */
public interface IVertrag {
	/**
	 * @return the vertragsNummer.
	 */
	String getVertragsNummer();

	/**
	 * @param vertragsNummer
	 *            The vertragsNummer to set.
	 */
	void setVertragsNummer(String vertragsNummer);

	/**
	 * @return the vertragsBeschreibung.
	 */
	String getVertragsBeschreibung();

	/**
	 * @param vertragsBeschreibung
	 *            The vertragsBeschreibung to set.
	 */
	void setVertragsBeschreibung(String vertragsBeschreibung);

	/**
	 * @return the vertragsSumme.
	 */
	Long getVertragsSumme();

	/**
	 * @param vertragsSumme
	 *            The vertragsSumme to set.
	 */
	void setVertragsSumme(Long vertragsSumme);
}