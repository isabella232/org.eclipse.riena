package org.eclipse.riena.navigation.ui.e4.part;

import javax.inject.Inject;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.ui.swt.DefaultStatuslineContentFactory;
import org.eclipse.riena.ui.swt.GrabCorner;
import org.eclipse.riena.ui.swt.IStatusLineContentFactory;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineSpacer;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;

/**
 * Creates the Riena header.
 * 
 * @author jdu
 * 
 */
public class StatusLinePart {

	@Inject
	public void create(final Composite parent) {
		final Composite c = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).spacing(0, 0).applyTo(c);

		final Statusline statusline = createStatusLine(c);
		statusline.setLayoutData(new GridData(GridData.FILL_BOTH));

		new GrabCorner(c, SWT.DOUBLE_BUFFERED, true).setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true));

		final InjectSwtViewBindingDelegate binding = new InjectSwtViewBindingDelegate();
		binding.addUIControl(statusline, "statusline");
		binding.injectAndBind((ApplicationController) ApplicationNodeManager.getApplicationNode().getNavigationNodeController());
	}

	private Statusline createStatusLine(final Composite parent) {
		//		final IStatusLineContentFactory statusLineFactory = getStatuslineContentFactory(); // TODO from extension point
		final IStatusLineContentFactory statusLineFactory = new DefaultStatuslineContentFactory();
		final Statusline statusLine = new Statusline(parent, SWT.None, StatuslineSpacer.class, statusLineFactory);
		LnFUpdater.getInstance().updateUIControls(statusLine, true);
		return statusLine;
	}
}
