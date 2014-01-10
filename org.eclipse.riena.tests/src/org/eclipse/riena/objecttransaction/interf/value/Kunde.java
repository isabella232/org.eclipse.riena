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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.ITransactedObject;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;

/**
 * Sample class for "Kunde"
 */
public class Kunde extends AbstractTransactedObject {

	private String vorname;
	private String nachname;
	private String kundennr;
	private IAddresse addresse;
	private HashSet<Vertrag> vertraege;

	/** constructor called by webservices only for clean objects * */
	@SuppressWarnings("unused")
	private Kunde() {
		super();
		if (getCurrentObjectTransaction().isCleanModus()) {
			getCurrentObjectTransaction().register(this);
		} else {
			throw new InvalidTransactionFailure("cannot instantiate Kunde with private method if not in clean state");
		}
	}

	/**
	 * @param kundennr
	 */
	public Kunde(final String kundennr) {
		super(new GenericOID("kunde", "kundennrpk", kundennr), "1");
		if (getCurrentObjectTransaction().isCleanModus()) {
			getCurrentObjectTransaction().register(this);
		} else {
			getCurrentObjectTransaction().registerNew(this);
		}
		setKundennr(kundennr);
		vertraege = new HashSet<Vertrag>();
	}

	/**
	 * @return the kundennr.
	 */
	public String getKundennr() {
		return (String) getCurrentObjectTransaction().getReference(this, "kundennr", kundennr);
	}

	/**
	 * @param kundennr
	 *            The kundennr to set.
	 */
	public void setKundennr(final String kundennr) {
		if (((GenericOID) getObjectId()).getProperties().get("primkey") != null) {
			throw new UnsupportedOperationException("cannot change kundennr (once it is set)");
		}
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.kundennr = kundennr;
		} else {
			getCurrentObjectTransaction().setReference(this, "kundennr", kundennr);
		}
	}

	/**
	 * @return the nachname.
	 */
	public String getNachname() {
		return (String) getCurrentObjectTransaction().getReference(this, "nachname", nachname);
	}

	/**
	 * @param nachname
	 *            The nachname to set.
	 */
	public void setNachname(final String nachname) {
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.nachname = nachname;
		} else {
			getCurrentObjectTransaction().setReference(this, "nachname", nachname);
		}
	}

	/**
	 * @return the vorname.
	 */
	public String getVorname() {
		return (String) getCurrentObjectTransaction().getReference(this, "vorname", vorname);
	}

	/**
	 * @param vorname
	 *            The vorname to set.
	 */
	public void setVorname(final String vorname) {
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.vorname = vorname;
		} else {
			getCurrentObjectTransaction().setReference(this, "vorname", vorname);
		}
	}

	/**
	 * @return the addresse.
	 */
	public IAddresse getAddresse() {
		return (IAddresse) getCurrentObjectTransaction().getReference(this, "addresse", (ITransactedObject) addresse);
	}

	/**
	 * @param addresse
	 *            The addresse to set.
	 */
	public void setAddresse(final IAddresse addresse) {
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			this.addresse = addresse;
		} else {
			getCurrentObjectTransaction().setReference(this, "addresse", (ITransactedObject) addresse);
		}
	}

	/**
	 * @param vertrag
	 */
	public void addVertrag(final Vertrag vertrag) {
		if (getCurrentObjectTransaction().isCleanModus()) {
			vertraege.add(vertrag);
		} else {
			getCurrentObjectTransaction().addReference(this, "vertrag", vertrag);
		}
	}

	/**
	 * @param vertragsNummer
	 */
	public void removeVertrag(final String vertragsNummer) {
		final Vertrag tempVertrag = getVertrag(vertragsNummer);
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			vertraege.remove(getVertrag(vertragsNummer));
		} else {
			getCurrentObjectTransaction().removeReference(this, "vertrag", tempVertrag);
		}
	}

	/**
	 * @param vertrag
	 */
	public void removeVertrag(final Vertrag vertrag) {
		// changeEvent
		if (getCurrentObjectTransaction().isCleanModus()) {
			vertraege.remove(vertrag);
		} else {
			getCurrentObjectTransaction().removeReference(this, "vertrag", vertrag);
		}
	}

	/**
	 * @param vertragsNummer
	 * @return
	 */
	public Vertrag getVertrag(final String vertragsNummer) {
		final Vertrag[] tempVertraege = listVertrag();
		for (final Vertrag element : tempVertraege) {
			if (element.getVertragsNummer().equals(vertragsNummer)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public Vertrag[] listVertrag() {
		final Set<Vertrag> vertraegeSet = getCurrentObjectTransaction().listReference(this, "vertrag", vertraege);
		if (vertraegeSet.size() == 0) {
			return new Vertrag[0];
		}
		return vertraegeSet.toArray(new Vertrag[vertraegeSet.size()]);
	}
}