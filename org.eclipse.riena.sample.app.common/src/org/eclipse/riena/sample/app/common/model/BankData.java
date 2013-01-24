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
package org.eclipse.riena.sample.app.common.model;

/**
 * This class contains bank data
 */
public class BankData implements Cloneable {

	private String accountNumber;
	private String bank;
	private String bankCode;

	/**
	 * Creates empty bank data
	 */
	public BankData() {

		super();

	}

	/**
	 * Creates bank data and sets the given values
	 * 
	 * @param accountNumber
	 *            the accountNumber to set
	 * @param bank
	 *            the zip bank to set
	 * @param bankCode
	 *            the bankCode to set
	 */
	public BankData(final String accountNumber, final String bank, final String bankCode) {

		this();

		this.accountNumber = accountNumber;
		this.bank = bank;
		this.bankCode = bankCode;

	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof BankData)) {
			return false;
		}
		final BankData bdO = (BankData) other;
		if (bdO.accountNumber.equals(accountNumber) && bdO.bank.equals(bank) && bdO.bankCode.equals(bankCode)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return accountNumber.hashCode() + bank.hashCode() + bankCode.hashCode();
	}

	/**
	 * Returns the account number
	 * 
	 * @return account number
	 */
	public String getAccountNumber() {

		return accountNumber;

	}

	/**
	 * Sets the given account number
	 * 
	 * @param accountNumber
	 *            the account number to set
	 */
	public void setAccountNumber(final String accountNumber) {

		this.accountNumber = accountNumber;

	}

	/**
	 * Returns the name of the bank
	 * 
	 * @return name of bank
	 */
	public String getBank() {

		return bank;

	}

	/**
	 * Sets the given name of the bank
	 * 
	 * @param bank
	 *            the name of bank to set
	 */
	public void setBank(final String bank) {

		this.bank = bank;

	}

	/**
	 * Returns the bank code
	 * 
	 * @return the bankCode.
	 */
	public String getBankCode() {

		return bankCode;

	}

	/**
	 * Sets the given bank code
	 * 
	 * @param bankCode
	 *            the bank codd to set.
	 */
	public void setBankCode(final String bankCode) {

		this.bankCode = bankCode;

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String strg = ""; //$NON-NLS-1$
		if (accountNumber != null) {
			strg += accountNumber;
		} else {
			strg += "?"; //$NON-NLS-1$
		}

		strg += " - "; //$NON-NLS-1$

		if (bank != null) {
			strg += bank;
		} else {
			strg += "?"; //$NON-NLS-1$
		}

		strg += " - "; //$NON-NLS-1$

		if (bankCode != null) {
			strg += bankCode;
		} else {
			strg += "?"; //$NON-NLS-1$
		}

		return strg;

	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {

		super.clone();
		final BankData clone = new BankData();
		clone.setAccountNumber(getAccountNumber());
		clone.setBank(getBank());
		clone.setBankCode(getBankCode());

		return clone;

	}

}
