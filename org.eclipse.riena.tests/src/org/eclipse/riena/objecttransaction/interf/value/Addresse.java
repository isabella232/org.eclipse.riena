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

import java.security.SecureRandom;

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;

/**
 * Sample class for "UTAddresse"
 */
public class Addresse extends AbstractTransactedObject implements IAddresse {

	private String plz;
	private String ort;
	private String strasse;
	private static SecureRandom random = new SecureRandom();

	@SuppressWarnings("unused")
	private Addresse() {
		super();
		if (getCurrentObjectTransaction().isCleanModus()) {
			getCurrentObjectTransaction().register(this);
		} else {
			throw new InvalidTransactionFailure("cannot instantiate Adresse with private method if not in clean state");
		}
	}

	/**
	 * @param dummy
	 */
	public Addresse(final boolean dummy) {
		super(new GenericOID("addresse", "primkey", Integer.valueOf(nextRandomInt())), "1");
		if (getCurrentObjectTransaction().isCleanModus()) {
			getCurrentObjectTransaction().register(this);
		} else {
			getCurrentObjectTransaction().registerNew(this);
		}
	}

	/**
	 * @param primKey
	 */
	public Addresse(final Integer primKey) {
		super(new GenericOID("addresse", "primkey", primKey), "1");
		if (getCurrentObjectTransaction().isCleanModus()) {
			getCurrentObjectTransaction().register(this);
		} else {
			getCurrentObjectTransaction().registerNew(this);
		}
	}

	/**
	 * @return the ort.
	 */
	public String getOrt() {
		return (String) getCurrentObjectTransaction().getReference(this, "ort", ort);
	}

	/**
	 * @param ort
	 *            The ort to set.
	 */
	public void setOrt(final String ort) {
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.ort = ort;
		}
		getCurrentObjectTransaction().setReference(this, "ort", ort);
	}

	/**
	 * @return the plz.
	 */
	public String getPlz() {
		return (String) getCurrentObjectTransaction().getReference(this, "plz", plz);
	}

	/**
	 * @param plz
	 *            The plz to set.
	 */
	public void setPlz(final String plz) {
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.plz = plz;
		}
		getCurrentObjectTransaction().setReference(this, "plz", plz);
	}

	/**
	 * @return the strasse.
	 */
	public String getStrasse() {
		return (String) getCurrentObjectTransaction().getReference(this, "strasse", strasse);
	}

	/**
	 * @param strasse
	 *            The strasse to set.
	 */
	public void setStrasse(final String strasse) {
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.strasse = strasse;
		}
		getCurrentObjectTransaction().setReference(this, "strasse", strasse);
	}

	private static int nextRandomInt() {
		return random.nextInt();
	}

}