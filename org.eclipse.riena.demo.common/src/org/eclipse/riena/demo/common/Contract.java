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
package org.eclipse.riena.demo.common;

public class Contract {

	private String contractNo;
	private String description;
	private double contractValue;
	private String status;

	public Contract() {
		contractNo = ""; //$NON-NLS-1$
		description = ""; //$NON-NLS-1$
		status = ""; //$NON-NLS-1$
	}

	/**
	 * @param contractNo
	 * @param description
	 * @param contractValue
	 * @param status
	 */
	public Contract(final String contractNo, final String description, final double contractValue, final String status) {
		super();
		this.contractNo = contractNo;
		this.description = description;
		this.contractValue = contractValue;
		this.status = status;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(final String contractNo) {
		this.contractNo = contractNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public double getContractValue() {
		return contractValue;
	}

	public void setContractValue(final double contractValue) {
		this.contractValue = contractValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

}
