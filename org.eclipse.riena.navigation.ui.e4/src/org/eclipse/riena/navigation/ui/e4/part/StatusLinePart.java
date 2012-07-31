package org.eclipse.riena.navigation.ui.e4.part;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
 * Creates the Riena status line.
 * 
 * @author jdu
 * 
 */
public class StatusLinePart {
	@Inject
	public void create(final Composite parent) {
		final Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FormLayout());

		createStatusLine(c, new GrabCorner(c, SWT.DOUBLE_BUFFERED, true));
	}

	private void createStatusLine(final Composite parent, final Composite grabCorner) {
		//		final IStatusLineContentFactory statusLineFactory = getStatuslineContentFactory(); // TODO from extension point
		final IStatusLineContentFactory statusLineFactory = new DefaultStatuslineContentFactory();
		final Statusline statusLine = new Statusline(parent, SWT.None, StatuslineSpacer.class, statusLineFactory);
		final FormData fd = new FormData();
		//		final Rectangle navigationBounds = TitlelessStackPresentation.calcNavigationBounds(parent);
		fd.top = new FormAttachment(0, 0);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(grabCorner, 0);
		fd.bottom = new FormAttachment(100, 0);
		statusLine.setLayoutData(fd);
		addUIControl(statusLine, "statusline"); //$NON-NLS-1$

		LnFUpdater.getInstance().updateUIControls(statusLine, true);
	}

	private void addUIControl(final Statusline statusLine, final String bindingId) {
		final InjectSwtViewBindingDelegate binding = new InjectSwtViewBindingDelegate();
		binding.addUIControl(statusLine, bindingId);
		binding.injectAndBind((ApplicationController) ApplicationNodeManager.getApplicationNode().getNavigationNodeController());
	}
}
