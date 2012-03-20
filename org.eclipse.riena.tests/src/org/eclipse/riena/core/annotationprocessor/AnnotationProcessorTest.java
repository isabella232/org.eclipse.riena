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
package org.eclipse.riena.core.annotationprocessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.annotationprocessor.IAnnotatedMethodHandlerExtension;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;
import org.eclipse.riena.ui.ridgets.annotation.OnClick;

/**
 * Test the {@code AnnotationProcessor}.
 */
@NonUITestCase
public class AnnotationProcessorTest extends RienaTestCase {

	private AnnotationProcessor processor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		processor = ReflectionUtils.newInstanceHidden(AnnotationProcessor.class);
	}

	public void testFailWithDoubledHandlers() {
		processor.update(new IAnnotatedMethodHandlerExtension[] { create(OnClick.class, new Handler()),
				create(OnClick.class, new Handler()) });
		try {
			processor.processMethods(new String());
			fail();
		} catch (final AnnotationProcessorFailure e) {
			ok();
		}
	}

	public void testAlreadyProcessed() {
		processor.update(new IAnnotatedMethodHandlerExtension[] { create(OnClick.class, new Handler()), });
		final String watch = new String();
		final IDisposer disposer1 = processor.processMethods(watch);
		final IDisposer disposer2 = processor.processMethods(watch);
		assertTrue(disposer1 != disposer2);
		assertTrue(disposer2 == ReflectionUtils.getHidden(processor, "EMPTY_DISPOSER"));
		final IDisposer disposer3 = processor.processMethods(watch);
		assertTrue(disposer2 == disposer3);
	}

	public void testFlatAnnotatedObject() {
		final Handler onClick = new Handler(OnClick.class);
		final Handler onActionCallback = new Handler(OnActionCallback.class);
		processor.update(new IAnnotatedMethodHandlerExtension[] { create(OnClick.class, onClick),
				create(OnActionCallback.class, onActionCallback) });
		processor.processMethods(new A());

		assertTrue(onClick.getHandled().size() == 1);
		assertTrue(onActionCallback.getHandled().size() == 1);

		assertTrue(onClick.getHandled().get(0) instanceof OnClick);
		assertTrue(onActionCallback.getHandled().get(0) instanceof OnActionCallback);

		assertEquals("RidgetA1", ((OnClick) onClick.getHandled().get(0)).ridgetId());
		assertEquals("RidgetA2", ((OnActionCallback) onActionCallback.getHandled().get(0)).ridgetId());
	}

	public void testHillyAnnotatedObject() {
		final Handler onClick = new Handler(OnClick.class);
		final Handler onActionCallback = new Handler(OnActionCallback.class);
		processor.update(new IAnnotatedMethodHandlerExtension[] { create(OnClick.class, onClick),
				create(OnActionCallback.class, onActionCallback) });
		processor.processMethods(new B());
		assertTrue(onClick.getHandled().size() == 1);
		assertTrue(onActionCallback.getHandled().size() == 2);

		assertTrue(onClick.getHandled().get(0) instanceof OnClick);
		assertTrue(onActionCallback.getHandled().get(0) instanceof OnActionCallback);
		assertTrue(onActionCallback.getHandled().get(1) instanceof OnActionCallback);

		assertEquals("RidgetA1", ((OnClick) onClick.getHandled().get(0)).ridgetId());
		assertEquals("RidgetA2", ((OnActionCallback) onActionCallback.getHandled().get(0)).ridgetId());
		assertEquals("RidgetA3", ((OnActionCallback) onActionCallback.getHandled().get(1)).ridgetId());
	}

	private IAnnotatedMethodHandlerExtension create(final Class<? extends Annotation> annotationClass,
			final IAnnotatedMethodHandler handler) {
		return new IAnnotatedMethodHandlerExtension() {
			public Class<? extends Annotation> getAnnotation() {
				return annotationClass;
			}

			public IAnnotatedMethodHandler createHandler() {
				return handler;
			}
		};
	}

	private static class Handler implements IAnnotatedMethodHandler {

		private Class<? extends Annotation> annotationClass;
		private final List<Annotation> handled = new ArrayList<Annotation>();

		public Handler() {
		}

		public Handler(final Class<? extends Annotation> annotationClass) {
			this.annotationClass = annotationClass;
		}

		public void handleAnnotation(final Annotation annotation, final Object object, final Method method,
				final Map<?, ?> optionalArgs, final AnnotatedOverriddenMethodsGuard guard, final DisposerList disposers) {
			assertTrue(annotationClass.isInstance(annotation));

			handled.add(annotation);
			disposers.add(new IDisposer() {
				public void dispose() {
				}
			});
		}

		public List<Annotation> getHandled() {
			return handled;
		}
	}

	private static class A {

		@OnClick(ridgetId = "RidgetA1")
		public void onClick() {
		}

		@OnActionCallback(ridgetId = "RidgetA2")
		public void onAcctionCallback() {
		}
	}

	private static class B extends A {

		@Override
		@OnClick(ridgetId = "RidgetA1")
		public void onClick() {
		}

		@Override
		@OnActionCallback(ridgetId = "RidgetA3")
		public void onAcctionCallback() {
		}

	}

}
