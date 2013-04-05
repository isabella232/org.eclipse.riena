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
package org.eclipse.riena.ui.ridgets.marker;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;

/**
 * Test for the MessageBoxMessageMarkerViewer.
 */
@UITestCase
public class MessageBoxMessageMarkerViewerTest extends TestCase {

	private DefaultRealm realm;
	private Shell shell;
	private MessageBoxMessageMarkerViewer messageMarkerViewer;
	private IMessageBoxRidget messageBoxRidget;
	private TextRidget ridget;
	private String errorMessage;
	private ErrorMessageMarker errorMessageMarker;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();
		shell = new Shell();

		ridget = new TextRidget();

		messageBoxRidget = EasyMock.createMock(IMessageBoxRidget.class);
		messageMarkerViewer = new MessageBoxMessageMarkerViewer(messageBoxRidget);
		messageMarkerViewer.addRidget(ridget);

		errorMessage = "TestErrorMessage";
		errorMessageMarker = new ErrorMessageMarker(errorMessage);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		shell.dispose();
		shell = null;
		realm.dispose();
		realm = null;
		super.tearDown();
	}

	public void testSetVisible() throws Exception {

		assertFalse(messageMarkerViewer.isVisible());

		EasyMock.replay(messageBoxRidget);

		messageMarkerViewer.setVisible(true);

		EasyMock.verify(messageBoxRidget);
		assertFalse(messageMarkerViewer.isVisible());

		EasyMock.reset(messageBoxRidget);
		EasyMock.replay(messageBoxRidget);

		ridget.addMarker(errorMessageMarker);

		EasyMock.verify(messageBoxRidget);
		assertFalse(messageMarkerViewer.isVisible());

		EasyMock.reset(messageBoxRidget);
		messageBoxRidget.setText(errorMessage);
		EasyMock.expect(messageBoxRidget.show()).andReturn(IMessageBoxRidget.OK);
		EasyMock.replay(messageBoxRidget);

		messageMarkerViewer.setVisible(true);

		EasyMock.verify(messageBoxRidget);
		assertFalse(messageMarkerViewer.isVisible());

		EasyMock.reset(messageBoxRidget);
		messageBoxRidget.setText(errorMessage);
		EasyMock.expect(messageBoxRidget.show()).andReturn(IMessageBoxRidget.OK);
		EasyMock.replay(messageBoxRidget);

		final TextRidget anotherRidget = new TextRidget();
		final String anotherMessage = errorMessage + "2";
		anotherRidget.addMarker(new MessageMarker(anotherMessage));
		messageMarkerViewer.addRidget(anotherRidget);
		messageMarkerViewer.setVisible(true);

		EasyMock.verify(messageBoxRidget);
		assertFalse(messageMarkerViewer.isVisible());

		EasyMock.reset(messageBoxRidget);
		messageBoxRidget.setText(errorMessage + "\n" + anotherMessage);
		EasyMock.expect(messageBoxRidget.show()).andReturn(IMessageBoxRidget.OK);
		EasyMock.replay(messageBoxRidget);

		messageMarkerViewer.addMarkerType(MessageMarker.class);
		messageMarkerViewer.setVisible(true);

		EasyMock.verify(messageBoxRidget);
		assertFalse(messageMarkerViewer.isVisible());
	}
}
