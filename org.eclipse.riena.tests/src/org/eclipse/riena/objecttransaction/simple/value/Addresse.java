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
package org.eclipse.riena.objecttransaction.simple.value;

import java.security.SecureRandom;

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.ITransactedObject;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;
import org.eclipse.riena.objecttransaction.ObjectTransactionManager;

/**
 * Sample class for "UTAddresse"
 */
public class Addresse extends AbstractTransactedObject implements ITransactedObject {

	private String plz;
	private String ort;
	private String strasse;
	private static SecureRandom random = new SecureRandom();

	@SuppressWarnings("unused")
	private Addresse() {
		super();
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			ObjectTransactionManager.getInstance().getCurrent().register(this);
		} else {
			throw new InvalidTransactionFailure("cannot instantiate Adresse with private method if not in clean state");
		}
	}

	/**
	 * @param dummy
	 */
	public Addresse(final boolean dummy) {
		super(new GenericOID("addresse", "primkey", Integer.valueOf(nextRandomInt())), "1");
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			ObjectTransactionManager.getInstance().getCurrent().register(this);
		} else {
			ObjectTransactionManager.getInstance().getCurrent().registerNew(this);
		}
	}

	/**
	 * @param primKey
	 */
	public Addresse(final Integer primKey) {
		super(new GenericOID("addresse", "primkey", primKey), "1");
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			ObjectTransactionManager.getInstance().getCurrent().register(this);
		} else {
			ObjectTransactionManager.getInstance().getCurrent().registerNew(this);
		}
	}

	/**
	 * @return the ort.
	 */
	public String getOrt() {
		return (String) ObjectTransactionManager.getInstance().getCurrent().getReference(this, "ort", ort);
	}

	/**
	 * @param ort
	 *            The ort to set.
	 */
	public void setOrt(final String ort) {
		// changeEvent
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			this.ort = ort;
		}
		ObjectTransactionManager.getInstance().getCurrent().setReference(this, "ort", ort);
	}

	/**
	 * @return the plz.
	 */
	public String getPlz() {
		return (String) ObjectTransactionManager.getInstance().getCurrent().getReference(this, "plz", plz);
	}

	/**
	 * @param plz
	 *            The plz to set.
	 */
	public void setPlz(final String plz) {
		// changeEvent
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			this.plz = plz;
		}
		ObjectTransactionManager.getInstance().getCurrent().setReference(this, "plz", plz);
	}

	/**
	 * @return the strasse.
	 */
	public String getStrasse() {
		return (String) ObjectTransactionManager.getInstance().getCurrent().getReference(this, "strasse", strasse);
	}

	/**
	 * @param strasse
	 *            The strasse to set.
	 */
	public void setStrasse(final String strasse) {
		// changeEvent
		if (ObjectTransactionManager.getInstance().getCurrent().isCleanModus()) {
			this.strasse = strasse;
		}
		ObjectTransactionManager.getInstance().getCurrent().setReference(this, "strasse", strasse);
	}

	private static int nextRandomInt() {
		return random.nextInt();
	}
}