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
package org.eclipse.riena.ui.ridgets;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.StatuslineRidget;
import org.eclipse.riena.ui.ridgets.marker.StatuslineMessageMarkerViewer;

/**
 * Tests for the {@link RidgetToStatuslineSubscriber}.
 */
@NonUITestCase
public class RidgetToStatuslineSubscriberTest extends TestCase {

	public void testSetStatuslineToShowMarkerMessages() throws Exception {
		final IRidget r1 = EasyMock.createMock(IRidget.class);
		final IComplexRidget r2 = EasyMock.createMock(IComplexRidget.class);
		final IBasicMarkableRidget r3 = EasyMock.createMock(IBasicMarkableRidget.class);

		final List<IRidget> ridgets = Arrays.asList(r1, r2, r3);
		final int[] addRidgetInvocationsCount = { 0 };
		final int[] removeRidgetInvocationsCount = { 0 };
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber() {
			@Override
			protected StatuslineMessageMarkerViewer createMessageViewer(final IStatuslineRidget statuslineToShowMarkerMessages) {
				return statuslineToShowMarkerMessages == null ? null : new StatuslineMessageMarkerViewer(statuslineToShowMarkerMessages) {
					@Override
					public void addRidget(final IBasicMarkableRidget markableRidget) {
						addRidgetInvocationsCount[0]++;
					}

					@Override
					public void removeRidget(final IBasicMarkableRidget markableRidget) {
						removeRidgetInvocationsCount[0]++;
					}
				};
			}
		};
		final StatuslineRidget statuslineToShowMarkerMessages = new StatuslineRidget();
		r2.setStatuslineToShowMarkerMessages(statuslineToShowMarkerMessages);
		EasyMock.replay(r1, r2, r3);
		s.setStatuslineToShowMarkerMessages(statuslineToShowMarkerMessages, ridgets);
		EasyMock.verify(r1, r2, r3);
		assertEquals(1, addRidgetInvocationsCount[0]);
		assertEquals(0, removeRidgetInvocationsCount[0]);

		//
		// set null /equal to removing the status line/
		EasyMock.reset(r1, r2, r3);
		r2.setStatuslineToShowMarkerMessages(null);
		EasyMock.replay(r1, r2, r3);
		s.setStatuslineToShowMarkerMessages(null, ridgets);
		EasyMock.verify(r1, r2, r3);
		assertEquals(1, addRidgetInvocationsCount[0]);
		assertEquals(1, removeRidgetInvocationsCount[0]);
	}

	public void testAddRemoveRidgetBasicMarkableNoStatusline() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		final IBasicMarkableRidget ridget = EasyMock.createMock(IBasicMarkableRidget.class);

		// no calls to the simple ridget expected
		EasyMock.replay(ridget);
		s.addRidget(ridget);
		EasyMock.verify(ridget);

