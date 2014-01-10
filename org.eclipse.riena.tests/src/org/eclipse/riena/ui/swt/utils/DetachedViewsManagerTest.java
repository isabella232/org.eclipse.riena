/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

/**
 * Tests for {@link DetachedViewsManager}.
 */
@UITestCase
public class DetachedViewsManagerTest extends RienaTestCase {

	private static int count = 0;

	private Display display;
	private Shell shell;
	private DetachedViewsManager manager;

	private IWorkbenchSite site;

	@Override
	protected void setUp() {
		count = 0;
		display = Display.getDefault();
		shell = new Shell(display);
		site = new Site(shell);
		manager = new DetachedViewsManager(site);
	}

	@Override
	protected void tearDown() {
		shell.dispose();
		manager.dispose();
	}

	public void testShowView() {
		assertEquals(0, count);

		manager.showView("viewOne", FTViewPart.class, SWT.TOP);
		// already showing - don't create another
		manager.showView("viewOne", FTViewPart.class, SWT.TOP);

		assertEquals(1, count);

		manager.showView("viewTwo", FTViewPart.class, SWT.BOTTOM);

		assertEquals(2, count);

		try {
			manager.showView("  ", FTViewPart.class, 1);
			fail();
		} catch (final RuntimeException rex) {
			ok("expected");
		}

		try {
			manager.showView(null, FTViewPart.class, 1);
			fail();
		} catch (final RuntimeException rex) {
			ok("expected");
		}

		try {
			manager.showView("", FTViewPart.class, 1);
			fail();
		} catch (final RuntimeException rex) {
			ok("expected");
		}

		try {
			manager.showView("viewThree", null, SWT.TOP);
			fail();
		} catch (final RuntimeException rex) {
			ok("expected");
		}

		try {
			manager.showView("viewThree", FTViewPart.class, -1);
			fail();
		} catch (final RuntimeException rex) {
			ok("expected");
		}
	}

	public void testShowViewWithBounds() {
		manager.showView("viewOne", FTViewPart.class, new Rectangle(20, 30, 400, 500));
		final Shell shellOne = manager.getShell("viewOne");
		final Rectangle shellBounds = shellOne.getBounds();

		assertEquals(20, shellBounds.x);
		assertEquals(30, shellBounds.y);
		assertEquals(400, shellBounds.width);
		assertEquals(500, shellBounds.height);
	}

	public void testHideView() {
		manager.showView("viewOne", FTViewPart.class, SWT.TOP);
		final Shell viewOne = manager.getShell("viewOne");

		assertEquals(1, count);
		assertTrue(viewOne.isVisible());

		manager.hideView("viewOne");

		assertEquals(1, count);
		assertFalse(viewOne.isVisible());
		assertFalse(shell.isDisposed());

		manager.showView("viewOne", FTViewPart.class, SWT.TOP);

		assertEquals(1, count);
		assertTrue(viewOne.isVisible());
		assertFalse(shell.isDisposed());

		try {
			manager.hideView(null);
			fail();
		} catch (final RuntimeException rex) {
			ok("expected");
		}
	}

	public void testCloseView() {
		manager.showView("viewOne", FTViewPart.class, SWT.TOP);
		manager.showView("viewTwo", FTViewPart.class, SWT.BOTTOM);
		final Shell viewOne = manager.getShell("viewOne");
		final Shell viewTwo = manager.getShell("viewTwo");

		assertEquals(2, count);
		assertTrue(viewOne.isVisible());
		assertTrue(viewTwo.isVisible());
		assertFalse(viewOne.isDisposed());
		assertFalse(viewTwo.isDisposed());

		manager.closeView("viewTwo");

		assertEquals(1, count);
		assertTrue(viewOne.isVisible());
		assertFalse(viewOne.isDisposed());
		assertTrue(viewTwo.isDisposed());

		manager.closeView("viewOne");

		assertEquals(0, count);
		assertTrue(viewOne.isDisposed());
		assertTrue(viewTwo.isDisposed());

		manager.closeView("viewOne");
		try {
			manager.closeView(null);
			fail();
		} catch (final RuntimeException rex) {
			ok("expected");
		}
	}

	public void testDispose() {
		manager.showView("viewOne", FTViewPart.class, SWT.TOP);
		manager.showView("viewTwo", FTViewPart.class, SWT.BOTTOM);

		assertEquals(2, count);

		manager.dispose();

		assertEquals(0, count);
	}

	public void testGetShell() {
		assertNull(manager.getShell("viewOne"));

		manager.showView("viewOne", FTViewPart.class, SWT.TOP);

		final Shell shellOne = manager.getShell("viewOne");
		assertNotNull(shellOne);

		manager.hideView("viewOne");

		assertSame(shellOne, manager.getShell("viewOne"));

		manager.closeView("viewOne");

		assertNull(manager.getShell("viewOne"));

		try {
			manager.getShell(null);
			fail();
		} catch (final RuntimeException rex) {
			ok("expected");
		}
	}

