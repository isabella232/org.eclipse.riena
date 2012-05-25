/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.e4;

import javax.inject.Inject;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.renderers.swt.TrimmedPartLayout;
import org.eclipse.e4.ui.workbench.renderers.swt.WBWRenderer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.WorkbenchWindow;

import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.navigation.ui.swt.CoolbarUtils;
import org.eclipse.riena.internal.navigation.ui.swt.RestoreFocusOnEscListener;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.navigation.ui.swt.component.TitleComposite;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellBorderRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellRenderer;
import org.eclipse.riena.navigation.ui.swt.statusline.IStatuslineContentFactoryExtension;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.swt.DefaultStatuslineContentFactory;
import org.eclipse.riena.ui.swt.GrabCorner;
import org.eclipse.riena.ui.swt.IStatusLineContentFactory;
import org.eclipse.riena.ui.swt.InfoFlyout;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineSpacer;
import org.eclipse.riena.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.riena.ui.swt.utils.WidgetIdentificationSupport;

@SuppressWarnings("restriction")
public class RienaWorkbenchRenderer extends WBWRenderer {
	/**
	 * System property defining the minimum width of the application window.
	 */
	private static final String PROPERTY_RIENA_APPLICATION_MINIMUM_WIDTH = "riena.application.minimum.width"; //$NON-NLS-1$
	/**
	 * System property defining the minimum height of the application window.
	 */
	private static final String PROPERTY_RIENA_APPLICATION_MINIMUM_HEIGHT = "riena.application.minimum.height"; //$NON-NLS-1$
	private static final int DEFAULT_COOLBAR_TOP_MARGIN = 2;
	public static final String SHELL_RIDGET_PROPERTY = "applicationWindow"; //$NON-NLS-1$

	enum BtnState {
		NONE, HOVER, HOVER_SELECTED;
	}

	// private final ApplicationController controller;
	private final AbstractViewBindingDelegate binding;

	// content factory for delegation of content creation from the statusline
	private IStatusLineContentFactory statuslineContentFactory;

	private Composite savedMainComposite;

	@Inject
	public EPartService epartService;

	public RienaWorkbenchRenderer() {
		super();
		binding = createBinding();
	}

	/**
	 * Creates a delegate for the binding of view and controller.
	 * 
	 * @return delegate for binding
	 */
	protected AbstractViewBindingDelegate createBinding() {
		return new InjectSwtViewBindingDelegate();

	}

	public void addUIControl(final Composite control, final String propertyName) {
		binding.addUIControl(control, propertyName);
	}

	@Override
	public Object createWidget(final MUIElement element, final Object parent) {
		if (!(element instanceof MWindow) || (parent != null && !(parent instanceof Control))) {
			return null;
		}

		final MWindow window = (MWindow) element;

		getContext(window).set(AbstractViewBindingDelegate.class, binding);

		final ApplicationController controller = getContext(window).get(ApplicationController.class);

		final Shell shell = createShell(window, controller);

		bindAndActivate(controller);

		return shell;
	}

	protected void bindAndActivate(final ApplicationController controller) {
		binding.injectAndBind(controller);
		controller.afterBind();
		controller.getNavigationNode().activate();
	}

