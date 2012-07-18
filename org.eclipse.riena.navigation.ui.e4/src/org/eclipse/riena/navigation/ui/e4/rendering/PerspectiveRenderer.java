package org.eclipse.riena.navigation.ui.e4.rendering;

import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 *
 */
public class PerspectiveRenderer extends SWTPartRenderer {

	public PerspectiveRenderer() {
		super();
	}

	@Override
	public Widget createWidget(final MUIElement element, final Object parent) {
		if (!(element instanceof MPerspective) || !(parent instanceof Composite)) {
			return null;
		}

		final Composite perspArea = new Composite((Composite) parent, SWT.NONE);
		//		perspArea.setLayout(new RienaWindowLayout(SWT.HORIZONTAL, 250, 0));
		perspArea.setLayout(new RienaDynamicWindowLayout(SWT.HORIZONTAL, new int[] { 250, -1 }));
		final IStylingEngine stylingEngine = (IStylingEngine) getContext(element).get(IStylingEngine.SERVICE_NAME);
		stylingEngine.setClassname(perspArea, "perspectiveLayout"); //$NON-NLS-1$

		return perspArea;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer#processContents (org.eclipse.e4.ui.model.application.ui.MElementContainer)
	 */
	@Override
	public void processContents(final MElementContainer<MUIElement> container) {
		// TODO Auto-generated method stub
		super.processContents(container);

		final IPresentationEngine renderer = (IPresentationEngine) context.get(IPresentationEngine.class.getName());

		final MPerspective persp = (MPerspective) ((MUIElement) container);
		final Shell shell = ((Composite) persp.getWidget()).getShell();
		for (final MWindow dw : persp.getWindows()) {
			renderer.createGui(dw, shell, persp.getContext());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer#getUIContainer (org.eclipse.e4.ui.model.application.ui.MUIElement)
	 */
	@Override
	public Object getUIContainer(final MUIElement element) {
		if (!(element instanceof MWindow)) {
			return super.getUIContainer(element);
		}

		final MPerspective persp = (MPerspective) ((EObject) element).eContainer();
		if (persp.getWidget() instanceof Composite) {
			final Composite comp = (Composite) persp.getWidget();
			return comp.getShell();
		}

		return null;
	}
}
