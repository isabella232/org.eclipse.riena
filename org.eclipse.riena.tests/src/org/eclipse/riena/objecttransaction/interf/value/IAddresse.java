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
package org.eclipse.riena.objecttransaction.interf.value;

/**
 * TODO Fehlender Klassen-Kommentar
 */
public interface IAddresse {
	/**
	 * @return the ort.
	 */
	String getOrt();

	/**
	 * @param ort
	 *            The ort to set.
	 */
	void setOrt(String ort);

	/**
	 * @return the plz.
	 */
	String getPlz();

	/**
	 * @param plz
	 *            The plz to set.
	 */
	void setPlz(String plz);

	/**
	 * @return the strasse.
	 */
	String getStrasse();

	/**
	 * @param strasse
	 *            The strasse to set.
	 */
	void setStrasse(String strasse);
}