package org.eclipse.riena.e4.demo.parts;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ContentPart1 extends SubModuleView {

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		final Label label = new Label(parent, SWT.BORDER);
		label.setText("Here is the content area 1.");
		// TODO Auto-generated method stub

	}
}
