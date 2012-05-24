package org.eclipse.riena.e4.demo.parts;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ContentPart31 extends SubModuleView {

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		UIControlsFactory.createLabel(parent,
				"This one is programmatically created.");
	}

}