	public void testCreateController() {
		Boolean result;

		result = ReflectionUtils.invokeHidden(manager, "checkController", new FTViewPart());
		assertTrue(result.booleanValue());

		result = ReflectionUtils.invokeHidden(manager, "checkController", new FTViewWithController());
		assertTrue(result.booleanValue());

		result = ReflectionUtils.invokeHidden(manager, "checkController", new FTViewWithoutController());
		assertFalse(result.booleanValue());
	}

	// helping classes
	//////////////////

	/**
	 * Mock implementation of {@link IWorkbenchSite}. Can return a Shell and a
	 * IWorkbenchWindow instance.
	 */
	private static final class Site implements IWorkbenchSite {

		private final Shell shell;
		private IWorkbenchWindow window;

		public Site(final Shell shell) {
			this.shell = shell;
		}

		public IWorkbenchPage getPage() {
			return null;
		}

		public ISelectionProvider getSelectionProvider() {
			return null;
		}

		public Shell getShell() {
			return shell;
		}

		public IWorkbenchWindow getWorkbenchWindow() {
			if (window == null) {
				window = new WorkbenchWindow(shell);
			}
			return window;
		}

		public void setSelectionProvider(final ISelectionProvider provider) {
			// unused
		}

		@SuppressWarnings("rawtypes")
		public Object getAdapter(final Class adapter) {
			return null;
		}

		@SuppressWarnings("rawtypes")
		public Object getService(final Class api) {
			return null;
		}

		@SuppressWarnings("rawtypes")
		public boolean hasService(final Class api) {
			return false;
		}
	}

	/**
	 * Mock implementation of {@link IWorkbenchWindow}. Can return a Shell
	 * instance.
	 */
	private static final class WorkbenchWindow implements IWorkbenchWindow {

		private final Shell shell;

		public WorkbenchWindow(final Shell shell) {
			this.shell = shell;
		}

		public boolean close() {
			shell.close();
			return true;
		}

		public IWorkbenchPage getActivePage() {
			return null;
		}

		public IExtensionTracker getExtensionTracker() {
			return null;
		}

		public IWorkbenchPage[] getPages() {
			return null;
		}

		public IPartService getPartService() {
			return null;
		}

		public ISelectionService getSelectionService() {
			return null;
		}

		public Shell getShell() {
			return shell;
		}

		public IWorkbench getWorkbench() {
			return null;
		}

		public boolean isApplicationMenu(final String menuId) {
			return false;
		}

		public IWorkbenchPage openPage(final IAdaptable input) throws WorkbenchException {
			return null;
		}

		public IWorkbenchPage openPage(final String perspectiveId, final IAdaptable input) throws WorkbenchException {
			return null;
		}

		public void run(final boolean fork, final boolean cancelable, final IRunnableWithProgress runnable)
				throws InvocationTargetException, InterruptedException {
		}

		public void setActivePage(final IWorkbenchPage page) {
		}

		public void addPageListener(final IPageListener listener) {
		}

		public void addPerspectiveListener(final IPerspectiveListener listener) {
		}

		public void removePageListener(final IPageListener listener) {
		}

		public void removePerspectiveListener(final IPerspectiveListener listener) {
		}

		@SuppressWarnings("rawtypes")
		public Object getService(final Class api) {
			return null;
		}

		@SuppressWarnings("rawtypes")
		public boolean hasService(final Class api) {
			return false;
		}
	}

	/**
	 * Mock implementation of a {@link ViewPart}. Will increment / decerement
	 * COUNT on ui-creation / ui-disposal.
	 * <p>
	 * Must be public because it is instantiated by the code under test via
	 * reflection.
	 */
	public static final class FTViewPart extends ViewPart {
		@Override
		public void createPartControl(final Composite parent) {
			count++;
		}

		@Override
		public void dispose() {
			count--;
			super.dispose();
		}

		@Override
		public void setFocus() {
			// unused
		}
	}

	/**
	 * Mock implementation of a {@link SubModuleController}.
	 */
	public static final class FTViewController extends SubModuleController {
	}

	/**
	 * A SubModuleView that returns a controller.
	 */
	public static final class FTViewWithController extends SubModuleView {
		private final FTViewController controller = new FTViewController();

		@Override
		protected FTViewController createController(final ISubModuleNode node) {
			return controller;
		}

		@Override
		protected void basicCreatePartControl(final Composite parent) {
			// unused
		}
	}

	/**
	 * A SubModuleView that does not return a controller.
	 */
	public static final class FTViewWithoutController extends SubModuleView {
		@Override
		protected void basicCreatePartControl(final Composite parent) {
			// unused
		}
	}
}
