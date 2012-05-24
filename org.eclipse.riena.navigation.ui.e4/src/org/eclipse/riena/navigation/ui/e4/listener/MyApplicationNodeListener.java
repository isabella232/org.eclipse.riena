package org.eclipse.riena.navigation.ui.e4.listener;

import org.eclipse.e4.core.contexts.IEclipseContext;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.listener.ApplicationNodeListener;
import org.eclipse.riena.ui.filter.IUIFilter;

public class MyApplicationNodeListener extends ApplicationNodeListener {
	private final INavigationNode<?> navigationNode;
	private final IEclipseContext context;

	public MyApplicationNodeListener(final INavigationNode<?> navigationNode, final IEclipseContext context) {
		this.navigationNode = navigationNode;
		this.context = context;
	}

	@Override
	public void filterAdded(final IApplicationNode source, final IUIFilter filter) {
		show();
	}

	@Override
	public void filterRemoved(final IApplicationNode source, final IUIFilter filter) {
		show();
	}

	private void show() {
		if (navigationNode == null || navigationNode.isDisposed()) {
			return;
		}

		throw new UnsupportedOperationException("TODO: show view");

		//		try {
		//			final IViewPart vp = getNavigationViewPart();
		//			if (vp == null) {
		//				final NavigationViewPart navi = (NavigationViewPart) getActivePage().showView(NavigationViewPart.ID);
		//				navi.updateNavigationSize();
		//			}
		//		} catch (final PartInitException e) {
		//			throw new UIViewFailure(e.getMessage(), e);
		//		}
	}

	//	private IWorkbenchPage getActivePage() {
	//		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	//	}
	//
	//	/**
	//	 * Returns the view part of the navigation.
	//	 * 
	//	 * @return view part of the navigation or
	//	 */
	//	private IViewPart getNavigationViewPart() {
	//		final IViewReference[] references = getActivePage().getViewReferences();
	//		for (final IViewReference viewReference : references) {
	//			if (viewReference.getId().equals(NavigationViewPart.ID)) {
	//				return viewReference.getView(true);
	//			}
	//		}
	//		return null;
	//	}

}