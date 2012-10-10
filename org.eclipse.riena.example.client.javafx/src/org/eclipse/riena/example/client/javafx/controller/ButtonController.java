/**
 * 
 */
package org.eclipse.riena.example.client.javafx.controller;

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

/**
 * @author tsc
 * 
 */
public class ButtonController extends SubModuleController {

	private int simpleClickedCount = 0;
	private IconModel iconModel;

	@Override
	public void configureRidgets() {

		iconModel = new IconModel();

		super.configureRidgets();

		final ILabelRidget simpleLabel = getRidget(ILabelRidget.class,
				"simpleLbl");
		final IActionRidget simpleButton = getRidget(IActionRidget.class,
				"simpleBtn");
		simpleButton.addListener(new IActionListener() {
			@Override
			public void callback() {
				simpleClickedCount++;
				String text = "Clickcount of Simple Button: "
						+ simpleClickedCount;
				simpleLabel.setText(text);
			}
		});

		final IActionRidget imageButton = getRidget(IActionRidget.class,
				"imageBtn");
		updateIcon(imageButton);
		imageButton.addListener(new IActionListener() {
			@Override
			public void callback() {
				iconModel.switchIcon();
				updateIcon(imageButton);
			}
		});

	}

	private void updateIcon(final IActionRidget imageButton) {
		imageButton.setIcon(iconModel.getIcon());
	}

	private class IconModel {

		private static final String GREEN = "ledlightgreen.png";
		private static final String RED = "ledred.png";

		private boolean green = true;

		public String getIcon() {
			if (green) {
				return GREEN;
			} else {
				return RED;
			}
		}

		public void switchIcon() {
			green = !green;
		}

	}

}