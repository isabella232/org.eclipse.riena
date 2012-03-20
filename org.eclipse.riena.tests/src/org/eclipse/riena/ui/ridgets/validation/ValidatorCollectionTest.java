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
package org.eclipse.riena.ui.ridgets.validation;

import static org.easymock.EasyMock.*;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests for the MinLength rule.
 */
@NonUITestCase
public class ValidatorCollectionTest extends RienaTestCase {

	private static final IValidator ALWAYS_SUCCEED_1 = new IValidator() {
		public IStatus validate(final Object value) {
			return Status.OK_STATUS;
		}
	};

	private static final IValidator ALWAYS_SUCCEED_2 = new IValidator() {
		public IStatus validate(final Object value) {
			return Status.OK_STATUS;
		}
	};

	private static final IValidator ALWAYS_FAIL_1 = new IValidator() {
		public IStatus validate(final Object value) {
			return Status.CANCEL_STATUS;
		}
	};

	private static final IValidator ALWAYS_FAIL_2 = new IValidator() {
		public IStatus validate(final Object value) {
			return Status.CANCEL_STATUS;
		}
	};

	private static final IValidator ALWAYS_THROW_EXCEPTION = new IValidator() {
		public IStatus validate(final Object value) {
			throw new ValidationFailure(getClass().getName() + "will always throw this excepion!");
		}
	};

	public void testJointStatus() {
		ValidatorCollection rule = new ValidatorCollection();
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_SUCCEED_2);
		assertTrue(rule.validate(null).isOK());

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_SUCCEED_1);
		assertFalse(rule.validate(null).isOK());

		rule = new ValidatorCollection();
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_FAIL_2);
		assertFalse(rule.validate(null).isOK());

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_FAIL_2);
		assertFalse(rule.validate(null).isOK());
	}

	public void testConcurrentModification() {
		// this test may not fail with a ConcurrentModificationException
		final ValidatorCollection rule = new ValidatorCollection();
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_SUCCEED_2);
		rule.add(ALWAYS_SUCCEED_2);
		rule.add(ALWAYS_SUCCEED_2);
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_FAIL_2);
		rule.add(ALWAYS_THROW_EXCEPTION);
		for (final IValidator validator : rule) {
			if (validator == ALWAYS_SUCCEED_1) {
				rule.remove(validator);
			}
		}
		assertEquals(4, rule.getValidators().size());

		for (final IValidator validator : rule.getValidators()) {
			if (validator == ALWAYS_SUCCEED_2) {
				rule.remove(validator);
			}
		}
		assertEquals(3, rule.getValidators().size());

		try {
			rule.iterator().remove();
			fail("the returned iterator may not support the remove() method, modification through rule#remove(IValidator) only.");
		} catch (final RuntimeException e) {
			ok("passed test");
		}

		try {
			rule.getValidators().clear();
			fail("the returned collection may not support the remove() method, modification through rule#remove(IValidator) only.");
		} catch (final RuntimeException e) {
			ok("passed test");
		}
	}

	public void testExceptions() {
		ValidatorCollection rule = new ValidatorCollection();
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		} catch (final RuntimeException e) {
			fail("expected a thrown ValidationFailure instead");
		}
		// two exception throwing rules
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		} catch (final RuntimeException e) {
			fail("expected a thrown ValidationFailure instead");
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		} catch (final RuntimeException e) {
			fail("expected a thrown ValidationFailure instead");
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_THROW_EXCEPTION);
		rule.add(ALWAYS_SUCCEED_1);
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		} catch (final RuntimeException e) {
			fail("expected a thrown ValidationFailure instead");
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		} catch (final RuntimeException e) {
			fail("expected a thrown ValidationFailure instead");
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		rule.add(ALWAYS_FAIL_1);
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		} catch (final RuntimeException e) {
			fail("expected a thrown ValidationFailure instead");
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		rule.add(ALWAYS_SUCCEED_1);
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		} catch (final RuntimeException e) {
			fail("expected a thrown ValidationFailure instead");
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		} catch (final RuntimeException e) {
			fail("expected a thrown ValidationFailure instead");
		}
	}

	public void testValidationCallback() {
		final ValidatorCollection rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_SUCCEED_1);

		final IValidationCallback callback = createMock(IValidationCallback.class);
		callback.validationRuleChecked(ALWAYS_FAIL_1, Status.CANCEL_STATUS);
		callback.validationRuleChecked(ALWAYS_SUCCEED_1, Status.OK_STATUS);
		callback.validationResult((IStatus) anyObject());
		replay(callback);

		rule.validate(new Object(), callback);

		verify(callback);
	}
}
