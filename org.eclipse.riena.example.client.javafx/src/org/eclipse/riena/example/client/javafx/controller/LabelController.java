/**
 * 
 */
package org.eclipse.riena.example.client.javafx.controller;

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;

/**
 * @author tsc
 * 
 */
public class LabelController extends SubModuleController {

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		final MyModel model = new MyModel();

		final ILabelRidget stringLabel = getRidget(ILabelRidget.class,
				"stringLabel");
		stringLabel.bindToModel(model, "stringValue");

		final ILabelRidget intLabel = getRidget(ILabelRidget.class, "intLabel");
		intLabel.bindToModel(model, "intValue");
		IClickListener incListener = new IClickListener() {

			@Override
			public void callback(ClickEvent event) {
				int intValue = model.getIntValue();
				if (event.getButton() == 1) {
					intValue++;
				} else if (event.getButton() == 2) {
					intValue--;
				}
				model.setIntValue(intValue);
				intLabel.updateFromModel();
			}
		};
		intLabel.addClickListener(incListener);

		final ILabelRidget incLabel = getRidget(ILabelRidget.class, "incLabel");
		incLabel.addClickListener(incListener);

		final ILabelRidget doubleLabel = getRidget(ILabelRidget.class,
				"doubleLabel");
		doubleLabel.bindToModel(model, "doubleValue");
		IActionListener doubleDisableListener = new IActionListener() {
			@Override
			public void callback() {
				boolean enabled = doubleLabel.isEnabled();
				doubleLabel.setEnabled(!enabled);
			}
		};
		doubleLabel.addDoubleClickListener(doubleDisableListener);
		final ILabelRidget disableLabel = getRidget(ILabelRidget.class,
				"disableLabel");
		disableLabel.addDoubleClickListener(doubleDisableListener);

		final ILabelRidget booleanLabel = getRidget(ILabelRidget.class,
				"booleanLabel");
		booleanLabel.bindToModel(model, "booleanValue");
		IActionListener doubleHideListener = new IActionListener() {
			@Override
			public void callback() {
				boolean visible = booleanLabel.isVisible();
				booleanLabel.setVisible(!visible);
			}
		};
		booleanLabel.addDoubleClickListener(doubleHideListener);
		final ILabelRidget hideLabel = getRidget(ILabelRidget.class,
				"hideLabel");
		hideLabel.addDoubleClickListener(doubleHideListener);

		final ILabelRidget imageLabel = getRidget(ILabelRidget.class,
				"imageLabel");
		imageLabel.setIcon("eclipse.gif");
		IClickListener imageListener = new IClickListener() {

			@Override
			public void callback(ClickEvent event) {
				if (event.getButton() == 1) {
					imageLabel.setIcon("cool.gif");
				} else if (event.getButton() == 2) {
					imageLabel.setIcon("eclipse.gif");
				}
			}
		};
		imageLabel.addClickListener(imageListener);
		final ILabelRidget changeImageLabel = getRidget(ILabelRidget.class,
				"changeImageLabel");
		changeImageLabel.addClickListener(imageListener);

		updateAllRidgetsFromModel();

	}

	public class MyModel {

		private final String stringValue = "Label Example";
		private int intValue = 0;
		private double doubleValue = 47.11;
		private boolean booleanValue = false;

		public String getStringValue() {
			return stringValue;
		}

		public int getIntValue() {
			return intValue;
		}

		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}

		public double getDoubleValue() {
			return doubleValue;
		}

		public void setDoubleValue(double doubleValue) {
			this.doubleValue = doubleValue;
		}

		public boolean isBooleanValue() {
			return booleanValue;
		}

		public void setBooleanValue(boolean booleanValue) {
			this.booleanValue = booleanValue;
		}

	}

}