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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.marker.AbstractMarker;
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.IMarkerPropertyChangeEvent;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Tests for the interface {@link IMarkableRidget}.
 */
public class MarkableRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected Control createWidget(final Composite parent) {
		return new Text(parent, SWT.SINGLE);
	}

	@Override
	protected IRidget createRidget() {
		return new TextRidget();
	}

	@Override
	protected IMarkableRidget getRidget() {
		return (IMarkableRidget) super.getRidget();
	}

	// test methods
	// /////////////

	public void testSetEnabled() {
		final IMarkableRidget ridget = getRidget();

		assertTrue(ridget.isEnabled());

		ridget.setEnabled(false);

		assertFalse(ridget.isEnabled());

		ridget.setEnabled(true);

		assertTrue(ridget.isEnabled());
	}

	public void testSetErrorMarked() {
		final IMarkableRidget ridget = getRidget();

		assertFalse(ridget.isErrorMarked());

		ridget.setErrorMarked(true);

		assertTrue(ridget.isErrorMarked());

		ridget.setErrorMarked(false);

		assertFalse(ridget.isErrorMarked());
	}

	public void testSetMandatory() {
		final IMarkableRidget ridget = getRidget();

		assertFalse(ridget.isMandatory());

		ridget.setMandatory(true);

		assertTrue(ridget.isMandatory());

		ridget.setMandatory(false);

		assertFalse(ridget.isMandatory());
	}

	public void testIsDisableMandatoryMarker() {
		final IMarkableRidget ridget = getRidget();
		ridget.setMandatory(true);

		assertFalse(ridget.isDisableMandatoryMarker());
		assertTrue(ridget.isMandatory());

		((ITextRidget) ridget).setText("foo");

		assertTrue(ridget.isDisableMandatoryMarker());
		assertTrue(ridget.isMandatory());

		((ITextRidget) ridget).setText("   ");

		assertTrue(ridget.isDisableMandatoryMarker());
		assertTrue(ridget.isMandatory());

		((ITextRidget) ridget).setText("");

		assertFalse(ridget.isDisableMandatoryMarker());
		assertTrue(ridget.isMandatory());
	}

	public void testSetOutputOnly() {
		final IMarkableRidget ridget = getRidget();

		assertFalse(ridget.isOutputOnly());

		ridget.setOutputOnly(true);

		assertTrue(ridget.isOutputOnly());

		ridget.setOutputOnly(false);

		assertFalse(ridget.isOutputOnly());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testAddMarker() throws Exception {
		final IMarkableRidget ridget = getRidget();
		final IMarker marker1 = new ErrorMarker();
		final IMarker marker2 = new MandatoryMarker();

		expectNoPropertyChangeEvent();

		ridget.addMarker(null);

		verifyPropertyChangeEvents();
		Collection<?> markers = ridget.getMarkers();
		assertEquals(0, markers.size());

		Collection newValue = new ArrayList<IMarker>();
		newValue.add(marker1);
		expectPropertyChangeEvents(new ExpectedMarkerPropertyChangeEvent(new ArrayList<IMarker>(), newValue, false));

		ridget.addMarker(marker1);

		verifyPropertyChangeEvents();
		newValue = ridget.getMarkers();
		markers = ridget.getMarkers();
		assertEquals(1, markers.size());
		assertTrue(markers.contains(marker1));

		newValue.add(marker2);
		expectPropertyChangeEvents(new ExpectedMarkerPropertyChangeEvent(ridget.getMarkers(), newValue, false));

		ridget.addMarker(marker2);

		verifyPropertyChangeEvents();
		markers = ridget.getMarkers();
		assertEquals(2, markers.size());
		assertTrue(markers.contains(marker1));
		assertTrue(markers.contains(marker2));

		expectNoPropertyChangeEvent();

		ridget.addMarker(marker2);

		verifyPropertyChangeEvents();
		markers = ridget.getMarkers();
		assertEquals(2, markers.size());
		assertTrue(markers.contains(marker1));
		assertTrue(markers.contains(marker2));

		resetPropertyChangeEvents();
	}

	public void testRemoveMarker() throws Exception {
		final IMarkableRidget ridget = getRidget();

		final IMarker marker1 = new ErrorMarker();
		final IMarker marker2 = new MandatoryMarker();
		final IMarker marker3 = new AbstractMarker() {
			/**/
		};
		ridget.addMarker(marker1);
		ridget.addMarker(marker2);

		expectNoPropertyChangeEvent();

		ridget.removeMarker(null);
		ridget.removeMarker(marker3);

		verifyPropertyChangeEvents();

		final Collection<?> newValue = new ArrayList<IMarker>(ridget.getMarkers());
		newValue.remove(marker1);
		expectPropertyChangeEvents(new ExpectedMarkerPropertyChangeEvent(ridget.getMarkers(), newValue, false));

		ridget.removeMarker(marker1);

		verifyPropertyChangeEvents();
		final Collection<?> markers = ridget.getMarkers();
		assertEquals(1, markers.size());
		assertTrue(markers.contains(marker2));

		expectNoPropertyChangeEvent();

		marker1.setAttribute("TestAttribute", "TestValue");

		verifyPropertyChangeEvents();

		resetPropertyChangeEvents();
	}

	public void testRemoveAllMarkers() throws Exception {
		final IMarkableRidget ridget = getRidget();
		final IMarker marker1 = new ErrorMarker();
		final IMarker marker2 = new MandatoryMarker();
		ridget.addMarker(marker1);
		ridget.addMarker(marker2);

		final Collection<IMarker> newValue = new ArrayList<IMarker>(ridget.getMarkers());
		newValue.clear();
		expectPropertyChangeEvents(new ExpectedMarkerPropertyChangeEvent(ridget.getMarkers(), newValue, false));

		ridget.removeAllMarkers();

		verifyPropertyChangeEvents();
		Collection<?> markers = ridget.getMarkers();
		assertEquals(0, markers.size());

		expectNoPropertyChangeEvent();

		ridget.removeAllMarkers();
		marker1.setAttribute("TestAttribute", "TestValue");

		verifyPropertyChangeEvents();
		markers = ridget.getMarkers();
		assertEquals(0, markers.size());
	}

	public void testGetMarkers() throws Exception {
		final IMarkableRidget ridget = getRidget();
		final IMarker marker = new ErrorMarker();

		final Collection<?> markers = ridget.getMarkers();
		assertNotNull(markers);

		ridget.addMarker(marker);
		final Collection<?> markers2 = ridget.getMarkers();

		assertEquals(0, markers.size());
		assertEquals(1, markers2.size());
	}

	public void testGetMarkersOfType() throws Exception {
		final IMarkableRidget ridget = getRidget();
		final ErrorMarker errorMarker = new ErrorMarker();
		final MandatoryMarker mandatoryMarker = new MandatoryMarker();

		Collection<? extends IMarker> markers = ridget.getMarkersOfType(null);
		assertNotNull(markers);
		assertEquals(0, markers.size());

		markers = ridget.getMarkersOfType(ErrorMarker.class);
		assertEquals(0, markers.size());
		markers = ridget.getMarkersOfType(MandatoryMarker.class);
		assertEquals(0, markers.size());

		ridget.addMarker(errorMarker);
		ridget.addMarker(mandatoryMarker);

		markers = ridget.getMarkersOfType(ErrorMarker.class);
		Iterator<?> iterator = markers.iterator();
		assertTrue(iterator.hasNext());
		assertSame(errorMarker, iterator.next());
		assertFalse(iterator.hasNext());

		markers = ridget.getMarkersOfType(MandatoryMarker.class);
		iterator = markers.iterator();
		assertTrue(iterator.hasNext());
		assertSame(mandatoryMarker, iterator.next());
		assertFalse(iterator.hasNext());

		markers = ridget.getMarkersOfType(null);
		assertNotNull(markers);
		assertEquals(0, markers.size());
	}

	public void testFireMarkerAttributeEvents() throws Exception {
		final IMarkableRidget ridget = getRidget();
		final ErrorMessageMarker errorMessageMarker = new ErrorMessageMarker("Message");
		ridget.addMarker(errorMessageMarker);

		expectPropertyChangeEvents(new ExpectedMarkerPropertyChangeEvent(null, ridget.getMarkers(), true));

		errorMessageMarker.setAttribute(IMessageMarker.MESSAGE, "NewMessage");

		verifyPropertyChangeEvents();

		final Collection<IMarker> newValue = new ArrayList<IMarker>(ridget.getMarkers());
		newValue.remove(errorMessageMarker);
		expectPropertyChangeEvents(new ExpectedMarkerPropertyChangeEvent(ridget.getMarkers(), newValue, false));

		ridget.removeMarker(errorMessageMarker);

		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();

		errorMessageMarker.setAttribute(IMessageMarker.MESSAGE, "YetAnotherMessage");

		verifyPropertyChangeEvents();
	}

	// helping methods
	// ////////////////

	private class ExpectedMarkerPropertyChangeEvent extends PropertyChangeEvent implements IMarkerPropertyChangeEvent {
		private static final long serialVersionUID = 4711L;

		private final boolean attributeRelated;

		public ExpectedMarkerPropertyChangeEvent(final Object oldValue, final Object newValue,
				final boolean attributeRelated) {
			super(getRidget(), IMarkableRidget.PROPERTY_MARKER, oldValue, newValue);
			this.attributeRelated = attributeRelated;
		}

		public boolean isAttributeRelated() {
			return attributeRelated;
		}
	}
}