	protected Shell createShell(final MWindow window, final ApplicationController controller) {
		final Shell shell = new Shell(SWT.NO_TRIM);
		initShell(shell, controller);
		final Rectangle bounds = shell.getBounds();
		shell.setBounds(bounds.x, bounds.y, window.getWidth(), window.getHeight());
		window.setWidget(shell);
		bindWidget(window, shell);

		createChilds(shell, controller, window);

		/*
		 * new ApplicationViewAdvisor(new IWorkbenchWindowConfigurer() {
		 * 
		 * public IWorkbenchWindow getWindow() { // TODO Auto-generated method
		 * stub return null; }
		 * 
		 * public IWorkbenchConfigurer getWorkbenchConfigurer() { // TODO
		 * Auto-generated method stub return null; }
		 * 
		 * public IActionBarConfigurer getActionBarConfigurer() { // TODO
		 * Auto-generated method stub return null; }
		 * 
		 * public String getTitle() { // TODO Auto-generated method stub return
		 * null; }
		 * 
		 * public void setTitle(final String title) { // TODO Auto-generated
		 * method stub
		 * 
		 * }
		 * 
		 * public boolean getShowMenuBar() { // TODO Auto-generated method stub
		 * return false; }
		 * 
		 * public void setShowMenuBar(final boolean show) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public boolean getShowCoolBar() { // TODO Auto-generated method stub
		 * return false; }
		 * 
		 * public void setShowCoolBar(final boolean show) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public boolean getShowStatusLine() { // TODO Auto-generated method
		 * stub return false; }
		 * 
		 * public void setShowStatusLine(final boolean show) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public boolean getShowPerspectiveBar() { // TODO Auto-generated
		 * method stub return false; }
		 * 
		 * public void setShowPerspectiveBar(final boolean show) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public boolean getShowFastViewBars() { // TODO Auto-generated method
		 * stub return false; }
		 * 
		 * public void setShowFastViewBars(final boolean enable) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public boolean getShowProgressIndicator() { // TODO Auto-generated
		 * method stub return false; }
		 * 
		 * public void setShowProgressIndicator(final boolean show) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public int getShellStyle() { // TODO Auto-generated method stub
		 * return 0; }
		 * 
		 * public void setShellStyle(final int shellStyle) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public Point getInitialSize() { // TODO Auto-generated method stub
		 * return null; }
		 * 
		 * public void setInitialSize(final Point initialSize) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public Object getData(final String key) { // TODO Auto-generated
		 * method stub return null; }
		 * 
		 * public void setData(final String key, final Object data) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public void addEditorAreaTransfer(final Transfer transfer) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * public void configureEditorAreaDropListener(final DropTargetListener
		 * dropTargetListener) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * public AbstractPresentationFactory getPresentationFactory() { // TODO
		 * Auto-generated method stub return null; }
		 * 
		 * public void setPresentationFactory(final AbstractPresentationFactory
		 * factory) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * public Menu createMenuBar() { // TODO Auto-generated method stub
		 * return null; }
		 * 
		 * public Control createCoolBarControl(final Composite parent) { // TODO
		 * Auto-generated method stub return null; }
		 * 
		 * public Control createStatusLineControl(final Composite parent) { //
		 * TODO Auto-generated method stub return null; }
		 * 
		 * public Control createPageComposite(final Composite parent) { // TODO
		 * Auto-generated method stub return null; }
		 * 
		 * public IStatus saveState(final IMemento memento) { // TODO
		 * Auto-generated method stub return null; } }, controller, null);
		 */

		return shell;
	}

	protected void createChilds(final Shell shell, final ApplicationController controller, final MWindow window) {
		final Composite grabCorner = createGrabCorner(shell);
		createStatusLine(shell, grabCorner);

		final IWorkbenchWindow workbenchWindow = createWorkbenchWindow(window);

		final TitleComposite titleComposite = createTitleComposite(shell, controller);
		final Composite menuBarComposite = createMenuBarComposite(workbenchWindow, shell, titleComposite);
		final Composite coolBarComposite = createCoolBarComposite(workbenchWindow, shell, menuBarComposite);
		final Composite mainComposite = createMainComposite(workbenchWindow, shell, coolBarComposite);
		this.savedMainComposite = mainComposite;
		createInfoFlyout(mainComposite);

		final RestoreFocusOnEscListener focusListener = new RestoreFocusOnEscListener(shell);
		focusListener.addControl(RestoreFocusOnEscListener.findCoolBar(menuBarComposite));
		focusListener.addControl(RestoreFocusOnEscListener.findCoolBar(coolBarComposite));
	}