		//
		// remove
		EasyMock.reset(ridget);
		EasyMock.replay(ridget);
		s.removeRidget(ridget);
		EasyMock.verify(ridget);
	}

	public void testAddRemoveRidgetBasicMarkable() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		final IBasicMarkableRidget ridget = EasyMock.createMock(IBasicMarkableRidget.class);
		final int[] addRidgetInvocationsCount = { 0 };
		final int[] removeRidgetInvocationsCount = { 0 };
		ReflectionUtils.setHidden(s, "messageViewer", new StatuslineMessageMarkerViewer(new StatuslineRidget()) {
			@Override
			public void addRidget(final IBasicMarkableRidget markableRidget) {
				addRidgetInvocationsCount[0]++;
			}

			@Override
			public void removeRidget(final IBasicMarkableRidget markableRidget) {
				removeRidgetInvocationsCount[0]++;
			}
		});

		// no calls to the simple ridget expected
		EasyMock.replay(ridget);
		s.addRidget(ridget);
		EasyMock.verify(ridget);
		assertEquals(1, addRidgetInvocationsCount[0]);
		assertEquals(0, removeRidgetInvocationsCount[0]);

		//
		// remove
		EasyMock.reset(ridget);
		EasyMock.replay(ridget);
		s.removeRidget(ridget);
		EasyMock.verify(ridget);
		assertEquals(1, addRidgetInvocationsCount[0]);
		assertEquals(1, removeRidgetInvocationsCount[0]);
	}

	public void testAddRemoveRidgetNoBasicMarkable() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		final IRidget ridget = EasyMock.createMock(IRidget.class);
		ReflectionUtils.setHidden(s, "messageViewer", new StatuslineMessageMarkerViewer(new StatuslineRidget()) {
			@Override
			public void addRidget(final IBasicMarkableRidget markableRidget) {
				fail("Invocation of this method is not expected in this case.");
			}

			@Override
			public void removeRidget(final IBasicMarkableRidget markableRidget) {
				fail("Invocation of this method is not expected in this case.");
			}
		});
		// no calls to the simple ridget expected
		EasyMock.replay(ridget);
		s.addRidget(ridget);
		EasyMock.verify(ridget);

		//
		// remove
		EasyMock.reset(ridget);
		EasyMock.replay(ridget);
		s.removeRidget(ridget);
		EasyMock.verify(ridget);

	}

	public void testAddRemoveRidgetContainer() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		final IComplexRidget ridget = EasyMock.createMock(IComplexRidget.class);
		final StatuslineRidget r = new StatuslineRidget();
		ReflectionUtils.setHidden(s, "messageViewer", new StatuslineMessageMarkerViewer(r) {
			@Override
			public void addRidget(final IBasicMarkableRidget markableRidget) {
				fail("Invocation of this method is not expected in this case.");
			}

			@Override
			public void removeRidget(final IBasicMarkableRidget markableRidget) {
				fail("Invocation of this method is not expected in this case.");
			}
		});

		ridget.setStatuslineToShowMarkerMessages(r);
		EasyMock.replay(ridget);
		s.addRidget(ridget);
		EasyMock.verify(ridget);

		//
		// remove
		EasyMock.reset(ridget);
		ridget.setStatuslineToShowMarkerMessages(null);
		EasyMock.replay(ridget);
		s.removeRidget(ridget);
		EasyMock.verify(ridget);
	}

	public void testAddRemoveRidgetContainerNoStatusline() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		final IComplexRidget ridget = EasyMock.createMock(IComplexRidget.class);
		// no call is expected
		EasyMock.replay(ridget);
		s.addRidget(ridget);
		EasyMock.verify(ridget);

		//
		// remove
		EasyMock.reset(ridget);
		EasyMock.replay(ridget);
		s.removeRidget(ridget);
		EasyMock.verify(ridget);
	}

	public void testIsDifferentStatuslineNullNull() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		assertFalse(s.isDifferentStatusline(null));
	}

	public void testIsDifferentStatuslineNullNotNull() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		assertTrue(s.isDifferentStatusline(new StatuslineRidget()));
	}

	public void testIsDifferentStatuslineNotNullNull() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		ReflectionUtils.setHidden(s, "messageViewer", new StatuslineMessageMarkerViewer(new StatuslineRidget()));
		assertTrue(s.isDifferentStatusline(null));
	}

	public void testIsDifferentStatuslineNotNullNotNull() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		ReflectionUtils.setHidden(s, "messageViewer", new StatuslineMessageMarkerViewer(new StatuslineRidget()));
		assertTrue(s.isDifferentStatusline(new StatuslineRidget()));
	}

	public void testIsDifferentStatuslineSame() throws Exception {
		final RidgetToStatuslineSubscriber s = new RidgetToStatuslineSubscriber();
		final StatuslineRidget r = new StatuslineRidget();
		ReflectionUtils.setHidden(s, "messageViewer", new StatuslineMessageMarkerViewer(r));
		assertFalse(s.isDifferentStatusline(r));
	}

}
