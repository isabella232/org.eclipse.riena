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
package org.eclipse.riena.objecttransaction.simple.value;

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.ITransactedObject;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;
import org.eclipse.riena.objecttransaction.ObjectTransactionManager;

/**
 * TODO Fehlender Klassen-Kommentar
 */
public class Vertrag extends AbstractTransactedObject implements ITransactedObject {

	private String vertragsNummer;
	private String vertragsBeschreibung;
	private Long vertragsSumme;

	@SuppressWarnings("unused")
	private Vertrag() {
		super();
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			ObjectTransactionManager.getInstance().getCurrent().register(this);
		} else {
			throw new InvalidTransactionFailure("cannot instantiate Vertrag with private method if not in clean state");
		}
	}

	/**
	 * @param vertragsnummer
	 */
	public Vertrag(final String vertragsnummer) {
		super(new GenericOID("vertrag", "vertragsnr", vertragsnummer), "1");
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			ObjectTransactionManager.getInstance().getCurrent().register(this);
		} else {
			ObjectTransactionManager.getInstance().getCurrent().registerNew(this);
		}
		setVertragsNummer(vertragsnummer);
	}

	/**
	 * @return the vertragsNummer.
	 */
	public String getVertragsNummer() {
		return (String) ObjectTransactionManager.getInstance().getCurrent()
				.getReference(this, "vertragsNummer", vertragsNummer);
	}

	/**
	 * @param vertragsNummer
	 *            The vertragsNummer to set.
	 */
	public void setVertragsNummer(final String vertragsNummer) {
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			this.vertragsNummer = vertragsNummer;
		}
		ObjectTransactionManager.getInstance().getCurrent().setReference(this, "vertragsNummer", vertragsNummer);
	}

	/**
	 * @return the vertragsBeschreibung.
	 */
	public String getVertragsBeschreibung() {
		return (String) ObjectTransactionManager.getInstance().getCurrent()
				.getReference(this, "vertragsBeschreibung", vertragsBeschreibung);
	}

	/**
	 * @param vertragsBeschreibung
	 *            The vertragsBeschreibung to set.
	 */
	public void setVertragsBeschreibung(final String vertragsBeschreibung) {
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			this.vertragsBeschreibung = vertragsBeschreibung;
		}
		ObjectTransactionManager.getInstance().getCurrent()
				.setReference(this, "vertragsBeschreibung", vertragsBeschreibung);
	}

	/**
	 * @return the vertragsSumme.
	 */
	public Long getVertragsSumme() {
		return (Long) ObjectTransactionManager.getInstance().getCurrent()
				.getReference(this, "vertragsSumme", vertragsSumme);
	}

	/**
	 * @param vertragsSumme
	 *            The vertragsSumme to set.
	 */
	public void setVertragsSumme(final Long vertragsSumme) {
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			this.vertragsSumme = vertragsSumme;
		}
		ObjectTransactionManager.getInstance().getCurrent().setReference(this, "vertragsSumme", vertragsSumme);

	}
}