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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.beans.common.TestBean;
import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.example.client.views.MarkerSubModuleView;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.AttentionMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ICComboRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ICompletionComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.IGroupedTreeTableRidget;
import org.eclipse.riena.ui.ridgets.ILinkRidget;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.swt.RienaMessageDialog;

/**
 * Controller for the {@link MarkerSubModuleView} example.
 */
public class MarkerSubModuleController extends SubModuleController {

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		final ITextRidget textName = getRidget(ITextRidget.class, "textName"); //$NON-NLS-1$
		textName.setText("Chateau Schaedelbrummer"); //$NON-NLS-1$

		final IDecimalTextRidget textPrice = getRidget(IDecimalTextRidget.class, "textPrice"); //$NON-NLS-1$
		textPrice.setGrouping(true);
		textPrice.setText(NumberFormat.getInstance().format(-29.99));

		final INumericTextRidget textAmount = getRidget(INumericTextRidget.class, "textAmount"); //$NON-NLS-1$
		textAmount.setSigned(false);
		textAmount.setGrouping(true);
		textAmount.setText("1001"); //$NON-NLS-1$

		final IDateTextRidget textDate = getRidget(IDateTextRidget.class, "textDate"); //$NON-NLS-1$
		textDate.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		textDate.setText("04.12.2008"); //$NON-NLS-1$

		final IDateTimeRidget dtDate = getRidget(IDateTimeRidget.class, "dtDate"); //$NON-NLS-1$
		final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
		try {
			final Date date = dateFormat.parse("04.12.2008"); //$NON-NLS-1$
			dtDate.setDate(date);
		} catch (final ParseException e) {
			dtDate.setDate(new Date());
		}

		final IDateTextRidget dtPicker = getRidget(IDateTextRidget.class, "dtPicker"); //$NON-NLS-1$
		dtPicker.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		dtPicker.setText("04.12.2008"); //$NON-NLS-1$

