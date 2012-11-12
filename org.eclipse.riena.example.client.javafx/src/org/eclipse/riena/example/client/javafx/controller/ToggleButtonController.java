
package org.eclipse.riena.example.client.javafx.controller;

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IChoiceRidget;
import org.eclipse.riena.ui.ridgets.IImageButtonRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * @author sgo
 * 
 */
public class ToggleButtonController extends SubModuleController {

	@Override
	public void configureRidgets() {
		
		ToggleModel toggleModel = new ToggleModel();
		toggleModel.setPressed(false);
		
		final DataModel dataModel = new DataModel();
		dataModel.setState(false);

		// iconModel = new IconModel();


		final IActionRidget button = getRidget(IActionRidget.class,"greenIconBtn");
		button.setIcon("ledlightgreen.png");
		button.setVisible(false);
		
		final IActionRidget button2 = getRidget(IActionRidget.class,"redIconBtn");
		button2.setIcon("ledred.png");
		button2.setVisible(false);
		
		
		final IToggleButtonRidget manToggleButton1 = getRidget(IToggleButtonRidget.class,"manToggleBtn1");
		manToggleButton1.bindToModel(toggleModel,"pressed");
		manToggleButton1.updateFromModel();
		manToggleButton1.addListener(new IActionListener(){

			@Override
			public void callback() {
											
				button.setVisible(!button.isVisible());
			}
		});
		
		
		final IToggleButtonRidget manToggleButton2 = getRidget(IToggleButtonRidget.class,"manToggleBtn2");
		manToggleButton2.addListener(new IActionListener(){

			@Override
			public void callback() {
				
				button2.setVisible(!button2.isVisible());
			}
		});
		
		
		final IToggleButtonRidget hiddenToggleButton1 = getRidget(IToggleButtonRidget.class,"hiddenToggleBtn1");
				
		final IToggleButtonRidget hiddenToggleButton2 = getRidget(IToggleButtonRidget.class,"hiddenToggleBtn2");
		
		final IActionRidget toggleVisibilityButton = getRidget(IToggleButtonRidget.class, "toggleVisibilityBtn");
		toggleVisibilityButton.addListener(new IActionListener(){

			@Override
			public void callback() {
				
				hiddenToggleButton1.setVisible(!hiddenToggleButton1.isVisible());
				hiddenToggleButton2.setVisible(!hiddenToggleButton2.isVisible());
			}
		});
		
		
		final IToggleButtonRidget disabledToggleButton1 = getRidget(IToggleButtonRidget.class,"disabledToggleBtn1");
		
		final IToggleButtonRidget disabledToggleButton2 = getRidget(IToggleButtonRidget.class,"disabledToggleBtn2");
		
		final IActionRidget toggleDisabledButton = getRidget(IToggleButtonRidget.class, "toggleDisabledBtn");
		
		toggleDisabledButton.addListener(new IActionListener(){

			@Override
			public void callback() {
				
				disabledToggleButton1.setEnabled(!disabledToggleButton1.isEnabled());
				
					/** set new text **/
					if(disabledToggleButton1.isEnabled()) {
						disabledToggleButton1.setText("enabled");
					}else
						disabledToggleButton1.setText("disabled");
				
				
				disabledToggleButton2.setEnabled(!disabledToggleButton2.isEnabled());
				
					/** set new text **/
					if(disabledToggleButton2.isEnabled()) {
						disabledToggleButton2.setText("enabled");
					}else
						disabledToggleButton2.setText("disabled");
			}
		});
				
		manToggleButton1.setMandatory(true);
		manToggleButton2.setMandatory(true);
		
		
		disabledToggleButton1.setEnabled(false);
		disabledToggleButton2.setEnabled(false);
	
		
		
		final IToggleButtonRidget outputOnlyButton1 = getRidget(IToggleButtonRidget.class,"outputOnlyBtn1");
		
		final IToggleButtonRidget outputOnlyButton2 = getRidget(IToggleButtonRidget.class,"outputOnlyBtn2");
		
		outputOnlyButton1.setOutputOnly(true);
		outputOnlyButton2.setOutputOnly(true);

		outputOnlyButton1.bindToModel(dataModel,"state");
		outputOnlyButton1.updateFromModel();
		
		outputOnlyButton2.bindToModel(dataModel,"state");
		outputOnlyButton2.updateFromModel();		
		
		final ILabelRidget simpleLabel = getRidget(ILabelRidget.class,"simpleLbl");
				
		final IActionRidget toggleModelState = getRidget(IToggleButtonRidget.class, "toggleModelStateBtn");
		toggleModelState.addListener(new IActionListener(){

			@Override
			public void callback() {
			
				dataModel.setState(!dataModel.getState());
				simpleLabel.setText(dataModel.printState());
				outputOnlyButton1.updateFromModel();
				outputOnlyButton2.updateFromModel();
			}
		});
		
		
		
		/** set a new icon**/
		// toggleButton.setIcon("ledlightgreen.png");

	}
	
	
	private class ToggleModel {
		
		private boolean pressed;

		public boolean isPressed() {
			return pressed;
		}

		public void setPressed(boolean pressed) {
			this.pressed = pressed;
			System.out.println("ToggleButtonController.ToggleModel.setPressed() " + pressed);
		}
		
	}
	
	private class DataModel {
		
		private boolean state;
		
		public String printState() {
			
			if(state)
				return "true";
			else
				return "false";
			
		}
		
		public boolean getState() {
			return state;
		}
		
		public void setState(boolean state) {
			this.state = state;
		}
		
	}

}