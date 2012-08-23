/**
 * 
 */
package org.eclipse.riena.example.client.javafx.controller;

import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * @author tsc
 * 
 */
public class GotoJavaFxController extends SubModuleController {

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		IActionRidget action = getRidget(IActionRidget.class,
				"additonJavaFxAction");
		action.addListener(new IActionListener() {
			@Override
			public void callback() {
				getNavigationNode()
						.jump(new NavigationNodeId(
								"org.eclipse.riena.example.client.javafx.Addition.submodule")); //$NON-NLS-1$
			}
		});

		action = getRidget(IActionRidget.class, "additonEclipseAction");
		action.addListener(new IActionListener() {
			@Override
			public void callback() {
				getNavigationNode()
						.jump(new NavigationNodeId(
								"org.eclipse.riena.example.client.javafx.AdditionEclipseBinding.submodule")); //$NON-NLS-1$
			}
		});

	}

}