		final IComboRidget comboAge = getRidget(IComboRidget.class, "comboAge"); //$NON-NLS-1$
		final List<String> ages = Arrays.asList(new String[] { "<none>", "young", "moderate", "aged", "old" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		comboAge.bindToModel(new WritableList(ages, String.class), String.class, null, new WritableValue());
		comboAge.updateFromModel();
		comboAge.setEmptySelectionItem("<none>"); //$NON-NLS-1$
		comboAge.setSelection(1);

		final ICompletionComboRidget comboStyle = getRidget(ICompletionComboRidget.class, "comboStyle"); //$NON-NLS-1$
		final List<String> styles = Arrays.asList(new String[] { "<none>", "Bordeaux", "Beaujolaix", "Merlot", "Pinot Noire", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"Syrah" }); //$NON-NLS-1$
		comboStyle.bindToModel(new WritableList(styles, String.class), String.class, null, new WritableValue());
		comboStyle.updateFromModel();
		comboStyle.setEmptySelectionItem("<none>"); //$NON-NLS-1$
		comboStyle.setSelection(1);

		final ICComboRidget comboSize = getRidget(ICComboRidget.class, "ccomboSize"); //$NON-NLS-1$
		final List<String> sizes = Arrays.asList(new String[] { "<none>", "xs", "s", "m", "l", "xl" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		comboSize.bindToModel(new WritableList(sizes, String.class), String.class, null, new WritableValue());
		comboSize.updateFromModel();
		comboSize.setEmptySelectionItem("<none>"); //$NON-NLS-1$
		comboSize.setSelection(1);

		final ISingleChoiceRidget choiceType = getRidget(ISingleChoiceRidget.class, "choiceType"); //$NON-NLS-1$
		choiceType.bindToModel(Arrays.asList("red", "white", "rose"), (List<String>) null, new TestBean(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				TestBean.PROPERTY);
		choiceType.updateFromModel();
		choiceType.setSelection("red"); //$NON-NLS-1$

		final IMultipleChoiceRidget choiceFlavor = getRidget(IMultipleChoiceRidget.class, "choiceFlavor"); //$NON-NLS-1$
		choiceFlavor.bindToModel(Arrays.asList("dry", "sweet", "sour", "spicy"), (List<String>) null, new TestBean(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				TestBean.PROPERTY);
		choiceFlavor.updateFromModel();
		choiceFlavor.setSelection(Arrays.asList("dry", "spicy")); //$NON-NLS-1$ //$NON-NLS-2$

		final IListRidget listPersons = getRidget(IListRidget.class, "listPersons"); //$NON-NLS-1$
		listPersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		listPersons.bindToModel(createPersonList(), Person.class, "listEntry"); //$NON-NLS-1$
		listPersons.updateFromModel();

		final ITableRidget tablePersons = getRidget(ITableRidget.class, "tablePersons"); //$NON-NLS-1$
		tablePersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		String[] colValues = new String[] { "lastname", "firstname" }; //$NON-NLS-1$ //$NON-NLS-2$
		final String[] colHeaders = new String[] { "Last Name", "First Name" }; //$NON-NLS-1$ //$NON-NLS-2$
		tablePersons.bindToModel(createPersonList(), Person.class, colValues, colHeaders);
		tablePersons.updateFromModel();

		final ITreeRidget treePersons = getRidget(ITreeRidget.class, "treePersons"); //$NON-NLS-1$
		treePersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		treePersons.bindToModel(createTreeRoots(), ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT, ITreeNode.PROPERTY_VALUE);
		treePersons.updateFromModel();

		final IGroupedTreeTableRidget treeWCols = getRidget(IGroupedTreeTableRidget.class, "treeWCols"); //$NON-NLS-1$
		treeWCols.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		treeWCols.setGroupingEnabled(true);
		colValues = new String[] { "word", "ACount" }; //$NON-NLS-1$ //$NON-NLS-2$
		final String[] colHeaders2 = new String[] { "Word", "#A" }; //$NON-NLS-1$ //$NON-NLS-2$
		treeWCols.bindToModel(createTreeTableRoots(), WordNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT, colValues, colHeaders2);
		treeWCols.updateFromModel();

		final IActionRidget buttonPush = getRidget(IActionRidget.class, "buttonPush"); //$NON-NLS-1$

		final ILinkRidget link = getRidget(ILinkRidget.class, "link"); //$NON-NLS-1$
		link.setText("eclipse", "http://eclipse.org"); //$NON-NLS-1$//$NON-NLS-2$

		final ISelectionListener listener = new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				final String linkUrl = (String) event.getNewSelection().get(0);
				RienaMessageDialog.openInformation(null, "Link pressed", "ULR: " + linkUrl); //$NON-NLS-1$ //$NON-NLS-2$
			}
		};
		link.addSelectionListener(listener);

		final IToggleButtonRidget buttonToggleA = getRidget(IToggleButtonRidget.class, "buttonToggleA"); //$NON-NLS-1$
		final IToggleButtonRidget buttonToggleB = getRidget(IToggleButtonRidget.class, "buttonToggleB"); //$NON-NLS-1$
		buttonToggleA.setSelected(true);

		final IToggleButtonRidget buttonRadioA = getRidget(IToggleButtonRidget.class, "buttonRadioA"); //$NON-NLS-1$
		final IToggleButtonRidget buttonRadioB = getRidget(IToggleButtonRidget.class, "buttonRadioB"); //$NON-NLS-1$
		buttonRadioA.setSelected(true);

		final IToggleButtonRidget buttonCheckA = getRidget(IToggleButtonRidget.class, "buttonCheckA"); //$NON-NLS-1$
		final IToggleButtonRidget buttonCheckB = getRidget(IToggleButtonRidget.class, "buttonCheckB"); //$NON-NLS-1$
		buttonCheckA.setSelected(true);

		final IRidget[] markables = new IRidget[] { textName, textPrice, textAmount, textDate, dtDate, dtPicker, comboAge, comboStyle, comboSize, choiceType,
				choiceFlavor, listPersons, tablePersons, treePersons, treeWCols, buttonToggleA, buttonToggleB, buttonPush, link, buttonRadioA, buttonRadioB,
				buttonCheckA, buttonCheckB };

		final IToggleButtonRidget checkMandatory = getRidget(IToggleButtonRidget.class, "checkMandatory"); //$NON-NLS-1$
		final IToggleButtonRidget checkError = getRidget(IToggleButtonRidget.class, "checkError"); //$NON-NLS-1$
		final IToggleButtonRidget checkDisabled = getRidget(IToggleButtonRidget.class, "checkDisabled"); //$NON-NLS-1$
		final IToggleButtonRidget checkOutput = getRidget(IToggleButtonRidget.class, "checkOutput"); //$NON-NLS-1$
		final IToggleButtonRidget checkHidden = getRidget(IToggleButtonRidget.class, "checkHidden"); //$NON-NLS-1$
		final IToggleButtonRidget checkHiddenParent = getRidget(IToggleButtonRidget.class, "checkHiddenParent"); //$NON-NLS-1$

		checkMandatory.addListener(new IActionListener() {
			public void callback() {
				final boolean isMandatory = checkMandatory.isSelected();
				for (final IRidget ridget : markables) {
					if (ridget instanceof IMarkableRidget) {
						((IMarkableRidget) ridget).setMandatory(isMandatory);
					} else {
						final String name = ridget.getClass().getSimpleName();
						System.out.println("No mandatory marker support on " + name); //$NON-NLS-1$
					}
				}
				if (isMandatory) {
					textName.setText(""); //$NON-NLS-1$
					textPrice.setText(""); //$NON-NLS-1$
					textAmount.setText(null);
					textDate.setText(null);
					dtPicker.setText(null);
					comboAge.setSelection("<none>"); //$NON-NLS-1$
					comboStyle.setSelection("<none>"); //$NON-NLS-1$
					comboSize.setSelection("<none>"); //$NON-NLS-1$
					choiceType.setSelection(null);
					choiceFlavor.setSelection(null);
					listPersons.setSelection((Object) null);
					tablePersons.setSelection((Object) null);
					treePersons.setSelection((Object) null);
					treeWCols.setSelection((Object) null);
					buttonToggleA.setSelected(false);
					buttonToggleB.setSelected(false);
					buttonRadioA.setSelected(false);
					buttonRadioB.setSelected(false);
					buttonCheckA.setSelected(false);
					buttonCheckB.setSelected(false);
				}
			}
		});

		checkError.addListener(new IActionListener() {
			private final IValidator alwaysWrong = new AlwaysWrongValidator();

			public void callback() {
				final boolean isError = checkError.isSelected();
				for (final IRidget ridget : markables) {
					if (ridget instanceof IMarkableRidget) {
						((IMarkableRidget) ridget).setErrorMarked(isError);
					}
				}
				// using this "always wrong" validator for purposes of this
				// demo. It prevents the error marker being removed from the
				// text field on the next revalidation (i.e. when the user
				// types).
				if (isError) {
					textName.addValidationRule(alwaysWrong, ValidationTime.ON_UI_CONTROL_EDIT);
					textPrice.addValidationRule(alwaysWrong, ValidationTime.ON_UI_CONTROL_EDIT);
					textAmount.addValidationRule(alwaysWrong, ValidationTime.ON_UI_CONTROL_EDIT);
					textDate.addValidationRule(alwaysWrong, ValidationTime.ON_UI_CONTROL_EDIT);
				} else {
					textName.removeValidationRule(alwaysWrong);
					textPrice.removeValidationRule(alwaysWrong);
					textAmount.removeValidationRule(alwaysWrong);
					textDate.removeValidationRule(alwaysWrong);
				}
			}
		});

		checkDisabled.addListener(new DisabledActionListener(markables, checkDisabled));
		checkOutput.addListener(new OutputActionListener(markables, checkOutput));
		checkHidden.addListener(new HiddenActionListener(checkHidden, markables));
		checkHiddenParent.addListener(new HiddenParentActionListener(checkHiddenParent, markables));

		getNavigationNode().addSimpleListener(new SimpleNavigationNodeAdapter() {
			@Override
			public void afterDeactivated(final INavigationNode<?> node) {
				super.afterDeactivated(node);
				final Collection<AttentionMarker> markers = node.getMarkersOfType(AttentionMarker.class);
				for (final AttentionMarker marker : markers) {
					node.removeMarker(marker);
				}
			}
		});
	}

	// helping methods
	// ////////////////

	private WritableList createPersonList() {
		return new WritableList(PersonFactory.createPersonList(), Person.class);
	}

	private ITreeNode[] createTreeRoots() {
		final ITreeNode rootA = new TreeNode("A"); //$NON-NLS-1$
		new TreeNode(rootA, new Person("Albinus", "Albert")); //$NON-NLS-1$ //$NON-NLS-2$
		new TreeNode(rootA, new Person("Aurelius", "Mark")); //$NON-NLS-1$ //$NON-NLS-2$
		final ITreeNode rootB = new TreeNode("B"); //$NON-NLS-1$
		new TreeNode(rootB, new Person("Barker", "Clyve")); //$NON-NLS-1$ //$NON-NLS-2$
		new TreeNode(rootB, new Person("Barclay", "Bob")); //$NON-NLS-1$ //$NON-NLS-2$
		return new ITreeNode[] { rootA, rootB };
	}

	private WordNode[] createTreeTableRoots() {
		final WordNode rootA = new WordNode("A"); //$NON-NLS-1$
		final WordNode rootB = new WordNode("B"); //$NON-NLS-1$
		new WordNode(rootA, "Astoria"); //$NON-NLS-1$
		new WordNode(rootA, "Ashland"); //$NON-NLS-1$
		new WordNode(rootA, "Aurora"); //$NON-NLS-1$
		new WordNode(rootA, "Alpine"); //$NON-NLS-1$
		new WordNode(rootB, "Boring"); //$NON-NLS-1$
		new WordNode(rootB, "Bend"); //$NON-NLS-1$
		new WordNode(rootB, "Beaverton"); //$NON-NLS-1$
		new WordNode(rootB, "Bridgeport"); //$NON-NLS-1$
		return new WordNode[] { rootA, rootB };
	}

	// helping classes
	// ////////////////

	private static final class DisabledActionListener implements IActionListener {

		private final IRidget[] markables;
		private final IToggleButtonRidget checkDisabled;

		private DisabledActionListener(final IRidget[] markables, final IToggleButtonRidget checkDisabled) {
			this.markables = markables;
			this.checkDisabled = checkDisabled;
		}

		public void callback() {
			final boolean isEnabled = !checkDisabled.isSelected();
			for (final IRidget ridget : markables) {
				ridget.setEnabled(isEnabled);
			}
		}

	}

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

	private static final class HiddenActionListener implements IActionListener {

		private final IToggleButtonRidget checkHidden;
		private final IRidget[] markables;

		private HiddenActionListener(final IToggleButtonRidget checkHidden, final IRidget[] markables) {
			this.checkHidden = checkHidden;
			this.markables = markables;
		}

		public void callback() {
			final boolean isVisible = !checkHidden.isSelected();
			for (final IRidget ridget : markables) {
				ridget.setVisible(isVisible);
			}
		}

	}

	private static final class HiddenParentActionListener implements IActionListener {

		private final IToggleButtonRidget checkHiddenParent;
		private final IRidget[] markables;

		private HiddenParentActionListener(final IToggleButtonRidget checkHiddenParent, final IRidget[] markables) {
			this.checkHiddenParent = checkHiddenParent;
			this.markables = markables;
		}

		public void callback() {
			final Composite parent = ((Control) markables[0].getUIControl()).getParent();
			final boolean isVisible = !checkHiddenParent.isSelected();
			parent.setVisible(isVisible);
		}

	}

	/**
	 * Validator that always returns an error status.
	 */
	private static final class AlwaysWrongValidator implements IValidator {
		public IStatus validate(final Object value) {
			return ValidationRuleStatus.error(false, ""); //$NON-NLS-1$
		}
	}

	/**
	 * A row ridget with two text ridgets for use with {@link ICompositeTableRidget}.
	 */
	public static final class RowRidget extends AbstractCompositeRidget implements IRowRidget {
		private Person rowData;

		public void setData(final Object rowData) {
			this.rowData = (Person) rowData;
		}

		@Override
		public void configureRidgets() {
			final ITextRidget txtLast = getRidget("txtLast"); //$NON-NLS-1$
			txtLast.bindToModel(rowData, Person.PROPERTY_FIRSTNAME);
			txtLast.updateFromModel();
			final ITextRidget txtFirst = getRidget("txtFirst"); //$NON-NLS-1$
			txtFirst.bindToModel(rowData, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();
		}
	}

}
