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
package org.eclipse.riena.demo.client.controllers;

import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.demo.common.Contract;
import org.eclipse.riena.demo.common.Customer;
import org.eclipse.riena.navigation.annotation.OnNavigationNodeEvent;
import org.eclipse.riena.navigation.annotation.OnNavigationNodeEvent.Event;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.AbstractMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.NumberColumnFormatter;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;

/**
 *
 */
public class CustomerContractController extends SubModuleController {

	@Override
	public void configureRidgets() {
		final Customer customer = (Customer) getNavigationNode().getParent().getContext("demo.customer"); //$NON-NLS-1$

		final ITextRidget firstName = getRidget(ITextRidget.class, "firstname"); //$NON-NLS-1$
		firstName.bindToModel(customer, "firstName"); //$NON-NLS-1$
		firstName.setMandatory(true);
		final ITextRidget lastName = getRidget(ITextRidget.class, "lastname"); //$NON-NLS-1$
		lastName.setMandatory(true);
		lastName.bindToModel(customer, "lastName"); //$NON-NLS-1$

		final IMasterDetailsRidget master = getRidget(IMasterDetailsRidget.class, "contracts"); //$NON-NLS-1$
		master.setDelegate(new ContractDelegate());
		final String[] properties = new String[] { "contractNo", "contractValue", "status" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final String[] headers = new String[] { "ContractNo", "Value in Euro", "Status" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		master.bindToModel(new WritableList(customer.getContracts(), Contract.class), Contract.class, properties, headers);

		final ITableRidget table = master.getRidget(ITableRidget.class, MasterDetailsComposite.BIND_ID_TABLE);
		table.setColumnFormatter(1, new NumberColumnFormatter(Double.class, 2) {
			@Override
			protected Number getValue(final Object element) {
				return ((Contract) element).getContractValue();
			}
		});
	}

	@OnNavigationNodeEvent(event = Event.ACTIVATED)
	protected void activated() {
		updateAllRidgetsFromModel();
	}

	public static final class ContractDelegate extends AbstractMasterDetailsDelegate {

		private final Contract workingCopy = createWorkingCopy();

		// configure ridgets for the details
		public void configureRidgets(final IRidgetContainer container) {

			final ITextRidget txtcontractNo = container.getRidget(ITextRidget.class, "contractno"); //$NON-NLS-1$
			txtcontractNo.setMandatory(true);
			txtcontractNo.bindToModel(workingCopy, "contractNo"); //$NON-NLS-1$
			txtcontractNo.updateFromModel();

			final ITextRidget txtLast = container.getRidget(ITextRidget.class, "description"); //$NON-NLS-1$
			txtLast.bindToModel(workingCopy, "description"); //$NON-NLS-1$
			txtLast.updateFromModel();

			final ITextRidget txtValue = container.getRidget(ITextRidget.class, "value"); //$NON-NLS-1$
			txtValue.setMandatory(true);
			txtValue.bindToModel(workingCopy, "contractValue"); //$NON-NLS-1$
			txtValue.updateFromModel();

			final ITextRidget txtStatus = container.getRidget(ITextRidget.class, "status"); //$NON-NLS-1$
			txtStatus.bindToModel(workingCopy, "status"); //$NON-NLS-1$
			txtStatus.updateFromModel();
		}

		public Contract createWorkingCopy() {
			return new Contract();
		}

		public Contract copyBean(final Object source, final Object target) {
			final Contract from = source != null ? (Contract) source : createWorkingCopy();
			final Contract to = target != null ? (Contract) target : createWorkingCopy();
			to.setContractNo(from.getContractNo());
			to.setDescription(from.getDescription());
			to.setContractValue(from.getContractValue());
			to.setStatus(from.getStatus());
			return to;
		}

		public Object getWorkingCopy() {
			return workingCopy;
		}

		@Override
		public boolean isChanged(final Object source, final Object target) {
			final Contract source2 = (Contract) source;
			final Contract target2 = (Contract) target;
			return !(source2.getContractNo().equals(target2.getContractNo()) && source2.getContractValue() == target2.getContractValue()
					&& source2.getDescription().equals(target2.getDescription()) && source2.getStatus().equals(target2.getStatus()));
		}
	}

}
