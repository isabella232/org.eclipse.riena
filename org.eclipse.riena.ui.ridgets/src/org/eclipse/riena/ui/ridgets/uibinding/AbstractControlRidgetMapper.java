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
package org.eclipse.riena.ui.ridgets.uibinding;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.BindingException;

import org.eclipse.riena.ui.ridgets.ClassRidgetMapper;
import org.eclipse.riena.ui.ridgets.IRidget;

/**
 *
 */
public class AbstractControlRidgetMapper implements IControlRidgetMapper<Object> {

	private final List<Mapping> mappings = new ArrayList<Mapping>();

	public void addMapping(final Class<? extends Object> controlClazz, final Class<? extends IRidget> ridgetClazz) {
		final Mapping mapping = new Mapping(controlClazz, ridgetClazz);
		addMapping(mapping);
		addMappingToClassRidgetMapper(ridgetClazz);
	}

	/**
	 * Adds a mapping of a UI control-class to a ridget-class. The mapping will only apply when the given condition evaluates to true.
	 * <p>
	 * Example:
	 * <p>
	 * {@code addMapping(Tree.class, TreeRidget.class, new
	 * TreeWithoutColumnsCondition());}
	 * <p>
	 * Adding the same mapping twice has no effect (but is possible).
	 * 
	 * @param controlClazz
	 *            the class of the UI control (<code>Object</code>)
	 * @param ridgetClazz
	 *            the class of the ridget
	 * @param condition
	 *            the condition to evaluate (non-null)
	 * @see IMappingCondition
	 */
	public void addMapping(final Class<? extends Object> controlClazz, final Class<? extends IRidget> ridgetClazz, final IMappingCondition condition) {
		final Mapping mapping = new Mapping(controlClazz, ridgetClazz, condition);
		addMapping(mapping);
		addMappingToClassRidgetMapper(ridgetClazz);
	}

	public void addMapping(final Mapping mapping) {
		mappings.add(mapping);
	}

	public List<Mapping> getMapping() {
		return mappings;
	}

	private void addMappingToClassRidgetMapper(final Class<? extends IRidget> ridgetClazz) {
		if (!Modifier.isAbstract(ridgetClazz.getModifiers())) {
			ClassRidgetMapper.getInstance().addMapping(getPrimaryRidgetInterface(ridgetClazz), ridgetClazz);
		}
	}

	public Class<? extends IRidget> getRidgetClass(final Class<? extends Object> controlClazz) {
		for (final Mapping mapping : getMapping()) {
			if (mapping.isMatching(controlClazz)) {
				return mapping.getRidgetClazz();
			}
		}
		throw new BindingException("No Ridget class defined for widget class " + controlClazz.getSimpleName()); //$NON-NLS-1$
	}

	public Class<? extends IRidget> getRidgetClass(final Object control) {
		// first look for matching mappings with condition
		// TODO: to optimize avoid double iteration over mappings
		for (final Mapping mapping : getMapping()) {
			if ((mapping.hasCondition()) && mapping.isMatching(control)) {
				return mapping.getRidgetClazz();
			}
		}
		// then look for matching mappings without condition
		for (final Mapping mapping : getMapping()) {
			if (!mapping.hasCondition() && mapping.isMatching(control)) {
				return mapping.getRidgetClazz();
			}
		}
		return getRidgetClass(control.getClass());
	}

	/**
	 * Finds the Primary-Interface for a given Ridget-Class. The Primary-Interface is the one that is used to represent a specific Ridget e.g. LabelRidget =>
	 * ILabelRidget. This Method tries to find a Interface that extends IRidget, if nothing was found in the current class, it searches the superclasses
	 * recursively.
	 * 
	 * @param ridgetClass
	 * @return the Primary-Interface extending IRidget or null if nothing was found
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends IRidget> getPrimaryRidgetInterface(final Class<? extends IRidget> ridgetClass) {
		if (ridgetClass == null || ridgetClass.isInterface()) {
			return null;
		}

		for (final Class<?> inf : ridgetClass.getInterfaces()) {
			if (IRidget.class.isAssignableFrom(inf)) {
				return (Class<? extends IRidget>) inf;
			}
		}
		return getPrimaryRidgetInterface((Class<? extends IRidget>) ridgetClass.getSuperclass());
	}

	public static final class Mapping {

		private final Class<? extends Object> controlClazz;
		private final Class<? extends IRidget> ridgetClazz;
		private final IMappingCondition condition;

		/**
		 * Create a new mapping of UI control and ridget.
		 * 
		 * @param controlClazz
		 *            the class of the UI control
		 * @param ridgetClazz
		 *            the class of the ridget
		 */
		public Mapping(final Class<? extends Object> controlClazz, final Class<? extends IRidget> ridgetClazz) {
			this(controlClazz, ridgetClazz, null);
		}

		public Mapping(final Class<? extends Object> controlClazz, final Class<? extends IRidget> ridgetClazz, final IMappingCondition condition) {
			this.controlClazz = controlClazz;
			this.ridgetClazz = ridgetClazz;
			this.condition = condition;
		}

		/**
		 * Checks if this mapping is for given UI control.
		 * 
		 * @param control
		 *            the UI control-class
		 * @return true, if the control matches; otherwise false
		 */
		public boolean isMatching(final Class<? extends Object> controlClazz) {
			if (condition == null) {
				return getControlClazz().isAssignableFrom(controlClazz);
			} else {
				return false;
			}
		}

		/**
		 * Checks if this mapping is for given UI control.
		 * 
		 * @param control
		 *            the UI control
		 * @return true, if the control matches; otherwise false
		 */
		public boolean isMatching(final Object control) {
			if (control.getClass() != getControlClazz()) {
				return false;
			}
			if (condition != null && !condition.isMatch(control)) {
				return false;
			}
			return true;

		}

		public Class<? extends IRidget> getRidgetClazz() {
			return ridgetClazz;
		}

		// helping methods
		// ////////////////

		private boolean hasCondition() {
			return getCondition() != null;
		}

		private Class<?> getControlClazz() {
			return controlClazz;
		}

		public IMappingCondition getCondition() {
			return condition;
		}

	}

}