	/**
	 * Initializes the given shell.
	 * 
	 * @param shell
	 *            shell to initialize
	 */
	protected void initShell(final Shell shell, final ApplicationController controller) {
		shell.setLayout(new FormLayout());

		shell.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND));
		shell.addPaintListener(new ShellPaintListener());

		final String iconName = controller.getNavigationNode().getIcon();
		shell.setImage(ImageStore.getInstance().getImage(iconName));
		shell.setMinimumSize(getApplicationSizeMinimum());

		// prepare shell for binding
		addUIControl(shell, SHELL_RIDGET_PROPERTY);

		if (getShellRenderer() != null) {
			getShellRenderer().setShell(shell);
		}

		// TODO check if this is the main window. maybe support more then one
		// workbench window.
		WidgetIdentificationSupport.setIdentification(shell);
	}

	protected Shell findOrCreateShell() {
		final Shell[] shells = Display.getCurrent().getShells();
		for (final Shell shell : shells) {
			if (shell != null) {
				return shell;
			}
		}
		return new Shell(Display.getCurrent(), SWT.Resize);
	}

	public IWorkbenchWindow createWorkbenchWindow(final MWindow wbModel) {
		// Return null if called from a non-UI thread.
		// This is not spec'ed behaviour and is misleading, however this is how
		// it
		// worked in 2.1 and we cannot change it now.
		// For more details, see [Bug 57384] [RCP] Main window not active on
		// startup
		if (Display.getCurrent() == null) { // || !initializationDone) {
			return null;
		}

		// the source providers try to update again during shutdown
		//		if (windowsClosed) {
		//			return null;
		//		}

		// rendering engine not available, can't make workbench windows, see bug
		// 320932
		final IEclipseContext e4Context = getContext(wbModel);
		if (e4Context.get(IPresentationEngine.class) == null) {
			return null;
		}

		return createWorkbenchWindow(null, null, wbModel, false); // getPerspectiveRegistry(e4Context).findPerspectiveWithId(getPerspectiveRegistry(e4Context).getDefaultPerspective())
	}

	IWorkbenchWindow createWorkbenchWindow(final IAdaptable input, final IPerspectiveDescriptor descriptor,
			final MWindow window, final boolean newWindow) {

		final IEclipseContext e4Context = getContext(window);
		final MApplication application = e4Context.get(MApplication.class);

		IEclipseContext windowContext = window.getContext();
		if (windowContext == null) {
			windowContext = E4Workbench.initializeContext(e4Context, window);
			E4Workbench.processHierarchy(window);
		}
		WorkbenchWindow result = (WorkbenchWindow) windowContext.get(IWorkbenchWindow.class.getName());
		if (result == null) {
			result = new WorkbenchWindow(input, descriptor);

			if (newWindow) {
				final Point size = getApplicationDefaultSizeMinimum(); //result.getWindowConfigurer().getInitialSize();
				window.setWidth(size.x);
				window.setHeight(size.y);
				application.getChildren().add(window);
				application.setSelectedElement(window);
			}

			//ContextInjectionFactory.inject(result, windowContext);
			windowContext.set(IWorkbenchWindow.class.getName(), result);

			if (application.getSelectedElement() == window) {
				//				application.getContext().set(ISources.ACTIVE_WORKBENCH_WINDOW_NAME, result);
				//				application.getContext().set(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME, result.getShell());
			}

			//fireWindowOpened(result);
			//result.fireWindowOpened();
		}
		return result;
	}

	public IPerspectiveRegistry getPerspectiveRegistry(final IEclipseContext e4Context) {
		return (IPerspectiveRegistry) e4Context.get(IPerspectiveRegistry.class.getName());
	}

	/**
	 * Create the composite that contains the:
	 * <ul>
	 * <li>shell title and title buttons</li>
	 * <li>the logo</li>
	 * <li>the sub application switcher</li>
	 * </ul>
	 * 
	 * @param parentShell
	 *            the parent shell (non null)
	 * @param controller
	 * @return the title composite (never null)
	 */
	private TitleComposite createTitleComposite(final Shell parentShell, final ApplicationController controller) {
		final ApplicationNode node = (ApplicationNode) controller.getNavigationNode();
		return new TitleComposite(parentShell, node);
	}

	/**
	 * Creates and positions the composite for the menu bar.
	 * 
	 * @param parent
	 *            parent of composite
	 * @param previous
	 *            previous composite in the layout
	 * @return composite
	 */
	private Composite createMenuBarComposite(final IWorkbenchWindow window, final Composite parent,
			final Composite previous) {
		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		final int padding = getShellPadding();

		// menu bar
		final MenuCoolBarComposite composite = new MenuCoolBarComposite(parent, SWT.NONE, window);

		final FormData formData = new FormData();
		formData.top = new FormAttachment(previous, getMenuBarTopMargin());
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		composite.setLayoutData(formData);

		return composite;
	}

	/**
	 * Returns the margin above the menu bar.
	 * 
	 * @return top margin
	 */
	private int getMenuBarTopMargin() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		return lnf.getIntegerSetting(LnfKeyConstants.MENUBAR_TOP_MARGIN, DEFAULT_COOLBAR_TOP_MARGIN);
	}

	/**
	 * Creates and positions the composite for the cool bar.
	 * 
	 * @param parent
	 *            parent of composite
	 * @param previous
	 *            previous composite in the layout
	 * @return composite
	 */
	private Composite createCoolBarComposite(final IWorkbenchWindow window, final Composite parent, Composite previous) {
		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		final Composite separator = UIControlsFactory.createSeparator(parent, SWT.HORIZONTAL);
		FormData formData = new FormData();
		formData.top = new FormAttachment(previous);
		formData.left = new FormAttachment(0, 2);
		formData.right = new FormAttachment(100, -2);
		formData.height = 2;
		separator.setLayoutData(formData);
		previous = separator;

		final int padding = getShellPadding();
		final Composite result = new Composite(parent, SWT.NONE);
		result.setLayout(new FillLayout());
		formData = new FormData();
		formData.top = new FormAttachment(previous, getToolBarTopMargin());
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		result.setLayoutData(formData);

		// final Control control = getWindowConfigurer().createCoolBarControl(
		// result);

		final Control control = new CoolBar(result, SWT.FLAT | SWT.HORIZONTAL);
		if (control instanceof CoolBar) {
			final CoolBar coolbar = (CoolBar) control;
			CoolbarUtils.initCoolBar(coolbar, getToolbarFont());
		}
		return result;
	}

	/**
	 * Returns the margin above the tool bar.
	 * 
	 * @return top margin
	 */
	private int getToolBarTopMargin() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		return lnf.getIntegerSetting(LnfKeyConstants.TOOLBAR_TOP_MARGIN, DEFAULT_COOLBAR_TOP_MARGIN);
	}

	private static Font getToolbarFont() {
		return LnfManager.getLnf().getFont(LnfKeyConstants.TOOLBAR_FONT);
	}

	/**
	 * Creates the main composite.
	 * 
	 * @param parent
	 *            parent of composite
	 * @param previous
	 *            previous composite in the layout
	 * @return composite
	 */
	private Composite createMainComposite(final IWorkbenchWindow window, final Composite parent,
			final Composite previous) {

		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		final int padding = getShellPadding();

		final Composite composite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		composite.setLayout(new FillLayout());
		final FormData formData = new FormData();
		formData.top = new FormAttachment(previous, LnfManager.getLnf().getIntegerSetting(
				LnfKeyConstants.TOOLBAR_WORK_AREA_VERTICAL_GAP), 0);
		formData.bottom = new FormAttachment(100, -padding);
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		composite.setLayoutData(formData);
		composite.setBackground(new Color(null, new RGB(0, 0, 0)));

		return composite;

	}

	private void createInfoFlyout(final Composite mainComposite) {
		final InfoFlyout flyout = UIControlsFactory.createInfoFlyout(mainComposite);
		binding.addUIControl(flyout, "infoFlyout"); //$NON-NLS-1$
	}

	/**
	 * Creates and positions the corner to grab.
	 * 
	 * @param shell
	 */
	private GrabCorner createGrabCorner(final Shell shell) {

		if (GrabCorner.isResizeable() && LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.SHELL_HIDE_OS_BORDER)) {
			return new GrabCorner(shell, SWT.DOUBLE_BUFFERED);
		}

		return null;

	}

	public static final int DEFAULT_PADDING_LEFT = 2;

	private void createStatusLine(final Composite shell, final Composite grabCorner) {
		final IStatusLineContentFactory statusLineFactory = getStatuslineContentFactory();
		final Statusline statusLine = new Statusline(shell, SWT.None, StatuslineSpacer.class, statusLineFactory);
		final FormData fd = new FormData();
		fd.height = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.STATUSLINE_HEIGHT);
		final int navigationX = LnfManager.getLnf().getIntegerSetting(
				LnfKeyConstants.TITLELESS_SHELL_NAVIGATION_HORIZONTAL_GAP, DEFAULT_PADDING_LEFT);
		fd.left = new FormAttachment(0, navigationX);
		if (grabCorner != null) {
			fd.right = new FormAttachment(grabCorner, 0);
		} else {
			final int padding = getShellPadding();
			fd.right = new FormAttachment(100, -padding);
		}
		fd.bottom = new FormAttachment(100, -5);
		statusLine.setLayoutData(fd);
		addUIControl(statusLine, "statusline"); //$NON-NLS-1$

		LnFUpdater.getInstance().updateUIControls(statusLine, true);
	}

	/**
	 * @since 4.0
	 */
	@InjectExtension(min = 0, max = 1)
	public void updateStatuslineContentFactory(
			final IStatuslineContentFactoryExtension statuslineContentFactoryExtension) {
		this.statuslineContentFactory = statuslineContentFactoryExtension == null ? new DefaultStatuslineContentFactory()
				: statuslineContentFactoryExtension.createFactory();
	}

	public IStatusLineContentFactory getStatuslineContentFactory() {
		return statuslineContentFactory;
	}

	/**
	 * Returns the padding between shell border and content.
	 * 
	 * @return padding
	 */
	private int getShellPadding() {

		final ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		return borderRenderer.getCompleteBorderWidth();

	}

	/**
	 * Returns the renderer of the shell.
	 * 
	 * @return renderer
	 */
	private ShellRenderer getShellRenderer() {
		final ShellRenderer shellRenderer = (ShellRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.TITLELESS_SHELL_RENDERER);
		return shellRenderer;
	}

	@Override
	public Object getUIContainer(final MUIElement element) {
		MUIElement parent = element.getParent();
		if (parent == null) {
			// might be a detached window
			parent = (MUIElement) ((EObject) element).eContainer();
			return parent == null ? null : parent.getWidget();
		}

		final Composite shellComp = (Composite) element.getParent().getWidget();
		final Layout layout = shellComp.getLayout();
		if (layout instanceof TrimmedPartLayout) {
			return ((TrimmedPartLayout) layout).clientArea;
		}
		if (layout instanceof FormLayout) {
			return savedMainComposite;
		}
		return null;
	}

	private Point getApplicationSizeMinimum() {
		final Point minSize = getApplicationDefaultSizeMinimum();
		final int widthMinimum = Integer.getInteger(PROPERTY_RIENA_APPLICATION_MINIMUM_WIDTH, minSize.x);
		final int heightMinimum = Integer.getInteger(PROPERTY_RIENA_APPLICATION_MINIMUM_HEIGHT, minSize.y);
		return new Point(widthMinimum, heightMinimum);
	}

	private Point getApplicationDefaultSizeMinimum() {
		return (Point) LnfManager.getLnf().getSetting(LnfKeyConstants.APPLICATION_MIN_SIZE);
	}

	/**
	 * This listener paints the shell (the border of the shell).
	 */
	private static class ShellPaintListener implements PaintListener {

		public void paintControl(final PaintEvent e) {
			onPaint(e);
		}

		/**
		 * Paints the border of the (titleless) shell.
		 * 
		 * @param e
		 *            event
		 */
		private void onPaint(final PaintEvent e) {
			if (e.getSource() instanceof Control) {
				final Control shell = (Control) e.getSource();

				final Rectangle shellBounds = shell.getBounds();
				final Rectangle bounds = new Rectangle(0, 0, shellBounds.width, shellBounds.height);

				final ILnfRenderer borderRenderer = LnfManager.getLnf().getRenderer(
						LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
				borderRenderer.setBounds(bounds);
				// TODO [ev] gc is sometimes disposed -- looks like a RAP bug,
				// adding a workaround, need to file bug
				if (!e.gc.isDisposed()) {
					borderRenderer.paint(e.gc, null);
				}
			}
		}
	}

}
