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
package org.eclipse.riena.ui.ridgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.beans.common.Address;
import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.TestBean;
import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.core.marker.Markable;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.marker.ValidationMessageMarker;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.ui.ridgets.validation.MinLength;
import org.eclipse.riena.ui.ridgets.validation.ValidRange;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 * Tests for the ValueBindingSupport.
 */
@NonUITestCase
public class ValueBindingSupportTest extends RienaTestCase {

	private DefaultRealm realm;
	private ValueBindingSupport valueBindingSupport;
	private TestBean bean;
	private IObservableValue model;
	private IObservableValue target;
	private IMarkable markable;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();

		bean = new TestBean();
		model = PojoObservables.observeValue(bean, TestBean.PROPERTY);
		target = new WritableValue();

		valueBindingSupport = new ValueBindingSupport(target, model);

		markable = new Markable();
		valueBindingSupport.setMarkable(markable);
	}

	@Override
	protected void tearDown() throws Exception {
		realm.dispose();
		realm = null;
		super.tearDown();
	}

	public void testAddRemoveAfterGetValidator() throws Exception {
		assertTrue(valueBindingSupport.getAllValidators().getValidators().isEmpty());

		final MinLength validator = new MinLength(5);
		assertFalse(valueBindingSupport.addValidationRule(validator, ValidationTime.ON_UPDATE_TO_MODEL));
		assertEquals(1, valueBindingSupport.getAllValidators().getValidators().size());

		assertFalse(valueBindingSupport.removeValidationRule(validator));
		assertTrue(valueBindingSupport.getAllValidators().getValidators().isEmpty());
	}

	public void testAddRemoveAfterSetValidator() throws Exception {
		assertTrue(valueBindingSupport.getAllValidators().getValidators().isEmpty());

		final MinLength validator = new MinLength(5);
		assertFalse(valueBindingSupport.addValidationRule(validator, ValidationTime.AFTER_UPDATE_TO_MODEL));
		assertEquals(1, valueBindingSupport.getAllValidators().getValidators().size());

		assertFalse(valueBindingSupport.removeValidationRule(validator));
		assertTrue(valueBindingSupport.getAllValidators().getValidators().isEmpty());
	}

	public void testAddRemoveOnEditValidator() throws Exception {
		assertTrue(valueBindingSupport.getAllValidators().getValidators().isEmpty());

		final MinLength validator = new MinLength(5);
		assertTrue(valueBindingSupport.addValidationRule(validator, ValidationTime.ON_UI_CONTROL_EDIT));
		assertEquals(1, valueBindingSupport.getAllValidators().getValidators().size());

		assertTrue(valueBindingSupport.removeValidationRule(validator));
		assertTrue(valueBindingSupport.getAllValidators().getValidators().isEmpty());
	}

	public void testUpdateFromModelOnRequest() throws Exception {
		assertNull(target.getValue());

		bean.setProperty("TestValue"); //$NON-NLS-1$

		assertNull(target.getValue());

		valueBindingSupport.updateFromModel();

		assertEquals("TestValue", target.getValue()); //$NON-NLS-1$
	}

	public void testUpdateFromTargetImmediately() throws Exception {
		assertNull(bean.getProperty());

		target.setValue("TestValue"); //$NON-NLS-1$

		assertEquals("TestValue", bean.getProperty()); //$NON-NLS-1$
	}

	public void testValidationMessagesAddAndRemove() throws Exception {
		valueBindingSupport.addValidationRule(new EvenNumberOfCharacters(), ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationMessage("TestMessage1"); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage("TestMessage2"); //$NON-NLS-1$
		final ErrorMessageMarker messageMarker1 = new ErrorMessageMarker("TestMessage3"); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage(messageMarker1);
		final MessageMarker messageMarker2 = new MessageMarker("TestMessage4"); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage(messageMarker2);

		assertEquals(0, markable.getMarkers().size());

		target.setValue("odd"); //$NON-NLS-1$

		assertEquals(5, markable.getMarkers().size());
		assertMessageMarkers("TestMessage1", "TestMessage2", "TestMessage3", "TestMessage4"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		target.setValue("even"); //$NON-NLS-1$

		assertEquals(0, markable.getMarkers().size());

		valueBindingSupport.removeValidationMessage("TestMessage1"); //$NON-NLS-1$
		valueBindingSupport.removeValidationMessage(messageMarker1);

		target.setValue("odd"); //$NON-NLS-1$

		assertEquals(3, markable.getMarkers().size());
		assertMessageMarkers("TestMessage2", "TestMessage4"); //$NON-NLS-1$ //$NON-NLS-2$

		target.setValue("even"); //$NON-NLS-1$

		assertEquals(0, markable.getMarkers().size());

		valueBindingSupport.removeValidationMessage("TestMessage2"); //$NON-NLS-1$
		valueBindingSupport.removeValidationMessage(messageMarker2);

		target.setValue("odd"); //$NON-NLS-1$

		assertEquals(1, markable.getMarkers().size());
		assertTrue(markable.getMarkers().iterator().next() instanceof ErrorMarker);

		target.setValue("even"); //$NON-NLS-1$

		assertEquals(0, markable.getMarkers().size());
	}

	public void testAddValidationMessageForUnknownRule() {
		final AlwaysWrongValidator rule = new AlwaysWrongValidator();
		valueBindingSupport.addValidationMessage("foo", rule); //$NON-NLS-1$

		assertEquals(0, markable.getMarkers().size());
	}

	public void testRemoveValidationMessageWhenRemovingRule() {
		final IValidator rule = new AlwaysWrongValidator();
		valueBindingSupport.addValidationMessage("foo", rule); //$NON-NLS-1$
		valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UPDATE_TO_MODEL);

		target.setValue("value"); //$NON-NLS-1$

		assertEquals(1, markable.getMarkersOfType(ValidationMessageMarker.class).size());

		valueBindingSupport.removeValidationRule(rule);

		assertEquals(0, markable.getMarkersOfType(ValidationMessageMarker.class).size());
	}

	/**
	 * Tests that adding the same validation several times has no effect
	 */
	public void testAddSameValidationMessage() {
		final EvenNumberOfCharacters rule = new EvenNumberOfCharacters();
		valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationMessage("TestMessage1"); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage("TestMessage1"); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage("TestMessage2", rule); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage("TestMessage2", rule); //$NON-NLS-1$
		final MessageMarker messageMarker = new MessageMarker("TestMessage3"); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage(messageMarker);
		valueBindingSupport.addValidationMessage(messageMarker);
		final MessageMarker messageMarker2 = new MessageMarker("TestMessage4"); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage(messageMarker2, rule);
		valueBindingSupport.addValidationMessage(messageMarker2, rule);

		assertEquals(0, markable.getMarkers().size());

		target.setValue("odd"); //$NON-NLS-1$

		assertEquals(4, markable.getMarkersOfType(ValidationMessageMarker.class).size());

		target.setValue("even"); //$NON-NLS-1$

		assertEquals(0, markable.getMarkers().size());
	}

	public void testValidationMessagesAddAndRemoveWhileActive() throws Exception {
		valueBindingSupport.addValidationRule(new EvenNumberOfCharacters(), ValidationTime.ON_UPDATE_TO_MODEL);
		target.setValue("odd"); //$NON-NLS-1$

		assertEquals(1, markable.getMarkers().size());
		assertTrue(markable.getMarkers().iterator().next() instanceof ErrorMarker);

		valueBindingSupport.addValidationMessage("TestMessage1"); //$NON-NLS-1$

		assertEquals(2, markable.getMarkers().size());
		assertMessageMarkers("TestMessage1"); //$NON-NLS-1$

		final MessageMarker messageMarker = new MessageMarker("TestMessage2"); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage(messageMarker);

		assertEquals(3, markable.getMarkers().size());
		assertMessageMarkers("TestMessage1", "TestMessage2"); //$NON-NLS-1$ //$NON-NLS-2$

		valueBindingSupport.removeValidationMessage("TestMessage1"); //$NON-NLS-1$

		assertEquals(2, markable.getMarkers().size());
		assertMessageMarkers("TestMessage2"); //$NON-NLS-1$

		valueBindingSupport.removeValidationMessage(messageMarker);

		assertEquals(1, markable.getMarkers().size());
		assertTrue(markable.getMarkers().iterator().next() instanceof ErrorMarker);
	}

	public void testSpecialValidationMessages() throws Exception {
		final EvenNumberOfCharacters evenNumberOfCharacters = new EvenNumberOfCharacters();
		final NotEndingWithDisaster notEndingWithDisaster = new NotEndingWithDisaster();
		valueBindingSupport.addValidationRule(evenNumberOfCharacters, ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationRule(notEndingWithDisaster, ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationMessage("TestNotEvenMessage1", evenNumberOfCharacters); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage(new MessageMarker("TestNotEvenMessage2"), evenNumberOfCharacters); //$NON-NLS-1$
		valueBindingSupport.addValidationMessage("TestDisasterMessage", notEndingWithDisaster); //$NON-NLS-1$

		assertEquals(0, markable.getMarkers().size());

		target.setValue("Disaster"); //$NON-NLS-1$

		assertEquals(2, markable.getMarkers().size());
		assertMessageMarkers("TestDisasterMessage"); //$NON-NLS-1$

		target.setValue("Disaster Area"); //$NON-NLS-1$

		assertEquals(3, markable.getMarkers().size());
		assertMessageMarkers("TestNotEvenMessage1", "TestNotEvenMessage2"); //$NON-NLS-1$ //$NON-NLS-2$

		target.setValue("We are teetering on the brink of disaster"); //$NON-NLS-1$

		assertEquals(5, markable.getMarkers().size());
		assertMessageMarkers("TestNotEvenMessage1", "TestNotEvenMessage2", "TestDisasterMessage"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		target.setValue("Save again"); //$NON-NLS-1$

		assertEquals(0, markable.getMarkers().size());
	}

	public void testValidationRuleAddAndRemove() {
		final IValidator rule = new EvenNumberOfCharacters();

		final boolean isOnEdit1 = valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UI_CONTROL_EDIT);

		assertTrue(isOnEdit1);
		assertTrue(valueBindingSupport.getOnEditValidators().contains(rule));
		assertFalse(valueBindingSupport.getAfterGetValidators().contains(rule));

		final boolean isOnEdit2 = valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UPDATE_TO_MODEL);

		assertFalse(isOnEdit2);
		assertTrue(valueBindingSupport.getOnEditValidators().contains(rule));
		assertTrue(valueBindingSupport.getAfterGetValidators().contains(rule));

		valueBindingSupport.removeValidationRule(rule);

		assertFalse(valueBindingSupport.getOnEditValidators().contains(rule));
		assertFalse(valueBindingSupport.getAfterGetValidators().contains(rule));
	}

	public void testValidationRuleAddAndRemoveNull() {
		try {
			valueBindingSupport.addValidationRule(null, ValidationTime.ON_UPDATE_TO_MODEL);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		final boolean result = valueBindingSupport.removeValidationRule(null);
		assertFalse(result);
	}

	public void testAddAndRemoveValidationMessageWithRule() {
		assertEquals(0, markable.getMarkersOfType(ValidationMessageMarker.class).size());

		final MinLength rule = new MinLength(3);
		valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationMessage("too short", rule); //$NON-NLS-1$
		target.setValue("a"); //$NON-NLS-1$

		assertEquals(1, markable.getMarkersOfType(ValidationMessageMarker.class).size());
		assertMessageMarkers("too short"); //$NON-NLS-1$

		valueBindingSupport.removeValidationMessage("too short", rule); //$NON-NLS-1$

		assertEquals(0, markable.getMarkersOfType(ValidationMessageMarker.class).size());
	}

	public void testAddAndRemoveValidationMessageWithoutRule() {
		assertEquals(0, markable.getMarkersOfType(ValidationMessageMarker.class).size());

		final MinLength rule = new MinLength(3);
		valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationMessage("too short"); //$NON-NLS-1$
		target.setValue("a"); //$NON-NLS-1$

		assertEquals(1, markable.getMarkersOfType(ValidationMessageMarker.class).size());
		assertMessageMarkers("too short"); //$NON-NLS-1$

		valueBindingSupport.removeValidationMessage("too short"); //$NON-NLS-1$

		assertEquals(0, markable.getMarkersOfType(ValidationMessageMarker.class).size());
	}

	/**
	 * As per Bug 289458
	 */
	public void testShowErrorAfterValidationOnUpdateWithBlock() {
		final ValidRange rule = new ValidRange(18, 80);
		valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UPDATE_TO_MODEL);
		final IStatus errorStatus = rule.validate("81"); //$NON-NLS-1$
		assertEquals(ValidationRuleStatus.ERROR_BLOCK_WITH_FLASH, errorStatus.getCode());

		assertEquals(0, markable.getMarkersOfType(ErrorMarker.class).size());

		valueBindingSupport.updateValidationStatus(rule, errorStatus);

		assertEquals(1, markable.getMarkersOfType(ErrorMarker.class).size());

		final IStatus okStatus = rule.validate("80"); //$NON-NLS-1$
		valueBindingSupport.updateValidationStatus(rule, okStatus);

		assertEquals(0, markable.getMarkersOfType(ErrorMarker.class).size());
	}

	/**
	 * As per Bug 313969
	 */
	public void testUsesBeanObservablesWhenBindingABean() {
		final Person person = new Person("Max", "Muster"); //$NON-NLS-1$ //$NON-NLS-2$
		final Address pdx = createAddress("pdx", "Portland", "97209", "USA"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		final Address fra = createAddress("fra", "Frankfurt", "60329", "DE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		person.setAddress(pdx);

		valueBindingSupport.bindToModel(person, "address.streetAndNumber"); //$NON-NLS-1$

		assertEquals("pdx", person.getAddress().getStreetAndNumber()); //$NON-NLS-1$
		assertEquals("pdx", pdx.getStreetAndNumber()); //$NON-NLS-1$

		target.setValue("new 1"); //$NON-NLS-1$

		assertEquals("new 1", person.getAddress().getStreetAndNumber()); //$NON-NLS-1$
		assertEquals("new 1", pdx.getStreetAndNumber()); //$NON-NLS-1$

		person.setAddress(fra);
		target.setValue("new 2"); //$NON-NLS-1$

		assertEquals("new 2", person.getAddress().getStreetAndNumber()); //$NON-NLS-1$
		assertEquals("new 2", fra.getStreetAndNumber()); //$NON-NLS-1$
		assertEquals("new 1", pdx.getStreetAndNumber()); //$NON-NLS-1$
	}

	public void testUpdateFromModelOnRequest2() {
		final Person person = new Person("muster", "max"); //$NON-NLS-1$ //$NON-NLS-2$
		valueBindingSupport.bindToModel(person, "firstname"); //$NON-NLS-1$
		valueBindingSupport.updateFromModel();

		assertEquals("max", person.getFirstname()); //$NON-NLS-1$
		assertEquals("max", target.getValue()); //$NON-NLS-1$

		target.setValue("moritz"); //$NON-NLS-1$

		assertEquals("moritz", person.getFirstname()); //$NON-NLS-1$
		assertEquals("moritz", target.getValue()); //$NON-NLS-1$

		person.setFirstname("michel"); //$NON-NLS-1$

		assertEquals("michel", person.getFirstname()); //$NON-NLS-1$
		assertEquals("moritz", target.getValue()); //$NON-NLS-1$

		valueBindingSupport.updateFromModel();

		assertEquals("michel", person.getFirstname()); //$NON-NLS-1$
		assertEquals("michel", target.getValue()); //$NON-NLS-1$
	}

	/**
	 * Tests the <i>private</i> method {@code isNestedProperty(String)}.
	 */
	public void testIsNestedProperty() {

		boolean ret = ReflectionUtils.invokeHidden(valueBindingSupport, "isNestedProperty", ""); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(ret);

		ret = ReflectionUtils.invokeHidden(valueBindingSupport, "isNestedProperty", "prop"); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(ret);

		ret = ReflectionUtils.invokeHidden(valueBindingSupport, "isNestedProperty", "prop1.prop2"); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(ret);

		ret = ReflectionUtils.invokeHidden(valueBindingSupport, "isNestedProperty", "."); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(ret);

	}

	// helping methods
	// ////////////////

	private void assertMessageMarkers(final String... messages) {
		final Collection<String> missingMessages = new ArrayList<String>(Arrays.asList(messages));

		for (final IMessageMarker messageMarker : markable.getMarkersOfType(IMessageMarker.class)) {
			missingMessages.remove(messageMarker.getMessage());
		}

		assertTrue("missing MessageMarker for " + missingMessages, missingMessages.isEmpty()); //$NON-NLS-1$
	}

	public static Address createAddress(final String str, final String city, final String zip, final String cc) {
		final Address result = new Address();
		result.setStreetAndNumber(str);
		result.setTown(city);
		result.setPostalCode(Integer.valueOf(zip));
		result.setCountry(cc);
		return result;
	}

	// helping clases
	// ///////////////

	private static class EvenNumberOfCharacters implements IValidator {
		public IStatus validate(final Object value) {
			if (value == null) {
				return ValidationRuleStatus.ok();
			}
			if (value instanceof String) {
				final String string = (String) value;
				if (string.length() % 2 == 0) {
					return ValidationRuleStatus.ok();
				}
				return ValidationRuleStatus.error(false, "Odd number of characters."); //$NON-NLS-1$
			}
			throw new ValidationFailure(getClass().getName() + " can only validate objects of type " //$NON-NLS-1$
					+ String.class.getName());
		}

	}

	private static class NotEndingWithDisaster implements IValidator {
		public IStatus validate(final Object value) {
			if (value == null) {
				return ValidationRuleStatus.ok();
			}
			if (value instanceof String) {
				final String string = (String) value;
				if (!string.toLowerCase().endsWith("disaster")) { //$NON-NLS-1$
					return ValidationRuleStatus.ok();
				}
				return ValidationRuleStatus.error(false, "It ends with disaster."); //$NON-NLS-1$
			}
			throw new ValidationFailure(getClass().getName() + " can only validate objects of type " //$NON-NLS-1$
					+ String.class.getName());
		}
	}

	private static final class AlwaysWrongValidator implements IValidator {
		public IStatus validate(final Object value) {
			return ValidationRuleStatus.error(false, "wrong"); //$NON-NLS-1$
		}
	}

}
