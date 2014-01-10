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
package org.eclipse.riena.example.client.controllers;

import java.text.NumberFormat;

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ICustomMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * This controller demonstrates the usage of custom markers.
 */
public class CustomMarkerSubModuleController extends SubModuleController {

	private static final ICustomMarker CUSTOM_OUTPUT_MARKER = new CustomOutputMarker();
	private static final ICustomMarker CUSTOM2_OUTPUT_MARKER = new Custom2OutputMarker();
	private static final ICustomMarker CUSTOM_MANDATORY_MARKER = new CustomMandatoryMarker();
	private static final ICustomMarker CUSTOM2_MANDATORY_MARKER = new Custom2MandatoryMarker();

	private ITextRidget textName;
	private IDecimalTextRidget textPrice;
	private INumericTextRidget textAmount;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configureRidgets() {

		textName = getRidget(ITextRidget.class, "textName"); //$NON-NLS-1$
		textName.setText("Chateau Schaedelbrummer"); //$NON-NLS-1$

		textPrice = getRidget(IDecimalTextRidget.class, "textPrice"); //$NON-NLS-1$
		textPrice.setGrouping(true);
		textPrice.setText(NumberFormat.getInstance().format(-29.99));

		textAmount = getRidget(INumericTextRidget.class, "textAmount"); //$NON-NLS-1$
		textAmount.setSigned(false);
		textAmount.setGrouping(true);
		textAmount.setText("1001"); //$NON-NLS-1$

		final IRidget[] markables = new IRidget[] { textName, textPrice, textAmount };

		final IToggleButtonRidget checkOutput = getRidget(IToggleButtonRidget.class, "checkOutput"); //$NON-NLS-1$
		checkOutput.addListener(new OutputActionListener(markables, checkOutput));
		final IToggleButtonRidget customCheckOutput = getRidget(IToggleButtonRidget.class, "customCheckOutput"); //$NON-NLS-1$
		customCheckOutput.addListener(new CustomOutputActionListener(markables, customCheckOutput));
		final IToggleButtonRidget custom2CheckOutput = getRidget(IToggleButtonRidget.class, "custom2CheckOutput"); //$NON-NLS-1$
		custom2CheckOutput.addListener(new Custom2OutputActionListener(markables, custom2CheckOutput));

		final IToggleButtonRidget checkMandatory = getRidget(IToggleButtonRidget.class, "checkMandatory"); //$NON-NLS-1$
		checkMandatory.addListener(new MandatoryActionListener(markables, checkMandatory));
		final IToggleButtonRidget customCheckMandatory = getRidget(IToggleButtonRidget.class, "customCheckMandatory"); //$NON-NLS-1$
		customCheckMandatory.addListener(new CustomMandatoryActionListener(markables, customCheckMandatory));
		final IToggleButtonRidget custom2CheckMandatory = getRidget(IToggleButtonRidget.class, "custom2CheckMandatory"); //$NON-NLS-1$
		custom2CheckMandatory.addListener(new Custom2MandatoryActionListener(markables, custom2CheckMandatory));

	}

	private void clearContent() {
		textName.setText(""); //$NON-NLS-1$
		textPrice.setText(""); //$NON-NLS-1$
		textAmount.setText(null);
	}

	/**
	 * Adds or removes a custom marker.
	 */
	private static abstract class AbstractCustomActionListener implements IActionListener {

		private final IRidget[] markables;
		private final IToggleButtonRidget checkButton;

		protected AbstractCustomActionListener(final IRidget[] markables, final IToggleButtonRidget checkButton) {
			this.markables = markables;
			this.checkButton = checkButton;
		}

		protected abstract ICustomMarker getMarker();

		public void callback() {
			final boolean isSelected = checkButton.isSelected();
			for (final IRidget ridget : markables) {
				if (ridget instanceof IMarkableRidget) {
					final IMarkableRidget markableRidget = (IMarkableRidget) ridget;
					if (isSelected) {
						markableRidget.addMarker(getMarker());
					} else {
						markableRidget.removeMarker(getMarker());
					}
				} else {
					final String name = ridget.getClass().getSimpleName();
					System.out.println("No output marker support on " + name); //$NON-NLS-1$
				}
			}
		}

	}

	/**
	 * Adds or remove a custom OutputMarker.
	 */
	private static class CustomOutputActionListener extends AbstractCustomActionListener {

		protected CustomOutputActionListener(final IRidget[] markables, final IToggleButtonRidget checkButton) {
			super(markables, checkButton);
		}

		@Override
		protected ICustomMarker getMarker() {
			return CUSTOM_OUTPUT_MARKER;
		}

	}

