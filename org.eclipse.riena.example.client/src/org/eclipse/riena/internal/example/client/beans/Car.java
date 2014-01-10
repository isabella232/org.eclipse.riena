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
package org.eclipse.riena.internal.example.client.beans;

/**
 * A simple POJO to store informations about a car.
 */
public class Car {

	private final String make;
	private String model;
	private int power;
	private int capacity;
	private float speedup;
	private float milage;

	public Car(final String make) {
		super();
		this.make = make;
	}

	public Car(final String make, final String model, final int power, final int capacity, final float speedup,
			final float milage) {
		this(make);
		this.model = model;
		this.power = power;
		this.capacity = capacity;
		this.speedup = speedup;
		this.milage = milage;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(final String model) {
		this.model = model;
	}

	public int getPower() {
		return power;
	}

	public void setPower(final int power) {
		this.power = power;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(final int capacity) {
		this.capacity = capacity;
	}

	public float getSpeedup() {
		return speedup;
	}

	public void setSpeedup(final float speedup) {
		this.speedup = speedup;
	}

	public float getMilage() {
		return milage;
	}

	public void setMilage(final float milage) {
		this.milage = milage;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getMake());
		sb.append(" "); //$NON-NLS-1$
		sb.append(getModel());
		sb.append(": "); //$NON-NLS-1$
		sb.append(getPower() + "KW"); //$NON-NLS-1$
		sb.append(", "); //$NON-NLS-1$
		sb.append(getCapacity() + "ccm"); //$NON-NLS-1$
		sb.append(", "); //$NON-NLS-1$
		sb.append(getSpeedup() + "sec"); //$NON-NLS-1$
		sb.append(", "); //$NON-NLS-1$
		sb.append(getMilage() + "l/100km"); //$NON-NLS-1$
		return sb.toString();
	}

}
