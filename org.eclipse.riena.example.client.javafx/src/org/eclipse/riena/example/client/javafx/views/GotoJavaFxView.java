/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author tsc
 * 
 */
public class GotoJavaFxView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(Composite parent) {

		parent.setLayout(new RowLayout(SWT.VERTICAL));

		UIControlsFactory.createButton(parent, "Addtion (JavaFX-Binding)",
				"additonJavaFxAction");
		UIControlsFactory.createButton(parent, "Addtion (Eclipse-Binding)",
				"additonEclipseAction");

	}

}