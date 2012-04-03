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
package org.eclipse.riena.client.controller.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;

import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.core.RienaStatus;
import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.internal.ui.ridgets.swt.CComboRidget;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.AbstractMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ILinkRidget;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.IScaleRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ISliderRidget;
import org.eclipse.riena.ui.ridgets.ISpinnerRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.ImageButtonRidget;
import org.eclipse.riena.ui.ridgets.validation.NotEmpty;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;

/**
 * Example Controller with as many ridgets as possible. Used for controller testing.
 */
public class ControllerTestsPlaygroundSubModuleController extends SubModuleController {
	private ITableRidget multiTable;
	private IListRidget tableList;
	private final Temperature temperature;
	private final List<Person> input = PersonFactory.createPersonList();
	private long now;
	private IToggleButtonRidget selectAllToggleButton;
	private IActionListener toggleListener;
	private ISelectionListener multiTableSelectionListener;

	/**
	 * 
	 */
	public ControllerTestsPlaygroundSubModuleController() {
		temperature = new Temperature();
		temperature.setKelvin(273.15f, true);
	}

	@Override
	public void configureRidgets() {
		configureTableGroup();
		configureComboGroup();
		configureBrowserGroup();
		configureTraverseGroup();
		configureMasterDetailsGroup();
		configureDateTimeGroup();
		configureImageButtonGroup();
		//TODO work in progress
	}

	/**
	 * 
	 */
	private void configureImageButtonGroup() {
		final IActionRidget imageButton = getRidget(ImageButtonRidget.class, "imageButton"); //$NON-NLS-1$
		imageButton.setIcon("imageBtn"); //$NON-NLS-1$
		imageButton.addListener(new IActionListener() {
			public void callback() {
				System.out.println("Button klicked..."); //$NON-NLS-1$
			}
		});

		final IActionRidget arrowButton = getRidget(ImageButtonRidget.class, "arrowButton"); //$NON-NLS-1$
		arrowButton.setIcon("arrowRight"); //$NON-NLS-1$
		arrowButton.addListener(new IActionListener() {
			public void callback() {
				System.out.println("Button klicked..."); //$NON-NLS-1$
			}
		});

		final IActionRidget arrowHotButton = getRidget(ImageButtonRidget.class, "arrowHotButton"); //$NON-NLS-1$
		arrowHotButton.setIcon("arrowRight"); //$NON-NLS-1$
		arrowHotButton.addListener(new IActionListener() {
			public void callback() {
				System.out.println("Button klicked..."); //$NON-NLS-1$
			}
		});
	}

	/**
	 * 
	 */
	private void configureDateTimeGroup() {
		final IDateTimeRidget dtDate = getRidget(IDateTimeRidget.class, "dtDate"); //$NON-NLS-1$
		final IDateTimeRidget dtTime = getRidget(IDateTimeRidget.class, "dtTime"); //$NON-NLS-1$
		final IDateTimeRidget dtDateOnly = getRidget(IDateTimeRidget.class, "dtDateOnly"); //$NON-NLS-1$
		final IDateTimeRidget dtTimeOnly = getRidget(IDateTimeRidget.class, "dtTimeOnly"); //$NON-NLS-1$
		final IDateTimeRidget dtCal = getRidget(IDateTimeRidget.class, "dtCal"); //$NON-NLS-1$
		final ITextRidget txt1 = getRidget(ITextRidget.class, "txt1"); //$NON-NLS-1$
		final ITextRidget txt2 = getRidget(ITextRidget.class, "txt2"); //$NON-NLS-1$
		final ITextRidget txt3 = getRidget(ITextRidget.class, "txt3"); //$NON-NLS-1$
		final ITextRidget txt4 = getRidget(ITextRidget.class, "txt4"); //$NON-NLS-1$

		now = System.currentTimeMillis();
		final TypedBean<Date> date1 = new TypedBean<Date>(new Date(now));
		final TypedBean<Date> date2 = new TypedBean<Date>(new Date(now));
		final TypedBean<Date> date3 = new TypedBean<Date>(new Date(now));
		final TypedBean<Date> date4 = new TypedBean<Date>(new Date(now));

		dtDate.bindToModel(date1, TypedBean.PROP_VALUE);
		dtDate.updateFromModel();
		dtTime.bindToModel(date1, TypedBean.PROP_VALUE);
		dtTime.updateFromModel();

		dtDateOnly.bindToModel(date2, TypedBean.PROP_VALUE);
		dtDateOnly.updateFromModel();

		dtTimeOnly.bindToModel(date3, TypedBean.PROP_VALUE);
		dtTimeOnly.updateFromModel();

		dtCal.bindToModel(date4, TypedBean.PROP_VALUE);
		dtCal.updateFromModel();

		final DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(BeansObservables.observeValue(txt1, ITextRidget.PROPERTY_TEXT), BeansObservables.observeValue(date1, TypedBean.PROP_VALUE));
		dbc.bindValue(BeansObservables.observeValue(txt2, ITextRidget.PROPERTY_TEXT), BeansObservables.observeValue(date2, TypedBean.PROP_VALUE));
		dbc.bindValue(BeansObservables.observeValue(txt3, ITextRidget.PROPERTY_TEXT), BeansObservables.observeValue(date3, TypedBean.PROP_VALUE));
		dbc.bindValue(BeansObservables.observeValue(txt4, ITextRidget.PROPERTY_TEXT), BeansObservables.observeValue(date4, TypedBean.PROP_VALUE));

		makeOutputOnly(txt1, txt2, txt3, txt4);

		final IDateTextRidget dateTextRidget = getRidget(IDateTextRidget.class, "dateText"); //$NON-NLS-1$
		dateTextRidget.setText("03.03.2011"); //$NON-NLS-1$
		final IActionRidget dateTimeButton = getRidget(IActionRidget.class, "dateTimeButton"); //$NON-NLS-1$
		dateTimeButton.setText("apply date"); //$NON-NLS-1$
		dateTimeButton.addListener(new IActionListener() {
			public void callback() {
				final DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
				try {
					final Date newDate = df.parse(dateTextRidget.getText());
					dtDate.setDate(newDate);
					dtDateOnly.setDate(newDate);
					dtCal.setDate(newDate);
				} catch (final ParseException e) {
					throw new MurphysLawFailure("Date parse error", e); //$NON-NLS-1$
				}
			}
		});
	}

