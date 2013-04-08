package org.eclipse.riena.e4.demo.controller;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ITextRidget;

public class Controller2 extends SubModuleController {
	public Controller2() {
		this(null);
	}

	public Controller2(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void configureRidgets() {
		ITextRidget r = getRidget(ITextRidget.class, "txt");
		r.setErrorMarked(true);
		r.setMandatory(true);

	}
}