	/**
	 * Adds or remove a custom OutputMarker.
	 */
	private static class Custom2OutputActionListener extends CustomOutputActionListener {

		protected Custom2OutputActionListener(final IRidget[] markables, final IToggleButtonRidget checkButton) {
			super(markables, checkButton);
		}

		@Override
		protected ICustomMarker getMarker() {
			return CUSTOM2_OUTPUT_MARKER;
		}

	}

	/**
	 * Adds or remove a custom MandatroyMarker.
	 */
	private class CustomMandatoryActionListener extends AbstractCustomActionListener {

		protected CustomMandatoryActionListener(final IRidget[] markables, final IToggleButtonRidget checkButton) {
			super(markables, checkButton);
		}

		@Override
		protected ICustomMarker getMarker() {
			return CUSTOM_MANDATORY_MARKER;
		}

		@Override
		public void callback() {
			clearContent();
			super.callback();
		}

	}

	/**
	 * Adds or remove a custom MandatroyMarker.
	 */
	private class Custom2MandatoryActionListener extends CustomMandatoryActionListener {

		protected Custom2MandatoryActionListener(final IRidget[] markables, final IToggleButtonRidget checkButton) {
			super(markables, checkButton);
		}

		@Override
		protected ICustomMarker getMarker() {
			return CUSTOM2_MANDATORY_MARKER;
		}

	}

	/**
	 * Toggles the flag <i>mandatory</i>.
	 */
	private static final class MandatoryActionListener implements IActionListener {

		private final IRidget[] markables;
		private final IToggleButtonRidget checkMandatory;

		private MandatoryActionListener(final IRidget[] markables, final IToggleButtonRidget checkMandatory) {
			this.markables = markables;
			this.checkMandatory = checkMandatory;
		}

		public void callback() {
			final boolean isMandatory = checkMandatory.isSelected();
			for (final IRidget ridget : markables) {
				if (ridget instanceof IMarkableRidget) {
					((IMarkableRidget) ridget).setMandatory(isMandatory);
				} else {
					final String name = ridget.getClass().getSimpleName();
					System.out.println("No output marker support on " + name); //$NON-NLS-1$
				}
			}
		}

	}

	/**
	 * Toggles the flag <i>outputOnly</i>.
	 */
	private static final class OutputActionListener implements IActionListener {

		private final IRidget[] markables;
		private final IToggleButtonRidget checkOutput;

		private OutputActionListener(final IRidget[] markables, final IToggleButtonRidget checkOutput) {
			this.markables = markables;
			this.checkOutput = checkOutput;
		}

		public void callback() {
			final boolean isOutput = checkOutput.isSelected();
			for (final IRidget ridget : markables) {
				if (ridget instanceof IMarkableRidget) {
					((IMarkableRidget) ridget).setOutputOnly(isOutput);
				} else {
					final String name = ridget.getClass().getSimpleName();
					System.out.println("No output marker support on " + name); //$NON-NLS-1$
				}
			}
		}

	}

	/**
	 * Custom OutputMarker
	 */
	private static class CustomOutputMarker extends OutputMarker implements ICustomMarker {

		public Object getBackground(final IMarkable markable) {
			if (markable.getMarkersOfType(MandatoryMarker.class).isEmpty()) {
				return LnfManager.getLnf().getColor("lightGray"); //$NON-NLS-1$
			} else {
				return LnfManager.getLnf().getColor("orange"); //$NON-NLS-1$
			}
		}

		public Object getForeground(final IMarkable markable) {
			return LnfManager.getLnf().getColor("white"); //$NON-NLS-1$
		}

	}

	/**
	 * Custom OutputMarker
	 */
	private static class Custom2OutputMarker extends OutputMarker implements ICustomMarker {

		public Object getBackground(final IMarkable markable) {
			return LnfManager.getLnf().getColor("darkGray"); //$NON-NLS-1$
		}

		public Object getForeground(final IMarkable markable) {
			return LnfManager.getLnf().getColor("yellow"); //$NON-NLS-1$
		}

	}

	/**
	 * Custom MandatoryMarker
	 */
	private static class CustomMandatoryMarker extends MandatoryMarker implements ICustomMarker {

		public Object getBackground(final IMarkable markable) {
			return LnfManager.getLnf().getColor("pink"); //$NON-NLS-1$
		}

		public Object getForeground(final IMarkable markable) {
			return null;
		}

	}

	/**
	 * Custom MandatoryMarker
	 */
	private static class Custom2MandatoryMarker extends MandatoryMarker implements ICustomMarker {

		public Object getBackground(final IMarkable markable) {
			return LnfManager.getLnf().getColor("red"); //$NON-NLS-1$
		}

		public Object getForeground(final IMarkable markable) {
			return null;
		}

	}

}