	private void configureMasterDetailsGroup() {
		final String[] properties = new String[] { "firstname", "lastname" }; //$NON-NLS-1$ //$NON-NLS-2$
		final String[] headers = new String[] { "First Name", "Last Name" }; //$NON-NLS-1$ //$NON-NLS-2$

		final IMasterDetailsRidget master = getRidget(IMasterDetailsRidget.class, "master"); //$NON-NLS-1$
		if (master != null) {
			if (RienaStatus.isTest()) {
				master.configureRidgets();
			}
			master.setDelegate(new PersonDelegate());
			master.bindToModel(new WritableList(input, Person.class), Person.class, properties, headers);
			master.updateFromModel();

			final IActionRidget actionApply = master.getRidget(IActionRidget.class, MasterDetailsComposite.BIND_ID_APPLY);
			addDefaultAction(master, actionApply);
		}

		final IActionRidget enableDisableButton = getRidget(IActionRidget.class, "enableDisable"); //$NON-NLS-1$
		if (enableDisableButton != null) {
			enableDisableButton.addListener(new IActionListener() {
				public void callback() {
					if (master != null) {
						master.setEnabled(!master.isEnabled());
					}
				}
			});
		}
	}

	private void configureBrowserGroup() {
		final ILinkRidget link1 = getRidget(ILinkRidget.class, "link1"); //$NON-NLS-1$
		link1.setText("<a>http://www.eclipse.org/</a>"); //$NON-NLS-1$

		final ILinkRidget link2 = getRidget(ILinkRidget.class, "link2"); //$NON-NLS-1$
		link2.setText("Visit <a href=\"http://www.eclipse.org/riena/\">Riena</a>"); //$NON-NLS-1$

		final ILinkRidget link3 = getRidget(ILinkRidget.class, "link3"); //$NON-NLS-1$
		link3.setText("Eclipse <a href=\"http://planeteclipse.org\">Blogs</a>, <a href=\"http://www.eclipse.org/community/news/\">News</a> and <a href=\"http://live.eclipse.org\">Events</a>"); //$NON-NLS-1$

		final ITextRidget textLinkUrl = getRidget(ITextRidget.class, "textLinkUrl"); //$NON-NLS-1$
		textLinkUrl.setOutputOnly(true);

		final IBrowserRidget browser = getRidget(IBrowserRidget.class, "browser"); //$NON-NLS-1$
		browser.bindToModel(textLinkUrl, ITextRidget.PROPERTY_TEXT);

		final ISelectionListener listener = new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				final String linkUrl = (String) event.getNewSelection().get(0);
				browser.setUrl(linkUrl);
			}
		};
		link1.addSelectionListener(listener);
		link2.addSelectionListener(listener);
		link3.addSelectionListener(listener);

	}

	private void configureComboGroup() {
		final ITextRidget comboText = getRidget(ITextRidget.class, "comboTextField"); //$NON-NLS-1$

		final List<String> ages = new ArrayList<String>(Arrays.asList(new String[] { "<none>", "young", "moderate", "aged", "old" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		final ILabelRidget comboLabel = getRidget(ILabelRidget.class, "comboLabel"); //$NON-NLS-1$
		comboLabel.setText(ages.get(0));

		final IComboRidget comboAge = getRidget(IComboRidget.class, "ageCombo"); //$NON-NLS-1$
		comboAge.bindToModel(new WritableList(ages, String.class), String.class, null, new WritableValue());
		comboAge.updateFromModel();
		comboAge.setEmptySelectionItem("<none>"); //$NON-NLS-1$
		comboAge.setSelection(0);

		final IComboRidget cComboAge = getRidget(CComboRidget.class, "ageCCombo"); //$NON-NLS-1$
		cComboAge.bindToModel(new WritableList(ages, String.class), String.class, null, new WritableValue());
		cComboAge.updateFromModel();
		cComboAge.setEmptySelectionItem("<none>"); //$NON-NLS-1$
		cComboAge.setSelection(0);

		final ISelectionListener selectionListener = new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				comboLabel.setText(event.getNewSelection().get(0).toString());
			}
		};

		comboAge.addSelectionListener(selectionListener);
		cComboAge.addSelectionListener(selectionListener);

		final IActionRidget addToComboButton = getRidget(IActionRidget.class, "addToComboButton"); //$NON-NLS-1$
		addToComboButton.addListener(new IActionListener() {
			public void callback() {
				final String comboString = comboText.getText();
				if (comboString.length() >= 0) {
					ages.add(comboString);
				}
				comboAge.bindToModel(new WritableList(ages, String.class), String.class, null, new WritableValue());
				comboAge.updateFromModel();
				comboAge.setSelection(comboAge.getObservableList().size() - 1);
				cComboAge.bindToModel(new WritableList(ages, String.class), String.class, null, new WritableValue());
				cComboAge.updateFromModel();
				cComboAge.setSelection(comboAge.getObservableList().size() - 1);
				comboText.setText(""); //$NON-NLS-1$
			}
		});
	}

	private void configureTableGroup() {
		multiTable = getRidget(ITableRidget.class, "multiTable"); //$NON-NLS-1$
		final ColumnLayoutData[] widths = { new ColumnPixelData(80, true), new ColumnPixelData(80, true) };
		multiTable.setColumnWidths(widths);
		multiTable.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		final String[] colValues = new String[] { "lastname", "firstname" }; //$NON-NLS-1$ //$NON-NLS-2$
		final String[] colHeaders = new String[] { "Last Name", "First Name" }; //$NON-NLS-1$ //$NON-NLS-2$
		multiTable.addDoubleClickListener(new IActionListener() {

			public void callback() {
				multiTable.getSelection();
			}
		});
		multiTable.bindToModel(createPersonList(), Person.class, colValues, colHeaders);
		multiTable.updateFromModel();

		tableList = getRidget(IListRidget.class, "tableList"); //$NON-NLS-1$

		tableList.setSelectionType(ISelectableRidget.SelectionType.MULTI);

		selectAllToggleButton = getRidget(IToggleButtonRidget.class, "toggleButton"); //$NON-NLS-1$
		selectAllToggleButton.setText("select all"); //$NON-NLS-1$

		multiTableSelectionListener = new MultiTableSelectionListener();
		multiTable.addSelectionListener(multiTableSelectionListener);

		toggleListener = new IActionListener() {
			public void callback() {
				multiTable.removeSelectionListener(multiTableSelectionListener);
				if (selectAllToggleButton.isSelected()) {
					selectAllToggleButton.setText("deselect"); //$NON-NLS-1$
					final int[] allIndices = new int[multiTable.getOptionCount()];
					for (int i = 0; i < multiTable.getOptionCount(); i++) {
						allIndices[i] = i;
					}
					multiTable.setSelection(allIndices);
				} else {
					multiTable.clearSelection();
					selectAllToggleButton.setText("select all"); //$NON-NLS-1$
				}
				multiTable.addSelectionListener(multiTableSelectionListener);
			}
		};
		selectAllToggleButton.addListener(toggleListener);

		final IActionRidget copySelectionButton = getRidget(IActionRidget.class, "copySelectionButton"); //$NON-NLS-1$
		copySelectionButton.addListener(new IActionListener() {
			public void callback() {
				final List<Object> selection = multiTable.getSelection();
				tableList.bindToModel(new WritableList(selection, Person.class), Person.class, "listEntry"); //$NON-NLS-1$
				tableList.updateFromModel();
			}
		});

	}

	private void configureTraverseGroup() {
		final ISpinnerRidget fahrenheitSpinner = getRidget(ISpinnerRidget.class, "fahrenheitSpinner"); //$NON-NLS-1$
		final ITraverseRidget celsiusScale = getRidget(IScaleRidget.class, "celsiusScale"); //$NON-NLS-1$
		final ISliderRidget kelvinSlider = getRidget(ISliderRidget.class, "kelvinSlider"); //$NON-NLS-1$

		final IActionListener listener = new IActionListener() {

			public void callback() {
				celsiusScale.updateFromModel();
				fahrenheitSpinner.updateFromModel();
				kelvinSlider.updateFromModel();
			}
		};

		kelvinSlider.setIncrement(1);
		kelvinSlider.setMaximum(324);
		kelvinSlider.setMinimum(273);
		kelvinSlider.setToolTipText("The current value is: [VALUE] (rounded)."); //$NON-NLS-1$
		kelvinSlider.bindToModel(BeansObservables.observeValue(temperature, Temperature.PROPERTY_KELVIN));
		kelvinSlider.updateFromModel();
		kelvinSlider.addListener(listener);

		fahrenheitSpinner.setIncrement(1);
		fahrenheitSpinner.setMaximum(122);
		fahrenheitSpinner.setMinimum(32);
		fahrenheitSpinner.bindToModel(BeansObservables.observeValue(temperature, Temperature.PROPERTY_DEGREE_FAHRENHEIT));
		fahrenheitSpinner.updateFromModel();
		fahrenheitSpinner.addListener(listener);

		celsiusScale.setIncrement(1);
		celsiusScale.setMaximum(50);
		celsiusScale.setMinimum(0);
		celsiusScale.bindToModel(BeansObservables.observeValue(temperature, Temperature.PROPERTY_DEGREE_CELSIUS));
		celsiusScale.updateFromModel();
		celsiusScale.addListener(listener);
	}

	// helpers
	//////////

	private class Temperature extends AbstractBean {

		static final String PROPERTY_DEGREE_CELSIUS = "degreeCelsius"; //$NON-NLS-1$
		static final String PROPERTY_DEGREE_FAHRENHEIT = "degreeFahrenheit"; //$NON-NLS-1$
		static final String PROPERTY_KELVIN = "kelvin"; //$NON-NLS-1$

		private float kelvin;
		private int degreeCelsius;
		private int degreeFahrenheit;

		public Temperature() {
			setDegreeCelsius(0);
		}

		public void setDegreeCelsius(final int degreeCelsius) {
			setDegreeCelsius(degreeCelsius, true);
		}

		private void setDegreeCelsius(final int degreeCelsius, final boolean updateKelvin) {
			final int oldValue = this.degreeCelsius;
			this.degreeCelsius = degreeCelsius;
			if (updateKelvin) {
				final float k = degreeCelsius + 273.15f;
				setKelvin(k, false);
				updateFahrenheit();
			}
			firePropertyChanged(PROPERTY_DEGREE_CELSIUS, oldValue, degreeCelsius);
		}

		@SuppressWarnings("unused")
		public int getDegreeCelsius() {
			return degreeCelsius;
		}

		@SuppressWarnings("unused")
		public void setDegreeFahrenheit(final int degreeFahrenheit) {
			setDegreeFahrenheit(degreeFahrenheit, true);
		}

		private void setDegreeFahrenheit(final int degreeFahrenheit, final boolean updateKelvin) {
			final int oldValue = this.degreeFahrenheit;
			this.degreeFahrenheit = degreeFahrenheit;
			if (updateKelvin) {
				final float c = (degreeFahrenheit - 32) / 1.8f;
				final float k = c + 273.15f;
				setKelvin(k, false);
				updateCelsius();
			}
			firePropertyChanged(PROPERTY_DEGREE_FAHRENHEIT, oldValue, degreeFahrenheit);
		}

		@SuppressWarnings("unused")
		public int getDegreeFahrenheit() {
			return degreeFahrenheit;
		}

		private void setKelvin(final float kelvin, final boolean updateOthers) {
			final float oldValue = this.kelvin;
			this.kelvin = kelvin;
			if (updateOthers) {
				updateCelsius();
				updateFahrenheit();
			}
			firePropertyChanged(PROPERTY_KELVIN, oldValue, Math.round(kelvin));
		}

		@SuppressWarnings("unused")
		public void setKelvin(final int kelvin) {
			setKelvin(kelvin, true);
		}

		public int getKelvin() {
			return Math.round(kelvin);
		}

		private void updateCelsius() {
			final int c = Math.round(getKelvin() - 273.15f);
			setDegreeCelsius(c, false);
		}

		private void updateFahrenheit() {
			final int c = Math.round(getKelvin() - 273.15f);
			final int f = Math.round(c * 1.8f + 32);
			setDegreeFahrenheit(f, false);
		}
	}

	private class MultiTableSelectionListener implements ISelectionListener {
		public void ridgetSelected(final SelectionEvent event) {
			selectAllToggleButton.removeListener(toggleListener);
			selectAllToggleButton.setSelected(true);
			selectAllToggleButton.setText("deselect"); //$NON-NLS-1$
			selectAllToggleButton.addListener(toggleListener);
		}
	}

	private WritableList createPersonList() {
		return new WritableList(PersonFactory.createPersonList(), Person.class);
	}

	/**
	 * Setup the ridgets for editing a person (text ridgets for name, single choice ridget for gender, multiple choice ridgets for pets).
	 */
	private static final class PersonDelegate extends AbstractMasterDetailsDelegate {

		private static final String[] GENDER = { Person.FEMALE, Person.MALE };

		private final Person workingCopy = createWorkingCopy();

		public void configureRidgets(final IRidgetContainer container) {
			final ITextRidget txtFirst = container.getRidget(ITextRidget.class, "first"); //$NON-NLS-1$
			txtFirst.setMandatory(true);
			txtFirst.bindToModel(workingCopy, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();

			final ITextRidget txtLast = container.getRidget(ITextRidget.class, "last"); //$NON-NLS-1$
			txtLast.setMandatory(true);
			txtLast.addValidationRule(new NotEmpty(), ValidationTime.ON_UI_CONTROL_EDIT);
			txtLast.bindToModel(workingCopy, Person.PROPERTY_LASTNAME);
			txtLast.updateFromModel();

			final ISingleChoiceRidget gender = container.getRidget(ISingleChoiceRidget.class, "gender"); //$NON-NLS-1$
			if (gender != null) {
				gender.bindToModel(Arrays.asList(GENDER), (List<String>) null, workingCopy, Person.PROPERTY_GENDER);
				gender.updateFromModel();
			}

			final IMultipleChoiceRidget pets = container.getRidget(IMultipleChoiceRidget.class, "pets"); //$NON-NLS-1$
			if (pets != null) {
				pets.bindToModel(Arrays.asList(Person.Pets.values()), (List<String>) null, workingCopy, Person.PROPERTY_PETS);
				pets.updateFromModel();
			}
		}

		public Person createWorkingCopy() {
			return new Person("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		public Person copyBean(final Object source, final Object target) {
			final Person from = source != null ? (Person) source : createWorkingCopy();
			final Person to = target != null ? (Person) target : createWorkingCopy();
			to.setFirstname(from.getFirstname());
			to.setLastname(from.getLastname());
			to.setGender(from.getGender());
			to.setPets(from.getPets());
			return to;
		}

		public Object getWorkingCopy() {
			return workingCopy;
		}

		@Override
		public boolean isChanged(final Object source, final Object target) {
			final Person p1 = (Person) source;
			final Person p2 = (Person) target;
			final boolean equals = p1.getFirstname().equals(p2.getFirstname()) && p1.getLastname().equals(p2.getLastname())
					&& p1.getGender().equals(p2.getGender()) && p1.getPets().equals(p2.getPets());
			return !equals;
		}

		@Override
		public String isValid(final IRidgetContainer container) {
			final ITextRidget txtLast = container.getRidget(ITextRidget.class, "last"); //$NON-NLS-1$
			if (txtLast.isErrorMarked()) {
				return "'Last Name' is not valid."; //$NON-NLS-1$
			}
			return null;
		}
	}

	private void makeOutputOnly(final ITextRidget... ridgets) {
		for (final ITextRidget ridget : ridgets) {
			ridget.setOutputOnly(true);
		}
	}
}
