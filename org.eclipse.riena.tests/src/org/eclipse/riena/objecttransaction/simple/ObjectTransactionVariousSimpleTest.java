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
package org.eclipse.riena.objecttransaction.simple;

import org.eclipse.core.runtime.AssertionFailedException;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.objecttransaction.IObjectTransaction;
import org.eclipse.riena.objecttransaction.IObjectTransactionExtract;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;
import org.eclipse.riena.objecttransaction.ObjectTransactionFactory;
import org.eclipse.riena.objecttransaction.delta.TransactionDelta;
import org.eclipse.riena.objecttransaction.simple.value.Addresse;
import org.eclipse.riena.objecttransaction.simple.value.Kunde;
import org.eclipse.riena.objecttransaction.simple.value.Vertrag;
import org.eclipse.riena.objecttransaction.state.State;

/**
 * TODO Fehlender Klassen-Kommentar
 */
@NonUITestCase
public class ObjectTransactionVariousSimpleTest extends RienaTestCase {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		loader.setDefaultAssertionStatus(true);
	}

	/**
	 * @throws Exception
	 */
	public void testNullValueAndCommitToObjects() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		objectTransaction.setCleanModus(false);
		kunde.setVorname("john");
		kunde.setNachname("Miller");
		assertEquals("john", kunde.getVorname());
		// objectTransaction.setCleanModus( false );
		assertEquals("john", kunde.getVorname());
		kunde.setVorname(null);
		// System.out.println( kunde.getVorname() );
		assertNull(kunde.getVorname());
		objectTransaction.commitToObjects();
		assertNull(kunde.getVorname());
	}

	/**
	 * @throws Exception
	 */
	public void testRegisterAsDeletedWithCommitToObjects() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		objectTransaction.setCleanModus(false);

		objectTransaction.registerAsDeleted(kunde);

		objectTransaction.commitToObjects();

		assertFalse("kunde must not be registered", objectTransaction.isRegistered(kunde));
	}

	/**
	 * @throws Exception
	 */
	public void testExtractIsImmutable() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		objectTransaction.setCleanModus(false);

		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final IObjectTransactionExtract extract = objectTransaction.exportExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("must only be changes for one object ", deltas.length == 1);

		assertTrue("must have only 2 changes ", deltas[0].getChanges().size() == 2);

		kunde.setAddresse(new Addresse(true));

		assertTrue("extract changed:: must only be changes for one object ", deltas.length == 1);

		assertTrue("extract changed:: must have only 2 changes ", deltas[0].getChanges().size() == 2);

	}

	/**
	 * @throws Exception
	 */
	public void testExportModified1() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final Addresse adresse = new Addresse(false);
		kunde.setAddresse(adresse);

		objectTransaction.setCleanModus(false);
		adresse.setOrt("frankfurt");
		adresse.setStrasse("gutleutstrasse");
		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only one transaction delta", deltas.length == 1);
		assertTrue("single delta should reference adresse", deltas[0].getObjectId() == adresse.getObjectId());
		assertTrue("delta status must be modified", deltas[0].getState() == State.MODIFIED);
	}

	/**
	 * @throws Exception
	 */
	public void testExportModified2() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final Addresse adresse = new Addresse(false);
		objectTransaction.setCleanModus(false);

		kunde.setAddresse(adresse);

		adresse.setOrt("frankfurt");
		adresse.setStrasse("gutleutstrasse");
		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only two transaction delta", deltas.length == 2);
		// sequence of objects in delta[0] and delta[1] is random. therefore we
		// check that one is adresse and the other is kunde
		// and that they are not equal
		assertTrue("single delta should reference adresse",
				(deltas[0].getObjectId() == adresse.getObjectId() || deltas[1].getObjectId() == adresse.getObjectId()));
		assertTrue("delta status must both be modified", (deltas[0].getState().equals(State.MODIFIED) && deltas[1]
				.getState().equals(State.MODIFIED)));
		assertTrue("single delta should reference kunde",
				(deltas[0].getObjectId() == kunde.getObjectId() || deltas[1].getObjectId() == kunde.getObjectId()));
		assertTrue("deltas should reference different objects", (deltas[0].getObjectId() != deltas[1].getObjectId()));

	}

	/**
	 * @throws Exception
	 */
	public void testExportModified3() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final Addresse adresse = new Addresse(false);
		adresse.setOrt("frankfurt");
		adresse.setStrasse("gutleutstrasse");
		objectTransaction.setCleanModus(false);
		kunde.setAddresse(adresse);

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only two transaction delta", deltas.length == 2);
		assertTrue("single delta should reference kunde", deltas[0].getObjectId() == kunde.getObjectId());
		assertTrue("delta status of Kunde must be modified", deltas[0].getState().equals(State.MODIFIED));
		assertTrue("single delta should reference adresse", deltas[1].getObjectId() == adresse.getObjectId());
		assertTrue("delta status of Adresse must be clean", deltas[1].getState().equals(State.CLEAN));
	}

	/**
	 * @throws Exception
	 */
	public void testExportModified4() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Addresse adresse = new Addresse(false);
		kunde.setAddresse(adresse);

		adresse.setOrt("frankfurt");
		adresse.setStrasse("gutleutstrasse");
		objectTransaction.setCleanModus(false);
		kunde.setVorname("john");
		kunde.setNachname("Miller");
		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only one transaction delta", deltas.length == 1);
		assertTrue("single delta should reference kunde", deltas[0].getObjectId() == kunde.getObjectId());
		assertTrue("delta status must be modified", deltas[0].getState().equals(State.MODIFIED));
	}

	/**
	 * Instantiating and adding a Vertrag after leaving CleanModus.
	 * 
	 * @throws Exception
	 */
	public void testExportModified5() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		objectTransaction.setCleanModus(false);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.addVertrag(v2);

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertEquals("should be only two transaction delta", 2, deltas.length);
		if (deltas[0].getObjectId() == v2.getObjectId()) {
			assertSame("single delta should reference v2", deltas[0].getObjectId(), v2.getObjectId());
			assertEquals("delta status must be created", State.CREATED, deltas[0].getState());
			assertSame("single delta should reference kunde", deltas[1].getObjectId(), kunde.getObjectId());
			assertEquals("delta status must be modified", State.MODIFIED, deltas[1].getState());
		} else if (deltas[0].getObjectId() == kunde.getObjectId()) {
			assertSame("single delta should reference v2", deltas[1].getObjectId(), v2.getObjectId());
			assertEquals("delta status must be created", State.CREATED, deltas[1].getState());
			assertSame("single delta should reference kunde", deltas[0].getObjectId(), kunde.getObjectId());
			assertEquals("delta status must be modified", State.MODIFIED, deltas[0].getState());
		} else {
			fail("delta must be v2 vertrag or kunde delta");
		}
	}

	/**
	 * Instantiating and adding 2 Verträge after leaving CleanModus.
	 * 
	 * @throws Exception
	 */
	public void testExportModified6() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		objectTransaction.setCleanModus(false);
		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.addVertrag(v2);

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertEquals("should be only three transaction delta", 3, deltas.length);
		for (final TransactionDelta delta : deltas) {
			if (delta.getObjectId() == v2.getObjectId()) {
				assertEquals("delta status must be created", State.CREATED, delta.getState());
			} else if (delta.getObjectId() == v1.getObjectId()) {
				assertEquals("delta status must be created", State.CREATED, delta.getState());
			} else if (delta.getObjectId() == kunde.getObjectId()) {
				assertEquals("delta status must be modified", State.MODIFIED, delta.getState());
			} else {
				fail("object id in delta does not match any of the expected ones");
			}
		}
	}

	/**
	 * Setting names of Kunde after leaving CleanModus.
	 * 
	 * @throws Exception
	 */
	public void testExportModified7() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.addVertrag(v2);
		objectTransaction.setCleanModus(false);
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only one transaction delta", deltas.length == 1);
		assertTrue("single delta should reference kunde", deltas[0].getObjectId() == kunde.getObjectId());
		assertTrue("delta status must be modified", deltas[0].getState().equals(State.MODIFIED));
	}

	/**
	 * Adding a previously-instantiated Vertrag after leaving CleanModus.
	 * 
	 * @throws Exception
	 */
	public void testExportModified8() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		objectTransaction.setCleanModus(false);
		kunde.addVertrag(v2);

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only two transaction delta", deltas.length == 2);
		assertTrue("single delta should reference kunde", deltas[0].getObjectId() == kunde.getObjectId());
		assertTrue("delta status must be modified", deltas[0].getState().equals(State.MODIFIED));
		assertTrue("single delta should reference v2", deltas[1].getObjectId() == v2.getObjectId());
		assertTrue("delta status must be clean", deltas[1].getState().equals(State.CLEAN));
	}

	/**
	 * Adding 2 previously-instantiated Verträge after leaving CleanModus.
	 * 
	 * @throws Exception
	 */
	public void testExportModified9() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		objectTransaction.setCleanModus(false);
		kunde.addVertrag(v1);
		kunde.addVertrag(v2);

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only three transaction delta", deltas.length == 3);
		assertTrue("single delta should reference kunde", deltas[0].getObjectId() == kunde.getObjectId());
		assertTrue("delta status must be modified", deltas[0].getState().equals(State.MODIFIED));
		assertTrue("single delta should reference v1", deltas[1].getObjectId() == v1.getObjectId());
		assertTrue("delta status must be clean", deltas[1].getState().equals(State.CLEAN));
		assertTrue("single delta should reference v2", deltas[2].getObjectId() == v2.getObjectId());
		assertTrue("delta status must be clean", deltas[2].getState().equals(State.CLEAN));
	}

	/**
	 * Modifying properties of 2 previously-added Verträge after leaving
	 * CleanModus.
	 * 
	 * @throws Exception
	 */
	public void testExportModified10() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final Vertrag v1 = new Vertrag("123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		kunde.addVertrag(v2);
		objectTransaction.setCleanModus(false);
		v1.setVertragsBeschreibung("vertrag nummer 123");
		v2.setVertragsBeschreibung("vertrag nummer 456");

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only two transaction delta", deltas.length == 2);
		assertTrue("single delta should reference v1", deltas[0].getObjectId() == v2.getObjectId());
		assertTrue("delta status must be modified", deltas[0].getState().equals(State.MODIFIED));
		assertTrue("single delta should reference adresse", deltas[1].getObjectId() == v1.getObjectId());
		assertTrue("delta status must be modified", deltas[1].getState() == State.MODIFIED);
	}

	/**
	 * Modifying properties of Kunde after leaving CleanModus.
	 * 
	 * @throws Exception
	 */
	public void testExportModified11() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.addVertrag(v2);
		objectTransaction.setCleanModus(false);
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be only one transaction delta", deltas.length == 1);
		assertTrue("single delta should reference kunde", deltas[0].getObjectId() == kunde.getObjectId());
		assertTrue("delta status must be modified", deltas[0].getState().equals(State.MODIFIED));
	}

	/**
	 * Adding clean transacted object to extract
	 * 
	 * @throws Exception
	 */
	public void testExportModified12() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.addVertrag(v2);
		objectTransaction.setCleanModus(false);
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		extract.addCleanTransactedObject(v1);
		final TransactionDelta[] deltas = extract.getDeltas();
		assertTrue("should be two transaction deltas", deltas.length == 2);
		assertTrue("single delta should reference kunde", deltas[0].getObjectId() == kunde.getObjectId());
		assertTrue("delta status must be modified", deltas[0].getState().equals(State.MODIFIED));
		assertTrue("single delta should reference kunde", deltas[1].getObjectId() == v1.getObjectId());
		assertTrue("delta status must be clean", deltas[1].getState().equals(State.CLEAN));
	}

	/**
	 * Attempting to add a non-clean transacted object to extract
	 * 
	 * @throws Exception
	 */
	public void testExportModified13() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.addVertrag(v2);
		objectTransaction.setCleanModus(false);
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();
		try {
			extract.addCleanTransactedObject(kunde);
			fail("non-clean transacted objects may not be added to an extract");
		} catch (final AssertionFailedException e) {
			ok();
		}
	}

	/**
	 * Importing an extract does not inadvertently register referenced
	 * transacted objects
	 * 
	 * @throws Exception
	 */
	public void testImportModified1() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.addVertrag(v2);
		objectTransaction.setCleanModus(false);
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction objectTransaction2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction2.register(kunde);
		objectTransaction2.importOnlyModifedObjectsFromExtract(extract);
		assertTrue("kunde must be registered", objectTransaction2.isRegistered(kunde));
		assertTrue("v1 must not be registered", (!objectTransaction2.isRegistered(v1)));
		assertTrue("v2 must not be registered", (!objectTransaction2.isRegistered(v2)));
	}

	/**
	 * @throws Exception
	 */
	public void testImportModified2() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		kunde.addVertrag(v2);
		objectTransaction.setCleanModus(false);
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction objectTransaction2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction2.register(kunde);
		objectTransaction2.register(v2);
		objectTransaction2.importOnlyModifedObjectsFromExtract(extract);
		assertTrue("kunde must be registered", objectTransaction2.isRegistered(kunde));
		assertTrue("v1 must not be registered", (!objectTransaction2.isRegistered(v1)));
		assertTrue("v2 must be registered", objectTransaction2.isRegistered(v2));
	}

	/**
	 * @throws Exception
	 */
	public void testImportModified3() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		kunde.addVertrag(v2);
		objectTransaction.setCleanModus(false);
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction objectTransaction2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction2.register(kunde);
		objectTransaction2.register(v2);
		objectTransaction2.register(v1);
		objectTransaction2.importOnlyModifedObjectsFromExtract(extract);
		assertTrue("kunde must be registered", objectTransaction2.isRegistered(kunde));
		assertTrue("v1 must be registered", objectTransaction2.isRegistered(v1));
		assertTrue("v2 must be registered", objectTransaction2.isRegistered(v2));
	}

	/**
	 * Target object-transaction misses a transacted-object registration that is
	 * required by the extract
	 * 
	 * @throws Exception
	 */
	public void testImportModified4() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.setVorname("john");
		kunde.setNachname("Miller");
		objectTransaction.setCleanModus(false);
		kunde.addVertrag(v2);

		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction objectTransaction2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction2.register(kunde);
		try {
			objectTransaction2.importOnlyModifedObjectsFromExtract(extract);
			fail("required transacted object v2 is not registered in target object transaction");
		} catch (final InvalidTransactionFailure e) {
			ok();
		}
	}

	/**
	 * @throws Exception
	 */
	public void testImportModified5() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");

		final Vertrag v1 = new Vertrag("123");
		v1.setVertragsBeschreibung("vertrag nummer 123");
		kunde.addVertrag(v1);
		final Vertrag v2 = new Vertrag("456");
		v2.setVertragsBeschreibung("vertrag nummer 456");
		kunde.setVorname("john");
		kunde.setNachname("Miller");
		kunde.addVertrag(v2);
		final Addresse adresse = new Addresse(true);
		objectTransaction.setCleanModus(false);
		kunde.setAddresse(adresse);

		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction objectTransaction2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction2.register(kunde);
		try {
			objectTransaction2.importOnlyModifedObjectsFromExtract(extract);
			fail("required transacted object adresse is not registered in target object transaction");
		} catch (final InvalidTransactionFailure e) {
			ok();
		}
	}

	/**
	 * @throws Exception
	 */
	public void testCheckNullValue1() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		kunde.setVorname("john");
		kunde.setNachname("Miller");

		final Addresse adresse = new Addresse(true);
		kunde.setAddresse(adresse);
		objectTransaction.setCleanModus(false);

		kunde.setAddresse(null);
		assertNull("adresse must be null", kunde.getAddresse());
		kunde.setVorname(null);
		assertNull("vorname must be null", kunde.getVorname());
	}

	/**
	 * @throws Exception
	 */
	public void testAddDeleteImportExport() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();

		final Kunde kunde = new Kunde("4711");
		objectTransaction.registerAsDeleted(kunde);
		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		ot2.importExtract(extract);
	}

	/**
	 * Export only modified objects
	 * 
	 * @throws Exception
	 */
	public void testAddDeleteImportExport2() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();

		final Kunde kunde = new Kunde("4711");
		objectTransaction.registerAsDeleted(kunde);
		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();

		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		ot2.importExtract(extract);
	}

	/**
	 * Import only modified objects
	 * 
	 * @throws Exception
	 */
	public void testAddDeleteImportExport3() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();

		final Kunde kunde = new Kunde("4711");
		objectTransaction.registerAsDeleted(kunde);
		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		ot2.importOnlyModifedObjectsFromExtract(extract);
	}

	/**
	 * @throws Exception
	 */
	public void testAddDeleteImportExport4() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();

		final Kunde kunde = new Kunde("4711");
		final Vertrag v1 = new Vertrag("11");
		final Vertrag v2 = new Vertrag("12");
		kunde.addVertrag(v1);
		kunde.addVertrag(v2);
		objectTransaction.registerAsDeleted(kunde);
		objectTransaction.registerAsDeleted(v1);
		objectTransaction.registerAsDeleted(v2);
		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		ot2.importOnlyModifedObjectsFromExtract(extract);
	}

	/**
	 * @throws Exception
	 */
	public void testAddRemoveImportExportWithNewObjects() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();

		final Kunde kunde = new Kunde("4711");
		final Addresse adresse = new Addresse(true);
		kunde.setAddresse(adresse);
		kunde.setAddresse(null);
		objectTransaction.registerAsDeleted(adresse);
		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		new Kunde("4711");
		ot2.importExtract(extract);
	}

	/**
	 * @throws Exception
	 */
	public void testAddRemoveImportExportWithExistingObjects() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		objectTransaction.setCleanModus(false);
		final Addresse adresse = new Addresse(true);
		kunde.setAddresse(adresse);
		kunde.setAddresse(null);
		objectTransaction.registerAsDeleted(adresse);
		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		new Kunde("4711");
		ot2.importExtract(extract);
	}

	/**
	 * @throws Exception
	 */
	public void testAddRemoveImportExportWithExistingObjectsOneToNRelation() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.setCleanModus(true);

		final Kunde kunde = new Kunde("4711");
		objectTransaction.setCleanModus(false);
		final Vertrag v1 = new Vertrag("11");
		kunde.addVertrag(v1);
		kunde.removeVertrag(v1);
		objectTransaction.registerAsDeleted(v1);
		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		ot2.setCleanModus(true);
		new Kunde("4711");
		ot2.setCleanModus(false);
		ot2.importExtract(extract);
	}

	/**
	 * @throws Exception
	 */
	public void testAddRemoveImportExportWithNewObjectsOneToNRelation() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();

		final Kunde kunde = new Kunde("4711");
		final Vertrag v1 = new Vertrag("11");
		kunde.addVertrag(v1);
		kunde.removeVertrag(v1);
		objectTransaction.registerAsDeleted(v1);
		objectTransaction.registerAsDeleted(kunde);
		final IObjectTransactionExtract extract = objectTransaction.exportExtract();

		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		ot2.importExtract(extract);
	}

	/**
	 * @throws Exception
	 */
	public void testTwoWayImportExport() throws Exception {
		// simulated client
		final IObjectTransaction objectTransaction =ObjectTransactionFactory.getInstance().createObjectTransaction();

		objectTransaction.setCleanModus(true);
		final Kunde kunde = new Kunde("4711");
		kunde.setNachname("müller");
		objectTransaction.setCleanModus(false);
		objectTransaction.registerAsDeleted(kunde);
		final IObjectTransactionExtract extract = objectTransaction.exportOnlyModifedObjectsToExtract();

		// simulierter server
		final IObjectTransaction ot2 = ObjectTransactionFactory.getInstance().createObjectTransaction();
		ot2.setCleanModus(true);
		new Kunde("4711");
		ot2.setCleanModus(false);
		ot2.importExtract(extract);
		ot2.commitToObjects();
		final IObjectTransactionExtract extract2 = ot2.exportExtract();

		// back to client
		objectTransaction.commitToObjects();
		objectTransaction.importExtract(extract2);
	}

	/**
	 * Tests whether SubSubTransaction works or throws an exception
	 * 
	 * @throws Exception
	 */
	public void testSubSubTransaction() throws Exception {
		final IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();

		objectTransaction.setCleanModus(true);
		final Kunde kunde = new Kunde("4711");
		kunde.setNachname("müller");
		objectTransaction.setCleanModus(false);

		final IObjectTransaction subOT = objectTransaction.createSubObjectTransaction();
		final Vertrag v1 = new Vertrag("4711");
		kunde.addVertrag(v1);

		final IObjectTransaction subSubOT = subOT.createSubObjectTransaction();
		final Vertrag v2 = new Vertrag("1015");
		kunde.addVertrag(v2);

		final Kunde kunde2 = new Kunde("4712");
		kunde2.setNachname("campo");

		subSubOT.commit();
		subOT.commit();
		objectTransaction.commitToObjects();

		kunde2.setNachname("Campo");
		kunde.setNachname("Schramm");
		v1.setVertragsBeschreibung("xxx");
		v2.setVertragsBeschreibung("yyyy");

	}
}