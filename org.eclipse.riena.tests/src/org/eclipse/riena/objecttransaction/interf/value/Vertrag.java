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

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;

/**
 * TODO Fehlender Klassen-Kommentar
 */
public class Vertrag extends AbstractTransactedObject implements IVertrag {

	private String vertragsNummer;
	private String vertragsBeschreibung;
	private Long vertragsSumme;

	@SuppressWarnings("unused")
	private Vertrag() {
		super();
		if (getCurrentObjectTransaction().isCleanModus()) {
			getCurrentObjectTransaction().register(this);
		} else {
			throw new InvalidTransactionFailure("cannot instantiate Vertrag with private method if not in clean state");
		}
	}

	/**
	 * @param vertragsnummer
	 */
	public Vertrag(final String vertragsnummer) {
		super(new GenericOID("vertrag", "vertragsnr", vertragsnummer), "1");
		if (getCurrentObjectTransaction().isCleanModus()) {
			getCurrentObjectTransaction().register(this);
		} else {
			getCurrentObjectTransaction().registerNew(this);
		}
		setVertragsNummer(vertragsnummer);
	}

	/**
	 * @return the vertragsNummer.
	 */
	public String getVertragsNummer() {
		return (String) getCurrentObjectTransaction().getReference(this, "vertragsNummer", vertragsNummer);
	}

	/**
	 * @param vertragsNummer
	 *            The vertragsNummer to set.
	 */
	public void setVertragsNummer(final String vertragsNummer) {
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.vertragsNummer = vertragsNummer;
		}
		getCurrentObjectTransaction().setReference(this, "vertragsNummer", vertragsNummer);
	}

	/**
	 * @return the vertragsBeschreibung.
	 */
	public String getVertragsBeschreibung() {
		return (String) getCurrentObjectTransaction().getReference(this, "vertragsBeschreibung", vertragsBeschreibung);
	}

	/**
	 * @param vertragsBeschreibung
	 *            The vertragsBeschreibung to set.
	 */
	public void setVertragsBeschreibung(final String vertragsBeschreibung) {
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.vertragsBeschreibung = vertragsBeschreibung;
		}
		getCurrentObjectTransaction().setReference(this, "vertragsBeschreibung", vertragsBeschreibung);
	}

	/**
	 * @return the vertragsSumme.
	 */
	public Long getVertragsSumme() {
		return (Long) getCurrentObjectTransaction().getReference(this, "vertragsSumme", vertragsSumme);
	}

	/**
	 * @param vertragsSumme
	 *            The vertragsSumme to set.
	 */
	public void setVertragsSumme(final Long vertragsSumme) {
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.vertragsSumme = vertragsSumme;
		}
		getCurrentObjectTransaction().setReference(this, "vertragsSumme", vertragsSumme);

	}
}