/*******************************************************************************
 * Copyright (c) 2007, 2015 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.annotationprocessor.AnnotatedOverriddenMethodsGuard;
import org.eclipse.riena.core.annotationprocessor.AnnotationProcessor;
import org.eclipse.riena.core.annotationprocessor.IAnnotatedMethodHandler;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.annotationprocessor.IAnnotatedMethodHandlerExtension;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.annotation.OnClick;
import org.eclipse.riena.ui.ridgets.annotation.handler.AbstractRidgetContainerAnnotationHandler;
import org.eclipse.riena.ui.ridgets.listener.FocusEvent;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

/**
 * Test the {@code AnnotationProcessor}.
 */
@SuppressWarnings("restriction")
@NonUITestCase
public class RidgetContainerAnnotationProcessorTest extends RienaTestCase {

	private AnnotationProcessor processor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		processor = AnnotationProcessor.getInstance();

		final Display display = Display.getDefault();
		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm); //$NON-NLS-1$
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SuppressWarnings("deprecation")
	public void testAddListenerToRidgetWithAbstractRidgetContainerAnnotationHandler() {
		final Handler onClick = new Handler(OnClick.class);
		processor.update(new IAnnotatedMethodHandlerExtension[] { create(OnClick.class, onClick) });

		final B container = new B();
		final IRidget ridget1 = new TextRidget();
		final IRidget ridget2 = new TextRidget();
		final IRidget ridget3 = new TextRidget();
		container.addRidget("RidgetA1", ridget1); //$NON-NLS-1$
		container.addRidget("RidgetA2", ridget2); //$NON-NLS-1$
		container.addRidget("RidgetB1", ridget3); //$NON-NLS-1$

		AnnotationProcessor.getInstance().processMethods(container);

		assertEquals(2, onClick.getHandled().size());

		assertTrue(onClick.getHandled().get(0) instanceof OnClick);
		assertTrue(onClick.getHandled().get(1) instanceof OnClick);

		assertEquals("RidgetA1", ((OnClick) onClick.getHandled().get(0)).ridgetId()); //$NON-NLS-1$
		assertEquals("RidgetA2", ((OnClick) onClick.getHandled().get(1)).ridgetId()); //$NON-NLS-1$

		assertEquals(ridget1, container.getRidget("RidgetA1")); //$NON-NLS-1$
		assertEquals(ridget2, container.getRidget("RidgetA2")); //$NON-NLS-1$
		assertEquals(ridget3, container.getRidget("RidgetB1")); //$NON-NLS-1$

		ListenerList<IFocusListener> focusListeners = ReflectionUtils.getHidden(ridget1, "focusListeners"); //$NON-NLS-1$
		assertTrue(focusListeners.size() == 1);

		focusListeners = ReflectionUtils.getHidden(ridget2, "focusListeners"); //$NON-NLS-1$
		assertTrue(focusListeners.size() == 1);

		focusListeners = ReflectionUtils.getHidden(ridget3, "focusListeners"); //$NON-NLS-1$
		assertTrue(focusListeners.size() == 0);

	}

	@SuppressWarnings("deprecation")
	public void testAddListenerToRidgetFromAnotherClass() {
		final Handler onClick = new Handler(OnClick.class);
		processor.update(new IAnnotatedMethodHandlerExtension[] { create(OnClick.class, onClick) });

		final B container = new B();
		final IRidget ridget1 = new TextRidget();
		final IRidget ridget2 = new TextRidget();
		container.addRidget("RidgetA1", ridget1); //$NON-NLS-1$
		container.addRidget("RidgetA2", ridget2); //$NON-NLS-1$

		AnnotationProcessor.getInstance().processMethods(container, new C());

		assertEquals(1, onClick.getHandled().size());

		assertTrue(onClick.getHandled().get(0) instanceof OnClick);

		assertEquals("RidgetA2", ((OnClick) onClick.getHandled().get(0)).ridgetId()); //$NON-NLS-1$

		assertEquals(ridget1, container.getRidget("RidgetA1")); //$NON-NLS-1$
		assertEquals(ridget2, container.getRidget("RidgetA2")); //$NON-NLS-1$

		ListenerList<IFocusListener> focusListeners = ReflectionUtils.getHidden(ridget1, "focusListeners"); //$NON-NLS-1$
		assertTrue(focusListeners.size() == 0);

		focusListeners = ReflectionUtils.getHidden(ridget2, "focusListeners"); //$NON-NLS-1$
		assertTrue(focusListeners.size() == 1);

	}

	// Helper functions & class
	///////////////////////////

	private IAnnotatedMethodHandlerExtension create(final Class<? extends Annotation> annotationClass, final IAnnotatedMethodHandler handler) {
		return new IAnnotatedMethodHandlerExtension() {
			public Class<? extends Annotation> getAnnotation() {
				return annotationClass;
			}

			public IAnnotatedMethodHandler createHandler() {
				return handler;
			}
		};
	}

	private static class Handler extends AbstractRidgetContainerAnnotationHandler {

		private final Class<? extends Annotation> annotationClass;
		private final List<Annotation> handled = new ArrayList<Annotation>();

		public Handler(final Class<? extends Annotation> annotationClass) {
			this.annotationClass = annotationClass;
		}

		@Override
		public void handleAnnotation(final Annotation annotation, final IRidgetContainer ridgetContainer, final Object target, final Method targetMethod,
				final AnnotatedOverriddenMethodsGuard guard) {
			assertTrue(annotationClass.isInstance(annotation));
			handled.add(annotation);

			final IRidget ridget = getRidget(annotation, targetMethod, ridgetContainer, ((OnClick) annotation).ridgetId());
			ridget.addFocusListener(new IFocusListener() {

				@Override
				public void focusLost(final FocusEvent event) {
				}

				@Override
				public void focusGained(final FocusEvent event) {
				}
			});
		}

		public List<Annotation> getHandled() {
			return handled;
		}

	}

	private static class A extends DummyContainer {

		@OnClick(ridgetId = "RidgetA1")
		public void onClick() {
		}

	}

	private static class B extends A {

		@Override
		@OnClick(ridgetId = "RidgetA2")
		public void onClick() {
		}

	}

	private static class C {

		@OnClick(ridgetId = "RidgetA2")
		public void onClick() {

		}

	}

	private static class DummyContainer implements IRidgetContainer {
		private final Map<String, IRidget> ridgets = new HashMap<String, IRidget>();

		public void addRidget(final String id, final IRidget ridget) {
			ridgets.put(id, ridget);
		}

		public boolean removeRidget(final String id) {
			return false;
		}

		public void configureRidgets() {
			// nothing
		}

		@SuppressWarnings("unchecked")
		public <R extends IRidget> R getRidget(final String id) {
			return (R) ridgets.get(id);
		}

		public <R extends IRidget> R getRidget(final Class<R> ridgetClazz, final String id) {
			return getRidget(id);
		}

		public Collection<? extends IRidget> getRidgets() {
			return ridgets.values();
		}

		public boolean isConfigured() {
			return false;
		}

		public void setConfigured(final boolean configured) {
		}

		public void setStatuslineToShowMarkerMessages(final IStatuslineRidget statuslineToShowMarkerMessages) {

		}

	}

}
