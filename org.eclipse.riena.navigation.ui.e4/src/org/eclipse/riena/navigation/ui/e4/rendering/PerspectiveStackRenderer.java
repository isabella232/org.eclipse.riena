package org.eclipse.riena.navigation.ui.e4.rendering;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.renderers.swt.StackRenderer;
import org.eclipse.swt.custom.CTabFolder;

/**
 *
 */
public class PerspectiveStackRenderer extends StackRenderer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.workbench.renderers.swt.StackRenderer#createWidget(org.eclipse.e4.ui.model.application.ui.MUIElement, java.lang.Object)
	 */
	@Override
	public Object createWidget(final MUIElement element, final Object parent) {
		final Object createWidget = super.createWidget(element, parent);
		final CTabFolder folder = (CTabFolder) createWidget;
		folder.setTabHeight(0);
		folder.setMaximizeVisible(false);
		folder.setMinimizeVisible(false);
		return createWidget;
	}

}
